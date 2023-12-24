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

package org.okstar.platform.org.resource;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.dto.MyOrgInfo;
import org.okstar.platform.org.dto.MyPostInfo;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.dto.Staff0;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.org.staff.service.OrgStaffPostService;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.rpc.SysProfileRpc;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织
 */
@Path("")
public class OrgResource extends OkCommonResource {

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
    @Path("current")
    public Res<Optional<Org>> current() {
        var resultDto = orgService.current();
        return Res.ok(Req.empty(), resultDto);
    }

    @GET
    @Path("me")
    public Res<MyOrgInfo> me() {
        Optional<Org> orgOpt = orgService.current();
        if (orgOpt.isEmpty()) {
            return Res.error(Req.empty());
        }
        Org org = orgOpt.get();
        Log.infof("org:%s", org.getName());

        String username = getUsername();
        Log.infof("username:%s", username);

        var account0 = RpcAssert.isTrue(sysAccountRpc.findByUsername(username));
        Log.infof("findByUsername:%s=>%s", account0);

        var staff = staffService.getByAccountId(account0.getId());
        OkAssert.isTrue(staff.isPresent(), "Staff is not exist");

        Log.infof("getByAccountId:%s=>%s", staff);

        var staffPosts = staffPostService.findByStaffId(staff.get().id);
        if (staffPosts.isEmpty()) {
            return Res.error(Req.empty());
        }

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

        MyOrgInfo info = new MyOrgInfo();
        info.setOrg(Org0.builder()
                .name(org.getName())
                .url(org.getUrl())
                .avatar(org.getAvatar())
                .location(org.getLocation())
                .build());

        SysProfileDTO profile = sysProfileRpc.getByAccount(account0.getId());
        info.setStaff(Staff0.builder()
                .no(staff.get().getFragment().getNo())
                .phone(profile.getPhone()).email(profile.getEmail())
                .build());
        info.setPostInfo(infos);
        return Res.ok(info);
    }
}
