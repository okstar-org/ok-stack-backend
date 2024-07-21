# Docker 构建文档

## 执行 Maven 打包
    mvn package -P dev -Dquarkus.profile=prod -Dmaven.test.skip

## 构建 Docker 镜像
    docker build . -t ok-stack

## 运行Docker测试
    docker run --rm -p 80:80  --name ok-stack-run ok-stack