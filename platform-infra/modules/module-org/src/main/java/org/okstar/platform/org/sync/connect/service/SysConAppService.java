package org.okstar.platform.org.sync.connect.service;


import org.okstar.platform.org.sync.connect.SysConEnums;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;

import java.util.List;

public interface SysConAppService {

    void save(OrgIntegrateConf orgIntegrateConf);

    List<OrgIntegrateConf> findAll();

    OrgIntegrateConf findOne(SysConEnums.SysConType type);
}

