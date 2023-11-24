package org.okstar.platform.org.rbac.service.impl;

import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.org.rbac.mapper.OrgRbacResourceMapper;
import org.okstar.platform.org.rbac.mapper.OrgRbacRoleMapper;
import org.okstar.platform.org.rbac.resource.vo.OrgRbacRoleResponseVo;
import org.okstar.platform.org.rbac.service.OrgRbacResourceService;
import org.okstar.platform.org.rbac.service.OrgRbacRoleService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 资源服务类
 */
@ApplicationScoped
public class OrgRbacResourceServiceImpl implements OrgRbacResourceService {
    @Inject
    private OrgRbacResourceMapper orgRbacResourceMapper;

    @Override
    public List<OrgRbacRoleResponseVo> querySourceList() {
        return orgRbacResourceMapper.listAll().stream().map(role -> {
            OrgRbacRoleResponseVo rbac = new OrgRbacRoleResponseVo();
            OkBeanUtils.copyPropertiesTo(role, rbac);
            return rbac;
        }).collect(Collectors.toList());
    }


}
