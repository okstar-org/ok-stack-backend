# 构建文档
##  Profile说明与选择
- <空> 本地测试，使用h2内存数据库
- dev 本地测试，使用MariaDB本地(local)数据库
- dev-okstar 社区测试，使用MariaDB社区(okstar.org.cn)数据库

## 编译
```shell
# 本地测试
mvn clean compile
# dev 本地测试
mvn clean compile -P dev
# dev-okstar 社区测试
mvn clean compile -P dev-okstar
```
