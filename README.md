# SpringBoot 项目初始模板

## 模板功能

- Spring Boot 2.7.0
- Spring MVC
- MySQL 驱动
- MyBatis
- MyBatis Plus
- Spring Session Redis 分布式登录
- Spring AOP
- Apache Commons Lang3 工具类
- Lombok 注解
- Swagger + Knife4j 接口文档
- Spring Boot 调试工具和项目处理器
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- 示例用户注册、登录、搜索功能
- 示例单元测试类
- 示例 SQL（用户表）

访问 localhost:7529/api/doc.html 就能在线调试接口

# OpenAPI平台



## 项目介绍

一个提供 API 接口供开发者调用的平台。
用户可以注册登录，开通接口调用权限。用户可以浏览接口并调用，且每次调用会进行统计。
管理员可以发布接口、下线接口、接入接口，以及可视化接口的调用情况、数据。
项目侧重于后端，包含较多的编程技巧和架构设计层面的知识。



## 业务流程

![image-20230828200032336](C:\Users\LiuMingyao\AppData\Roaming\Typora\typora-user-images\image-20230828200032336.png)



## 技术选型

### 前端

- Ant Design Pro 
- React
- Ant Design Procomponents
- Umi
- Umi Request（Axios 的封装）

### 后端

- Java Spring Boot
- Spring Boot Starter（SDK 开发）
- Dubbo（RPC）
- Nacos
- Spring Cloud Gateway（网关、限流、日志实现）

### 数据表设计

~~~sql
-- 接口信息
create table if not exists yuapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态（0-关闭，1-开启）',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '接口信息';
~~~



### 模拟接口项目

项目名称：yuapi-interface

提供三个不同种类的模拟接口：

1. GET接口
2. POST接口（url传参）
3. POST接口（restful）



#### 调用接口

几种HTTP调用方式：

1. HttpClient
2. RestTemplate
3. 第三方库（OKHTTP，Hutool）



Hutool:https://hutool.cn/docs/#/

Http工具类：

[https://hutool.cn/docs/#/http/Http%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%B7%A5%E5%85%B7%E7%B1%BB-HttpUtil](https://hutool.cn/docs/#/http/Http客户端工具类-HttpUtil)



### API签名认证

本质：

1. 签发签名
2. 使用签名（校验签名）



为什么需要？

1. 保证安全性，不能随便一个人调用
2. 适用于无需保存登录态的场景。只认签名，不关注用户登录态。



#### 签名认证实现

通过http request header头传递参数

参数1：accessKey：调用的标识userA，userB（复杂，无序，无规律）

参数2：secretKey：密匙（复杂，无序，无规律）**该参数不能放到请求头中** （类似用户名和密码，区别：ak，sk是无状态的）

> 无状态指的是对于请求方的每个请求，接收方都当这次请求是第一次请求
>
> https://blog.csdn.net/wangpaiblog/article/details/122570919



千万不能把密匙直接在服务器之间传递，有可能会被拦截



参数3：用户请求参数

参数4：sign（签名）

加密方式：对称加密，非对称加密，md5签名（不可解密）

用户参数+密钥 =》签名生成算法（MD5，HMac， Sha1）=》不可解密的值



怎么知道这个签名对不对？

服务端用一模一样的参数和算法去生成签名，只要和用户传的一致，就表示一致



怎么防重放？
参数 5：加 nonce 随机数，只能用一次
服务端要保存用过的随机数

参数 6：加 timestamp 时间戳，校验时间戳是否过期。

API 签名认证是一个很灵活的设计，具体要有哪些参数、参数名如何一定要根据场景来。（比如 userId、appId、version、固定值等）



### 开发简单易用的SDK

项目名：yuapi-client-sdk



为什么需要Starter？

理想情况：开发者只需要关心调用哪些接口，传递哪些参数，就跟调用自己写的代码一样简单。

开发starter的好处：开发者引入后，可以直接在application.yml中写配置，自动创建客户端



#### Starter开发流程

初始化环境依赖（一定要移除build）

~~~xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-autoconfigure</artifactId>
</dependency>
<!--spring-boot-configuration-processor 的作用是自动生成配置的代码提示-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-configuration-processor</artifactId>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>
~~~



编写配置类（启动类）：

~~~java
@Configuration
@ConfigurationProperties(prefix = "yuapi.client")
@Data
@ComponentScan
public class YuApiClientConfig {


    private String accessKey;

    private String secretKey;



    @Bean
    public YuApiClient yuApiClient() {
        return new YuApiClient(accessKey, secretKey);
    }
}
~~~



注册配置类，resource/META_INF/spring.factories文件

~~~
# spring boot starter
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.yupi.yuapiclientsdk.YuApiClientConfig
~~~



mvn install打包为本地依赖包





## 功能开发

### 接口发布/下线的功能

权限控制：仅管理员可操作



业务逻辑

发布接口：

1. 校验该接口是否存在
2. 判断该接口是否可以调用
3. 修改接口数据库中的状态字段为1

下线接口：

1. 校验该接口是否存在
2. 修改接口数据库中的状态字段为0





接口调用次数统计

需求：

1. 用户每次调用接口成功，次数+1
2. 给用户分配或者用户自主申请接口调用次数

用户调用接口关系表：

~~~sql
-- 用户调用接口关系表
create table if not exists yuapi.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
  	`userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
   	`totalNum` int default 0 not null comment '总调用次数',
  	`leftNum` int default 0 not null comment '剩余调用次数',
  	`status` int default 0 not null comment '0-正常，1-禁用',
  	`createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)',
) comment '用户调用接口关系';
~~~



问题：

如果每个接口的方法都调用次数+1，比较麻烦

致命问题：接口开发者需要自己去添加统计代码

使用 AOP 切面的优点：独立于接口，在每个接口调用后统计次数 + 1
AOP 切面的缺点：只存在于单个项目中，如果每个团队都要开发自己的模拟接口，那么都要写一个切面





## 网关

### 作用

统一去进行一些操作，处理一些问题。

比如

- 路由
- 负载均衡
- 统一鉴权
- 跨域
- 统一业务处理
- 访问控制
- 发布控制
- 流量染色
- 接口保护
  - 限制请求
  - 信息脱敏
  - 降级（熔断）
  - 限流
  - 超时时间
- 统一日志
- 统一文档

 路由 

起到转发的作用，比如有接口 A 和接口 B，网关会记录这些信息，根据用户访问的地址和参数，转发请求到对应的接口（服务器 / 集群）

/a => 接口A

/b => 接口B

https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories



 负载均衡 

在路由的基础上

/c => 服务 A / 集群 A（随机转发到其中的某一个机器）

uri 从固定地址改成 lb:xxxx



 统一处理跨域 

网关统一处理跨域，不用在每个项目里单独处理

https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#cors-configuration



 发布控制 

灰度发布，比如上线新接口，先给新接口分配 20% 的流量，老接口 80%，再慢慢调整比重。

https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-weight-route-predicate-factory



 流量染色 

给请求（流量）添加一些标识，一般是设置请求头中，添加新的请求头

https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-addrequestheader-gatewayfilter-factory

全局染色：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#default-filters



 统一接口保护 

1限制请求：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#requestheadersize-gatewayfilter-factory

2信息脱敏：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-removerequestheader-gatewayfilter-factory

3降级（熔断）：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#fallback-headers

4限流：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory

5超时时间：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#http-timeouts-configuration

6重试（业务保护）：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-retry-gatewayfilter-factory



 统一业务处理 

把一些每个项目中都要做的通用逻辑放到上层（网关），统一处理，比如本项目的次数统计



 统一鉴权 

判断用户是否有权限进行操作，无论访问什么接口，我都统一去判断权限，不用重复写。



 访问控制 

黑白名单，比如限制 DDOS IP



 统一日志 

统一的请求、响应信息记录



 统一文档 

将下游项目的文档进行聚合，在一个页面统一查看

建议用：https://doc.xiaominfo.com/docs/middleware-sources/aggregation-introduction



 网关的分类 

1全局网关（接入层网关）：作用是负载均衡、请求日志等，不和业务逻辑绑定

2业务网关（微服务网关）：会有一些业务逻辑，作用是将请求转发到不同的业务 / 项目 / 接口 / 服务

参考文章：https://blog.csdn.net/qq_21040559/article/details/122961395



 网关实现 

1Nginx（全局网关）、Kong 网关（API 网关，Kong：https://github.com/Kong/kong），编程成本相对高一点

2Spring Cloud Gateway（取代了 Zuul）性能高、可以用 Java 代码来写逻辑，适于学习



网关技术选型：https://zhuanlan.zhihu.com/p/500587132



 Spring Cloud Gateway 用法 

去看官网：https://spring.io/projects/spring-cloud-gateway/

官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/



 核心概念 

路由（根据什么条件，转发请求到哪里）

断言：一组规则、条件，用来确定如何转发路由

过滤器：对请求进行一系列的处理，比如添加请求头、添加请求参数





请求流程：

1客户端发起请求

2Handler Mapping：根据断言，去将请求转发到对应的路由

3Web Handler：处理请求（一层层经过过滤器）

4实际调用服务



 两种配置方式 

1配置式（方便、规范）

​	a简化版

​	b全称版

2编程式（灵活、相对麻烦）



 建议开启日志 

~~~
logging:
  level:
    org:
      springframework:
        cloud:
          gateway: trace
~~~



断言

1. After在xx时间之后
2. Before在xx时间之前
3. Between在xx时间之间
4. 请求类别
5. 请求头（包含Cookie）
6. 查询参数
7. 客户端地址
8. 权重



过滤器

基本功能：对请求头，请求参数，响应头的增删改查

1. 添加请求头
2. 添加请求参数
3. 添加响应头
4. 降级
5. 限流
6. 重试

~~~
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-reactor-resilience4j</artifactId>
</dependency>
~~~





实现统一的接口鉴权和计费

使用了 GlobalFilter（编程式），全局请求拦截处理（类似 AOP）



因为网关项目没引入 MyBatis 等操作数据库的类库，如果该操作较为复杂，可以由 backend 增删改查项目提供接口，我们直接调用，不用再重复写逻辑了。

- HTTP 请求（用 HTTPClient、用 RestTemplate、Feign）

- RPC（Dubbo）



 问题 

预期是等模拟接口调用完成，才记录响应日志、统计调用次数。

但现实是 chain.filter 方法立刻返回了，直到 filter 过滤器 return 后才调用了模拟接口。

原因是：chain.filter 是个异步操作，理解为前端的 promise



解决方案：利用 response 装饰者，增强原有 response 的处理能力

参考博客：https://blog.csdn.net/qq_19636353/article/details/126759522（以这个为主）

其他参考：

- https://blog.csdn.net/m0_67595943/article/details/124667975

- [https://blog.csdn.net/weixin_43933728/article/details/121359727](https://blog.csdn.net/weixin_43933728/article/details/121359727?spm=1001.2014.3001.5501)

- https://blog.csdn.net/zx156955/article/details/121670681

- https://blog.csdn.net/qq_39529562/article/details/108911983





 网关业务逻辑 

问题：网关项目比较纯净，没有操作数据库的包、并且还要调用我们之前写过的代码？复制粘贴维护麻烦

理想：直接请求到其他项目的方法



 怎么调用其他项目的方法？ 

1. 复制代码和依赖、环境

2. HTTP 请求（提供一个接口，供其他项目调用）

3. RPC

4. 把公共的代码打个 jar 包，其他项目去引用（客户端 SDK）



 HTTP 请求怎么调用？ 

1. 提供方开发一个接口（地址、请求方法、参数、返回值）

2. 调用方使用 HTTP Client 之类的代码包去发送 HTTP 请求



 RPC 

作用：像调用本地方法一样调用远程方法。

和直接 HTTP 调用的区别：

1. 对开发者更透明，减少了很多的沟通成本。

2. RPC 向远程服务器发送请求时，未必要使用 HTTP 协议，比如还可以用 TCP / IP，性能更高。（内部服务更适用）



Dubbo框架（RPC实现）

其他 RPC 框架：GRPC、TRPC
最好的学习方式：阅读官方文档
https://dubbo.incubator.apache.org/zh/docs3-v2/java-sdk/quick-start/spring-boot/

两种使用方式：

1. Spring Boot 代码（注解 + 编程式）：写 Java 接口，服务提供者和消费者都去引用这个接口
2. IDL（接口调用语言）：创建一个公共的接口定义文件，服务提供者和消费者读取这个文件。优点是跨语言，所有的框架都认识

底层是 Triple 协议：https://dubbo.incubator.apache.org/zh/docs3-v2/java-sdk/concepts-and-architecture/triple/



1. backend 项目作为服务提供者，提供 3 个方法：
   a.实际情况应该是去数据库中查是否已分配给用户
   b.从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
   c.调用成功，接口调用次数 + 1 invokeCount
2. gateway 项目作为服务调用者，调用这 3 个方法

建议大家用 Nacos！
整合 Nacos 注册中心：https://dubbo.apache.org/zh/docs3-v2/java-sdk/reference-manual/registry/nacos/

注意：

1. 服务接口类必须要在同一个包下，建议是抽象出一个公共项目（放接口、实体类等）
2. 设置注解（比如启动类的 EnableDubbo、接口实现类和 Bean 引用的注解）
3. 添加配置
4. 服务调用项目和提供者项目尽量引入相同的依赖和配置

~~~
        <!-- https://mvnrepository.com/artifact/org.apache.dubbo/dubbo -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
            <version>3.0.9</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.nacos</groupId>
            <artifactId>nacos-client</artifactId>
            <version>2.1.0</version>
        </dependency>
~~~





 统计分析功能 

 需求 

各接口的总调用次数占比（饼图）取调用最多的前 3 个接口，从而分析出哪些接口没有人用（降低资源、或者下线），高频接口（增加资源、提高收费）。

用饼图展示。



后端
写一个接口，得到下列示例数据：
接口 A：2次
接口 B：3次

步骤：

1. SQL 查询调用数据：select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3;
2. 业务层去关联查询接口信息

