/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
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

package interceptor;

import org.junit.jupiter.api.Test;
import org.okstar.platform.common.security.AesEncryptUtil;


class EncryptConfigInterceptorTest {
    private static final String AES_IV = "123456789okstar.";
    private static final String key = ".okstar.org(.cn)";

    @Test
    void testEncrypt() {
        String plaintext = "123456";
        String encrypted = AesEncryptUtil.encrypt(plaintext, key, AES_IV);
        System.out.println("加密后：" + encrypted);

        String decrypted = AesEncryptUtil.decrypt(encrypted, key, AES_IV);
        System.out.println("解密后：" + decrypted);
    }

}
