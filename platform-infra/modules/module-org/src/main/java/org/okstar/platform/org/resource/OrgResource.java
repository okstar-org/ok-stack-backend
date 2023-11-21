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

import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.dto.MyOrgInfo;
import org.okstar.platform.org.dto.MyPostInfo;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.org.staff.service.OrgStaffPostService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织
 */
@Path("")
public class OrgResource {

    @Inject
    OrgService orgService;
    @Inject
    OrgStaffPostService staffPostService;
    @Inject
    OrgPostService orgPostService;
    @Inject
    OrgDeptService orgDeptService;

    @GET
    @Path("current")
    public Res<Optional<Org>> current() {
        var resultDto = orgService.current();
        return Res.ok(Req.empty(), resultDto);
    }

    @GET
    @Path("me")
    public Res<MyOrgInfo> me() {
        Optional<Org> org = orgService.current();
        if (org.isEmpty()) {
            return Res.error(Req.empty());
        }

        //TODO 暂时固定1L
        var staffPosts = staffPostService.findByStaffId(1L);
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
        info.setOrg(org.get().getName());
        info.setPostInfo(infos);
        return Res.ok(Req.empty(), info);
    }
}
