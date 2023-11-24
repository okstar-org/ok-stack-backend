package org.okstar.platform.org.rbac.resource.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrgRbacRoleResponseVo implements Serializable {

    private Long roleId;
    /**
     * 角色名称
     */
    private String name;

    /**
     * 岗位 [OrgPost]
     */
    private Long postId;
}
