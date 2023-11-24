package org.okstar.platform.org.rbac.resource.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrgRbacUserResponseVo implements Serializable {

    private Long accountId;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;


    /**
     * 用户头像
     */
    private String avatar;
}
