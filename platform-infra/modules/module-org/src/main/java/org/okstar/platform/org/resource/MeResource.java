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

package org.okstar.platform.org.resource;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.dto.MyOrgInfo;
import org.okstar.platform.org.dto.MyPostInfo;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.dto.OrgEmployee;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.staff.service.OrgStaffPostService;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.rpc.SysProfileRpc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 获取自己相关信息的接口
 */
@Path("me")
public class MeResource extends BaseResource {
    @Inject
    OrgService orgService;
    @Inject
    OrgStaffPostService staffPostService;
    @Inject
    OrgStaffService staffService;
    @Inject
    OrgPostService orgPostService;
    @Inject
    OrgDeptService orgDeptService;
    @Inject
    @RestClient
    SysProfileRpc sysProfileRpc;
    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @GET
    public Res<MyOrgInfo> me() {
        Org org = orgService.loadCurrent();
        Log.infof("org: %s", org.getName());

        String username = getUsername();
        Log.infof("username=> %s", username);

        Optional<SysAccountDTO> optional = (sysAccountRpc.findByUsername(username));
        Log.infof("SysAccount=> %s", optional);
        if (optional.isEmpty()) {
            return Res.ok(new MyOrgInfo());
        }

        SysAccountDTO account = optional.get();
        SysProfileDTO profile = sysProfileRpc.getByAccount(account.getId());

        var staffOptional = staffService.getByAccountId(account.getId());
        var orgStaff = staffOptional.orElseGet(() -> {
            OrgStaff staff0 = new OrgStaff();
            staff0.setAccountId(account.getId());
            staffService.create(staff0, account.getId());
            return staff0;
        });


        MyOrgInfo info = new MyOrgInfo();
        info.setAccount(account);
        info.setProfile(profile);

        Org0 org0 = Org0.builder()
                .name(org.getName())
                .url(org.getUrl())
                .avatar(org.getAvatar())
                .location(org.getLocation())
                .no(org.getNo())
                .uuid(org.getUuid())
                .build();
        info.setOrg(org0);

        OrgEmployee employee = staffService.toEmployee(orgStaff);
        info.setStaff(employee);

        var staffPosts = staffPostService.findByStaffId(orgStaff.id);
        if (!staffPosts.isEmpty()) {
            List<MyPostInfo> infos = staffPosts.stream().map(sp -> {
                Long postId = sp.getPostId();
                OrgPost post = orgPostService.get(postId);
                MyPostInfo postInfo = new MyPostInfo();
                postInfo.setPost(post.getName());
                OrgDept dept = orgDeptService.get(post.getDeptId());
                if (dept != null)
                    postInfo.setDept(dept.getName());
                return postInfo;
            }).collect(Collectors.toList());
            info.setPostInfo(infos);
        }

        return Res.ok(info);
    }
}
