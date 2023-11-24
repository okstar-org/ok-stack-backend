package org.okstar.platform.org.rbac.mapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.okstar.platform.org.rbac.domain.OrgRbacRole;

import javax.enterprise.context.ApplicationScoped;

/**
 * 角色数据层
 */
@ApplicationScoped
public class OrgRbacRoleMapper implements PanacheRepository<OrgRbacRole> {
}
