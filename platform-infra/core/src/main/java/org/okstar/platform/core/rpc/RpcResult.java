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

package org.okstar.platform.core.rpc;

import lombok.*;
import org.okstar.platform.common.web.bean.DTO;

/**
 * Rpc返回值
 * @param <T>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResult<T> extends DTO {
    //是否成功
    private boolean success;
    //消息
    private String msg;
    //数据
    private T data;


    public static <T> RpcResult<T> success(T data){
      return RpcResult.<T>builder().success(true).data(data).build();
    }

    public static <T> RpcResult<T> failed(Exception e){
        return failed(e.getMessage());
    }

    public static <T> RpcResult<T> failed(String msg){
        return RpcResult.<T>builder().success(false).msg(msg).build();
    }
}
