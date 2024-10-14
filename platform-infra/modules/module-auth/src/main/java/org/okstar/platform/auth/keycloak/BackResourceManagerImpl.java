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
import org.keycloak.admin.client.resource.AuthorizationResource;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.okstar.platform.common.web.page.OkPageable;

import java.util.List;

@ApplicationScoped
public class BackResourceManagerImpl implements BackResourceManager {

    @Inject
    KeycloakService keycloakService;

    @Override
    public List<BackResourceDTO> list() {
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            AuthorizationResource authorization = keycloakService.getAuthorizationResource(keycloak);
            return authorization.resources().resources().stream().map(this::toDTO).toList();
        }
    }

    @Override
    public List<BackResourceDTO> page(OkPageable pageable,
                                      String name,
                                      String uri,
                                      String owner,
                                      String type,
                                      String scope
    ) {
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            AuthorizationResource authorization = keycloakService.getAuthorizationResource(keycloak);
            return authorization.resources()
                    .find(name, uri, owner, type, scope,
                    pageable.getPageIndex() * pageable.getPageSize(),
                    pageable.getPageSize())
                    .stream()
                    .map(this::toDTO).toList();
        }
    }

    private BackResourceDTO toDTO(ResourceRepresentation representation) {
        return BackResourceDTO.builder()
                .id(representation.getId())
                .name(representation.getName())
                .displayName(representation.getDisplayName())
                .uris(representation.getUris())
                .type(representation.getType())
                .iconUri(representation.getIconUri())
                .build();
    }
}
