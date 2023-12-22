package org.okstar.platform.org.rbac.mapper;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.org.rbac.domain.OrgRbacResource;



@ApplicationScoped
public class OrgRbacResourceMapper implements PanacheRepository<OrgRbacResource> {
}
