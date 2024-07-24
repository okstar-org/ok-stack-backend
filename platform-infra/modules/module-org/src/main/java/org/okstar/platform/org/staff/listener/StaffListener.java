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

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import org.okstar.platform.common.OkJsonUtils;
import org.okstar.platform.org.bus.ConsumerJms;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.system.dto.SysProfileDTO;

@Startup
@ApplicationScoped
public class StaffListener {
    @Inject
    OkJsonUtils jsonUtils;
    @Inject
    ConsumerJms consumerJms;
    @Inject
    OrgStaffService orgStaffService;

    public void onStart(@Observes StartupEvent ev) {
        String update = "ModuleSystemApplication.SysProfile.UPDATE";
        consumerJms.setTopicListener(update, (Message msg)->{
            try {
                String body = msg.getBody(String.class);
                SysProfileDTO dto = jsonUtils.asObject(body, SysProfileDTO.class);
                updateStaff(dto);
            } catch (JMSException e) {
                Log.warnf(e, "Unable to parse message=%s", msg);
            }
        });

        String insert = "ModuleSystemApplication.SysProfile.INSERT";
        consumerJms.setTopicListener(insert, (Message msg)->{
            try {
                String body = msg.getBody(String.class);
                SysProfileDTO dto = jsonUtils.asObject(body, SysProfileDTO.class);
                updateStaff(dto);
            } catch (JMSException e) {
                Log.warnf(e, "Unable to parse message=%s", msg);
            }
        });
    }

    /**
     * 更新人员信息
     * @param dto
     */
    private void updateStaff(SysProfileDTO dto) {
        Log.infof("updateStaff profile:%s", dto);
        orgStaffService.save(dto);
    }
}
