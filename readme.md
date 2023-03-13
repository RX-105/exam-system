# exam-system

## 介绍

exam-system，顾名思义哦（同时也是玩梗:)）。

这是一个使用SpringBoot框架开发的B/S考试报名查询系统。想法来自我大三时Java Web开发实训课的项目，由于完成时间太少，从**那里*了一个项目交上去完事了。
但这个项目挺有意思的，所以决定改用SpringBoot继续开发了。

课程提供的开发文档极其落后，推荐JDK是1.6，技术栈是Java Servlet和JSP。所以，如果觉得项目规划有哪里不合适的地方，尽情怪罪老师的文档哟！

## 技术简介

这个项目的技术栈是基于SpringBoot开发的，使用嵌入版Tomcat作为应用服务器和Jakarta Servlet。数据库采用的是MySQL，使用Hikari CP作为数据源，使用
Spring Data JPA和Hibernate ORM实现实体关系映射。

这个项目采取了前后端半分离的模式，也就是Tomcat同时担当HTTP服务器，使用Thymeleaf作为模板引擎，由Spring MVC和Thymeleaf进行首次页面渲染，后续二次
渲染则使用axios请求RestAPI进行页面数据加载。前端页面基于AdminLTE开发。

这个项目的前端很有意思，你甚至可以看到三个时代的写法：
 - 1.Vanilla JS，原生API操作DOM；
 - 2.使用jQuery的API操作DOM；
 - 3.使用Vue作为Web Components嵌入网页，实现页面加载

（因为我很菜。）

项目的配置默认是缺失的。你可以按照下面的模板，自行配置。

```yaml
spring:
  datasource:
    url: 
    username: 
    password: 
    driver-class-name: com.mysql.cj.jdbc.Driver
  servlet:
    multipart:
      enabled: true
      file-size-threshold: 
      max-file-size: 
      max-request-size: 
  application:
    name: exam-system
  jpa:
    database: mysql
    hibernate:
      ddl-auto: none
      naming:
        physical-strategy: org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
    show-sql: 
  mvc:
    static-path-pattern: /static/**
# web和thymeleaf的配置是可选项，可以提供更好的开发体验
  thymeleaf:
    check-template-location: true
    cache: false
    prefix: file:./src/main/resources/templates/
  web:
    resources:
      static-locations: file:./src/main/resources/static/
      cache:
        period: 0

server:
  port: 
  error:
    path: /error

application:
  version: ^project.version^
  admin-token: 
  app-data-location: 
  datasource:
    host: 
    table-name: 
    mysql-home: 
    backup-location: 
  dev:
    validate-stage-time: 
```

## 致谢

直到参与到开发中才知道，学的东西和写的东西区别甚大。

[Baeldung](https://www.baeldung.com/)和[StackOverflow](https://stackoverflow.com/)两个网站在我开发和学习的过程中提供了诸多帮助，
少走了很多弯路。

这个项目全程使用IntelliJ IDEA完成。不愧是Ultimate IDE，前后端都能写。

这个项目的诞生离不开诸多开源软件的支持，我也籍由此受益良多。
