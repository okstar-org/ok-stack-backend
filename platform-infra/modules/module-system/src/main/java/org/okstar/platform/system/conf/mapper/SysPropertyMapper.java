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

package org.okstar.platform.system.conf.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.common.datasource.OkRepository;
import org.okstar.platform.system.conf.domain.SysProperty;
import org.okstar.platform.system.conf.domain.SysProperty_;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SysPropertyMapper implements OkRepository<SysProperty> {

    public List<SysProperty> findByGroup(String group) {
        return find(SysProperty_.GROUPING, group).list();
    }

    public List<SysProperty> findByGroupDomain(String group, String domain) {
        return find("%s=?1 and %s=?2".formatted(SysProperty_.GROUPING, SysProperty_.DOMAIN), group, domain).list();
    }

    public Optional<SysProperty> findByKey(String group, String domain, String key) {
        return find("%s=?1 and %s=?2 and %s=?3"
                        .formatted(SysProperty_.GROUPING, SysProperty_.DOMAIN, SysProperty_.K),
                group, domain, key).firstResultOptional();
    }

    public void deleteByGroup(String group) {
        delete(SysProperty_.GROUPING, group);
    }

    public Optional<SysProperty> findByKey(String group, String k) {
        return find("%s=?1 and %s=?2".formatted(SysProperty_.GROUPING,   SysProperty_.K),
                group, k).firstResultOptional();
    }
}
