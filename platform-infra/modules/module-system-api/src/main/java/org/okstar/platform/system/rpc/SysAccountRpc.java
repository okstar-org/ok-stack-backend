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

package org.okstar.platform.system.rpc;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.system.dto.SysAccountBindDTO;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;

import java.util.List;
import java.util.Optional;

/**
 * 用户帐号RPC
 */
@Path("rpc/SysAccountRpc")
@RegisterRestClient
public interface SysAccountRpc {

    /**
     * 获取最近密码
     * @param accountId 帐号ID
     * @return 密码
     */
    @GET
    @Path("lastPassword/{accountId}")
    RpcResult<String> lastPassword(@PathParam("accountId") Long accountId);

    /**
     * 注册
     * @param signUpForm 注册实体
     * @return SignUpResult
     */
    @POST
    @Path("signUp")
    RpcResult<SignUpResult> signUp(SignUpForm signUpForm);

    /**
     * 注销帐号
     * @param accountId 帐号ID
     */
    @DELETE
    @Path("signDown/{accountId}")
    void signDown(@PathParam("accountId") Long accountId);

    /**
     * 获取用户
     * @param account 帐号
     * @return SysAccountDTO
     */
    @GET
    @Path("getByAccount/{account}")
    Optional<SysAccountDTO> getByAccount(@PathParam("account") String account);

    /**
     * 通过绑定信息查询用户
     * @param type 绑定类型
     * @param bindValue 绑定值
     * @return SysAccountDTO
     */
    @GET
    @Path("findByBind")
    Optional<SysAccountDTO> findByBind(@QueryParam("type") AccountDefines.BindType type,
                                       @QueryParam("bindValue") String bindValue);

    /**
     * 通过邮箱查询用户
     * @param email 邮箱
     * @return SysAccountDTO
     */
    @GET
    @Path("findByEmail")
    Optional<SysAccountDTO> findByEmail(@QueryParam("email") String email);

    /**
     * 通过手机号查询用户
     * @param phone 手机号
     * @param iso 国家，比如CN
     * @return SysAccountDTO
     */
    @GET
    @Path("findByPhone")
    Optional<SysAccountDTO> findByPhone(@QueryParam("phone") String phone, @QueryParam("iso") String iso);

    /**
     * 查询帐号信息
     * @param username 用户名
     * @return SysAccountDTO
     */
    @GET
    @Path("findByUsername")
    Optional<SysAccountDTO> findByUsername(@QueryParam("username") String username);

    /**
     * 查询帐号信息
     * @param id 用户ID
     * @return SysAccountDTO
     */
    @GET
    @Path("findById/{id}")
    Optional<SysAccountDTO> findById(@PathParam("id") Long id);

    /**
     * 设置认证编号
     * @param id
     * @param cert
     */
    @POST
    @Path("setCert/{id}")
    void setCert(@PathParam("id") Long id, String cert);

    /**
     * 获取帐号绑定信息
     * @param id 用户ID
     * @return 结果
     */
    @GET
    @Path("getBinds/{id}")
    RpcResult<List<SysAccountBindDTO>> getBinds(@PathParam("id") Long id);

    /**
     * 设置用户ID
     * @param username 用户
     * @param uid ID
     */
    @POST
    @Path("{username}/uid")
    void setUid(@PathParam("username") String username, String uid);

    /**
     * 同步指定用户到`KC`
     * @param username 用户
     */
    @POST
    @Path("{username}/syncDb2Ldap")
    void sync(@PathParam("username") String username);
}
