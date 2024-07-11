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

package org.okstar.platform.org.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.org.dto.Org0;

@QuarkusTest
class OrgServiceImplTest {

    @Inject
    OrgService orgService;


    @Test
    void save() throws JsonProcessingException {
        String json = """
                {"id": 1001, 
                "no": "1",
                "name": "OkStar开源社区0",
                "url": "okstar.org0",
                "avatar": "https://www.chuanshaninfo.com/download/logo/OkStar/01.png",
                "location": "中国 - 长沙0",
                "cert": "d9b3d4f0-5552-4d76-ac22-1126e249a104" }
                """;
        Org0 org0 = new JsonMapper().readValue(json, Org0.class);
        Boolean save = orgService.save(org0);
        System.out.printf("save: %s%n", save);
    }
}