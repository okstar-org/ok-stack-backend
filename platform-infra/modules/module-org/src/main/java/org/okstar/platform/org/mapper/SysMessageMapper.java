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

package org.okstar.platform.org.mapper;


import org.okstar.platform.org.domain.SysMessage;

import javax.inject.Singleton;
import java.util.List;

/**
 * 消息管理Mapper接口
 * 
 * 
 */
@Singleton

public interface SysMessageMapper 
{
    /**
     * 查询消息管理
     * 
     * @param id 消息管理主键
     * @return 消息管理
     */
    SysMessage selectSysMessageById(Long id);

    /**
     * 查询消息管理列表
     * 
     * @param sysMessage 消息管理
     * @return 消息管理集合
     */
    List<SysMessage> selectSysMessageList(SysMessage sysMessage);

    /**
     * 新增消息管理
     * 
     * @param sysMessage 消息管理
     * @return 结果
     */
    int insertSysMessage(SysMessage sysMessage);

    /**
     * 修改消息管理
     * 
     * @param sysMessage 消息管理
     * @return 结果
     */
    int updateSysMessage(SysMessage sysMessage);

    /**
     * 删除消息管理
     * 
     * @param id 消息管理主键
     * @return 结果
     */
    int deleteSysMessageById(Long id);

    /**
     * 批量删除消息管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteSysMessageByIds(Long[] ids);
}
