# 构建文档
## 必备依赖
### 准备域名
```shell
# 本地址采用如下域名，自己根据情况修改
okstar.org
```
### 准备开放的端口
```shell
mysql: 13306  
ldap: 10389
keycloak:
- 18043 
- 18443
- 10990
```
### 创建子域名
- 认证服务：kc.okstar.org 
- 管理服务：stack.okstar.org
- 通讯服务：meet.okstar.org

### 准备 Linux服务器(X64)一台
```shell
sudo apt update
sudo apt dist-upgrade -y
```

### 安装Docker或者Podman
> Podman 参考：https://podman.io/docs/installation

### 安装 Git
```shell
sudo apt install git #ubuntu
sudo dnf install git #centos
```

### 安装 Java 17 
推荐　GraalVM CE 22.0.0.2
```shell
sudo apt install java-openjdk-17
```

### 安装 Maven 
```shell
sudo apt install maven
```

## 准备安装目录
```shell
# 根据本机存储情况，创建项目工作目录。
mkdir ~/okstar && cd ~/okstar
```


### 部署 Openfire（OkStar修改版）
> 官方的存在一些 bug，请使用社区的分支。
- Clone Openfire 项目，执行：`git clone -b 4.7 https://gitee.com/okstar-org/ok-openfire`
- 进入目录，执行：`cd ok-openfire`
- 构建项目, 执行：`mvn clean package -Dmaven.test.skip`
- 构建Docker镜像
```shell

# 如果在大陆执行超时，请执行下面代码单独拉取java（具体地址根据情况选择地址，有可能各家云网络策略导致失败）
docker pull mirror.ccs.tencentyun.com/openjdk:11-jre
docker build . -t okstar-openfire:v4.7
```


## 构建项目
### 下载 OkStack 源码
```shell
git clone https://{gitee|github}.com/okstar-org/ok-stack-backend.git
cd ok-stack-backend
# 更新子项目
git submodule update --init --recursive
```

### 初始化依赖环境
```shell
# 进入到依赖目录
cd depends

# 修改配置 docker-compose.yml
# 设置 Mariadb 数据库root密码
MYSQL_ROOT_PASSWORD=okstar

# 配置keycloak
- KC_DB_USERNAME=root
- KC_DB_PASSWORD=okstar
- KC_HOSTNAME_URL=https://kc.okstar.org
- KC_HOSTNAME_ADMIN_URL=https://kc.okstar.org
- KEYCLOAK_ADMIN=admin
- KEYCLOAK_ADMIN_PASSWORD=okstar

# 拉取镜像
docker pull mirror.ccs.tencentyun.com/library/mariadb:10.6.15
docker pull mirror.ccs.tencentyun.com/keycloak/keycloak:latest
docker pull mirror.ccs.tencentyun.com/library/nginx
docker pull mirror.ccs.tencentyun.com/library/maven

# 启动依赖服务
docker-compose up -d #docker
podman-compose up -d #podman
```

### 打包项目
- 配置根目录下`pom.xml`文件，在profiles增加自己的profile配置且设置 profile id
- 执行打包，命令如下：
```shell
docker run -it -v=/root/.m2:/root/.m2 -v=.:/work -w /work --name mvn maven mvn clean package -P dev -Dmaven.test.skip 
```

- 输出目标执行程序
> 执行构建打包命令后，输出3种格式的可执行项目，根据需要自己选择即可。
```shell
ls distribution/target
ok-stack-assembly           #解压目录
ok-stack-assembly.tar.gz    #tar.gz格式(unix/linux)
ok-stack-assembly.zip       #zip格式(windows)
```
- 准备运行环境
> 首次执行启动前将｀libsigar-amd64-linux-1.6.4.so｀拷贝到系统lib目录｀/usr/lib/｀下。
```shell
sudo cp platform-infra/commons/common-base/src/main/resources/lib/libsigar-amd64-linux-1.6.4.so /usr/lib/
```

- 运行项目
```shell
# 到项目目录下
cd ok-stack-assembly/ok-stack
# 启动项目
./bin/startup.sh
# 停止项目
./bin/shutdown.sh
```
- systemd 管理
需要手动将 `BASE_DIR` 改为存放 **ok-stack-assembly** 的路径
注意：如果有严格权限管理的话，User 指定为所属用户，如 root

```bash
# 安装 ok-stack-backend.service
$ sudo install -Dm0644 ok-stack-backend.service -t /etc/systemd/system/

# 重新加载 systemd 配置
$ sudo systemctl daemon-reload

# 设置 ok-stack-backend.service 开机自启动并启动
$ sudo systemctl enable --now ok-stack-backend.service

# 查看 ok-stack-backend.service 服务状态
$ sudo systemctl status ok-stack-backend.service

# 停止 ok-stack-backend.service 服务
$ sudo systemctl stop ok-stack-backend.service

# 禁用 ok-stack-backend.service 开机自启
$ sudo systemctl disable ok-stack-backend.service
```

# 配置前端
到此后端构建已经全部完成具备运行条件，接下来就是配置前端项目`OkStack-UI`。
> gitee：https://gitee.com/okstar-org/ok-stack-ui
> github：https://gitee.com/okstar-org/ok-stack-ui

# 配置项目
请按照构建文档完成前端部署，下一步则请参考[配置文档](./configurations.md)配置项目。
