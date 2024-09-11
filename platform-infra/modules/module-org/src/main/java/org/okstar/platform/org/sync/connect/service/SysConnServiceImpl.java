///*
// * * Copyright (c) 2022 船山信息 chuanshaninfo.com
// * OkStack is licensed under Mulan PubL v2.
// * You can use this software according to the terms and conditions of the Mulan
// * PubL v2. You may obtain a copy of Mulan PubL v2 at:
// *          http://license.coscl.org.cn/MulanPubL-2.0
// * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
// * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
// * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
// * See the Mulan PubL v2 for more details.
// * /
// */
//
//package org.okstar.platform.org.sync.connect.service;
//
//
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.inject.Inject;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.okstar.platform.common.asserts.OkAssert;
//import org.okstar.platform.common.bean.OkBeanUtils;
//import org.okstar.platform.org.sync.connect.SysConEnums;
//import org.okstar.platform.org.sync.connect.SysConnFactory;
//import org.okstar.platform.org.sync.connect.SysConnector;
//import org.okstar.platform.org.sync.connect.dept.service.SysConDeptService;
//import org.okstar.platform.org.sync.connect.domain.SysConUser;
//import org.okstar.platform.org.sync.connect.dto.SysConUserDTO;
//import org.okstar.platform.org.sync.connect.dto.SysOrgDTO;
//import org.okstar.platform.org.sync.connect.proto.SysConnAccessToken;
//import org.okstar.platform.org.sync.connect.proto.SysConnDepartment;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Consumer;
//
///**
// * 组织连接器
// */
//@Slf4j
//@ApplicationScoped
//public class SysConnServiceImpl implements SysConnService {
//
//    @Inject
//    private SysConAppService sysConAppService;
//    @Inject
//    private SysConnFactory sysConnFactory;
//    @Inject
//    private SysConDeptService sysConDeptService;
//
//
//    @Override
//    public synchronized void sync(String orgId, SysConEnums.SysConType type) {
//        log.info("sync orgId:{} type:{}", orgId, type);
//
//        SysOrgDTO org = null;//sysOrgService.findById(orgId);
//        OkAssert.notNull(org, "orgId异常!");
//        OrgIntegrateConf app = loadApp(type, org);
//        OkAssert.notNull(app, "type异常!");
//
//        syncDept(orgId, app);
//    }
//
//
//    @Override
//    public synchronized void syncUser(String orgId, SysConEnums.SysConType type) {
//        log.info("syncUser orgId:{} type:{}", orgId, type);
//
//        SysOrgDTO org = new SysOrgDTO();
//        OkAssert.notNull(org, "orgId异常!");
//        OrgIntegrateConf app = loadApp(type, org);
//        OkAssert.notNull(app, "type异常!");
//
//        syncUser(orgId, app);
//    }
//
//
//    @Override
//    public SysConnAccessToken test(String orgId, SysConEnums.SysConType type) {
//
//        SysOrgDTO org = new SysOrgDTO();
//
//        OrgIntegrateConf app = loadApp(type, org);
//
//        SysConnAccessToken accessToken = getAccessToken(orgId, app);
//
//        return accessToken;
//    }
//
//    private OrgIntegrateConf loadApp(SysConEnums.SysConType type, SysOrgDTO org) {
//        OrgIntegrateConf q = new OrgIntegrateConf();
//        q.setOrgId(org.getId());
//        q.setType(type);
//        List<OrgIntegrateConf> apps = sysConAppService.findAll(q);
//        OkAssert.isTrue(!apps.isEmpty(), "无数据！");
//        OrgIntegrateConf app = apps.get(0);
//        return app;
//    }
//
//
//    public SysConnAccessToken getAccessToken(String orgId, OrgIntegrateConf app) {
//
//        log.info("Using orgId:{}", orgId);
//        log.info("Using app:{}", app);
//
//        SysConnector connector = sysConnFactory.getConnector(app);
//        log.info("Using sysConnector:{}", connector);
//
//        return connector.getAccessToken(orgId, app);
//    }
//
//    @Override
//    public synchronized void syncAll() {
////        orgConnectorMap.forEach((orgId, con) -> {
////            SysOrgDTO org = sysOrgService.findById(orgId);
////
////            OrgIntegrateConf q = new OrgIntegrateConf();
////            q.setOrgId(org.getId());
////            q.setType(SysConEnums.SysConType.WX);
////            List<OrgIntegrateConf> apps = sysConAppService.findAll(q);
////            OkAssert.notEmpty(apps, "无数据！");
////
////            OrgIntegrateConf app = apps.get(0);
////            syncDept(orgId, con);
////        });
//    }
//
//    /**
//     * 同步员工
//     *
//     * @param orgId
//     * @param app
//     */
//    private void syncUser(String orgId, OrgIntegrateConf app) {
//        /**
//         * TODO 飞书部门加载不支持
//         */
//        OkAssert.isTrue(app.getType() != SysConEnums.SysConType.FS, "暂不支持飞书！");
//
//        SysConDeptDTO q2 = new SysConDeptDTO();
//        q2.setOrgId(orgId);
//        q2.setAppId(app.id);
//
//        List<SysConDeptDTO> deptDTOS = sysConDeptService.findAll(q2);
//        if (CollectionUtils.isEmpty(deptDTOS)) {
//            log.warn("部门为空！");
//            return;
//        }
//
//        SysConnector connector = sysConnFactory.getConnector(app);
//        log.info("Using sysConnector:{}", connector.getType());
//
//        deptDTOS.forEach(dept -> {
//            List<SysConUser> list = connector.getUserIdList(orgId, app, dept.getSrcId());
//            OkAssert.notNull(list, "未实现！");
//            log.info("getUserIdList dept:{}=>{}", dept.getName(), list);
//
//            list.forEach(sysConUser -> {
//                log.info("用户:{}", sysConUser);
//
//                SysConUserDTO q = new SysConUserDTO();
//                q.setUserId(sysConUser.getUserId());
//                q.setUnionId(sysConUser.getUnionId());
//                q.setAppId(sysConUser.getAppId());
//
//                q.setDeptId(dept.getId());
//                q.setOrgId(dept.getOrgId());
//
//                SysConUserDTO userDTO = Optional.ofNullable(sysConUserService.findOne(q))
//                        .orElse(q);
//
//                OkBeanUtils.copyPropertiesTo(sysConUser, userDTO);
//
//                SysConUserDTO save = sysConUserService.save(userDTO);
//                log.info("保存用户【{}】成功", save.getName());
//
//            });
//        });
//    }
//
//    /**
//     * 同步部门
//     *
//     * @param orgId
//     * @param app
//     */
//    private void syncDept(String orgId, OrgIntegrateConf app) {
//        /**
//         * TODO 飞书部门加载不支持
//         */
//        OkAssert.isTrue(app.getType() != SysConEnums.SysConType.FS, "暂不支持飞书！");
//
//        int shitLevel = app.getType() == SysConEnums.SysConType.WX ? -1 : 0;
//
//        getDept(orgId, app, "1", (dept) -> {
//            log.info("syncDept:{}", dept);
//
//            SysConDeptDTO q = new SysConDeptDTO();
//            q.setOrgId(orgId);
//            q.setAppId(dept.getAppId());
//            q.setType(dept.getType());
//            q.setSrcId(dept.getId());
//            q.setSrcParentId(dept.getParentId());
//            q.setName(dept.getName());
//
//            SysConDeptDTO q2 = new SysConDeptDTO();
//            q2.setOrgId(orgId);
//            q2.setSrcId(dept.getParentId());
//            q2.setAppId(dept.getAppId());
//            q2.setType(dept.getType());
//            SysConDeptDTO parent = sysConDeptService.findOne(q2);
//            if (parent != null) {
//                q.setLevel(parent.getLevel() + 1);
//            } else {
//                q.setLevel(shitLevel);
//            }
//
//            SysConDeptDTO deptDTO;
//            //查询已存在的部门
//            List<SysConDeptDTO> saved = sysConDeptService.findAll(q);
//            if (saved.isEmpty()) {
//                log.info("保存:{}！", q.getName());
//                deptDTO = sysConDeptService.save(q);
//            } else {
//                log.info("已存在:{}！", q.getName());
//                deptDTO = saved.get(0);
//            }
//
//            log.info("同步:{}完成！", deptDTO.getName());
//        });
//    }
//
//    public void getDept(
//            String orgId,
//            OrgIntegrateConf app,
//            String parentId,
//            Consumer<SysConnDepartment> consumer) {
//
//        SysConnector connector = sysConnFactory.getConnector(app);
//        log.info("Using sysConnector:{}", connector);
//
//        List<SysConnDepartment> list = connector.getDepartmentList(orgId, app, parentId);
//        log.info("getDepartmentList parent:{}=>{}", parentId, list);
//
//        for (SysConnDepartment department : list) {
//            if (consumer != null) {
//                consumer.accept(department);
//            }
//
//            if (!department.getId().equals(parentId)) {
//                getDept(orgId, app, department.getId(), consumer);
//            }
//        }
//    }
//}
