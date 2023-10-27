# 生成镜像，需服务器有openjdk:8镜像。docker pull openjdk:8
FROM openjdk:8
# 将jar包重命名
COPY *.jar /app.jar
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 指定服务端口
CMD ["--server.port=9833"]
# 指定对外开放端口
EXPOSE 9833
# 服务启动命令（暂时未输出日志，需使用 docker logs -f 容器名 查看）
ENTRYPOINT ["java", "-jar", "/app.jar"]