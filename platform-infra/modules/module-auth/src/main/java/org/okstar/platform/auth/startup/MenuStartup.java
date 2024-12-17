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

package org.okstar.platform.auth.startup;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.okstar.platform.auth.dto.Menu;
import org.okstar.platform.auth.keycloak.BackResourceDTO;
import org.okstar.platform.auth.keycloak.BackResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单初始化服务
 */
@ApplicationScoped
public class MenuStartup {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    BackResourceManager resourceManager;

    /**
     * 启动初始化菜单相关信息
     *
     */
    void startup(@Observes StartupEvent event) {
        syncMenuToDB();
    }

    private void syncMenuToDB() {
        Log.infof("Synchronize menu to DB...");
        MenuFile file = readMenuFile();
        for (Menu menu : file.getMenu()) {
            BackResourceDTO dto = menu2dto(menu);
            resourceManager.add(dto);
            addChildren(menu);
        }
        Log.infof("Synchronize menu to DB completed.");
    }

    private void addChildren(Menu parent) {
        Log.infof("Add children: %s", parent.getName());

        List<Menu> children = parent.getChildren();
        children.forEach(child -> {
            BackResourceDTO dto = menu2dto(child);
            dto.setUris(Collections.singleton(parent.getUri() + "/" + dto.getUri()));
            Log.infof("Add resource: %s", dto.getUri());
            resourceManager.add(dto);
            addChildren(child);
        });
    }

    private static BackResourceDTO menu2dto(Menu menu) {
        BackResourceDTO dto = new BackResourceDTO();
        dto.setName(menu.getRoute());
        dto.setDisplayName(menu.getName());
        dto.setIconUri(menu.getIcon());
        dto.setUris(Collections.singleton(menu.getRoute()));

        Map<String, List<String>> attr = new HashMap<>();
        attr.put("type", Collections.singletonList(menu.getType()));
        if (menu.getIcon() != null) {
            attr.put("icon", Collections.singletonList(menu.getIcon()));
        }

        attr.put("idx", Collections.singletonList(menu.getIdx() + ""));
        dto.setAttributes(attr);
        return dto;
    }

    public MenuFile readMenuFile() {
        try (InputStream in = getClass().getResourceAsStream("/menu.json")) {
            if (in == null) {
                return null;
            }
            return objectMapper.readValue(in, MenuFile.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
