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

package org.okstar.platform.common.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AesEncryptUtil {

    private static final int AES_KEY_LENGTH = 16;
    /**
     * AES加密模式
     */
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";


    public static Cipher getCipher(int mode, String key, String iv) {
        if (key == null || key.length() != AES_KEY_LENGTH) {
            throw new RuntimeException("config.encrypt.key不能为空，且长度为16位");
        }
        SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES");
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv0 = new IvParameterSpec(iv.getBytes());
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(AES_MODE);
            cipher.init(mode, spec, iv0);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException |
                 NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return cipher;
    }

    /**
     * AES加密函数
     *
     * @param plaintext 被加密的字符串
     * @param key       AES key
     * @return 加密后的值
     */
    public static String encrypt(final Object plaintext, String key, String iv) {
        if (null == plaintext) {
            return null;
        }
        try {
            Cipher encryptCipher = getCipher(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encrypted = encryptCipher.doFinal(String.valueOf(plaintext).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES 解密函数
     *
     * @param ciphertext 被解密的字符串
     * @param key        AES key
     * @param iv
     * @return 解密后的值
     */
    public static String decrypt(final String ciphertext, String key, String iv) {
        if (null == ciphertext) {
            return null;
        }
        try {
            Cipher decryptCipher = getCipher(Cipher.DECRYPT_MODE, key, iv);
            //先用base64解密
            byte[] encrypted1 = Base64.getDecoder().decode(ciphertext);
            byte[] original = decryptCipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
