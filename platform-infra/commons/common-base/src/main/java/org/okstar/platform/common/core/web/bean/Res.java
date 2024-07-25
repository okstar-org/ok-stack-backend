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

package org.okstar.platform.common.core.web.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.okstar.platform.common.date.OkDateUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


/**
 * 响应信息主体
 * <p>
 * <p>
 * 返回码：https://www.runoob.com/http/http-status-codes.html
 * HTTP 状态码
 * 当浏览者访问一个网页时，浏览者的浏览器会向网页所在服务器发出请求。
 * 当浏览器接收并显示网页前，此网页所在的服务器会返回一个包含 HTTP 状态码的信息头（server header）用以响应浏览器的请求。
 * <p>
 * HTTP 状态码的英文为 HTTP Status Code。。
 * <p>
 * 下面是常见的 HTTP 状态码：
 * <p>
 * 200 - 请求成功
 * 301 - 资源（网页等）被永久转移到其它URL
 * 404 - 请求的资源（网页等）不存在
 * 500 - 内部服务器错误
 * HTTP 状态码分类
 * HTTP 状态码由三个十进制数字组成，第一个十进制数字定义了状态码的类型。
 * 响应分为五类：
 * 信息响应(100–199)，成功响应(200–299)，重定向(300–399)，
 * 客户端错误(400–499)和服务器错误 (500–599)：
 * <p>
 * 分类	分类描述
 * 1**	信息，服务器收到请求，需要请求者继续执行操作
 * 2**	成功，操作被成功接收并处理
 * 3**	重定向，需要进一步的操作以完成请求
 * 4**	客户端错误，请求包含语法错误或无法完成请求
 * 5**	服务器错误，服务器在处理请求的过程中发生了错误
 * HTTP状态码列表:
 * <p>
 * 状态码	状态码英文名称	中文描述
 * 100	Continue	继续。客户端应继续其请求
 * 101	Switching Protocols	切换协议。服务器根据客户端的请求切换协议。只能切换到更高级的协议，例如，切换到HTTP的新版本协议
 * 200	OK	请求成功。一般用于GET与POST请求
 * 201	Created	已创建。成功请求并创建了新的资源
 * 202	Accepted	已接受。已经接受请求，但未处理完成
 * 203	Non-Authoritative Information	非授权信息。请求成功。但返回的meta信息不在原始的服务器，而是一个副本
 * 204	No Content	无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档
 * 205	Reset Content	重置内容。服务器处理成功，用户终端（例如：浏览器）应重置文档视图。可通过此返回码清除浏览器的表单域
 * 206	Partial Content	部分内容。服务器成功处理了部分GET请求
 * 300	Multiple Choices	多种选择。请求的资源可包括多个位置，相应可返回一个资源特征与地址的列表用于用户终端（例如：浏览器）选择
 * 301	Moved Permanently	永久移动。请求的资源已被永久的移动到新URI，返回信息会包括新的URI，浏览器会自动定向到新URI。今后任何新的请求都应使用新的URI代替
 * 302	Found	临时移动。与301类似。但资源只是临时被移动。客户端应继续使用原有URI
 * 303	See Other	查看其它地址。与301类似。使用GET和POST请求查看
 * 304	Not Modified	未修改。所请求的资源未修改，服务器返回此状态码时，不会返回任何资源。客户端通常会缓存访问过的资源，通过提供一个头信息指出客户端希望只返回在指定日期之后修改的资源
 * 305	Use Proxy	使用代理。所请求的资源必须通过代理访问
 * 306	Unused	已经被废弃的HTTP状态码
 * 307	Temporary Redirect	临时重定向。与302类似。使用GET请求重定向
 * 400	Bad Request	客户端请求的语法错误，服务器无法理解
 * 401	Unauthorized	请求要求用户的身份认证
 * 402	Payment Required	保留，将来使用
 * 403	Forbidden	服务器理解请求客户端的请求，但是拒绝执行此请求
 * 404	Not Found	服务器无法根据客户端的请求找到资源（网页）。通过此代码，网站设计人员可设置"您所请求的资源无法找到"的个性页面
 * 405	Method Not Allowed	客户端请求中的方法被禁止
 * 406	Not Acceptable	服务器无法根据客户端请求的内容特性完成请求
 * 407	Proxy Authentication Required	请求要求代理的身份认证，与401类似，但请求者应当使用代理进行授权
 * 408	Request Time-out	服务器等待客户端发送的请求时间过长，超时
 * 409	Conflict	服务器完成客户端的 PUT 请求时可能返回此代码，服务器处理请求时发生了冲突
 * 410	Gone	客户端请求的资源已经不存在。410不同于404，如果资源以前有现在被永久删除了可使用410代码，网站设计人员可通过301代码指定资源的新位置
 * 411	Length Required	服务器无法处理客户端发送的不带Content-Length的请求信息
 * 412	Precondition Failed	客户端请求信息的先决条件错误
 * 413	Request Entity Too Large	由于请求的实体过大，服务器无法处理，因此拒绝请求。为防止客户端的连续请求，服务器可能会关闭连接。如果只是服务器暂时无法处理，则会包含一个Retry-After的响应信息
 * 414	Request-URI Too Large	请求的URI过长（URI通常为网址），服务器无法处理
 * 415	Unsupported Media Type	服务器无法处理请求附带的媒体格式
 * 416	Requested range not satisfiable	客户端请求的范围无效
 * 417	Expectation Failed	服务器无法满足Expect的请求头信息
 * 500	Internal Server Error	服务器内部错误，无法完成请求
 * 501	Not Implemented	服务器不支持请求的功能，无法完成请求
 * 502	Bad Gateway	作为网关或者代理工作的服务器尝试执行请求时，从远程服务器接收到了一个无效的响应
 * 503	Service Unavailable	由于超载或系统维护，服务器暂时的无法处理客户端的请求。延时的长度可包含在服务器的Retry-After头信息中
 * 504	Gateway Time-out	充当网关或代理的服务器，未及时从远端服务器获取请求
 * 505	HTTP Version not supported	服务器不支持请求的HTTP协议的版本，无法完成处理
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Res<T> extends DTO {

    private static final int OK = 0;
    private static final int CREATED = 1;
    private static final int ERROR = -1;
    private int takes;
    private int code;
    private String msg;
    private T data;

    private final Map<String, Object> extra = new LinkedHashMap<>();


    /**
     * 处理成功，使用200返回码
     *
     * @param <T>
     * @return
     */
    public static <T> Res<T> ok(Req req) {
        return build(req, null, OK, null);
    }

    public static <T> Res<T> ok(T data) {
        return build(Req.empty(), data, OK, null);
    }

    public static <T> Res<T> ok(Req req, T data) {
        return build(req, data, OK, null);
    }

    public static <T> Res<T> ok(Req req, T data, String msg) {
        return build(req, data, OK, msg);
    }


    /**
     * 创建成功，使用201返回码
     *
     * @param <T>
     * @return
     */
    public static <T> Res<T> created() {
        return build(null, null, CREATED, null);
    }

    public static <T> Res<T> created(T data) {
        return build(null, data, CREATED, null);
    }

    public static <T> Res<T> created(T data, String msg) {
        return build(null, null, CREATED, msg);
    }

    public static <T> Res<T> error() {
        return build(Req.empty(), null, ERROR, null);
    }

    /**
     * 内部服务器错误，使用500返回码
     *
     * @param <T>
     * @return
     */
    public static <T> Res<T> error(Req req) {
        return build(req, null, ERROR, null);
    }

    public static <T> Res<T> error(Req req, String msg) {
        return build(req, null, ERROR, msg);
    }

    public static <T> Res<T> error(Req req, T data, String msg) {
        return build(req, data, ERROR, msg);
    }


    private static <T> Res<T> build(Req req, T data, int code, String msg) {
        Res<T> res = new Res<>();
        res.setCode(code);
        res.setData(data);
        res.setMsg(msg);
        var times = OkDateUtils.getTime() - Optional.ofNullable(req).orElse(Req.empty()).getTs();
        res.setTakes(Math.toIntExact(times));
        return res;
    }

    public void putExtra(String k, Object o) {
        extra.put(k, o);
    }


    public boolean success() {
        return code == OK || code == CREATED;
    }

}
