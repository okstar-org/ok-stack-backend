/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
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

import io.quarkus.panache.common.Page;
import org.okstar.platform.common.core.defined.JobDefines;
import org.okstar.platform.common.core.utils.OkDateUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.domain.OrgStaff;
import org.okstar.platform.org.domain.OrgStaffPost;
import org.okstar.platform.org.dto.OrgStaffFragment;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.staff.mapper.OrgStaffMapper;
import org.okstar.platform.org.vo.OrgStaffReq;
import org.springframework.util.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
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
        var all = orgStaffMapper.findAll();
        var query = all.page(Page.of(pageable.getPageNumber(), pageable.getPageSize()));
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
    public List<OrgStaff> children(Long deptId) {
        //获取部门下面的岗位
        var posIds = orgPostService.findByDept(deptId)//
                .stream().map(p->p.id)//
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


        List<OrgStaff> list = orgStaffMapper.list("id in ?1", staffIds);
        for (OrgStaff staff : list) {
            List<OrgStaffPost> staffPosts1 = orgStaffPostService.findByStaffId(staff.id);
            staffPosts1.forEach(sp -> {
                OrgPost post = orgPostService.get(sp.getPostId());
                staff.getPostInfo().add(post.getName());
            });
        }
        return list;

    }


    @Override
    public List<OrgStaff> findPendings() {
        return orgStaffMapper.list("postStatus = ?1", JobDefines.PostStatus.pending);
    }

    @Override
    public List<OrgStaff> findLefts() {
        return orgStaffMapper.list("postStatus = ?1", JobDefines.PostStatus.left);
    }

    @Override
    public boolean add(OrgStaffReq req) {
        Assert.notNull(req, "参数异常！");
        OrgStaffFragment fragment = req.getFragment();
        Assert.notNull(fragment, "参数异常！");

        //设置姓名
        fragment.setName(fragment.getFirstName() + fragment.getLastName());

        //检查编号是否存在
        String no = fragment.getNo();
        Assert.notNull(no, "编号不能为空！");
        var exist = findByNo(no);
        Assert.isTrue(exist.isEmpty(), "用户已存在！");

        OrgStaff entity = new OrgStaff();
        entity.setDisabled(false);
        entity.setFragment(req.getFragment());
        entity.setJoinedDate(OkDateUtils.now());
        entity.setPostStatus(JobDefines.PostStatus.pending);
        save(entity);

        return true;
    }

    private Optional<OrgStaff> findByNo(String no) {
        return orgStaffMapper.find("no = ?1", no).stream().findFirst();
    }


}
