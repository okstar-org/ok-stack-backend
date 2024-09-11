package org.okstar.platform.org.sync.connect.dto;

import lombok.Data;
import org.okstar.platform.core.user.UserDefines;
import org.okstar.platform.org.defined.StaffDefines;

/**
 * 同步模块-用户
 */
@Data
public class SysConUser {

    StaffDefines.Source source;

    private String appId;

    private String userId;

    private String unionId;

    private String name;

    UserDefines.Gender gender;

    Boolean leader;

    
    Boolean boss;

    
    String mobile;

    
    String telephone;

    
    Boolean active;

    
    Boolean admin;

    
    String remark;

    
    String avatar;
    
    String hideMobile;
    
    String title;

    /**
     * 雇佣时间
     */
    String hiredDate;

    String workPlace;
    
    String jobNumber;
    
    String sateCode;
    
    String email;
    
    String address;
}
