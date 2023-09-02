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

package org.okstar.platform.org.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 消息管理对象 sys_message
 * 
 *  
 * @date 2021-10-11
 */
public class SysMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 消息名称 */
    private String messageName;

    /** 消息内容 */
    private String messageContent;

    /** 消息状态 */
    private String messageState;

    /** 消息类型 */
    private String messageType;

    /** 消息来源 */
    private String messageSource;

    /** 业务类型 */
    private Long businessType;

    /** 处理人 */
    private Long operatorId;

    /** 处理连接 */
    private String operatorLink;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMessageName(String messageName) 
    {
        this.messageName = messageName;
    }

    public String getMessageName() 
    {
        return messageName;
    }
    public void setMessageContent(String messageContent) 
    {
        this.messageContent = messageContent;
    }

    public String getMessageContent() 
    {
        return messageContent;
    }
    public void setMessageState(String messageState)
    {
        this.messageState = messageState;
    }

    public String getMessageState()
    {
        return messageState;
    }
    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getMessageType()
    {
        return messageType;
    }
    public void setMessageSource(String messageSource)
    {
        this.messageSource = messageSource;
    }

    public String getMessageSource()
    {
        return messageSource;
    }
    public void setBusinessType(Long businessType)
    {
        this.businessType = businessType;
    }

    public Long getBusinessType()
    {
        return businessType;
    }
    public void setOperatorId(Long operatorId)
    {
        this.operatorId = operatorId;
    }

    public Long getOperatorId() 
    {
        return operatorId;
    }
    public void setOperatorLink(String operatorLink) 
    {
        this.operatorLink = operatorLink;
    }

    public String getOperatorLink() 
    {
        return operatorLink;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("messageName", getMessageName())
            .append("messageContent", getMessageContent())
            .append("messageState", getMessageState())
            .append("messageType", getMessageType())
            .append("messageSource", getMessageSource())
            .append("businessType", getBusinessType())
            .append("operatorId", getOperatorId())
            .append("operatorLink", getOperatorLink())
            .append("createBy", getCreateBy())
            .append("createAt", getCreateAt())
            .append("updateBy", getUpdateBy())
            .append("updateAt", getUpdateAt())
            
            .toString();
    }
}
