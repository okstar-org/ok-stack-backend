package org.okstar.platform.org.rbac.mapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.okstar.platform.org.rbac.domain.OrgRbacRole;
import org.okstar.platform.org.rbac.domain.OrgRbacUser;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrgRbacUserMapper implements PanacheRepository<OrgRbacUser> {
}
