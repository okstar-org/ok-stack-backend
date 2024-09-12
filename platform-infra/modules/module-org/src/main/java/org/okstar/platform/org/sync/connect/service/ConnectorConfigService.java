package org.okstar.platform.org.sync.connect.service;


import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;

import java.util.List;

public interface ConnectorConfigService {

    void save(OrgIntegrateConf orgIntegrateConf);

    List<OrgIntegrateConf> findAll();

    OrgIntegrateConf findOne(ConnectorDefines.Type type);
}

