# 构建文档
## 必备依赖
- Java 17 (选择　GraalVM CE 22.0.0.2)
- 最新　Maven
- 最新　Docker
- Linux服务器(X86)一台
- 准备子域名 meet.{your_domain}

## 获取源码与子项目
```shell
git clone https://{gitee|github}.com/okstar-org/ok-stack-backend.git
cd ok-stack-backend
# 更新子项目
git submodule update --init --recursive
```

## 准备依赖环境
> 请参考如下步骤完成：
### 部署 Openfire
> 官方的存在一些 bug，请使用社区的分支。
- Clone Openfire 项目，执行：`git clone -b 4.7 https://gitee.com/okstar-org/ok-openfire`
- 进入目录，执行：`cd ok-openfire`
- 构建项目, 执行：`./build/docker/buildWithDocker.sh`
- 构建Docker镜像，执行： `docker build . -t okstar-openfire:v4.7`

### 启动依赖服务
```shell
# 进入到依赖目录
cd depends
# 启动依赖服务
depends$ docker-compose up -d
```

## 配置 KeyCloak 服务
- 登录地址：`http://localhost:8043/admin/`
- 输入帐号：admin,okstar登录
- 到左上角，选择 `okstar` realm (如果没有则增加okstar，按如下配置，保存即可)
- 创建Client `okstack`
```text
======>General Settings<=========
Client ID * : okstack
Name        : OkStack

======>Capability config<=========
Client authentication: ON
Authentication flow: ON [Standard flow]  ON [Direct access grants]

======>Access settings<=========
Root URL:   http://localhost:9100
Home URL:   http://localhost:9100/q/swagger-ui/
Valid redirect URIs: *
Valid post logout redirect URIs:    http://localhost:9100/q/swagger-ui/
Admin URL:  http://localhost:9100
```
- 第三个Tab [credentials]
```text
Client Authenticator：Client Id and Secret 
Client Secret：点击复制和保存 (保留后续配置到项目) 
```
- 点击左下角  `User Federation`，选择增加`LDAP`
> General options
```text
UI display name *   :ldap 
Vendor *            :Other
```
> Connection and authentication settings
```text
Connection URL *    :ldap://apacheds:10389
Connection pooling  :On
Connection timeout  :10000
Bind type *         :simple
Bind DN *           :uid=admin,ou=system
Bind credentials *  :secret

# 可以点击Test测试是否成功
```
> LDAP searching and updating
```text
Edit mode *                 :WRITABLE
Users DN *                  :ou=users,dc=okstar,dc=org
Username LDAP attribute *   :uid
RDN LDAP attribute *        :uid
UUID LDAP attribute *   :entryUUID
User object classes *   :inetOrgPerson, organizationalPerson
Search scope        :Subtree
Read timeout        :10000
Pagination          :On
```

> Synchronization settings

```text
Import users        :On
Sync Registrations  :On
Periodic full sync  :On
Full sync period    :604800
Periodic changed users sync :On
Changed users sync period   :86400
```
- 点击`Save`保存

### Keycloak 配置邮箱（可选）
> 在找回密码使用该配置，分两步完成
#### 第一步
- 1，选择Realm`master`
- 2，点击菜单`Users`
- 3，点击菜单`admin`
- 4，选择`Details`-》`Email`输入邮箱发送地址即可。
#### 第二步
- 1，选择Realm`okstar`
- 2，点击菜单`Realm settings`
- 3，选择tab`Email`
- 4，输入邮箱相关信息即可。

## 配置启动 Openfire 服务器
> 打开服务器地址：`http://localhost:9090/`
- 第一步，选择合适的语言
- 第二步，服务器设置不用修改
- 第三步，使用标准数据库
  - 选择MySQL数据库
  - 修改host和数据库名称其他不变，为：`db:3306/openfire`
  - 用户名:`root`，密码:`okstar`
- 第四步，设置LDAP服务器
  - 目录服务器 (LDAP)
  - 服务类型，选择“其他”
  - 设置连接，Protocol:`ldap`	主机:`apacheds`	端口:`10389`
  - 基础的DN:`ou=users,dc=okstar,dc=org`
  - 管理员DN:`uid=admin,ou=system`，密码: `secret`，点击测试
- 第五步，选择LDAP管理员
  - 第一项，输入`okstar`
  - 第二项，选择第一个
  - 第三项，点击`添加`列出用户即可，点击`完成`
- 第六步，登录到主界面
  - 输入管理员`okstar`和密码`okstar`。
  - 点击登录

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