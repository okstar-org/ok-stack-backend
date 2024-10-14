/*
 * * Copyright (c) 2022 船山信息 chuanshaninfo.com
 * OkStack is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.auth.keycloak;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.okstar.platform.common.web.page.OkPageable;

import java.util.List;

@ApplicationScoped
public class BackRoleManagerImpl implements BackRoleManager {
    @Inject
    KeycloakService keycloakService;

    public List<BackRoleDTO> page(OkPageable pageable) {
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            RolesResource roleResource = keycloakService.getRoleResource(keycloak);
            return roleResource.list(pageable.getPageIndex() * pageable.getPageSize(), pageable.getPageSize())//
                    .stream().map(this::toDTO).toList();
        }
    }

    @Override
    public List<BackRoleDTO> list() {
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            RolesResource roleResource = keycloakService.getRoleResource(keycloak);
            List<RoleRepresentation> list = roleResource.list();
            return list.stream().map(this::toDTO).toList();
        }
    }

    @Override
    public BackRoleDTO toDTO(RoleRepresentation representation) {
        return BackRoleDTO.builder()
                .description(representation.getDescription())
                .name(representation.getName())
                .id(representation.getId()).build();
    }


}
