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

package org.okstar.platform.common.os;

import lombok.SneakyThrows;
import org.hyperic.sigar.*;
import org.okstar.platform.common.web.OkWebUtil;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class HostUtils {

    private HostUtils() {

    }


    public static HostInfo getHostInfo() throws SigarException {
        Sigar sigar = new Sigar();
        NetInfo netInfo = sigar.getNetInfo();
        return HostInfo.builder()
                .fqdn(sigar.getFQDN())
                .hostName(netInfo.getHostName())
                .publicIp(OkWebUtil.getPublicIp())
                .pid(sigar.getPid())
                .build();
    }

    /**
     * 获取全部主机信息
     *
     * @return
     */
    @SneakyThrows
    public static HostServerInfos getWholeInfo() {
        //服务器信息
        Properties properties = System.getProperties();
        Map<String, String> env = System.getenv();
        InetAddress localHost = InetAddress.getLocalHost();

        Sigar sigar = new Sigar();



        //系统信息
        OperatingSystem instance = OperatingSystem.getInstance();
            instance.getName();

        HostServerInfos.OsInfo systemInfo = HostServerInfos.OsInfo.builder()
                .vendor(instance.getVendorName())
                .arch(instance.getArch())
                .description(instance.getDescription())
                .version(instance.getVersion()).build();

        //CPU信息


        List<HostServerInfos.CpuInfo> cpuInfoList = new ArrayList<>();
        CpuInfo[] infoList = sigar.getCpuInfoList();
        CpuPerc[] cpuList = sigar.getCpuPercList();
        for (int i = 0; i < infoList.length; i++) {
            CpuInfo cpuInfo = infoList[i];
            CpuPerc cpu = cpuList[i];
            cpuInfoList.add(HostServerInfos.CpuInfo.builder()
                    .mhz(cpuInfo.getMhz())
                    .vendor(cpuInfo.getVendor())
                    .cacheSize(cpuInfo.getCacheSize())
                    .freqUser(cpu.getUser())
                    .freqSys(cpu.getSys())
                    .freqWait(cpu.getWait())
                    .freqNice(cpu.getNice())
                    .freqIdle(cpu.getIdle())
                    .freqCombined(cpu.getCombined())
                    .build());
        }

        //JVM信息
        Runtime runtime = Runtime.getRuntime();
        HostServerInfos.JvmInfo jvmInfo = HostServerInfos.JvmInfo.builder()
                .totalMemory(runtime.totalMemory())
                .freeMemory(runtime.freeMemory())
                .availableProcessors(runtime.availableProcessors())
                .version(properties.getProperty("version"))
                .vendor(properties.getProperty("java.vendor"))
                .home(properties.getProperty("java.home"))
                .specificationVersion(properties.getProperty("java.specification.version"))
                .tmpdir(properties.getProperty("java.io.tmpdir"))
                .dirs(properties.getProperty("java.ext.dirs"))
                .build();

        //内存信息
        Mem mem = sigar.getMem();
        Swap swap = sigar.getSwap();
        HostServerInfos.MemoryInfo memoryInfo = HostServerInfos.MemoryInfo.builder()
                .memoryTotal(mem.getTotal() / (1024 * 1024L))
                .memoryUsed(mem.getUsed() / (1024 * 1024L))
                .memoryFree(mem.getFree() / (1024 * 1024L))
                .swapTotal(swap.getTotal() / (1024 * 1024L))
                .swapUsed(swap.getUsed() / (1024 * 1024L))
                .swapFree(swap.getFree() / (1024 * 1024L))
                .build();

        return HostServerInfos.builder()
                .systemInfo(systemInfo)
                .cpuInfo(cpuInfoList)
                .jvmInfo(jvmInfo)
                .memoryInfo(memoryInfo).build();
    }
}