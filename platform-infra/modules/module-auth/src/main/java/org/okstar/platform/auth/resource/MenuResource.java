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

package org.okstar.platform.auth.resource;


import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.apache.commons.collections.CollectionUtils;
import org.okstar.platform.auth.dto.Menu;
import org.okstar.platform.auth.keycloak.BackResourceDTO;
import org.okstar.platform.auth.keycloak.BackResourceManager;

import java.time.Duration;
import java.util.*;

/**
 * 菜单资源，提供菜单接口
 */
@Path("menu")
public class MenuResource extends BaseResource {
    /**
     * 资源后端
     */
    @Inject
    BackResourceManager resourceManager;

    /**
     * 菜单Cache
     */
    @Inject
    @CacheName("ok-menu")
    Cache cache;

    /**
     * 获取菜单
     * 1、有限查询缓存
     * 2、Cache为1分钟失效
     * 3、没有则查询DB
     * 使用cache降低后端压力
     * @return
     */
    @GET
    @Path("")
    public Map<String, List<Menu>> list() {
        CaffeineCache cc = (CaffeineCache) cache;
        cc.setExpireAfterAccess(Duration.ofMinutes(1));

        List<Menu> menus = cache.get("menus", (k) -> getMenus()).subscribe().asCompletionStage().toCompletableFuture().join();
        return Map.of("menu", menus);
    }

    /**
     * 从后端查询菜单
     * @return
     */
    private List<Menu> getMenus() {
        Log.debugf("fetch resources...");
        List<BackResourceDTO> list = resourceManager.list();
        List<Menu> top = list.stream().map(this::toRootMenu)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(Menu::getIdx)).toList();

        List<Menu> layer = top;
        while (!layer.isEmpty()) {
            for (Menu menu : layer) {
                forChildren(menu, list);
                layer = menu.getChildren();
            }
        }
        return top;
    }

    /**
     * 遍历查询子级菜单
     * @param parent
     * @param list
     */
    private void forChildren(Menu parent, List<BackResourceDTO> list) {

        if (parent == null) {
            return;
        }

        for (BackResourceDTO resourceDTO : list) {
            Menu menu = toMenu(resourceDTO);
            if (menu == null) {
                continue;
            }
            if (!(parent.getUri() + "/" + menu.getRoute()).equals(menu.getUri())) {
                continue;
            }
            parent.addChild(menu);
        }

        List<Menu> sorted = parent.getChildren().stream().sorted(Comparator.comparingInt(Menu::getIdx)).toList();
        parent.setChildren(sorted);

    }

    /**
     * 获取跟菜单
     * @param resourceDTO
     * @return
     */
    private Menu toRootMenu(BackResourceDTO resourceDTO) {
        var root = toMenu(resourceDTO);
        if (root == null || root.getPath().size() != 1) return null;
        return root;
    }

    /**
     * 从后端资源解析成菜单
     * @param resourceDTO
     * @return
     */
    private static Menu toMenu(BackResourceDTO resourceDTO) {
        Set<String> uris = resourceDTO.getUris();
        if (CollectionUtils.isEmpty(uris)) {
            return null;
        }

        Optional<String> url = uris.stream().findFirst();
        if (url.isEmpty()) {
            return null;
        }

        String[] segment = url.get()
                //remove first and last "/"
                .replaceAll("^/+", "").replaceAll("/+$", "")
                .split("/");

        String route = segment[segment.length - 1];

        Menu menu = new Menu();
        menu.setRoute(route);
        menu.setUri(url.get());
        menu.setPath(List.of(segment));
        menu.setId(resourceDTO.getId());
        menu.setName(resourceDTO.getDisplayName());
        menu.setAttribute(resourceDTO.getAttributes());

        Optional.ofNullable(resourceDTO.getAttributes())
                .ifPresent(attribute -> {
                    attribute.forEach((key, value) -> {
                        try {
                            switch (key) {
                                case "icon":
                                    menu.setIcon(value.get(0));
                                    break;
                                case "type":
                                    menu.setType(value.get(0));
                                    break;
                                case "idx":
                                    menu.setIdx(Integer.parseInt(value.get(0)));
                                    break;
                            }
                        } catch (Exception ignored) {
                        }
                    });
                });
        return menu;
    }
}
