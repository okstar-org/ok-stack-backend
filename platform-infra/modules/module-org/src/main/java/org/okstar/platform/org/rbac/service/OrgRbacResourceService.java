package org.okstar.platform.org.rbac.service;

import org.okstar.platform.org.rbac.resource.vo.OrgRbacRoleResponseVo;

import java.util.List;

public interface OrgRbacResourceService {
    /**
     * 查询资源列表
     *
     * @return
     */
    List<OrgRbacRoleResponseVo> querySourceList();
}
