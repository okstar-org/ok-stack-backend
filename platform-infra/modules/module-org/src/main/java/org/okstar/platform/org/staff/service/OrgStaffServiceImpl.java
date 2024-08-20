/*
 * * Copyright (c) 2022 船山信息 chuanshaninfo.com
 * OkStack is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.org.staff.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.locationtech.jts.util.Assert;
import org.okstar.platform.common.core.defined.JobDefines;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.staff.domain.OrgStaffPost;
import org.okstar.platform.org.dto.OrgPost0;
import org.okstar.platform.org.dto.OrgStaff0;
import org.okstar.platform.org.dto.OrgStaffFragment;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.staff.mapper.OrgStaffMapper;
import org.okstar.platform.org.vo.OrgStaffFind;
import org.okstar.platform.org.vo.OrgStaffReq;
import org.okstar.platform.system.dto.SysProfileDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 人员服务
 */
@Transactional
@ApplicationScoped
public class OrgStaffServiceImpl implements OrgStaffService {
    @Inject
    OrgStaffMapper orgStaffMapper;

    @Inject
    OrgStaffPostService orgStaffPostService;

    @Inject
    OrgPostService orgPostService;


    @Override
    public void save(OrgStaff staff) {
        if (staff.id == null) {
            staff.setCreateAt(OkDateUtils.now());
        } else {
            staff.setUpdateAt(OkDateUtils.now());
        }
        orgStaffMapper.persist(staff);
    }

    @Override
    public List<OrgStaff> findAll() {
        return orgStaffMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgStaff> findPage(OkPageable pageable) {
        OrgStaffFind find = (OrgStaffFind) pageable;


        var posIds = orgPostService.findByDept(find.getDeptId())//
                .stream().map(p -> p.id)//
                .collect(Collectors.toSet());
        if (posIds.isEmpty()) {
            return OkPageResult.build(Collections.emptyList(), 0, 0);
        }

        //获取与岗位关联的人员
        List<OrgStaffPost> staffPosts = orgStaffPostService.findByPostIds(posIds);
        List<Long> staffIds = staffPosts
                .stream()//
                .map(OrgStaffPost::getStaffId)//
                .toList();
        if (staffIds.isEmpty()) {
            return OkPageResult.build(Collections.emptyList(), 0, 0);
        }

        var all = orgStaffMapper.find("id in ?1", staffIds);
        var query = all.page(Page.of(pageable.getPageIndex(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public OrgStaff get(Long id) {
        return orgStaffMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        orgStaffMapper.deleteById(id);
    }

    @Override
    public void delete(OrgStaff sysDept) {
        orgStaffMapper.delete(sysDept);
    }

    @Override
    public OrgStaff get(String uuid) {
        return orgStaffMapper.find("uuid", uuid).firstResult();
    }

    @Override
    public List<OrgStaff> children(Long deptId) {
        //获取部门下面的岗位
        var posIds = orgPostService.findByDept(deptId)//
                .stream().map(p -> p.id)//
                .collect(Collectors.toSet());
        if (posIds.isEmpty()) {
            return Collections.emptyList();
        }

        //获取与岗位关联的人员
        List<OrgStaffPost> staffPosts = orgStaffPostService.findByPostIds(posIds);
        List<Long> staffIds = staffPosts
                .stream()//
                .map(OrgStaffPost::getStaffId)//
                .collect(Collectors.toList());
        if (staffIds.isEmpty()) {
            return Collections.emptyList();
        }

        return orgStaffMapper.list("id in ?1", staffIds);
    }


    @Override
    public OkPageResult<OrgStaff> findPendings(OkPageable page) {
        var paged = orgStaffMapper
                .find("postStatus",
                        Sort.descending("createAt"),
                        JobDefines.PostStatus.pending)
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());
    }

    @Override
    public OkPageResult<OrgStaff0> findEmployees(OrgStaffFind find) {

        //获取部门下面的岗位
        var posIds = orgPostService.findByDept(find.getDeptId())
                .stream().map(p -> p.id)//
                .collect(Collectors.toSet());

        if (posIds.isEmpty()) {
            return OkPageResult.build(Collections.emptyList(), 0, 0);
        }

        //获取与岗位关联的人员
        List<OrgStaffPost> staffPosts = orgStaffPostService.findByPostIds(posIds);
        List<Long> staffIds = staffPosts
                .stream()//
                .map(OrgStaffPost::getStaffId)//
                .toList();
        if (staffIds.isEmpty()) {
            return OkPageResult.build(Collections.emptyList(), 0, 0);
        }

        PanacheQuery<OrgStaff> pq = orgStaffMapper.find("id in ?1", staffIds)
                .page(find.getPageIndex(), find.getPageSize());
        List<OrgStaff0> list = forResult(pq.list());
        return OkPageResult.build(list, pq.count(), pq.pageCount());
    }

    @Override
    public List<OrgStaff0> search(String query) {
        PanacheQuery<OrgStaff> pq = orgStaffMapper.find("accountId IS NOT null");
        return forResult(pq.list());
    }

    private List<OrgStaff0> forResult(List<OrgStaff> pq) {
        return pq.stream().map(staff -> {
            OrgStaff0 staff0 = new OrgStaff0();

            OkBeanUtils.copyPropertiesTo(staff, staff0);
            OkBeanUtils.copyPropertiesTo(staff.getFragment(), staff0);

            List<OrgPost0> posts = orgStaffPostService.findByStaffId(staff.id).stream().map(e -> {
                OrgPost0 p0 = new OrgPost0();
                OrgPost post = orgPostService.get(e.getPostId());
                OkBeanUtils.copyPropertiesTo(post, p0);
                return p0;
            }).toList();

            staff0.setPosts(posts);
            return staff0;
        }).toList();

    }

    @Override
    public OkPageResult<OrgStaff> findLefts(OkPageable page) {
        var paged = orgStaffMapper
                .find("postStatus", JobDefines.PostStatus.left)
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());

    }

    @Override
    public boolean add(OrgStaffReq req) {
        OkAssert.notNull(req, "参数异常！");
        Log.infof("req:%s", req);

        OrgStaffFragment fragment = req.getFragment();
        OkAssert.notNull(fragment, "参数异常！");


        Long id = req.getId();

        //检查编号是否存在
        String no = fragment.getNo();
        if (!OkStringUtil.isEmpty(no)) {
            var exist = findByNo(no);
            Assert.isTrue(exist.isEmpty() || Objects.equals(id, exist.get().id),
                    "存在该编号的用户！");
        }
        OrgStaff entity;
        if (id != null) {
            entity = get(id);
        } else {
            entity = new OrgStaff();
            entity.setDisabled(false);
            entity.setJoinedDate(OkDateUtils.now());
            entity.setPostStatus(JobDefines.PostStatus.pending);
        }

        entity.setFragment(req.getFragment());
        entity.setAccountId(req.getAccountId());

        save(entity);
        return true;
    }

    @Override
    public void setAccountId(Long id, Long accountId) {
        OrgStaff staff = get(id);
        if (staff == null)
            return;
        staff.setAccountId(accountId);
    }

    @Override
    public Optional<OrgStaff> getByAccountId(Long id) {
        return orgStaffMapper.find("accountId", id).stream().findFirst();
    }


    @Override
    public long getCount() {
        return orgStaffMapper.count("disabled", false);
    }

    @Override
    public void save(SysProfileDTO dto) {
        Long accountId = dto.getAccountId();
        if (accountId == null) {
            Log.warnf("accountId is null");
            return;
        }

        Optional<OrgStaff> staff = getByAccountId(accountId);
        if (staff.isEmpty()) {
            Log.warnf("Unable to find staff[accountId=%s]", accountId);
            return;
        }

        OrgStaff orgStaff = staff.get();
        OrgStaffFragment fragment = orgStaff.getFragment();
        if (fragment == null) {
            return;
        }
        fragment.setFirstName(dto.getFirstName());
        fragment.setLastName(dto.getLastName());
        fragment.setName(OkStringUtil.combinePeopleName(dto.getLanguage(), dto.getFirstName(), dto.getLastName()));
        fragment.setEmail(dto.getEmail());
        fragment.setPhone(dto.getPhone());
        fragment.setGender(dto.getGender());
        fragment.setBirthday(dto.getBirthday());
        fragment.setIdentity(dto.getIdentify());
        fragment.setLivingIn(dto.getAddress());
        fragment.setCity(dto.getCity());
        fragment.setCountry(dto.getCountry());
        fragment.setLanguage(dto.getLanguage());
    }

    private Optional<OrgStaff> findByNo(String no) {
        return orgStaffMapper.find("no", no).stream().findFirst();
    }
}
