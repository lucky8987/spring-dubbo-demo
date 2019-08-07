- #技术框架选型

Name | Version | Desc
---|---|---
Java | 1.8+ | 开发语言
Maven | 3.x | 项目编译&构建
Dubbo Spring Boot | 0.2.0 | Dubbo+Spring-Boot 启动器
Spring Boot | 2.1.x |  容器框架
Dubbo | 2.6.2+ | 分布式框架
Mybatis | 3.4.6 | 持久层框架

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/docs/{bootVersion}/reference/htmlsingle/#boot-features-amqp)
* [MyBatis Framework](http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/{bootVersion}/reference/htmlsingle/#using-boot-devtools)

### Guides
The following guides illustrate how to use some features concretely:

* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
* [MyBatis Quick Start](https://github.com/mybatis/spring-boot-starter/wiki/Quick-Start)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

- #接口定义(待完善)
- 请求示例

  - 请求方式：Dubbo
  - 注册机制：zookeeper
  - 接口路径：com.example.service.XXXServiceApi
  - 接口方法：demo
  - 接口说明：接口示例

  | 请求参数                         | 类型                | 是否必填 | 字段说明 |
  | ---------------------------- | ----------------- | ---- | ---- |
  | requestId         | String               | Y | 请求ID |
  | remark | String | N | 备注

- 响应示例

    ```java
    // 成功示例：
    
    {"message":"success","status":200,"data":"true"}
    
    // 失败示例：
    
    {"message":"服务器内部错误:","status":500}
    
    ```
---

- ### 待完善