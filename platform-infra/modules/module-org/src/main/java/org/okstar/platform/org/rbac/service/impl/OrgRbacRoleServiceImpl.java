package org.okstar.platform.org.rbac.service.impl;

import io.quarkus.panache.common.Parameters;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.org.rbac.domain.SysRbacRoleResource;
import org.okstar.platform.org.rbac.mapper.OrgRbacRoleMapper;
import org.okstar.platform.org.rbac.mapper.SysRbacRoleResourceMapper;
import org.okstar.platform.org.rbac.resource.vo.OrgRbacRoleResponseVo;
import org.okstar.platform.org.rbac.service.OrgRbacRoleService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色服务类
 */
@ApplicationScoped
public class OrgRbacRoleServiceImpl implements OrgRbacRoleService {
    @Inject
    private OrgRbacRoleMapper orgRbacRoleMapper;
    @Inject
    private SysRbacRoleResourceMapper sysRbacRoleResourceMapper;

    @Override
    public List<OrgRbacRoleResponseVo> queryRoleList() {
        return orgRbacRoleMapper.listAll().stream().map(role -> {
            OrgRbacRoleResponseVo rbac = new OrgRbacRoleResponseVo();
            OkBeanUtils.copyPropertiesTo(role, rbac);
            return rbac;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrgRbacRoleResponseVo> queryRoleList(Long menuId) {
        return sysRbacRoleResourceMapper.list("menuId = :menuId", Parameters.with("menuId", menuId)).stream().map(rbacRoleResource -> {
            OrgRbacRoleResponseVo orgRbacRoleResponseVo = new OrgRbacRoleResponseVo();
            orgRbacRoleResponseVo.setRoleId(rbacRoleResource.id);
            OkBeanUtils.copyPropertiesTo(rbacRoleResource, orgRbacRoleResponseVo);
            return orgRbacRoleResponseVo;
        }).collect(Collectors.toList());
    }
}
