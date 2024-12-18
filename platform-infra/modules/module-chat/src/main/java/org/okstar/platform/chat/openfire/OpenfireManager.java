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

package org.okstar.platform.chat.openfire;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.*;
import org.igniterealtime.restclient.enums.SupportedMediaType;
import org.okstar.platform.chat.ChatUtils;
import org.okstar.platform.chat.beans.ChatGeneral;
import org.okstar.platform.chat.beans.ChatGroup;
import org.okstar.platform.chat.beans.ChatParticipant;
import org.okstar.platform.chat.beans.ChatRoom;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.dto.SysConfImDTO;
import org.okstar.platform.system.dto.SysConfIntegrationDTO;
import org.okstar.platform.system.rpc.SysConfIntegrationRpc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Openfire 管理器
 * <p>实现对Openfire相关管理</p>
 * <ol>
 *     <li>获取Openfire配置</li>
 *     <li>初始化RestApi客户端</li>
 *     <li>获取用户</li>
 * </ol>
 */
@ApplicationScoped
public class OpenfireManager implements XmppClient {

    /**
     * Openfire Rest API 客户端
     */
    private RestApiClient restApiClient;

    /**
     * IM配置信息
     */
    private SysConfImDTO imConfDTO;

    /**
     * 集成配置RPC
     * <p>从系统模块获取IM配置</p>
     */
    @Inject
    @RestClient
    SysConfIntegrationRpc settingsRpc;

    /**
     * 确保Rest接口可用
     * @return RestApiClient
     */
    private RestApiClient ensure() {
        SysConfIntegrationDTO global = settingsRpc.getIntegrationConf();
        if (global == null) {
            Log.warnf("Can not find global settings!");
            return null;
        }

        var newXmppConf = global.getIm();
        if (restApiClient != null && newXmppConf.equals(this.imConfDTO)) {
            Log.infof("Using cached client.");
            return restApiClient;
        }

        if (OkStringUtil.isEmpty(newXmppConf.getHost())) {
            Log.warnf("Unable to find xmpp host!");
            return null;
        }

        int xmppAdminPort = newXmppConf.getAdminPort();
        if (xmppAdminPort == 0) {
            Log.warnf("Unable to find xmpp admin port!");
            return null;
        }

        String secretKey = newXmppConf.getApiSecretKey();
        if (OkStringUtil.isEmpty(secretKey)) {
            Log.warnf("Unable to find xmpp api secret key!");
            return null;
        }

        imConfDTO = newXmppConf;
        restApiClient = makeXmppClient(newXmppConf.getHost(), xmppAdminPort, secretKey);
        return restApiClient;
    }

    /**
     * 生成Rest API
     * @param host
     * @param xmppAdminPort
     * @param secretKey
     * @return
     */
    @Override
    public RestApiClient makeXmppClient(String host, int xmppAdminPort, String secretKey) {
        AuthenticationToken token = new AuthenticationToken(secretKey);
        return new RestApiClient(
                "http://" + host, xmppAdminPort, //
                token, SupportedMediaType.JSON);
    }

    /**
     * 获取聊天系统用户列表
     * @return UserEntities
     */
    @Override
    public UserEntities users() {
        UserEntities users = ensure().getUsers();
        Log.infof("users:%s", users);
        return users;
    }

    /**
     * 获取指定的聊天系统用户
     * @return UserEntity
     */
    @Override
    public UserEntity findUserByUsername(String username) {
        Log.infof("findUserByUsername:%s", username);
        UserEntity user = ensure().getUser(username);
        Log.infof("user:%s", user);
        return user;
    }

    /**
     * 获取指定的聊天系统用户的花名册（好友列表）
     * @return UserEntity
     */
    @Override
    public RosterEntities findRosterByUsername(String username) {
        Log.infof("findRosterByUsername:%s", username);
        RosterEntities rosterEntities = ensure().getRoster(username);
        Log.infof("rosterEntities:%s", rosterEntities.getRosterItem());
        return rosterEntities;
    }

    /**
     * 获取用户概括信息
     * @param username 用户名
     * @return ChatGeneral
     */
    @Override
    public ChatGeneral findChatGeneralByUsername(String username) {
        RestApiClient ensure = ensure();
        RosterEntities roster = ensure.getRoster(username);
        UserGroupsEntity groups = ensure.getUserGroups(username);
        MessageArchiveEntities archive = ensure.getMessageArchive(username);

        return ChatGeneral.builder()//
                .groups(groups.getGroupNames() == null ? 0 : groups.getGroupNames().size())
                .contacts(roster.getRosterItem() == null ? 0 : roster.getRosterItem().size())
                .msgs(archive.getArchive() == null ? 0 : archive.getArchive().getCount())
                .build();
    }

    /**
     * 列出所有群组
     * @return List<ChatRoom>
     */
    @Override
    public List<ChatRoom> listRooms() {
        Map<String, String> q = new HashMap<>();
        q.put("type", "all");
        var rooms = ensure().getChatRooms(q);
        return rooms.getChatRooms().stream()        //
                .map(r -> ChatUtils.convertRoom(r)) //
                .collect(Collectors.toList());
    }

    /**
     * 查询指定的群组
     * @param username 群组帐号
     * @return ChatRoom
     */
    @Override
    public ChatRoom findRoomByName(String username) {
        MUCRoomEntity room = ensure().getChatRoom(username);
        return ChatUtils.convertRoom(room);
    }

    /**
     * 查询指定的群组成员
     * @param username 群组帐号
     * @return List<ChatParticipant>
     */
    @Override
    public List<ChatParticipant> findParticipantsByName(String username) {
        ParticipantEntities participants = ensure().getChatRoomParticipants(username);
        if (participants == null || participants.getParticipants() == null)
            return Collections.emptyList();
        return participants.getParticipants().stream().map(e ->
                ChatUtils.convertParticipant(e)
        ).toList();
    }

    /**
     * 更新群聊信息
     * @param room 群
     * @return boolean
     */
    @Override
    public boolean updateRoom(ChatRoom room) {
        MUCRoomEntity entity = ChatUtils.convertRoom(room);
        var response = ensure().updateChatRoom(entity);
        return response.getStatus() == 200;
    }

    /**
     * 查询群组
     * @return List<ChatGroup>
     */
    @Override
    public List<ChatGroup> listGroups() {
        var groups = ensure().getGroups();
        List<GroupEntity> list = groups.getGroups();
        if (list == null)
            return Collections.emptyList();
        return list.stream().map(ChatUtils::convertGroup).toList();
    }

    @Override
    public boolean updateGroup(ChatGroup room) {
        GroupEntity entity = ChatUtils.convertGroup(room);
        var response = ensure().updateGroup(entity);
        return response.getStatus() == 200;
    }

}
