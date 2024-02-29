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

package org.okstar.platform.common.core.utils;

import io.vertx.core.http.Cookie;
import io.vertx.core.http.HttpServerRequest;
import lombok.SneakyThrows;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OkWebUtil {
    private static final Logger logger = LoggerFactory.getLogger(OkWebUtil.class);

    private static HttpClient buildHttpClient() {
        //设置参数
        HttpClient client = new HttpClient();
        // 使用系统提供的默认的恢复策略
        HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
        // 设置连接的超时时间
        managerParams.setConnectionTimeout(3 * 60 * 1000);
        // 设置读取数据的超时时间
        managerParams.setSoTimeout(5 * 60 * 1000);
        return client;
    }


    @SneakyThrows
    public static String getPublicIp() {
        return doGet("https://ifconfig.me/");
    }

    /**
     * @param url
     * @return 成功时返回数据字符串
     */
    public static String doGet(String url) {
        // 构造HttpClient的实例
        HttpClient client = buildHttpClient();

        // 创建GET方法的实例
        GetMethod method = new GetMethod(url);
        try {
            method.setRequestHeader("user-agent", "curl/7.81.0");
            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
            // 执行getMethod
            client.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK || method.getStatusCode() == HttpStatus.SC_CREATED) {
                return method.getResponseBodyAsString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return null;
    }

    public static String getIpAddr(HttpServerRequest request) {
        if (request == null) {
            return null;
        }

        String ip = null;

        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            // X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.remoteAddress().hostAddress();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


    public static String getValue(HttpServerRequest request, String name) {
        String param = request.getParam(name);
        if (param != null)
            return param;

        String header = request.getHeader(name);
        if (header != null)
            return header;

        Cookie cookie = request.getCookie(name);
        if (cookie != null) return cookie.getValue();

        return null;
    }

    /**
     * Adjust URL.
     *
     * @param url the url
     * @return the string
     */
    private String fixHttpURL(String url) {
        if (!url.startsWith("http")) {
            url = "http://" + url;
        }
        return url;
    }
}
