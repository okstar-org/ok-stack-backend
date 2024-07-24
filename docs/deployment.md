# 部署文档
## 必备依赖
### 准备域名
```shell
# 本地址采用如下域名，自己根据情况修改
okstar.org
```
### 准备开放的端口
```shell
mysql: 13306
keycloak:
    - 18080
    - 18443
openfire: 
    - 9090
    - 5222
ok-stack:
    - 1080
```
### 创建子域名3个
- 认证服务：kc.example.org 
- 管理服务：stack.example.org
- 通讯服务：meet.example.org

## 服务部署
- 请参考`ok-stack-installer`一键自动化部署。
```shell
git clone https://github.com/okstar-org/ok-stack-installer.git
```

## 配置项目
请按照构建文档完成前端部署，下一步则请参考[配置文档](./configurations.md)配置项目。
