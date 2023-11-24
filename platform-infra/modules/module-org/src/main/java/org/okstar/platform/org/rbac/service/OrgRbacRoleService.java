package org.okstar.platform.org.rbac.service;

import org.okstar.platform.org.rbac.resource.vo.OrgRbacRoleResponseVo;

import java.util.List;

public interface OrgRbacRoleService {
    /**
     * 查询角色列表
     *
     * @return
     */
    List<OrgRbacRoleResponseVo> queryRoleList();

    /**
     * 获取角色列表
     * @param menuId 资源id
     * @return
     */
    List<OrgRbacRoleResponseVo> queryRoleList(Long menuId);
}
