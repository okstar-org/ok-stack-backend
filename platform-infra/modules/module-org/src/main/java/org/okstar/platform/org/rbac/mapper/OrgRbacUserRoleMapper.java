package org.okstar.platform.org.rbac.mapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.org.rbac.domain.OrgRbacUserRole;



@ApplicationScoped
public class OrgRbacUserRoleMapper implements PanacheRepository<OrgRbacUserRole> {
}
