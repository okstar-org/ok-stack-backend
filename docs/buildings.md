# 构建文档
## 必备依赖
- Java 17 (选择GraalVM CE 22.0.0.2)
- Maven 3.6.x

> 支持两种构建方式
- 依赖本地环境（Docker）
- 依赖社区环境（社区已经搭建了完整测试依赖）


## 获取源码与子项目
```shell
git clone https://gitee.com/okstar-org/ok-stack-backend.git
cd ok-stack-backend
# 更新子项目
git submodule update --init --recursive
```

## 依赖社区环境
- Maven 构建时使用 `dev-okstar` profile，如下：
> 编译
```shell
# 依赖社区依赖服务
mvn clean compile -P dev-okstar
```

> 打包
```shell
# 输出目录 distribution/target/okstack-platform-assembly.zip
mvn package -P dev-okstar -Dmaven.test.skip 
```

> 运行项目
```shell
# 进入项目目录
cd distribution/target/okstack-platform-assembly/okstack-platform
# 启动项目
chmod a+x ./bin/*.sh
./bin/startup.sh 
```

## 依赖本地（依赖Docker）
> 按照如下步骤完成。

### 部署 Openfire
- Clone Openfire 项目，执行：`git clone -b 4.7 https://gitee.com/okstar-org/ok-openfire`
- 进入目录，执行：`cd ok-openfire`
- 构建项目, 执行：`./build/docker/buildWithDocker.sh`
- 构建Docker镜像，执行： `docker build . -t okstar-openfire:v4.7`

### 启动 Docker 依赖服务
```shell
# 进入到依赖目录
cd depends

# 启动依赖服务
depends$ docker-compose up -d
```

### 配置 KeyCloak 服务
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

### 配置启动Openfire服务器
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
- Maven 构建时使用 `dev-okstar` profile，如下：
```shell
# 依赖社区依赖服务
mvn clean compile -P dev-okstar
```

# 测试
## 启动依赖服务
分别启动如下两个服务
- ModuleOrgApplication
- ModuleSystemApplication

## 创建新帐号
- 执行如下单元测试即可。
```shell
org.okstar.platform.auth.service.PassportServiceImplTest.signUp
```
- 通过日志，查找用户名
```shell
# 找到如下日志(用户名：B5Ev0cK4i2Lq，你们可能不一样)
2023-10-15 15:32:16,260 INFO  [org.oks.pla.aut.ser.PassportServiceImplTest] (main) result=>SignUpResultDto(userId=7, username=B5Ev0cK4i2Lq)
```

## 测试接口
> 通过如上步骤：用户: B5Ev0cK4i2Lq 密码: okstar
- 打开Swagger接口测试页面 `http://localhost:9000/q/swagger-ui/`
- 找到接口`/user/findAll`输入用户名和密码执行，得到如下则后端配置完全成功！
```text
{
  "ts": null,
  "code": 200,
  "data": [
    {
      "id": 1,
      "disabled": true,
      "username": "B5Ev0cK4i2Lq",
      "firstName": "Ok",
      "lastName": "Star",
      ...
    },
  ],
  "msg": null,
  "extra": {}
}
```
> 通过上述流程就确认服务搭建通过！

# 接口访问

## 注册
- 打开Swagger接口测试页面 `http://localhost:9000/q/swagger-ui/`
- 调用方法 `/password/signUp`,输入信息如下：
```text
{
  "ts": 0,
  "iso": "CN",
  "accountType": "phone",
  "account": "<手机号>",
  "password": "<password>",
  "firstName": "<firstName>",
  "lastName": "<lastName>"
}
```
- 返回成功即注册成功。

## 查询全部用户（测试）
https://localhost/api/infra/org/user/findAll
