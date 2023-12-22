package org.okstar.platform.org.rbac.mapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.org.rbac.domain.OrgRbacRole;



/**
 * 角色数据层
 */
@ApplicationScoped
public class OrgRbacRoleMapper implements PanacheRepository<OrgRbacRole> {
}
