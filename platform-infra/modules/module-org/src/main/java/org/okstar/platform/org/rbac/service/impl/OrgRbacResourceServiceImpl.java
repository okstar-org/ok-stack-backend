package org.okstar.platform.org.rbac.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.org.rbac.mapper.OrgRbacResourceMapper;
import org.okstar.platform.org.rbac.resource.vo.OrgRbacRoleResponseVo;
import org.okstar.platform.org.rbac.service.OrgRbacResourceService;

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
