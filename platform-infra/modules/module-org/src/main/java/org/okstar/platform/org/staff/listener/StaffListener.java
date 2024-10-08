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

package org.okstar.platform.org.staff.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.Message;
import org.okstar.platform.common.thread.OkThreadUtils;
import org.okstar.platform.org.bus.ConsumerJms;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.system.dto.SysProfileDTO;

import java.util.Map;

@Startup
@ApplicationScoped
public class StaffListener {
    @Inject
    ObjectMapper objectMapper;
    @Inject
    ConsumerJms consumerJms;
    @Inject
    OrgStaffService orgStaffService;
    int sec = 5;
    //监听系统模块(启动类).
    static String TOPIC = "ModuleSystemApplication.SysProfile";

    public void onStart(@Observes StartupEvent ev) {
        boolean b;
        do {
            b = addStaffUpdateListener(TOPIC);
            if (!b) {
                Log.warnf("Unable to add staff update listener to topic: %s ", TOPIC);
                Log.warnf("Try again in %d seconds", sec);
                OkThreadUtils.sleepSeconds(sec, true);
            }
        } while (!b);
    }

    private boolean addStaffUpdateListener(String update) {
        return consumerJms.setTopicListener(update, (Message msg) -> {
            try {
                Map<String, String> body = msg.getBody(Map.class);
                Log.infof("body=%s", body);
                body.forEach((k, v) -> {
                    Log.infof("%s => %s", k, v);
                    try {
                        SysProfileDTO dto = objectMapper.readValue(v, SysProfileDTO.class);
                        updateStaff(dto);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                Log.warnf(e, "Unable to parse message=%s", msg);
            }
        });
    }

    /**
     * 更新人员信息
     *
     * @param dto
     */
    private void updateStaff(SysProfileDTO dto) {
        Log.infof("updateStaff profile:%s", dto);
        orgStaffService.save(dto);
    }
}
