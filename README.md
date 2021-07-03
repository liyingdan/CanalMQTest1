# CanalMQTest1
跟上个项目CanalMQTest的目的一致，不过该项目自己写连接监控

在项目中因为写了canal的连接，就会导致 RabbitMQ 初始化不了，所以就实现了 Springboot 的 CommandLineRunner 方法，并设置优先级。
该项目只整合了 canal 监听了据库以及 RabbitMQ 监听队列，没写具体的逻辑在里面。
