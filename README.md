# 一、简介

当前主要用于更新青龙面板上的jdck


## 1、DockerHub直接使用

- 拉取镜像：`docker pull jancehui/update-jd-ck:1.0`
- docker运行：`docker run -d -it -p 9833:9833 -m 256m --name update-jd-ck jancehui/update-jd-ck:1.0`
- docker运行（arm架构-未测试过）：`docker run -d -it -p 9833:9833 -m 256m --name update-jd-ck jancehui/update-jd-ck:arm64-v8`
- 跟踪查看日志：`docker logs -f update-jd-ck`


## 2、苹果手机 + alook浏览器 + 快捷指令 快速食用教程

- 1.下载 `alook浏览器` 6块大洋，或者在网上找共享账号下载
- 2.导入快捷指令 `https://www.icloud.com/shortcuts/77486ad4edeb4382bd0a21d82cab065c`
- 3.【可选】修改快捷指令①，第一个url地址`http://api.jancehui.top/xxxxx/getCk`可换成自己的`http://你的IP:9833/xxxxx/getCk`
- 4.【可选】修改快捷指令②，第二个url地址`http://api.jancehui.top/xxxxx/updateJdCk`同样可换成自己的`http://你的IP:9833/xxxxx/updateJdCk`
- 5.修改快捷指令③，第二个url展开，`qlAddr`换成你的青龙地址`http://你的青龙IP:5700`；`clientId`使用青龙面板中添加的应用id；`clientSecret`使用青龙面板中添加的应用密钥
- 6.alook浏览器获取全部ck后，直接执行此快捷指令即可。

Alook浏览器获取ck教程：https://blog.csdn.net/weixin_44011530/article/details/130943930

注：如不执行3、4，需保证`qlAddr`可在公网访问（最好是国内服务器，外网不保证能不能通）

## 3、如何发现ck失效？

- 1.faker库有`ck检测`脚本，如果ck失效后会自动将环境变量禁用，并发送通知（接口已支持更新ck后，自动启用）
- 2.晚上11点左右打开jd看一下，不需要重新登录就是没有失效

# 二、项目介绍

## 1、接口信息

端口：9833

提供接口：

### 获取ck `/v1/getJdCk/getCk`：

- 根据alook浏览器中提取的所有cookies，获取需要的jdck（pt_key=xxx; pt_pin=xxx;）
- 接口调用方式`post`
- 接口调用url实例：`http://xxx.xxx.xxx.xxx:9833/v1/getJdCk/getCk` （具体地址及端口以部署的docker服务为准）
- 请求参数如下：
``` json
{
    "ck": "提取的所有cookies"
}
```

### 更新青龙环境变量 `/v1/ql/updateJdCk`

- 根据前面接口提取的jdck（pt_key=xxx; pt_pin=xxx;）更新环境变量（也可自己手工提取）
- 接口调用方式`post`
- 接口调用url实例：`http://xxx.xxx.xxx.xxx:9833/v1/ql/updateJdCk` （具体地址及端口以部署的docker服务为准）
- 请求参数如下：
``` json
{
    "qlAddr": "青龙应用地址，实例 http://127.0.0.1:5700",
    "clientId": "青龙应用id，实例 213hdsald-n",
    "clientSecret": "青龙应用密钥，实例 213hdeqweqsald-n",
    "ck": "京东ck（pt_key=xxx; pt_pin=xxx;）"
}
```
注：ck环境变量需备注为`pt_pin=xxx`，代码自动识别所有环境变量中是否有此备注的ck，有则更新、无则新增

## 2、项目打包发布 docker 启动

- 运行 `mvn clean install` 将项目打包成jar包（可直接从Releases下载）
- 将 `jar包` 及 `Dockerfile` 文件上传至服务器
- 执行docker构建镜像命令 `docker build -t update-jd-ck .`
- docker执行：`docker run -d -it -p 9833:9833 -m 256m --name update-jd-ck update-jd-ck`
- 跟踪查看日志：`docker logs -f update-jd-ck`

