# 构建文档
## 必备依赖
- Linux服务器(X86)一台
- 准备子域名 meet.{your_domain}
- Java 17 (选择　GraalVM CE 22.0.0.2)
- 最新　Maven 
### 安装最新Docker
```shell
sudo snap install docker
sudo apt install docker-compose
```


### 部署Openfire（OkStar修改版）
> 官方的存在一些 bug，请使用社区的分支。
- Clone Openfire 项目，执行：`git clone -b 4.7 https://gitee.com/okstar-org/ok-openfire`
- 进入目录，执行：`cd ok-openfire`
- 构建项目, 执行：`./build/docker/buildWithDocker.sh`
- 构建Docker镜像，执行： `docker build . -t okstar-openfire:v4.7`


## 获取源码与子项目
```shell
git clone https://{gitee|github}.com/okstar-org/ok-stack-backend.git
cd ok-stack-backend
# 更新子项目
git submodule update --init --recursive
```

## 准备依赖环境
> 请参考如下步骤完成：

### 启动依赖服务
```shell
# 进入到依赖目录
cd depends
# 启动依赖服务
depends$ docker-compose up -d
```

### 构建项目
- 配置根目录下`pom.xml`文件，在profiles增加自己的profile配置且设置 profile id
- 执行打包，命令如下：
```shell
mvn clean package -P {profileId} -Dmaven.test.skip #最好忽略测试
```
- 输出目标执行程序
> 执行构建打包命令后，输出3种格式的可执行项目，根据需要自己选择即可。
```shell
ls distribution/target
okstack-platform-assembly           #解压目录
okstack-platform-assembly.tar.gz    #tar.gz格式(unix/linux)
okstack-platform-assembly.zip       #zip格式(windows)
```
- 准备运行环境
> 首次执行启动前将｀libsigar-amd64-linux-1.6.4.so｀拷贝到系统lib目录｀/usr/lib/｀下。
```shell
sudo cp platform-infra/commons/common-base/src/main/resources/lib/libsigar-amd64-linux-1.6.4.so /usr/lib/
```

- 运行项目
```shell
# 到项目目录下
cd okstack-platform-assembly/okstack-platform
# 启动项目
./bin/startup.sh
# 停止项目
./bin/shutdown.sh
```

# 配置前端
到此后端构建已经全部完成具备运行条件，接下来就是配置前端项目`OkStack-UI`。
> gitee：https://gitee.com/okstar-org/ok-stack-ui
> github：https://gitee.com/okstar-org/ok-stack-ui

# 配置项目
请按照构建文档完成前端部署，下一步则请参考[配置文档](./configurations.md)配置项目。