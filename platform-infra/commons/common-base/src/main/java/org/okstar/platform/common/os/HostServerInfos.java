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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Accessors(chain = true)
public class HostServerInfos {

    private HostInfo serverInfo;

    private OsInfo systemInfo;

    private List<CpuInfo> cpuInfo;

    private JvmInfo jvmInfo;

    private MemoryInfo memoryInfo;



    /**
     * 系统信息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @Accessors(chain = true)
    public static class OsInfo {
        //操作系统名称
        private String name;
        //供应商
        private String vendor;
        //内核构架
        private String arch;
        //操作系统的描述
        private String description;
        //操作系统的版本号
        private String version;
    }

    /**
     * CPU信息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @Accessors(chain = true)
    public static class CpuInfo {
        //CPU的总量MHz
        private int mhz;
        //CPU的厂商
        private String vendor;
        //CPU型号类别
        private String model;
        //缓冲缓存数量
        private long cacheSize;
        //CPU的用户使用率
        private double freqUser;
        //CPU的系统使用率
        private double freqSys;
        //CPU的当前等待率
        private double freqWait;
        //CPU的当前错误率
        private double freqNice;
        //CPU的当前空闲率
        private double freqIdle;
        //CPU总的使用率
        private double freqCombined;
    }

    /**
     * JVM信息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @Accessors(chain = true)
    public static class JvmInfo {
        //JVM可以使用的总内存
        private long totalMemory;
        //JVM可以使用的剩余内存
        private long freeMemory;
        //JVM可以使用的处理器个数
        private int availableProcessors;
        //Java的运行环境版本
        private String version;
        //Java的运行环境供应商
        private String vendor;
        //Java的安装路径
        private String home;
        //Java运行时环境规范版本
        private String specificationVersion;
        //Java的类路径
//        private String classPath;
        //Java加载库时搜索的路径列表
//        private String libraryPath;
        //默认的临时文件路径
        private String tmpdir;
        //扩展目录的路径
        private String dirs;
    }

    /**
     * 内存信息
     */
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    @Builder
    @Accessors(chain = true)
    public static class MemoryInfo {
        //内存总量
        private long memoryTotal;
        //当前内存使用量
        private long memoryUsed;
        //当前内存剩余量
        private long memoryFree;
        //交换区总量
        private long swapTotal;
        //当前交换区使用量
        private long swapUsed;
        //当前交换区剩余量
        private long swapFree;
    }
}