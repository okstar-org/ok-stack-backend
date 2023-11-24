package org.okstar.platform.org.rbac.service;

import org.okstar.platform.org.rbac.resource.vo.OrgRbacRoleResponseVo;
import org.okstar.platform.org.rbac.resource.vo.OrgRbacUserResponseVo;

import java.util.List;

public interface OrgRbacUserService {
    /**
     * 查询用户列表
     * @param roleId 角色id
     * @return
     */
    List<OrgRbacUserResponseVo> queryUserList(Long roleId);
    List<OrgRbacUserResponseVo> queryUserList();
}
