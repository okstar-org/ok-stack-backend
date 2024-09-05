package org.okstar.platform.org.rbac.service.impl;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.org.rbac.domain.OrgRbacUser;
import org.okstar.platform.org.rbac.domain.OrgRbacUserRole;
import org.okstar.platform.org.rbac.mapper.OrgRbacUserMapper;
import org.okstar.platform.org.rbac.mapper.OrgRbacUserRoleMapper;
import org.okstar.platform.org.rbac.resource.vo.OrgRbacUserResponseVo;
import org.okstar.platform.org.rbac.service.OrgRbacUserService;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.dto.SysAccountDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户服务类
 */
@ApplicationScoped
public class OrgRbacUserServiceImpl implements OrgRbacUserService, PanacheRepositoryBase<OrgRbacUserRole, Long> {
    @Inject
    private OrgRbacUserMapper orgRbacUserMapper;
    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;
    @Inject
    private OrgRbacUserRoleMapper orgRbacUserRoleMapper;

    @Override
    public List<OrgRbacUserResponseVo> queryUserList(Long roleId) {
        var query = getEntityManager().createQuery("SELECT ur.* FROM OrgRbacUserRole ur WHERE ur.role.id = :roleId", OrgRbacUserRole.class);
        query.setParameter("roleId", roleId);
        return query.getResultList().stream().map(orgRbacUserRole -> {
            OrgRbacUser user = orgRbacUserRole.getUser();
            SysAccountDTO account = getAccount(user.getAccountId());
            if (account != null) {
                OrgRbacUserResponseVo orgRbacUserResponseVo = new OrgRbacUserResponseVo();
                orgRbacUserResponseVo.setNickname(account.getNickname());
                orgRbacUserResponseVo.setAvatar(account.getAvatar());
                orgRbacUserResponseVo.setAccountId(user.getAccountId());
                orgRbacUserResponseVo.setUsername(account.getUsername());
                return orgRbacUserResponseVo;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<OrgRbacUserResponseVo> queryUserList() {
        return orgRbacUserMapper.listAll().stream().map(ids -> {
            RpcResult<SysAccountDTO> sysAccount0RpcResult = sysAccountRpc.findById(ids.getAccountId());
            SysAccountDTO sysAccount = sysAccount0RpcResult.getData();
            if (sysAccount != null) {
                OrgRbacUserResponseVo user = new OrgRbacUserResponseVo();
                OkBeanUtils.copyPropertiesTo(sysAccount, user);
                return user;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public SysAccountDTO getAccount(Long accountId) {
        RpcResult<SysAccountDTO> sysAccount0RpcResult = sysAccountRpc.findById(accountId);
        return sysAccount0RpcResult.getData();
    }
}
