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

package org.okstar.platform.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 菜单
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    private int idx;

    private String id;

    /**
     * 路由
     */
    private String route;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * The attributes associated wth the resource.
     */
    private Map<String, List<String>> attribute;

    /**
     * 类型： link 链接、sub 子目录
     */
    private String type;

    /**
     * 图标
     */
    private String icon;

    private String uri;

    private List<String> path = new LinkedList<>();

    private List<Menu> children = new LinkedList<>();

    public void addChild(Menu menu) {
      children.add(menu);
    }
}
