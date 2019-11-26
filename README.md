## SpringBoot+MyBatis整合Quartz作为调度中心 并自定义任务调度
#### 本例是一个将quartz集成为调度器中心的典型示例,主要用于定时执行一些Jar_JobHttp_Job，基于Spring Boot 2.1.5,spring-boot-starter-quartz

如果你只是想要简单的SpringBoot整合Quartz调度,对定时任务进行 自定义逻辑,启动,暂停,恢复,删除,修改。 可以参考 :
[https://github.com/KittyMi/springboot-quartz](https://github.com/KittyMi/springboot-quartz)


##### 运行此项项目步骤：
> 1.首先数据库建库建表 resources文件下有个quartz.sql文件

> 2.配置数据库连接属性
```yaml
spring:
  datasource:
    dynamic:
      datasource:
        master:
          driver-class-name: com.mysql.cj.jdbc.Driver
          url: 数据库访问路径
          username: 用户名
          password: 密码
```
> 3 项目启动

``QuartzApplication 运行该方法``

> 4 启动看到下面的内容

```log
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 20 threads.
  Using job-store 'org.springframework.scheduling.quartz.LocalDataSourceJobStore' - which supports persistence. and is clustered.
```
SwaggerUI访问路径：
[http://ip:port/swagger-ui.html](http://127.0.0.1:9090/swagger-ui.html)