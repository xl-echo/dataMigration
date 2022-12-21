### 历史数据迁移
- 项目地址：```https://gitee.com/xl-echo/dataMigration```

历史迁移解决方案。微服务的架构为基础，使用多种设计模式，如：单利、桥接、工厂、模板、策略等。其中涉及的核心技术有，多线程、过滤器等。致力于解决mysql大表迁移的问题。提供多种迁移模式，如：库到库、库到文件再到库等！ 
Historical migration solution. Based on the architecture of microservices, multiple design patterns are used, such as simple interest, bridge, factory, template, strategy, etc. The core technologies involved include multithreading, filters, etc. It is committed to solving the problem of MySQL large table migration. Provide multiple migration modes, such as library to library, library to file, and then to library

### 开发环境
工具 | 版本 | 描述
--- | --- | --- 
IDEA | 2019.2 | 
JDK | 1.8 | 
MySQL | 5.7 | 5.7 +


### 技术框架
技术 | 描述 | 用途 | 备注
--- | --- | --- | ---
SpringBoot | 基础框架 | 构建系统架构 | 
MyBatis | ORM框架 | 构建数据层 | 
JDBC | 构建外部数据源 | 用于配置迁移 | 
Maven | 项目管理 | 项目管理 | 
ExecutorService | 线程池 | 分批执行任务 | 
Bloom Filter | 布隆过滤器 | 校验数据是否一致 | 

### 功能模块
![在这里插入图片描述](https://img-blog.csdnimg.cn/d65939b87d89409884bfb73b854762e0.jpeg#pic_center)


### 设计模式
- 桥接
- 单例
- 工厂
- 模板
- 策略

### 迁移逻辑图解

开源项目持续开发中，文档将跟随项目持续更新

### 项目陷入深坑一：
ResultSet拼接5000条数据需要200多秒，拼接代码如下:
![请添加图片描述](https://img-blog.csdnimg.cn/ff87289f95b541c49d9b5910ce2ec1f0.png)
![请添加图片描述](https://img-blog.csdnimg.cn/b84f34473ec04f1bb65e0e3e7d695bc9.png)

### 项目陷入深坑二：
数据库连接不够用
![请添加图片描述](https://img-blog.csdnimg.cn/04de6a649ca64a578e58191306d7c08d.png)

