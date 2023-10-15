# 构建文档
##  Profile说明与选择
- <空> 本地测试，使用h2内存数据库
- dev 本地测试，使用MariaDB本地(local)数据库
- dev-okstar 社区测试，使用MariaDB社区(okstar.org.cn)数据库

## 编译
```shell
# 本地测试
mvn clean compile
# dev 本地测试 连接本地Docker启动的MariaDB数据库
mvn clean compile -P dev
# dev-okstar 社区测试 连接社区的MariaDB数据库
mvn clean compile -P dev-okstar
```


## 测试方法

### 启动依赖
```shell
# 进入到依赖
cd depends

# 启动MariaDB数据库与Keycloak认证服务器
depends$ docker-compose up -d
```

### 配置Keycloak的LDAP服务
- 登录：http://localhost:8080/admin/
- 输入帐号：admin,okstar登录
- 到左上角，选择 `okstar` ream (如果没有则增加okstar，按如下配置，保存即可)
> 第一个Tab [Settings]
```text
======>General Settings<=========

Client ID *:    okstack
Name:   OkStack
Always display in UI 

======>Access settings<=========

Root URL :http://localhost:9100
Home URL:http://localhost:9100/q/swagger-ui/
Valid redirect URIs: *
Valid post logout redirect URIs: http://localhost:9100/q/swagger-ui/
Admin URL :http://localhost:9100

======>Capability config<=========
Client authentication: ON
Authentication flow: ON [Standard flow]  ON [Direct access grants]



```
> 第三个Tab [credentials]
```text
Client Authenticator：Client Id and Secret 
Client Secret：点击复制和保存 
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


### 启动依赖服务
分别启动如下两个服务
- ModuleOrgApplication
- ModuleSystemApplication

### 测试创建新帐号
- 执行如下单元测试即可。
```shell
org.okstar.platform.auth.service.PassportServiceImplTest.signUp
```
- 通过日志，查找用户名
```shell
# 找到如下日志(用户名：B5Ev0cK4i2Lq)
2023-10-15 15:32:16,260 INFO  [org.oks.pla.aut.ser.PassportServiceImplTest] (main) result=>SignUpResultDto(userId=7, username=B5Ev0cK4i2Lq)
```

### 测试认证
> 通过如上步骤：用户: B5Ev0cK4i2Lq 密码: okstar
- 打开Swagger接口测试页面 `http://localhost:9200/q/swagger-ui/`
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
