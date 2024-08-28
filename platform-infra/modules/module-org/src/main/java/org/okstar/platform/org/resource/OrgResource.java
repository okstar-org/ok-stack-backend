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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.OkCloudFactory;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.service.FederalService;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.dto.*;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.org.staff.service.OrgStaffPostService;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.system.dto.SysAccountBindDTO;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.rpc.SysProfileRpc;
import org.okstar.platform.system.dto.SysAccountDTO;

import java.util.List;
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
    @Inject
    FederalService federalService;


    @GET
    @Path("current")
    public Res<Org0> current() {
        return Res.ok(orgService.loadCurrent0());
    }

    @PUT
    @Path("save")
    public Res<Boolean> save(Org0 org0){
        Org saved = orgService.save(org0);


        federalService.save(saved);


        return Res.ok(saved!=null);
    }

    @GET
    @Path("me")
    public Res<MyOrgInfo> me() {
        Org org = orgService.loadCurrent();
        Log.infof("org: %s", org.getName());

        String username = getUsername();
        Log.infof("username=> %s", username);

        SysAccountDTO account0 = RpcAssert.isTrue(sysAccountRpc.findByUsername(username));
        Log.infof("SysAccount0=> %s", account0);

        SysProfileDTO profile = sysProfileRpc.getByAccount(account0.getId());

        var staffOptional = staffService.getByAccountId(account0.getId());
        var orgStaff = staffOptional.orElseGet(() -> {
            OrgStaff staff0 = new OrgStaff();
            staff0.setAccountId(account0.getId());

            OrgStaffFragment f = new OrgStaffFragment();
            f.setIso(account0.getIso());
            List<SysAccountBindDTO> bindDTOS = RpcAssert.isTrue(sysAccountRpc.getBinds(account0.getId()));
            bindDTOS.forEach(bind -> {
                switch (bind.getBindType()) {
                    case email -> f.setEmail(bind.getBindValue());
                    case phone -> f.setPhone(bind.getBindValue());
                }
            });
            staff0.setFragment(f);
            staffService.create(staff0, account0.getId());
            return staff0;
        });


        MyOrgInfo info = new MyOrgInfo();
        info.setAccount(account0);
        info.setProfile(profile);
        info.setOrg(Org0.builder()
                .name(org.getName())
                .url(org.getUrl())
                .avatar(org.getAvatar())
                .location(org.getLocation())
                .build());

        info.setStaff(OrgStaff0.builder()
                .accountId(account0.getId())
                .name(profile.getPersonalName())
                .id(orgStaff.id)
                .no(orgStaff.getFragment().getNo())
                .phone(orgStaff.getFragment().getPhone())
                .email(orgStaff.getFragment().getEmail())
                .gender(orgStaff.getFragment().getGender())
                .birthday(orgStaff.getFragment().getBirthday())
                .joinedDate(orgStaff.getJoinedDate())
                .build());

        /**
         * 加载岗位信息
         */
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
