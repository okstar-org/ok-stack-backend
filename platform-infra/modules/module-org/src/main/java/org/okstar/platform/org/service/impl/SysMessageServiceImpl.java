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

package org.okstar.platform.org.service.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.common.core.utils.OkDateUtils;
import org.okstar.platform.org.domain.SysMessage;
import org.okstar.platform.org.mapper.SysMessageMapper;
import org.okstar.platform.org.service.ISysMessageService;

import java.util.List;

/**
 * 消息管理Service业务层处理
 * 
 * 
 */
@ApplicationScoped
public class SysMessageServiceImpl implements ISysMessageService
{
//    @Inject
    private SysMessageMapper sysMessageMapper;

    /**
     * 查询消息管理
     * 
     * @param id 消息管理主键
     * @return 消息管理
     */
    @Override
    public SysMessage selectSysMessageById(Long id)
    {
        return sysMessageMapper.selectSysMessageById(id);
    }

    /**
     * 查询消息管理列表
     * 
     * @param sysMessage 消息管理
     * @return 消息管理
     */
    @Override
    public List<SysMessage> selectSysMessageList(SysMessage sysMessage)
    {
        return sysMessageMapper.selectSysMessageList(sysMessage);
    }

    /**
     * 新增消息管理
     * 
     * @param sysMessage 消息管理
     * @return 结果
     */
    @Override
    public int insertSysMessage(SysMessage sysMessage)
    {
        sysMessage.setCreateAt(OkDateUtils.now());
        return sysMessageMapper.insertSysMessage(sysMessage);
    }

    /**
     * 修改消息管理
     * 
     * @param sysMessage 消息管理
     * @return 结果
     */
    @Override
    public int updateSysMessage(SysMessage sysMessage)
    {
        sysMessage.setUpdateAt(OkDateUtils.now());
        return sysMessageMapper.updateSysMessage(sysMessage);
    }

    /**
     * 批量删除消息管理
     * 
     * @param ids 需要删除的消息管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMessageByIds(Long[] ids)
    {
        return sysMessageMapper.deleteSysMessageByIds(ids);
    }

    /**
     * 删除消息管理信息
     * 
     * @param id 消息管理主键
     * @return 结果
     */
    @Override
    public int deleteSysMessageById(Long id)
    {
        return sysMessageMapper.deleteSysMessageById(id);
    }
}
