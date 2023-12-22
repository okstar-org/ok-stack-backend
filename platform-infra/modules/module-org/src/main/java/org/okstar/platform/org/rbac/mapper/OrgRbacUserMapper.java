package org.okstar.platform.org.rbac.mapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.org.rbac.domain.OrgRbacUser;



@ApplicationScoped
public class OrgRbacUserMapper implements PanacheRepository<OrgRbacUser> {
}
