- [微服务学习](#微服务学习)
  - [1. Eureka注册中心](#1-eureka注册中心)
  - [2.Ribbon负载均衡](#2ribbon负载均衡)
  - [3. Nacos](#3-nacos)
    - [Nacos和Eureka不同点来说](#nacos和eureka不同点来说)
    - [Nacos的自动配置，热更新](#nacos的自动配置热更新)
      - [多环境配置共享](#多环境配置共享)
  - [4.Feign 代替  RestTemplate](#4feign-代替--resttemplate)
      - [4.1自定义配置](#41自定义配置)
      - [4.2Feign优化    日志 or    HTTPClient代替  UrlConnection（不支持连接池）](#42feign优化----日志-or----httpclient代替--urlconnection不支持连接池)
      - [4.3Feign最佳实践](#43feign最佳实践)
  - [5.GateWay](#5gateway)
    - [路由断言工厂](#路由断言工厂)
    - [GateWayFilter网关过滤器](#gatewayfilter网关过滤器)
    - [全局过滤器自定义配置](#全局过滤器自定义配置)
    - [3种过滤器排序](#3种过滤器排序)
  - [6.Docker](#6docker)
    - [虚拟机vs Docker](#虚拟机vs-docker)
    - [Image \&\& Container](#image--container)
    - [数据卷](#数据卷)
    - [镜像分层结构 layer](#镜像分层结构-layer)
    - [Dockerfile](#dockerfile)
    - [使用Dockercompose部署微服务的时候发现的一个bug](#使用dockercompose部署微服务的时候发现的一个bug)
    - [Bug实录](#bug实录)
    - [Docker私有镜像仓库](#docker私有镜像仓库)
      - [带有图形化界面版本](#带有图形化界面版本)
      - [配置Docker信任地址](#配置docker信任地址)
    - [部署Docker Bug实录](#部署docker-bug实录)
    - [](#)
  - [7.MQ](#7mq)
    - [AMQP](#amqp)
    - [1.SpringAMQP实现RabbitMQ的simplequeue](#1springamqp实现rabbitmq的simplequeue)
    - [2.workQueue 消息预取机制](#2workqueue-消息预取机制)
    - [3.FanoutExchange](#3fanoutexchange)
    - [Bug实录](#bug实录-1)
    - [4.DirectExchange](#4directexchange)
    - [5.TopicExchange](#5topicexchange)
    - [6.消息转化器](#6消息转化器)
    - [BUG实录（故意）](#bug实录故意)
  - [8.ES](#8es)
    - [IK分词器](#ik分词器)
    - [IK扩展词典，阻止词典](#ik扩展词典阻止词典)
    - [操作索引库](#操作索引库)
    - [修改文档](#修改文档)
    - [RestClient    CURD](#restclient----curd)
      - [RestClient操作索引库](#restclient操作索引库)
      - [RestClient操作文档](#restclient操作文档)
    - [DSL查询](#dsl查询)
        - [1.查询所有](#1查询所有)
        - [思考：copy\_to复合字段有了还要multi\_match干什么？](#思考copy_to复合字段有了还要multi_match干什么)
        - [2.精确匹配](#2精确匹配)
        - [3.地理查询](#3地理查询)
        - [4.复合查询](#4复合查询)
          - [4.1Function Query在原始基础上进行计算](#41function-query在原始基础上进行计算)
          - [4.2Boolean Query   不会修改算法，组合一个或多个查询字句](#42boolean-query---不会修改算法组合一个或多个查询字句)
          - [BUG实录 unit错误](#bug实录-unit错误)
    - [DSL排序](#dsl排序)
    - [DSL分页](#dsl分页)
    - [DSL高亮](#dsl高亮)
    - [RestClient查询文档](#restclient查询文档)
    - [发现自己的漏洞](#发现自己的漏洞)
    - [RestClient分页 ，高亮](#restclient分页-高亮)
    - [地理位置排序得用SortBuliders](#地理位置排序得用sortbuliders)
    - [RestClient函数算分](#restclient函数算分)
    - [心得有感](#心得有感)
  - [9.ES进阶](#9es进阶)
    - [数据聚合](#数据聚合)
      - [聚合类型](#聚合类型)
      - [Bucket聚合](#bucket聚合)
      - [Stats聚合       度量](#stats聚合-------度量)
      - [RestClient实现聚合](#restclient实现聚合)
    - [拼音分词器](#拼音分词器)
    - [自定义分词器](#自定义分词器)
    - [分词器思考](#分词器思考)
    - [自动补全](#自动补全)
      - [自动补全思考](#自动补全思考)
      - [BUG实录](#bug实录-2)
      - [RestClient实现自动补全](#restclient实现自动补全)
  - [ES，数据库数据同步](#es数据库数据同步)
  - [MQ异步 解决消息一致性报错实录](#mq异步-解决消息一致性报错实录)
  - [ES集群](#es集群)
    - [部署es集群](#部署es集群)
    - [4.1.创建es集群](#41创建es集群)
    - [4.2.集群状态监控](#42集群状态监控)
    - [4.3.创建索引库](#43创建索引库)
      - [1）利用kibana的DevTools创建索引库](#1利用kibana的devtools创建索引库)
      - [2）利用cerebro创建索引库](#2利用cerebro创建索引库)
    - [4.4.查看分片效果](#44查看分片效果)
    - [ES集群结点职责](#es集群结点职责)
    - [脑裂问题](#脑裂问题)
    - [工作机理](#工作机理)
    - [集群结点故障转移](#集群结点故障转移)
# 微服务学习

## 1. Eureka注册中心

消费者如何去获取生产者的url地址呢？硬编码的指定不好改不好管理不支持分布式

EurekaServer的功能：

1. 所有服务启动都会被注册在注册中心
2. 心跳机制，每30s一次（避免访问宕机的机器）



当消费者进程想要向提供者拿信息，它会向EureKaServer去问有没有注册的信息，有的话如果有好几台，通过负载均衡分配一个合适的url回去

EureKaClient就是消费者和生产者

![image-20240316234756481](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20240316234756481.png)

## 2.Ribbon负载均衡

![image-20240317081427119](C:\Users\lenovo\AppData\Roaming\Typora\typora-user-images\image-20240317081427119.png)

代码配置（全部微服务配置），yml文件配置（制定微服务名称配置）

Ribbon默认是懒加载的，如果你不配置yml文件，就不会提前去启动server，这样第一次请求时间就比较长

## 3. Nacos 

服务注册和发现 分布式配置（可以配置集群） ：8848

`PS E:\nacos-server-1.4.1\nacos\bin> `

`.\startup.cmd -m standalone`

启动代码

父工程：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
    <version>2.2.5.RELEASE</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```



客户端：

```xml
<!-- nacos客户端依赖包 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>

```



如果在配置文件还保留eureka的配置就会启动报错，显示是jdbc.pro..文件or db null





![image-20240317093947293](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317093947293.png)

配置NacosRUle 优先本地集群，然后本地集群随机选择，同级群找不到就会去远程集群找，但是会报警告

Nacos网站可以配置权重

设置权重为0，之后再进行升级，测试的时候逐渐增加权重

环境隔离使用namespace，不同环境下的实例不可见



### Nacos和Eureka不同点来说

服务注册和拉取，心跳机制都有



不一样的是Nacos对于临时实例和非临时实例，临时实例被动监测心跳，非临时主动询问

不只是消费者服务主动pull 服务列表，当服务挂了的时候，nacos会主动推送push 新服务列表，防止用户访问没了的实例





### Nacos的自动配置，热更新

实现方式：

1.首先添加@RefreshScope 然后利用value注解拿具体的配置

![](../AppData/Roaming/Typora/typora-user-images/image-20240317112439024.png)

2.下面那张图的@ConfigurationProperties("pattern") 拼接private String dateformat;之后就是配置的具体字段

![image-20240317112559053](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317112559053.png)

#### 多环境配置共享

![image-20240317114503105](../AppData/Roaming/Typora/typora-user-images/image-20240317114503105.png)

原来是去拿  userservice-dev.yaml



通用就是不同环境都可以获得的配置    所以nacos配置命名为   userservice.yaml

![image-20240317114618452](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317114618452.png)

测试环境可以这么设置

Feign

遇到一个错误记录一下

我把那个client文件夹放到了service文件夹的上一级目录，报错找不到注入的包

## 4.Feign 代替  RestTemplate

Feign不需要配置负载均衡，因为以及集成了RestTemplate



注入依赖，Application添加注解@EnableFeignClients

写Client接口，调用Client发送请求

#### 4.1自定义配置

1.在配置文件中配置

2.创建个config下的Bean，放到类（局部）或者application中（全局）





#### 4.2Feign优化    日志 or    HTTPClient代替  UrlConnection（不支持连接池）

![image-20240317143106276](../AppData/Roaming/Typora/typora-user-images/image-20240317143106276.png)

![image-20240317144037463](../AppData/Roaming/Typora/typora-user-images/image-20240317144037463.png)

#### 4.3Feign最佳实践

1.继承        一个统一的接口类，让client端和调用者的controller全部继承这个其实就是维护一个契约

2.抽取      将消费者 的 pojo， client，配置啥的全部提取出来放到一个单独的包下，然后这个包可以被其他类去引用，缺点是包中不是每一个方法都会被用到，优点是解耦合抽离逻辑



问题！！！！   提取出来的client代码，因为不在原来类的Beanscan中，所以不会被扫描到就算有FeignClient的注解也不会去注册成Bean，也就引用不到了，解决方法有两个



方式- -:指定FeignClient所在包

@EnabLeFeignClients(basePackages = "cn. itcast . feign . cLients")

方式二:指定FeignClient字节码

@EnabLeFeignCLients(clients = {UserClient . class})

## 5.GateWay 

网关



引入Nacos服务发现依赖和网关依赖

~~~JAVA
    <dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
    <!--网关gateway依赖-->
    <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
~~~

然后再yml中配置

~~~yml
server:
  port: 10010
spring:
  application:
    name: gateway
  cloud:
    nacos:
      server-addr: localhost:8848
    gateway:
      routes:
        - id: user-service #路由标识
          uri: lb://userservice
          predicates:
            - Path=/user/**
        - id: order-service
          uri: lb://orderservice
          predicates:
            - Path=/order/**
~~~



### 路由断言工厂

按我们需求 （11种限制）生成路由判断，时间区域，路径等等限制

### GateWayFilter网关过滤器

30多种过滤器功能

~~~yml
    gateway:
      routes:
        - id: user-service #路由标识
          uri: lb://userservice
          predicates:
            - Path=/user/**
#          filters:
#            - AddRequestHeader=Truth,DUT-SUN is NB
        - id: order-service
          uri: lb://orderservice
          predicates:
            - Path=/order/**
      default-filters:
        - AddRequestHeader=Truth,DUT-SUN is NB 添加请求头全局

~~~

全局过滤和部分过滤



### 全局过滤器自定义配置

Bug：

import org.springframework.http.server.reactive.ServerHttpRequest;



包名reactive有没有

决定

ServerHttpRequest request=  exchange.getRequest();
MultiValueMap<String,String>params=request.getQueryParams();这两行爆红与否

~~~
@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取参数
        ServerHttpRequest request=  exchange.getRequest();
        MultiValueMap<String,String>params=request.getQueryParams();
        //判断参数值是admin
        String auth=params.getFirst("authorization");
        if ("admin".equals(auth)) {
            return chain.filter(exchange);
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        return exchange.getResponse().setComplete();
    }
}

~~~



### 3种过滤器排序

默认过滤器   ->路由过滤器->GlobalFilter             都是GateWayFilter

前两种的order按照声明顺序从1开始依次增加1





## 6.Docker           

快速交付应用、运行应用的技术



大型项目组件较多允许环境也较为复杂，可能不兼容允许环境也不通



将函数库（libs）、deps依赖，配置应用一起打包

每个应用放到一个隔离系统中运行（沙箱机制不可见）



 ubuntu和centos函数库不同内核相同，

所以正常来说不能跨系统运行呀？



docker解决方法：把函数库一起打包，然后直接用函数库区访问内核

不需要去调用系统的函数库



### 虚拟机vs Docker 

虚拟机内存太大了而且其实是装了一个新操作系统，由于调用链的关系，性能不好



docker直接启动应用在本机的操作系统上，只是调用自己的函数库和依赖去操作内核

### Image && Container

镜像image  ：Docker将应用程序及其所需的依赖、函数库、环境打包在一起称为镜像

容器 Container ：镜像中规定应用程序运行后形成的进程就叫做容器，镜像是只读的，不可以写去污染，所有的写操作都是在复制的本地data中的

![image-20240317200303576](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317200303576.png)

![image-20240317212533563](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317212533563.png)

![image-20240317215543500](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317215543500.png)

![image-20240317220033867](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317220033867.png)

![image-20240317220440717](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317220440717.png)

![image-20240317221244060](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317221244060.png)

ps -a看的更详细就算已经退出了

### 数据卷

![image-20240317233511998](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317233511998.png)

![image-20240317233601463](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240317233601463.png)

可以数据卷挂载或者宿主机挂载（文件挂载，目录挂载）

### 镜像分层结构 layer

![image-20240318103201533](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318103201533.png)

### Dockerfile

![image-20240318103800427](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318103800427.png)

每一行都是一层

因为有些必要的层已经有人包装好了在FROM后面直接使用java:8-alpine

![image-20240318104042821](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318104042821.png)

![image-20240318104120339](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318104120339.png)

Docker Compose部署分布式应用

![image-20240318111048245](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318111048245.png)

### 使用Dockercompose部署微服务的时候发现的一个bug

### Bug实录

![img](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/4add3acfc930fcd07d06ea5e10a3a377314141c2.jpg@160w_160h_1c_1s_!web-avatar-comment.avif



解决mysql的连接问题了，因为docker的mysql默认阻止docker容器的ip去连接，就算密码正确也会deny，解决办法GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
FLUSH PRIVILEGES;这样就行了





### Docker私有镜像仓库

#### 带有图形化界面版本

使用DockerCompose部署带有图象界面的DockerRegistry，命令如下：

```yaml
version: '3.0'
services:
  registry:
    image: registry
    volumes:
      - ./registry-data:/var/lib/registry
  ui:
    image: joxit/docker-registry-ui:static
    ports:
      - 8080:80
    environment:
      - REGISTRY_TITLE=传智教育私有仓库
      - REGISTRY_URL=http://registry:5000
    depends_on:
      - registry
```



#### 配置Docker信任地址

我们的私服采用的是http协议，默认不被Docker信任，所以需要做一个配置：

```sh
# 打开要修改的文件
vi /etc/docker/daemon.json
# 添加内容：
"insecure-registries":["http://192.168.150.101:8080"]
# 重加载
systemctl daemon-reload
# 重启docker
systemctl restart docker
```





### 部署Docker Bug实录

上传一个images上私有仓库之后报错

![image-20240318202024260](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318202024260.png)

解决办法加一行在compose.yml

### ![image-20240318202015375](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318202015375.png)

![image-20240318202700792](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318202700792.png)

## 7.MQ

不会出现级联失败，解耦合，流量削峰



blocker可用性并发性要求很高，调用链关系不清晰，难以排查bug，对吞吐量要求高，就用异步通信（MQ消息队列是一种常见的Blocker）



消息可靠性高延迟低的话最好是RabbitMQ（适合业务）

吞吐量最大是Kafka（适合日志）

![image-20240318210821394](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318210821394.png)

### AMQP

高级消息队列协议，就是一个标准支持各种语言按照这种协议通信，给业务消息准备的



### 1.SpringAMQP实现RabbitMQ的simplequeue

![image-20240318221321115](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318221321115.png)

这里的@Component是为了注入容器，随mean函数启动而启动，消息监听消费



![image-20240318221353124](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318221353124.png)

这里的@Runwith是为了可以在测试类注入依赖





步骤：移入依赖，yml配置，生产者 注入RabbitTemplate  然后调用

convertAndSend(queue,message);消费者@Component注解通过

@RabbitListener(queues="simple.queue")在方法参数中指定一个String类型的，然后这个参数就是传来的消息的值

### 2.workQueue 消息预取机制

不管两个队列有没有能力去快速处理消息都会去从消息队列里先拿，导致一人拿一半，结果快的先处理完可是慢的还在处理



在配置文件中设置消费限制

![image-20240318223553085](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318223553085.png)

### 3.FanoutExchange

~~~java
package cn.itcast.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/18  22:46
 */
@Configuration
public class FanoutConfig {
    @Bean
    public FanoutExchange fanoutExchange(){
        System.out.println("Creating fanoutExchange");
        return new FanoutExchange("sun.fanout");
    }

    @Bean
    public Queue FanoutQueue1(){
        return new Queue("fanout.queue1");
    }
    @Bean
    public Binding BindExange(Queue FanoutQueue1, FanoutExchange fanoutExchange){
        return BindingBuilder
                .bind(FanoutQueue1)
                .to(fanoutExchange);
    }


    @Bean
    public Queue FanoutQueue2(){
        return new Queue("fanout.queue2");
    }
    @Bean
    public Binding BindExange2(Queue FanoutQueue2, FanoutExchange fanoutExchange){
        return BindingBuilder
                .bind(FanoutQueue2)
                .to(fanoutExchange);
    }
}

~~~

~~~java
    @Test
    public void sendMessageToFanoutQueue(){
        String exchangename="sun.fanout";
        String message="Fanout的消息";
        rabbitTemplate.convertAndSend(exchangename,"",message);
    }
~~~

![image-20240318234203197](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240318234203197.png)

监听这两个队列的消费者虽然监听的队列不同但是当消息发送到交换机时，有两个队列绑定到交换机上，此时交换机就会去广播到每一个bind到它的队列，然后消费者从两个队列拿数据，这个交换机和队列的配置是在消费者的代码文件中

### Bug实录

在注释下面的消费者代码的时候，上面的广播队列的配置不会生效，注入虽然注入了，尽管监听的不是我配置文件的队列1和2

~~~java
package cn.itcast.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

/**
 * 功能描述
 * <p>
 * 成略在胸，良计速出
 *
 * @author SUN
 * @date 2024/03/18  22:00
 */

@Component
public class SpringRabbitListen {
//    @RabbitListener(queues="simple.queue")
//    public void Listener(String msg){
//        System.out.println("接受到消息为："+msg);
//    }
@RabbitListener(queues="simple.queue")
public void workListener1(String msg) throws InterruptedException {
    System.out.println("1接受到消息为："+msg+LocalDateTime.now()) ;
    Thread.sleep(20);
}
    @RabbitListener(queues="simple.queue")
    public void workListener2(String msg) throws InterruptedException {
        System.out.println("2接受到消息为："+msg+LocalDateTime.now());
        Thread.sleep(200);
    }
}

~~~

### 4.DirectExchange  

在@RabbitListener直接去配置交换机和队列以及key



它会去转发到routing key和 binding key一致的队列上

![image-20240319000353162](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319000353162.png)

![image-20240319000325720](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319000325720.png)

### 5.TopicExchange

个人感觉话题交换机和DirectExchange差不多只是交换机类型需要指定一下还有在key的时候不是一个key={}，而是key=“”，然后有通配符可以加强匹配

![](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319100216600.png)

![image-20240319100143435](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319100143435.png)

### 6.消息转化器

![image-20240319101323224](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319101323224.png)

当我们直接去发送一个对象的时候，java会默认帮我序列化对象，但是会有问题在于一个非常简单的对象却被序列化这么长不便于传输迅速

引入发送方依赖在总的pom.xml

~~~java
       <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
        </dependency>
~~~



发送方的main函数中去注入消息转换器![image-20240319104034739](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319104034739.png)

![image-20240319104001542](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319104001542.png)

消费者在自己的pom.xml引入依赖

~~~java
       <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-xml</artifactId>
        </dependency>
~~~

![](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319104222583.png)

![image-20240319104211983](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319104211983.png)

### BUG实录（故意）

具体就是先不配置消息转换器，在发送消息的时候对象会转化序列化的字符串，此时如果你使用了消息转换器接收，会报错AmqpRejectAndDontRequeueException这个错误，然后如果此时队列有正常被转换成json对象的数据，还是可以正常处理的



思考：当我去查看队列的时候发现队列已经空了就算数据没有正确去处理，也不是消息预取机制，因为我设置了一次只能取一条。不过也是比较合理，比较处理错误的消息也不能由消费者再放回队列里

## 8.ES

![image-20240319104705487](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319104705487.png)

ES基于Lucene开发，底层技术就是倒排索引

![image-20240319105450952](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319105450952.png)

![image-20240319105627839](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319105627839.png)

![image-20240319111431016](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319111431016.png)

### IK分词器

放到插件数据卷目录里，重启Docker

IK分词器包含两种模式：

* `ik_smart`：最少切分      粗粒度分，看5个字能不能是一个词可以就不往下分了

* `ik_max_word`：最细切分  

### IK扩展词典，阻止词典

![image-20240319122443707](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319122443707.png)

![image-20240319122423782](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319122423782.png)

### 操作索引库

![image-20240319130531775](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319130531775.png)

### 修改文档

![image-20240319135923607](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319135923607.png)

![image-20240319141323435](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319141323435.png)

![image-20240319141537888](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319141537888.png)

### RestClient    CURD

#### RestClient操作索引库

索引库的初始化

![image-20240319161950594](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319161950594.png)

![image-20240319161926242](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319161926242.png)

![image-20240319162952429](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319162952429.png)

![image-20240319163628952](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319163628952.png)

#### RestClient操作文档



批量插入

~~~java
    @Test
    void testBulkRequest() throws IOException {
        //批量数据库查询数据
        List<Hotel>hotels=iHotelService.list();
        //1.创建request
        BulkRequest request=new BulkRequest();
        //转换为文档类型HotelDoc
        for(Hotel hotel:hotels){
            HotelDoc hotelDoc=new HotelDoc(hotel);
            request.add(new IndexRequest("hotel").id(hotelDoc.getId().toString()).source(JSON.toJSONString(hotelDoc),XContentType.JSON));
        }
        //2.设置request属性
        client.bulk(request,RequestOptions.DEFAULT);
    }
}
~~~

### DSL查询

![image-20240319174743584](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319174743584.png)

##### 1.查询所有

~~~es
#查询所有
GET /hotel/_search
{
  "query": {
    "match_all": {}
  }
}



GET /hotel/_search
{
  "query": {"match": {
    "all": "汉庭深圳"
  }}
}



GET /hotel/_search
{
  "query": {"multi_match": {
    "query": "汉庭深圳",
    "fields": ["name","brand","city"]
  }}
}
~~~

##### 思考：copy_to复合字段有了还要multi_match干什么？

我的理想是copyTo的复合字段，是许多经常一起查询的字段的合并，而multi_match是为了满足更多匹配的查询需求，两者结合查询效果更好

##### 2.精确匹配

~~~es

//精确匹配

GET /hotel/_search
{
  "query": {
    "term": {
      "city": {
        "value": "深圳"
      }
    }
  }
}


GET /hotel/_search
{
  "query":{
    "range":{
      "price":{
        "gte": 100,
        "lte":1000
      }
    }
  }
}
~~~



##### 3.地理查询

有两种查询一种是矩形查询一种是圆形查询

~~~es
//地理匹配
GET /hotel/_search
{
"query": {
  "geo_distance":{
    "distance":"50km",
    "location":"40.159255,117.12401"
  }
}  
}
~~~

##### 4.复合查询 

###### 4.1Function Query在原始基础上进行计算

![image-20240319190423308](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319190423308.png)

![image-20240319190729101](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319190729101.png)

~~~es
//复合查询（算分）
GET /hotel/_search
{
  "query": {
    "function_score": {
      "query": {
        "term": {
          "all": {
            "value": "外滩"
          }
        }
      },
      "functions": [
        {
          "filter": {"term": {
            "brand": "7天酒店"
          }},
          "weight": 3
        }
      ],
      "boost_mode": "sum"
    }
  }
}


~~~

###### 4.2Boolean Query   不会修改算法，组合一个或多个查询字句



注意must,should中越少越好，因为是要算分的，筛选放在must_not 必须不成立，filter必须成立



![image-20240319192310565](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319192310565.png)

###### BUG实录 unit错误

![image-20240319193831514](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319193831514.png)

要改成

![image-20240319193913896](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319193913896.png)

~~~es
//Boolean Query     只有must和should算分，然后筛选是must_not和filter

GET /hotel/_search
{
  "query": {
    "bool": {
      "must": [
        {"match": {
          "name":  "如家"
        }}
      ],
      "must_not": [
        {
          "range": {
            "price": {
               "gt": 400
            }
          }
        }
      ],
      "filter": [
        {"geo_distance": {
          "distance": "10km",
          "location": {
            "lat": 31.21,
            "lon": 121.5
          }
        }}
      ]
    }
  }
}


~~~

### DSL排序

![image-20240319195338762](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319195338762.png)

~~~es

#价格升序，评分降序
GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "score": {
        "order": "desc"
      },
      "price": {
        "order": "asc"
      }
    }
  ]
}

~~~



~~~es
#距离升序排列

GET /hotel/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "_geo_distance": {
        "location": {
          "lat": 39.05015,
          "lon": 121.78259
        },
          "order": "asc"
      }
    }
  ]
  
}



~~~

### DSL分页

![image-20240319201544327](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319201544327.png)

![image-20240319201800100](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319201800100.png)



深度分页问题

因为es本身是采用倒排索引，所以问题在于它不能像mysql一样直接去查询下标900到1000的数据，而是去

![image-20240319210138575](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319210138575.png)

![image-20240319210251781](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319210251781.png)

### DSL高亮

![image-20240319210620637](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319210620637.png)

![image-20240319211257584](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319211257584.png)

默认是em，但是name要和all一样，否则用require_field_match为假

### RestClient查询文档

![image-20240319212519527](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319212519527.png)

![image-20240319213554315](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319213554315.png)

![image-20240319213631227](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319213631227.png)

字符串转对象打印

通过JSON.parseObject(String ..., ...     .class)

![image-20240319220651505](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319220651505.png)

### 发现自己的漏洞

![image-20240319215137352](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319215137352.png)

注意这并不是精确查询的term，range





还有一个问题是记住在mapping中定义的type不是keyword的是text不能进行精确查询

### RestClient分页 ，高亮

![image-20240319221319028](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319221319028.png)

![image-20240319224335210](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319224335210.png)

![image-20240319224255928](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240319224255928.png)

### 地理位置排序得用SortBuliders

![image-20240320111517860](../AppData/Roaming/Typora/typora-user-images/image-20240320111517860.png)

### RestClient函数算分

![image-20240320120441683](../AppData/Roaming/Typora/typora-user-images/image-20240320120441683.png)

### 心得有感

在看了高并发的书还有这个es的代码逻辑之后发现，其实代码设计都是有迹可循的，不知道怎么写就找规律看源码就行，甚至都不需要看视频，文档

## 9.ES进阶

### 数据聚合

#### 聚合类型

桶聚合，度量聚合，管道聚合

![image-20240320122510931](../AppData/Roaming/Typora/typora-user-images/image-20240320122510931.png)

![image-20240320122355726](../AppData/Roaming/Typora/typora-user-images/image-20240320122355726.png)

#### Bucket聚合

~~~es
GET /hotel/_search
{
  "size": 0,//表示不要返回文档只要显示聚合结果
  "aggs":{//指定我需要聚合
    "brandAgg": {//指定聚合名称
      "terms": {//指定聚合的类型，按照字段聚合注意不要说text可以分词的类型
        "field": "brand",//聚合的字段是brand，前面的field不用变
        "size": 20//意思就是获得20种分类的类型，20>真实的类别不会报错，少了就会少
         "order": {
          "_count": "asc"
        }
      }
    }
  }
}

~~~

聚合的aggregations是hit同级的，直接request获取应该就能拿到，然后再通过指定的名称去获得具体哪个桶聚合，然后get"buckets"应该能够拿到数组了，默认是  按照 "doc_count" 降序排列，如果你需要自定义排序就在具体的聚合下去添加order字段，如果你想限定文档的聚合范围用query就行了

![image-20240320123405077](../AppData/Roaming/Typora/typora-user-images/image-20240320123405077.png)

#### Stats聚合       度量

~~~es
GET /hotel/_search
{
  "size": 0,
  "aggs":{
    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20,
        "order": {
          "scoreAgg.max":"desc"
        }
      },
      "aggs": {
        "scoreAgg": {
          "stats": {
            "field": "score"
          }
        }
      }
    }
  }
}

~~~

需要值得注意的是，在Bucket聚合下的Status聚合的排序不是和桶聚合一样在具体的聚合下里的order去定义

    "terms": {//指定聚合的类型，按照字段聚合注意不要说text可以分词的类型
        "field": "brand",//聚合的字段是brand，前面的field不用变
        "size": 20//意思就是获得20种分类的类型，20>真实的类别不会报错，少了就会少
         "order": {
          "_count": "asc"
        }

而是也是在父聚合桶聚合下的相同地方定义

    "brandAgg": {
      "terms": {
        "field": "brand",
        "size": 20,
        "order": {
          "scoreAgg.max":"desc"
        }
      },

![image-20240320125406943](../AppData/Roaming/Typora/typora-user-images/image-20240320125406943.png)

没有桶聚合的探究，发现没桶聚合的度量聚合意义不大

#### RestClient实现聚合

![image-20240320150021152](../AppData/Roaming/Typora/typora-user-images/image-20240320150021152.png)

![image-20240320152232367](../AppData/Roaming/Typora/typora-user-images/image-20240320152232367.png)

~~~java
 @Test
    void SearchBucketAgg() throws IOException {
        SearchRequest request=new SearchRequest("hotel");
        //2.准备DSL
        request.source().size(0).aggregation(AggregationBuilders
                .terms("brandAgg")
                .size(20)
                .field("brand")
        );
        SearchResponse response=client.search(request,RequestOptions.DEFAULT);
        Aggregations aggregations= response.getAggregations();
        Terms brandTerms=aggregations.get("brandAgg");
        List<? extends Terms.Bucket> buckets=brandTerms.getBuckets();
        for(Terms.Bucket bucket:buckets){
            String brandName=bucket.getKeyAsString();
            System.out.println(brandName);
        }
    }
}
~~~

### 拼音分词器

![image-20240320201153818](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320201153818.png)

### 自定义分词器

拼音分词器是为了我们输入拼音时也能正确的帮我们补全词语

但是原始的拼音分词器，把一个个字都分层了拼音，还有首字母的拼音串



所以我们需要自定义分词器

![image-20240320201528962](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320201528962.png)

![image-20240320201855491](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320201855491.png)

上面配置还是不能满足我们的需求还需要自定义配置pinyin分词器如下图



![image-20240320201930526](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320201930526.png)

创建倒排索引的时候用先用ik分词器分词

每个词再用pinyin分词器进行拼音的分词



问题又出现了，当我去搜索的时候假如还用默认的

GET /test/_search
{
  "query": {
    "match": {
      "name": "掉入狮子笼咋办"
    }
  }
}去搜索就会有问题，问题在于明明我想搜的是狮子，但是虱子也被搜出来了，因为我分完词之后，狮子是狮子，sz，shizi虱子是虱子，sz，shizi，通过默认分词器倒排索引去找的时候，会发现sz，shizi有两个符合，分数就会很高，一起被搜出来

### 分词器思考

所以为了解决上面的问题，在我去搜索的时候，不要去把狮子分成狮子，sz，shizi这三个

，而是只要狮子（也就是不要拼音），所以需要修改下面的值（但是我存倒排索引的时候还是需要用拼音，为什么呢？是因为拼音分词器让文档的词有了拼音的索引，所以当用户去搜索拼音的时候，可以搜到索引表中的数据，而搜中文的时候又能返回正确的中文数据，而不会出现同音词）

![image-20240320202843026](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320202843026.png)

### 自动补全



参与补全的字段必须是completion类型

![image-20240320204052997](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320204052997.png)

#### 自动补全思考

![image-20240320211127628](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320211127628.png)

刚开始没想明白为啥要在实体类中去添加补全的字段为了在去构造的时候直接初始化，把你需要补全的字段直接add进去

![image-20240320211027009](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320211027009.png)

#### BUG实录

因为马虎犯错了，

在mapping定义的时候，定义是suggestion字段，但是我在类中写了suggestions，多了个s

![image-20240320211826606](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320211826606.png)

![image-20240320211910325](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320211910325.png)

很快啊，我想都没想，我直接就是一个

~~~DSL

GET /hotel/_search
{
  "suggest": {
    "suggestions": {
      "text": "h",
      "completion": {
        "field": "suggestion",
        "skip_duplicates":true,
        "size": 10
      }
    }
  }
}
~~~

然后不出意外的空，因为是什么原因呢，我插入的时候

实际是插入json格式的数据，而json数据怎么来的呢是通过，类对象转化而来的，

尽管我mapping中没有suggestions只有suggestion，但是插入的时候没有字段是可以的，当新字段了，但是我去补全的时候会有什么问题呢，因为自动补全所制定的字段必须是completion类型的（也就是数组）当我区搜suggestions，全为空自然搜索不到，如果搜suggestion因为没有定义这个字段是completion类型自然也就报错了

#### RestClient实现自动补全

![image-20240320212447015](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320212447015.png)

![1](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320214823955.png)

## ES，数据库数据同步



![image-20240320223357157](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320223357157.png)

![image-20240320223604224](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320223604224.png)

![image-20240320223728622](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240320223728622.png)

## MQ异步 解决消息一致性报错实录

![image-20240321013510154](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321013510154.png)

导致发现我置顶的找不到了

## ES集群

### 部署es集群

我们会在单机上利用docker容器运行多个es实例来模拟es集群。不过生产环境推荐大家每一台服务节点仅部署一个es的实例。

部署es集群可以直接使用docker-compose来完成，但这要求你的Linux虚拟机至少有**4G**的内存空间

### 4.1.创建es集群

首先编写一个docker-compose文件，内容如下：

```sh
version: '2.2'
services:
  es01:
    image: elasticsearch:7.12.1
    container_name: es01
    environment:
      - node.name=es01
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es02,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data01:/usr/share/elasticsearch/data
    ports:
      - 9200:9200
    networks:
      - elastic
  es02:
    image: elasticsearch:7.12.1
    container_name: es02
    environment:
      - node.name=es02
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es03
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data02:/usr/share/elasticsearch/data
    ports:
      - 9201:9200
    networks:
      - elastic
  es03:
    image: elasticsearch:7.12.1
    container_name: es03
    environment:
      - node.name=es03
      - cluster.name=es-docker-cluster
      - discovery.seed_hosts=es01,es02
      - cluster.initial_master_nodes=es01,es02,es03
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    volumes:
      - data03:/usr/share/elasticsearch/data
    networks:
      - elastic
    ports:
      - 9202:9200
volumes:
  data01:
    driver: local
  data02:
    driver: local
  data03:
    driver: local

networks:
  elastic:
    driver: bridge
```





es运行需要修改一些linux系统权限，修改`/etc/sysctl.conf`文件

```sh
vi /etc/sysctl.conf
```

添加下面的内容：

```sh
vm.max_map_count=262144
```

然后执行命令，让配置生效：

```sh
sysctl -p
```



通过docker-compose启动集群：

```sh
docker-compose up -d
```





### 4.2.集群状态监控

kibana可以监控es集群，不过新版本需要依赖es的x-pack 功能，配置比较复杂。

这里推荐使用cerebro来监控es集群状态，官方网址：https://github.com/lmenezes/cerebro

课前资料已经提供了安装包：

![image-20210602220751081](E:/BaiduNetdiskDownload/1、微服务开发框架SpringCloud+RabbitMQ+Docker+Redis+搜索+分布式微服务全技术栈课程/实用篇/学习资料/day07-Elasticsearch03/资料/assets/image-20210602220751081.png)

解压即可使用，非常方便。

解压好的目录如下：

![image-20210602220824668](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20210602220824668.png)

进入对应的bin目录：

![image-20210602220846137](E:/BaiduNetdiskDownload/1、微服务开发框架SpringCloud+RabbitMQ+Docker+Redis+搜索+分布式微服务全技术栈课程/实用篇/学习资料/day07-Elasticsearch03/资料/assets/image-20210602220846137.png)



双击其中的cerebro.bat文件即可启动服务。

![image-20210602220941101](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20210602220941101.png)



访问http://localhost:9000 即可进入管理界面：

![image-20210602221115763](E:/BaiduNetdiskDownload/1、微服务开发框架SpringCloud+RabbitMQ+Docker+Redis+搜索+分布式微服务全技术栈课程/实用篇/学习资料/day07-Elasticsearch03/资料/assets/image-20210602221115763.png)

输入你的elasticsearch的任意节点的地址和端口，点击connect即可：

![image-20210109181106866](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20210109181106866.png)

绿色的条，代表集群处于绿色（健康状态）。



### 4.3.创建索引库

#### 1）利用kibana的DevTools创建索引库

在DevTools中输入指令：

```json
PUT /itcast
{
  "settings": {
    "number_of_shards": 3, // 分片数量
    "number_of_replicas": 1 // 副本数量
  },
  "mappings": {
    "properties": {
      // mapping映射定义 ...
    }
  }
}
```





#### 2）利用cerebro创建索引库

利用cerebro还可以创建索引库：

![image-20210602221409524](E:/BaiduNetdiskDownload/1、微服务开发框架SpringCloud+RabbitMQ+Docker+Redis+搜索+分布式微服务全技术栈课程/实用篇/学习资料/day07-Elasticsearch03/资料/assets/image-20210602221409524.png)

填写索引库信息：

![image-20210602221520629](E:/BaiduNetdiskDownload/1、微服务开发框架SpringCloud+RabbitMQ+Docker+Redis+搜索+分布式微服务全技术栈课程/实用篇/学习资料/day07-Elasticsearch03/资料/assets/image-20210602221520629.png)

点击右下角的create按钮：

![image-20210602221542745](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20210602221542745.png)



### 4.4.查看分片效果

回到首页，即可查看索引库分片效果：

![image-20210602221914483](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20210602221914483.png)

### ES集群结点职责

![image-20240321143447485](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321143447485.png)

![image-20240321143517679](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321143517679.png)

每个结点默认是协调结点负责转发请求，为了职责分明，可以将data，ingest，master都为false，那么就是纯粹的协调节点了，LB就是loadBanlance做了一个负载均衡器，用于协调节点的负载均衡

![image-20240321144314301](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321144314301.png)

### 脑裂问题

就是比如说3个主节点，由于网络问题，老二老三联系不到大哥，以为大哥没了，然后他们就选了一个当新老大，当网络恢复的时候，发现有两个老大不知道听谁的了



es7出现前的解决方案

选票超过（节点数+1）/2(总共奇数节点)

现在脑裂问题在es7之后已经解决了

### 工作机理

![image-20240321154612027](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321154612027.png)

![image-20240321154751756](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321154751756.png)

### 集群结点故障转移

![image-20240321155707340](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321155707340.png)

![image-20240321155729931](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321155729931.png)

![image-20240321155649153](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321155649153.png)

经过探究发现，当主节点1宕机后，从节点也选一个主节点，新主节点会去看看宕机节点数据，然后分散同步到其他节点上，保证副本存在，当原来节点回归的时候，数据会分一些副本到原来主节点上但是不一定是原来的那些数据了

## 高可用集群

### 雪崩问题

所谓雪崩问题就是一个服务挂掉了导致所有服务几乎全部挂掉了，本质原因是因为拿不到资源就阻塞等待，导致前置服务中线程过多把tomcat内存占完了，导致其他请求也无法处理了，前置的前置从而也会出现这样的问题。





#### 解决方案

一共有4种

1.超时处理   

 比如说1秒还没有响应就报错，问题在于我请求一多还是不好使，顶多就是个缓解作用

2.舱壁模式    

解决方案是每个服务限定线程个数，类似于船舱有一层层隔板，当水涌入的时候不会祸害全部资源。

3.熔断降级

检测出现错误的请求数/总请求数，如果这个值大于所指定的阈值就不会再向故障服务发送任何请求，断开连接

4.流量控制

流量控制是一种预防措施，当很多请求来了的时候，通过sentinel技术按照服务可以接受的QPS来持续供给请求，也就避免了故障

![image-20240321182935905](../../AppData/Roaming/Typora/typora-user-images/image-20240321182935905.png)

### Sentinel 流量控制

已经下载到E盘了

![image-20240321192437966](../../AppData/Roaming/Typora/typora-user-images/image-20240321192437966.png)

限流隔离熔断都结合了



Jmeter压测工具

在桌面



### 流控模式

![image-20240321203451672](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321203451672.png)

关联模式的作用

通常是当优先级高的请求来了之后让优先级低的限流





链路模式通常是在两个业务去调用另一个业务的时候，为了防止高并发业务导致低并发业务无法去调用哪个业务，所以需要对两个链路的来源不同进行限流，对并发高的限流，如果两个业务调用的业务不是一个controller，那么需要添加注解。并且如果两个业务是在同一个类下的controller的时候需要去添加配置

![image-20240321210418417](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321210418417.png)

![](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321210524488.png)

防止sentinel默认将同一个类下的保存到同一个上下文，也就是一个链路下了，变成了两个子链路，这样就做不了链路控制了





### 流控效果

![image-20240321210810846](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321210810846.png)

![image-20240321211903600](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321211903600.png)

### 热点资源（特殊限流）

通俗来说就是

商品id访问量不同，根据不同的id去设置不同的QPS

在热点选项不是流控选项

![image-20240321213124385](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321213124385.png)



注意！！！！

sentinel默认不对mvc做热点参数限流

（也能理解，毕竟全做的话压力太大）

所以需要添加注解

![image-20240321213317580](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321213317580.png)

### Sentinel隔离和熔断降级

![image-20240321214418406](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321214418406.png)

无论是隔离去限制业务的线程数还是说

去检查线程失败的数量的比率然后去熔断

都是对于访问方也就是客户端的限制



所以最好的方式就是

#### FeignClient整合Sentinel实现降级

##### 报错!

什么autowridelement报错

解决方法是修改Spring Cloud 版本为Hoxton.SR9 启动成功

![image-20240321230716915](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321230716915.png)

自我总结



作用限流隔离熔断降级

降级"就是在系统出现问题时，通过牺牲部分功能或者数据的准确性，来保证整个系统的稳定性和可用性。



#### 线程隔离

![image-20240321231348871](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321231348871.png)

#### 熔断降级——断路器

![image-20240321235119048](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321235119048.png)

#### 熔断策略

![image-20240321235314044](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240321235314044.png)

#### 授权规则 防内鬼

哈哈哈，解决了我的疑惑，围过网关都可以访问微服务



![image-20240322000923827](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322000923827.png)

![image-20240322000946290](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322000946290.png)

![image-20240322001045834](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322001045834.png)

三足鼎立

![image-20240322004421814](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322004421814.png)

#### 顶级BUG

*cluster-name*的bug

 逆天bug分享，一次white page404一次正常返回请求



当我以为是网关的问题的时候，经过打印发现不是，而是在之前的学习中吗，添加了一个上海集群的服务，根据负载均衡机制，一次被分配给那个服务一次分配给我现在添加权限校验的网关服务，不过想不明白的是，虽然是一个业务代码，但是我代码新添加了网关过滤逻辑啊，为啥它能正常返回

![image-20240322010207245](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322010207245.png)后来经过搜索发现

因为我的强制关机

这可能是因为你的服务在注销之前没有正确地关闭。当一个Spring Cloud应用关闭时，它应该会自动从Nacos的服务注册表中注销。但是，如果应用没有正常关闭（例如，如果它崩溃了或者被强制杀掉)，那么它可能就不会从注册表中注销。



所以关机也是个技术活                      



#### 重要接口                  

![image-20240322103223748](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322103223748.png)

### 规则持久化

![image-20240322103555806](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322103555806.png)

pull模式



缺点在于虽然写入了数据库但是由于同一个实例只有一个sentinel客户端，所以有客户端的实例可以在写入数据库或者文件之间，更新自己的本地缓存，但是没有客户端的实例呢，就只能去轮询同步了

所以有时效性问题

![image-20240322103830759](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322103830759.png)

![image-20240322104141627](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322104141627.png)

## 分布式事务

![image-20240322120452170](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322120452170.png)

### Seata

解决分布式事务问题的

![image-20240322125810137](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322125810137.png)

![image-20240322125851360](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322125851360.png)

![image-20240322130157989](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322130157989.png)

所谓CAP定理其实就是一致性可用性和分区



因为服务之间都是通过网络连接不可避免出现分区

而出现分区之后一致性和可用性不能同时兼顾



大智慧BASE理论解决CAP的方法

![image-20240322130600431](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322130600431.png)

![image-20240322130811820](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322130811820.png)

![image-20240322131220522](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322131220522.png)

### 逆天BUG

BUG的报错是，cannot create Bean什么什么，以为是链接错误或者配置错误，甚至还在seata-server中发现了两个版本的mysql驱动连接文件，默认选择的是5版本的删了就报错，8版本的改名成5版本的，想来一招偷天换日，不好使，更改

file.conf不好使，因为它不是真正的配置文件，要修改就去修改在nacos的配置文件

终于解决了，首先是mysql8之后的要加cj问题否则无法正常启动

![image-20240322202056717](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322202056717.png)![image-20240322202115823](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322202115823.png)

2.要添加nacos注解

![image-20240322202220557](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322202220557.png)

3.注意JVM内存，毕竟启动的太多了，sentinel，nacos，seata还有运行的微服务





### 1.Seata的XA模式

![image-20240322203601894](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322203601894.png)

强一致性



简单的说就是两个子服务被RM所管理，执行完sql报告状态给TC。，TC分析报告然后统一返回结果，告诉他们应该怎么做





### 2.Seata的AT模式

![image-20240322211238979](../../AppData/Roaming/Typora/typora-user-images/image-20240322211238979.png)

![](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322211404985.png)

最终一致





### AT模式脏写问题

![image-20240322212054967](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322212054967.png)



本质原因呢其实就是因为一阶段提交事务和二阶段对于事务的回滚还是删除快照中间有锁的释放过程，让其他线程获得到了锁，然后事务1可能说失败了回滚的时候居然把事务2的更新给覆盖掉了跟没执行一样



### AT模式的写隔离

![image-20240322213257900](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322213257900.png)

简单我的理解来说就是为了防止中间插入一个小子过来修改，导致自己修改可能无效的情况，所以要全局上锁，

此时呢就让TC创建了一个锁，这个锁是在执行完事务释放DB锁的时候获取（原因是因为这时候才能知道是操作了哪个字段，哪个表）这时候事务2执行了sql之后去获取这个TC锁的时候，发现对于id为1的这个row，已经有了大哥在占有了，就等待把，然后等1执行完阶段二的操作释放TC锁，这时候2才能去执行。



这时候就有了个思考，全局上锁保持的一致性AT这不就和XA模式差不多了嘛，资源还不是全局被锁定了？



这是错误的，因为db锁和TC锁的粒度是不同的，TC锁是针对于某个字段的并发访问进行隔离，而db锁是对一张表的全部

![image-20240322214040269](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322214040269.png)

两次快照防止有不被seata管理的服务中间修改

### 3.TCC模式

![image-20240322215818489](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322215818489.png)



![image-20240322215809338](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322215809338.png)

![image-20240322220026754](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322220026754.png)

在try的时候阻塞了，然后超时之后肯定不能报错，不然重复执行了，所以就给了一个回滚信号，这时候回滚，但是吧被阻塞的事务，又没try，那么就只能执行一个空回滚，但是吧，假如cancel完了，我突然又能try了那么，这个事务就会悬挂在这，后续无论是confirm还是cancel都没有了，也就没人管它了，所以在try的时候要判断是否cancel过了，cancel的时候又得看try没try要不用空回滚哈哈哈





那么为了持久化这些状态就得用一张表



### 4.Saga模式

![image-20240322224822488](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322224822488.png)

没有TCC的资源预留和AT模式的全局锁



极致的并发处理，最后补偿，没有隔离性



### 4种模式比较

![image-20240322225247873](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322225247873.png)

### Seata的高可用

![image-20240322230314677](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322230314677.png)

人话来说就是为了有些seata-server挂了切换到其他集群，

而集群的配置实际是事务组的一个映射，而且是具体的服务yml文件中，那么如果要切换我就得去修改文件然后重启



为了实现热更新就用到了nacos的热更新功能，把这个配置放到nacos的配置文件中





## 分布式Redis集群

### Redis持久化

#### 1.RDB

![image-20240322232133376](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322232133376.png)

Redis默认的的持久化，是在停止运行的时候保存，但是我突然宕机呢就不会了



![image-20240322232350485](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322232350485.png)

bgsave子进程异步保存

![image-20240322232833652](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322232833652.png)

![image-20240322233101137](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322233101137.png)

子进程fork页表

然后物理内存只可读，修改在副本上(写时复制)



缺点，RDB持续时间很长，可能两次RDB保存中间的修改，导致数据丢失

#### 2.AOF

![image-20240322233630425](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322233630425.png)

![image-20240322233936872](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322233936872.png)

![image-20240322234259598](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322234259598.png)

![image-20240322234637685](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240322234637685.png)

### 1.Redis主从架构



![image-20240323130849352](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323130849352.png)

#### Redis同步原理

##### 第一次同步——全量不同

![image-20240323131242201](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323131242201.png)

redis第一次数据同步的时候需要用到的同步方案就是全量同步，因为比较慢，

请求数据同步过程如上，就是从发同步请求，主节点fork子进程然后去保存RDB文件发送过去，

如果在保存RDB的时候有命令就写到aof文件一样的一个log文件然后发送给从节点让从节点运行这些命令

![image-20240323131716092](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323131716092.png)

主节点怎么知道哪个是第一次来我需要去进行全量同步呢，

让从节点id和master 的id一样，这样我发现id和我一样，是自己人，再一看offset是0，就证明是第一次了？？？？

这是错误的



它的判断依据是第一次发现从的repID和自己的不一样，从想要增量同步就会被拒绝然后进行全量同步，主节点返回自己的id给从，之后从就一直用这个id进行增量同步了



![image-20240323132230712](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323132230712.png)

增量不同





![image-20240323132322253](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323132322253.png)

![image-20240323132509959](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323132509959.png)

log本质是一个环形数组结构，然后当master不断新命令，它就不断写，自己的offset就不断++，然后slave发来自己的offset一看他们直接的差异只需要去同步差异就行了，然后至于说是数组会被占满的问题是不会存在的，环形数组结构保证了只要他们直接数据差异不超过一个数组大小，就能去同步数据

![image-20240323132807564](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323132807564.png)

### Redis主从集群优化

![image-20240323133117558](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323133117558.png)

网络直接IO代替磁盘IO（要求网络好带宽高速度快）

redis单节点不要存太多，减小RDB的磁盘IO

提高log大小，这样短时间内都不用全量同步

采用主 链 链结构，减小master压力



![image-20240323133400333](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323133400333.png)

### 2.Redis哨兵架构

![image-20240323134120776](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323134120776.png)

![image-20240323134437699](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323134437699.png)

![image-20240323134533601](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323134533601.png)

选哪个节点主要就是去看优先级和offset



选到节点，给它发信号slaveof则就是主节点了，然后发给其他人现在要slaveof谁，然后从节点也有了，以前的主节点呢，配置文件也是被改成主节点是新主节点了





redis-sentinel和sentinel不是一个东西，然后的话每个sentinel没有主从之分共同监听集群中的节点，在配置文件中只需要指定每一个sentinel的主节点是谁就行了，然后他就能监听主节点及其所有子节点的连接状态了



然后当出现故障的时候，故障转移的时候先会去通过投票去选出一个sentinel大哥去唯一的处理redis主节点变更，然后的话，假如有多个人发现了，同时进行投票怎么办？这个呢是按照时间去同意投票请求，比如C能接受到两个投票请求，谁先来就同意谁，后来的也就不同意了。



选redis大哥的时候1看优先级2看offset



~~~yml
version: '3'
services:
  redis-master:
    image: redis:latest
    container_name: redis-master
    ports:
      - "6379:6379"
    command: redis-server --requirepass 123456
    networks:
      - redis-net

  redis-slave1:
    image: redis:latest
    container_name: redis-slave1
    ports:
      - "6378:6379"
    command: redis-server --slaveof redis-master 6379 --masterauth 123456 --requirepass 123456
    networks:
      - redis-net

  redis-slave2:
    image: redis:latest
    container_name: redis-slave2
    ports:
      - "6377:6379"
    command: redis-server --slaveof redis-master 6379 --masterauth 123456 --requirepass 123456
    networks:
      - redis-net
 sentinel1:
    image: bitnami/redis-sentinel:latest
    container_name: sentinel1
    ports:
      - "27001:26379"
    environment:
      - REDIS_MASTER_PASSWORD=123456
      - REDIS_SENTINEL_DOWN_AFTER=5000
      - REDIS_SENTINEL_FAILOVER_TIMEOUT=60000
      - SENTINEL_NAME=sentinel1
      - REDIS_MASTER_HOST=81.70.175.60
      - REDIS_MASTER_PORT=6379
    volumes:
      - ./sentinel.conf:/opt/bitnami/redis-sentinel/conf/sentinel.conf
    networks:
      - redis-net

  sentinel2:
    image: bitnami/redis-sentinel:latest
    container_name: sentinel2
    ports:
      - "27002:26379"
    environment:
      - REDIS_MASTER_PASSWORD=123456
      - REDIS_SENTINEL_DOWN_AFTER=5000
      - REDIS_SENTINEL_FAILOVER_TIMEOUT=60000
      - SENTINEL_NAME=sentinel2
      - REDIS_MASTER_HOST=81.70.175.60
  - REDIS_MASTER_PORT=6379
    volumes:
      - ./sentinel.conf:/opt/bitnami/redis-sentinel/conf/sentinel.conf
    networks:
      - redis-net
  sentinel3:
    image: bitnami/redis-sentinel:latest
    container_name: sentinel3
    ports:
      - "27003:26379"
    environment:
      - REDIS_MASTER_PASSWORD=123456
      - REDIS_SENTINEL_DOWN_AFTER=5000
      - REDIS_SENTINEL_FAILOVER_TIMEOUT=60000
      - SENTINEL_NAME=sentinel3
      - REDIS_MASTER_HOST=81.70.175.60
      - REDIS_MASTER_PORT=6379

    volumes:
      - ./sentinel.conf:/opt/bitnami/redis-sentinel/conf/sentinel.conf
    networks:
      - redis-net
networks:
  redis-net:
    driver: bridge

~~~

#### 哨兵机制报错实录！！！（排查时间4小时+）

BUG报错1

无法连接未定义的master

![image-20240323194744672](../../AppData/Roaming/Typora/typora-user-images/image-20240323194744672.png)

当你没指定的时候默认就是mymaster而不是你配置文件中指定的redis-master

BUG报错2

显示连接不上容器

问题

- REDIS_MASTER_HOST=81.70.175.60不能写容器内的容器名

BUG报错3

一旦程序运行，去连接集群之后，主redis节点立马就不能set了，而且变成了一个从节点，你去访问其他从节点以为是主从切换了，结果并不是，而是全都不能set，当我非常郁闷不已的时候，以为是我的配置文件有问题，但是我发现控制台一直一会通道连接成功，一会又连接不到哪个节点，一直循环，但是我只是一个简单的测试啊，我去网上搜索看到一个和我差不多的是因为主节点阻塞而切换了从节点，我后来想了想第一次程序运行的时候还是正常的，说明也就不是配置的问题，于是我就把es集群给down了，



恢复内存之后，成功了，也没有了任何报错





![image-20240323195035800](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323195035800.png)

![image-20240323195054101](../../AppData/Roaming/Typora/typora-user-images/image-20240323195054101.png)

### Redis分片集群结构

![image-20240323202621545](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240323202621545.png)

### 对于一致性hash为什么不用于redis分片集群的思考

[Redis Cluster集群之hash算法和一致性hash算法对比_redis哈希槽,为什么不用一致性哈希的方案-CSDN博客](https://blog.csdn.net/qq_28175019/article/details/125046957#:~:text=为什么redis不采用一致性hash算法 看上面的两种方法，基本没有太大的区别。 数据来了hash运算，然后路由到不同的节点。 但是有一个比较明显的区别，就是当一个节点挂掉后数据的路由策略。 差异：,1. 假如node1因为热点key问题宕机了，然后选取slave1作为主节点，那么salve也会承受不了压力进而宕机。 由于一致性hash算法，那么master1被剔除了，它的请求就会路由到master2，然后master2也会宕机，然后master3也会宕机，导致 缓存的雪崩效应 。)

这篇文章讲的好好

## 多级缓存



![image-20240329170621830](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240329170621830.png)

具体的流程：

1.请求访问浏览器，查浏览器本地缓存

2.再去访问请求 到nginx负载均衡服务器上，转发到nginx服务集群（集群由Openresty管理）

3.查询nginx的服务集群配置的共享词典本地缓存

4.查询不到去查redis缓存

5.查不到去查tomcat的进程缓存通过（caffeine实现）

6.再查不到通过caffeine的函数去调用数据库查询语句去返回数据



数据写回本地缓存是在openresty的lua文件里定义的，

文件里需要写的逻辑是先查本地缓存，查不到去查redis，还差不到查数据库，

然后这时候肯定能查到了，是redis查到的还是数据库查到的，不知道，

反正是查到了，然后写入nginx的本地缓存



至于redis和数据库的一致性通过canal来处理

### Nginx本地缓存

#### Lua语法

lua的数组取值的时候不能从下标0开始，得从下标1开始



### JVM进程缓存

#### Caffeine高性能本地缓存库

#### 清除缓存策略

1.可以选择设置缓存有效期

2.可以选择设置缓存有效个数



### 缓存同步策略

![image-20240329212421770](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301109630.png)

## MQ常见问题及其可靠性![image-20240330103355678](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/image-20240330103355678.png)

### 消息可靠性问题

发送时丢失

1.到exchange前丢失

2.到queue前丢失



发送到丢失

3.MQ宕机，消息队列的消息就丢失了

4.consumer接受到消息未消费就丢失





### 消息可靠性（防丢失）



#### 生产者确认机制

保证消息传到队列里

![image-20240330104229112](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301109351.png)

假如消息成功到了交换机——返回ack

没有则——返回nack



到了交换机没路由到队列，返回publish-return ack

只有到了queue消息被取走才返回publish-confirm ack





很多消息返回很多ack那么怎么知道是哪个消息成功or失败需要重发

（消息都有一个全局ID）



#### 实现生产者确认

1.在publisher微服务中添加配置

2.编写ReturnCallback（全局唯一）

3.ConfirmCallback编写

![image-20240330111023524](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301110662.png)

![image-20240330111059575](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301110775.png)

![image-20240330111137902](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301111048.png)

#### BUG实录

明明全部消息都成功投递到了exchange而且被正确被消息队列处理了，但是结果显示消息没有被投递到交换机



非常懵逼，本来以为是没有手动去返回ack的原因，但是本来就默认自动返回ack啊



解决方案：

![image-20240330133920165](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301339380.png)

在后面添加上这段代码，为了不让rabbitmq太快去断开连接

#### 消息失败的几种情况

![image-20240330134256913](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301342061.png)

### 消息持久化（防宕机）

#### 交换机持久化和队列持久化

![image-20240330134605243](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301346406.png)

单走一张6，队列和交换机持久化，消息不会持久





#### 消息持久化

![image-20240330135256074](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301352297.png)

### 消费者消息确认（防失败，服务挂）

虽然MQ有预取机制，但是并不代表消息被取走之后立即就被消息队列删了，而是有消息确认的保障

![image-20240330140510872](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301405066.png)

none就是你消息传来了，只要我异常就扔了，消息队列不会重发



auto就是你消息来了，由spring容器的AOP机制去检查异常与否，异常返回nack，否则ack，nack的时候，消息就会从消息队列重发，如果你没有异常的处理逻辑那可能就会导致死循环，一直重发一直返回nack



#### 消息retry配置

![image-20240330141554635](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301415839.png)

#### 错误交换机—重试完成后错误的记录

![image-20240330152435189](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301524318.png)

![image-20240330152823912](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301528073.png)

![image-20240330153000567](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301530767.png)





错误交换机，当出现问题之后消息就会被发送到error的交换机转发到error消息队列

在队列中的消息可以被管理员查看请求头的错误栈信息及其payload进行代码的修改和调整

![image-20240330162015013](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301620169.png)



#### 死信交换机

![image-20240330163351615](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301633807.png)





#### 对比错误交换机和死信交换机

![image-20240330163621405](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301636613.png)

你会发现他俩一个是消费者发送一个是队列发送



一个是程序错误一个是过期，被拒绝，队列满了（清理老的，先进的）

#### 如何绑定死信交换机

![image-20240330163829753](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301638879.png)

### TTL

![image-20240330164149292](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301641463.png)

使用TTL和死信交换机实现延时队列



什么意思呢其实就是原本队列如果在TTL时间内不消费就会被加入死信队列

然后消费者去监听死信队列，也就实现了TTL时间后才会去收到消息



#### 两种TTL配置

![image-20240330164441035](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301644207.png)

![image-20240330164529656](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301645841.png)

![image-20240330172625280](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301726426.png)



### 延迟队列（用插件不用TTL+死信）



![image-20240330172934236](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301729391.png)

比如说15min未支付，15miin通知消费者去取消订单





使用方式，

1.就是你去下载插件，放到那个插件包下



2.然后创建延迟队列就在控制台去创建就行了不需要去在代码中创建



![image-20240330173958346](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301739488.png)

3.设定延迟的时间在哪设定呢，是在消息请求头里设置

![image-20240330174206278](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301742411.png)

### SpringAMQP实现延时队列

![image-20240330174236941](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301742122.png)

![image-20240330174333218](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301743386.png)



### 忽略报错

![image-20240330174520789](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301745010.png)

由于callback函数它的触发时间一个是到达交换机之后的过期时间过了之后还有就是routing-key未匹配，所以要忽略





### 消息堆积问题

![image-20240330181926326](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301819505.png)



#### 惰性队列——扩大队列容积的方法，因为普通队列在内存（有上限）

![image-20240330182558925](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301825090.png)

上面直接改变队列为惰性队列



下面是声明为惰性队列

![image-20240330182755277](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301827460.png)

##### 比较与评价

![image-20240330182931819](https://cdn.jsdelivr.net/gh/DUT-SUN/myImg/img/202403301829963.png)
