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

package org.okstar.platform.common.core.web.bean;

import lombok.Getter;
import org.okstar.platform.common.core.constant.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 响应信息主体
 */
@Getter
public class Res<T> extends VO
{
    /** 成功 */
    public static final int SUCCESS = Constants.SUCCESS;

    /** 失败 */
    public static final int FAIL = Constants.FAIL;

    private Long ts;
    private int code;
    private T data;
    private String msg;



    private final Map<String, Object> extra = new LinkedHashMap<>();

    public static <T> Res<T> ok()
    {
        return restResult(null, SUCCESS, null);
    }

    public static <T> Res<T> ok(T data)
    {
        return restResult(data, SUCCESS, null);
    }

    public static <T> Res<T> ok(T data, String msg)
    {
        return restResult(data, SUCCESS, msg);
    }

    public static <T> Res<T> fail()
    {
        return restResult(null, FAIL, null);
    }

    public static <T> Res<T> fail(String msg)
    {
        return restResult(null, FAIL, msg);
    }

    public static <T> Res<T> fail(T data)
    {
        return restResult(data, FAIL, null);
    }

    public static <T> Res<T> fail(T data, String msg)
    {
        return restResult(data, FAIL, msg);
    }

    public static <T> Res<T> fail(int code, String msg)
    {
        return restResult(null, code, msg);
    }

    private static <T> Res<T> restResult(T data, int code, String msg)
    {
        Res<T> apiResult = new Res<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public void putExtra(String k, Object o) {
        extra.put(k, o);
    }


}
