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

### 需求分析和结构整理
```历史数据迁移```，顾名思义就是将某个地方的历史数据转移到另外一个地方。需求明了，就一句话能够概括完整。其中要涉及的技术，其实也不是很难。无非是设计一个复制数据的流程，设计一个插入数据的流程和一个删除数据的流程。有三个流程去完成这件事情，节本就能搞定。当然，我们不能仅仅考虑把流程设计的能执行就好，还需要保障流程的健全性。虽然三个流程，就有了可行性，并且在实际应用中基本这三个流程是不可缺少的。但是我们必须保证数据安全，流程安全，并且可以直接，拓展，这样更有利于我们的项目的接入，使用。关键是解决了数据丢失，数据复制不完整的问题。
```支付行业```,支付行业中对迁移的应用，都是必须保障数据安全的，宁可数据迁移不成功，也必须保障数据不因为迁移二缺少，所以以上这三个流程并不完善。
```检查流程的加入```,处理以上说的三个流程，本次需求设计了另外两个检查流程,用来检查查询出来的数据和插入的数据是否完整，这样保障了源库数据和迁移到目标库的数据一致
```事务设计```,迁移过程中可能出现的各种系统异常会导致数据复制问题，如果不加入事务的考虑，会导致数据丢失，这也是需要解决的问题。
```内存管理```,在迁移过成功，如果一次性加载所以需要迁移的数据到项目或者内存中，基本都会导致OOM之类的问题，这也是我们要仔细考虑到的问题。

### 代码流程设计如下
数据迁移的办法有很多种，但是其目的都是一致的，都是将数据从源位置，转移到目标位置。项目中设计了一个通用流程来专门管理多种迁移办法，并且用到了多种设计模式来让代码更加简洁和更便于管理迁移。让代码灵活，并且方便二开或者多开。通用流程执行示意图如下，也是该项目主要流程
![代码流程设计](https://img-blog.csdnimg.cn/9b54d5faa291443193af4333f779e3ce.png#pic_center)

> 简洁的代码和流程，其实得益于原本的流程设计和设计模式的运用。

### 迁移物理架构图解
![迁移物理架构图解](https://img-blog.csdnimg.cn/531b3f0bb4394b2e8c01b618f9f7a208.png)

### 数据库模型设计
数据库模型的设计，是我们整个流程的核心，如果没有这个数据库的模型，我们的代码将会乱成一团。也正是数据库先一步做出了规定，让我们的代码能够更加的灵活。
#### 表名详解
- transfer_data_task: 迁移任务。在该表中配置对应的迁移任务。该表仅做任务管理，并且指定迁移的执行模式
- transfer_database_config: 数据源配置。一个迁移任务对应两个数据源配置，一个源数据库配置，一个目标数据库配置，由```database_direction```字段控制是需要源库还是目标，字段有两个值```source,target```
- transfer_select_config: 源库表配置。迁移任务通过task_id关联到源库查询配置，其中主要配置信息就是我们需要查询的表，表字段有哪些，查询条件是什么,一次性查询多少条。最终会在代码中拼接成为查询源库的SQL语句。
    拼接规则：```select 配置的字段 from 配置的表名 where 配置的条件 limit 配置的限制条数````，目前一个定时任务仅支持配置一个表的迁移。
- transfer_insert_config: 目标库表配置。迁移任务通过task_id关联到目标库配置，其中主要配置信息就是我们需要插入的表，需要插入的字段有哪些，查询的条件是什么，一次性插入多少条。最终会在代码中拼接成为插入目标库的SQL语句。
    拼接规则: ```insert into 配置的表名 (配置的字段) values (...),(),()...配置的插入条数据控制value后面有多少个值```
- transfer_log: 日志记录表。在整个迁移过程中，log是不可或缺的，这里设计了任务迁移的日志跟踪。
![数据库模型设计](https://img-blog.csdnimg.cn/e5568c3fc43d4ca3b3cf8a1657f009c0.png#pic_center)

### 日志的选型，日志设计（链路追踪）
- 日志框架用的是：Log4j
这是一个由Java编写可靠、灵活的日志框架，是Apache旗下的一个开源项目。最开始没有做过多的考虑，选用的原则就一个，市面上实际使用多的，并且开源的框架就行。不过日志的输出确实精心设计的。
在我们很多的项目当中，一个接口的日志，从前到后可能会有很多。在排查的过程中，我们也基本是跟着日志从代码最前面往代码最后面推。这个流程在实际应用当中会略微有一些问题，特别是有新线程，或者并发、大流量的情况基本就很难能一句一句排查了。因为你看到的上一句和下一句，并不一定是一个线程写出来的。所以这个时候我们需要去精心设计整个日志输出，让他能够在各种环境中一目了然。
```TLog```为啥要去自己设计，不直接使用TLog这样的链路最终日志框架呢？这里有一个问题，TLog链路追踪会在我们的项目中为每一次执行创建一个唯一键，不管传递多少个服务，只要都整合了TLog就能按照唯一键一路追踪下去。这里没有使用的原因只有一个，那就是为了方便管理，并且能够更加快捷的访问到日志。我们对日志的输出设计不仅仅做了链路追踪的流程，还讲唯一键直接存入了库里面，这样我们更加的便捷去追踪我们的迁移任务。
自己设计的日志追踪流程，确实会更加的灵便，而且由于放入了库中与log表关联起来，更便于我们查询。当然他也会有缺点，对于日志这一块我们如果忘记这条规则，会导致我们链路追踪在链路中断裂
按照链路追踪的模式我们设计了日志，其中最关键的环节就两个
- 唯一键的设计（雪花算法、UUID）
雪花算法和UUID的选型，刚开始自己实现了一个雪花算法
```java
package com.echo.one.utils.uuid;

/**
 * 雪花算法
 *
 * @author echo
 * @date 2022/11/16 11:09
 */
public class SnowflakeIdWorker {
    /**
     * 开始时间截 (2015-01-01)
     */
    private final long twepoch = 1420041600000L;
    /**
     * 机器id所占的位数
     */
    private final long workerIdBits = 5L;
    /**
     * 数据标识id所占的位数
     */
    private final long datacenterIdBits = 5L;
    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /**
     * 支持的最大数据标识id，结果是31
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    /**
     * 序列在id中占的位数
     */
    private final long sequenceBits = 12L;
    /**
     * 机器ID向左移12位
     */
    private final long workerIdShift = sequenceBits;
    /**
     * 数据标识id向左移17位(12+5)
     */
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    /**
     * 时间截向左移22位(5+5+12)
     */
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);
    /**
     * 工作机器ID(0~31)
     */
    private long workerId;
    /**
     * 数据中心ID(0~31)
     */
    private long datacenterId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param workerId     工作ID (0~31)
     * @param datacenterId 数据中心ID (0~31)
     */
    public SnowflakeIdWorker(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();
        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        // 如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            // 毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        // 时间戳改变，毫秒内序列重置
        else {
            sequence = 0L;
        }
        // 上次生成ID的时间截
        lastTimestamp = timestamp;
        // 移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
```

这个算法其实还是很优秀的，其中的分布式id保障唯一设计也确实能够很有效的帮组我们获取链路追踪的唯一键。不过在项目中也暴露出来了两个问题：
1、ID重复（雪花算法就是获取唯一id的，为啥会出现重复的呢？后面解释）
2、性能瓶颈

在使用过程中雪花算法出现重复，导致唯一键报错。
雪花算法出现重复的可能就几种：
1）、因为雪花算法强依赖机器时钟，所以难以避免受到时钟回拨的影响，有可能产生ID重复。
2）、同一台机器同一毫秒需要生成多个Id,并且对long workerId, long datacenterId控制不完善导致
第一种情况这基本没法避免，不过时钟回拨很久才发生一次。所以问题还是出现在第二种看情况，主要还是对代码的不完善导致。后面也正是因为代码的不完善直接改用UUID。UUID就一行代码就能够解决的事情，这里还要不断的去完善设计的代码。

UUID确实简洁，选型UUID看中的也是这一点，同时也还有其他因素。在整个项目中由于代码的灵活性和整个代码的开放性，在执行的过程中可能会出现多个线程，并行执行，并且为了资源能够更高效的执行，那么对锁的应用是要慎重考虑的。雪花算法中，使用了sync同步锁，当我们使用多线程去执行代码的时候，同步锁会成为重量级锁，并且成为我们项目执行的性能瓶颈。这样是最终选择UUID的重要因素


- 链路的设计
> 链路的设计目前还是极为简单的手动将需要加入到整个链路中的日志加上唯一键。
链路的设计比较简单，而且整个流程链路中，只需要在相应流程模式中进行链路的完善即可，比如：在多线程的环境下，怎么去保证当前线程是我原本的前一任务的链路线；执行模式中循环执行回来的时候，怎么保证新的流程能够用原本的唯一件

### 迁移模式
迁移模式总共三种，分别对应transfer_data_task中transfer_mode的0、1、2，推荐使用模式1
- 0、库 -> 文件 -> 库(不推荐使用)
#### 模式流程图
![库 -> 文件 -> 库](https://img-blog.csdnimg.cn/27b76db369e54a04b28b024321f53fb4.png)
该模式，可以有效的安全保障数据在迁移过程中，不会出现数据的错漏，文件相当于备份操作，后期可以直接追溯迁移过程。唯一的缺点就是大表迁移中，如果使用这种模式需要两个东西，一个是具备```local\temp```读写权限的账号，一个是足够的磁盘。大表数据写入磁盘很暂用空间，在测试中间，尝试了100w数据，写入文件完毕后，发现有10G（大表，150字段左右）

- 1、库 -> 库（推荐使用）
![库 -> 库](https://img-blog.csdnimg.cn/b67ff6761ea6463a800ca3019ec2bda3.png)

- 2、source.dump copy target.dump（有风险）
![source.dump copy target.dump](https://img-blog.csdnimg.cn/522e6f4c5bc043e9a41dc33121b926f0.png)


### 模块设计
```text
dataMigration
-----bin
---------startup.sh
-----doc
---------blog.sql
---------blogbak.sql
---------community.sql
-----src
---------main
-------------java
-----------------com.echo.one
---------------------common
-------------------------base
-----------------------------TransferContext.java
-----------------------------TransferDataContext.java
-----------------------------BeanUtilsConfig.java
-------------------------constant
-----------------------------DataMigrationConstant.java
-------------------------enums
-----------------------------DatabaseColumnType.java
-----------------------------DatabaseDirection.java
-----------------------------FormatPattern.java
-----------------------------StatusCode.java
-----------------------------TaskStatus.java
-------------------------exception
-----------------------------DataMigrationException.java
-------------------------factory
-----------------------------DataMigrationBeanFactory.java
-------------------------framework
-----------------------------config
---------------------------------RemoveDruidAdConfig.java
-----------------------------filter
---------------------------------WebMvcConfig.java
-----------------------------handler
---------------------------------GlobalExceptionHandler.java
-----------------------------result
---------------------------------Result.java
---------------------controller
-------------------------TransferDatabaseConfigController.java
-------------------------TransferDataTaskController.java
-------------------------TransferHealthy.java
-------------------------TransferInsertConfigController.java
-------------------------TransferLogController.java
-------------------------TransferSelectConfigController.java
---------------------dao
-------------------------TransferDatabaseConfigMapper.java
-------------------------TransferDataTaskMapper.java
-------------------------TransferInsertConfigMapper.java
-------------------------TransferLogMapper.java
-------------------------TransferSelectConfigMapper.java
---------------------DataMigrationApplication.java
---------------------job
-------------------------DataTaskThread.java
-------------------------TransferDataTaskJob.java
-------------------------TransferLogJob.java
---------------------po
-------------------------TransferDatabaseConfig.java
-------------------------TransferDataTask.java
-------------------------TransferInsertConfig.java
-------------------------TransferLog.java
-------------------------TransferSelectConfig.java
---------------------processer
-------------------------CheckInsertDataProcessModeOne.java
-------------------------CheckInsertDataProcessModeTwo.java
-------------------------CheckInsertDataProcessModeZero.java
-------------------------CheckInsertDataTransactionalProcessModeThree.java
-------------------------CheckSelectDataProcessModeOne.java
-------------------------CheckSelectDataProcessModeTwo.java
-------------------------CheckSelectDataProcessModeZero.java
-------------------------CheckSelectDataTransactionalProcessModeThree.java
-------------------------DeleteDataProcessModeOne.java
-------------------------DeleteDataProcessModeTwo.java
-------------------------DeleteDataProcessModeZero.java
-------------------------DeleteDataTransactionalProcessModeThree.java
-------------------------InsertDataProcessModeOne.java
-------------------------InsertDataProcessModeTwo.java
-------------------------InsertDataProcessModeZero.java
-------------------------InsertDataProcessOneThread.java
-------------------------InsertDataTransactionalProcessModeThree.java
-------------------------ProcessMode.java
-------------------------SelectDataProcessModeOne.java
-------------------------SelectDataProcessModeTwo.java
-------------------------SelectDataProcessModeZero.java
-------------------------SelectDataTransactionalProcessModeThree.java
---------------------service
-------------------------imp
-----------------------------TransferDatabaseConfigServiceImpl.java
-----------------------------TransferDataTaskServiceImpl.java
-----------------------------TransferHealthyServiceImpl.java
-----------------------------TransferInsertConfigServiceImpl.java
-----------------------------TransferLogServiceImpl.java
-----------------------------TransferSelectConfigServiceImpl.java
-------------------------TransferDatabaseConfigService.java
-------------------------TransferDataTaskService.java
-------------------------TransferHealthyService.java
-------------------------TransferInsertConfigService.java
-------------------------TransferLogService.java
-------------------------TransferSelectConfigService.java
---------------------utils
-------------------------DateUtils.java
-------------------------jdbc
-----------------------------JdbcUtils.java
-------------------------SpringContextUtils.java
-------------------------thread
-----------------------------CommunityThreadFactory.java
-------------------------thread
-----------------------------DataMigrationRejectedExecutionHandler.java
-------------------------thread
-----------------------------ExecutorServiceUtil.java
-------------------------uuid
-----------------------------SnowflakeIdWorker.java
---------main
-------------resources
-----------------application.yml
-----------------banner.txt
-----------------logback-spring.xml
-----------------mapper
---------------------TransferDatabaseConfigMapper.xml
---------------------TransferDataTaskMapper.xml
---------------------TransferInsertConfigMapper.xml
---------------------TransferLogMapper.xml
---------------------TransferSelectConfigMapper.xml
-----LICENSE
-----pom.xml
-----README.md
-----dataMigration.iml
```


### 设计模式
如果不考虑设计模式，直接硬编码在我们项目当中会有很多的藕合，也会导致整个代码的可读性很差。如下面的代码：
首先来看项目类的代码执行的步骤：
1、查询源库数据
2、检查查询出的源库数据
3、插入目标数据库
4、检查插入
5、删除源库数据
```java
    if(mode = 1) ...
        selectData();
        checkSelectData();
        insertData();
        checkInsertData();
        deleteData();
    if(mode = 2) ...
        selectData();
        checkSelectData();
        insertData();
        checkInsertData();
        deleteData();
    if(mode = 3) ...
        selectData();
        checkSelectData();
        insertData();
        checkInsertData();
        deleteData();
    ...
```
> 每次新增一个mode，我们就需要手动去修改代码，增加一个if，极度不易于拓展，而且if的多少影响阅读和美观。当我们加入设计模式之后，这样的if可以直接被取消。

- 桥接模式的应用
什么是桥接模式？桥接（Bridge）是用于把抽象化与实现化解耦，使得二者可以独立变化。这种类型的设计模式属于结构型模式，它通过提供抽象化和实现化之间的桥接结构，来实现二者的解耦。这种模式涉及到一个作为桥接的接口，使得实体类的功能独立于接口实现类。这两种类型的类可被结构化改变而互不影响。

可以在上面的代码中看到，假若我们能消除```if else```很多的代码就不会重复，比如其中的固定执行流程。然后每一个流程代码，我们设计成为一个固定的模板，每个模板都有一个固定的方法，这个时候，对于具体的实现独立出来就完美的解决了这样的问题。

首先解决第一个问题：将步骤代码抽象出来，不管哪个模式，都要走定义好的步骤方法。但是具体实现每个模式互不干扰
```java
public abstract class ProcessMode {

    public final void invoke(TransferContext transferContext) {
        try {
            handler(transferContext);
        } catch (DataMigrationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataMigrationException("system error, msg: " + e.getMessage());
        }
    }

    protected abstract void handler(TransferContext transferContext);

}
```
> 这里直接抽象出来一个类，保障每个流程步骤都属于他的实现类，这样解决了流程步骤的多实现互不干扰。

然后解决```if else```：有了抽象类了，我们这里直接使用mode去定位对应的具体实现。当然，这里要配合一个小巧的类的注入名的修改。原本靠```if else```检索位置，现在直接采用类型来定位，这样就不需要使用判断来做了。类加载到容器中都有自己固定的名字，这里我们将每一个mode对应的实现类设计成为带有mode的类，设计规则：```process + 功能 + mode号```， 然后我们按照类的实现自动加载使用对应的实现类
```java
ProcessMode sourceBean = SpringContextUtils.getBean("process.selectData." + transferContext.getTransferDataTask().getTransferMode(), ProcessMode.class);
```

在桥接模式中，我们可以看到需要一个桥（类）来连接对应的实现
![桥接模式](https://img-blog.csdnimg.cn/58181db83792448dbabba60d73d2aee8.png)
在我们的代码中其实也正是使用了这种思想，来让我们的代码每个mode方式之间互不干扰，但又能和代码紧密相连接。
![桥接模式代码实现](https://img-blog.csdnimg.cn/7cd408786afb40d7b6a0f678abc370ba.png)

```ProcessMode```完美的衔接了我们代码，充当了我们的桥

- 单例模式的应用
单利模式很好被理解，单例（Singleton）模式的定义：指一个类只有一个实例，且该类能自行创建这个实例的一种模式。单例模式有 三个特点：1、类只有一个实例对象； 2、该单例对象必须由单例类自行创建； 3、类对外提供一个访问该单例的全局访问点；

为什么用单利？
单利最常见的应用场景有
1、Windows的Task Manager（任务管理器）就是很典型的单例模式 2、项目中，读取配置文件的类，一般也只有一个对象。没有必要每次使用配置文件数据，每次new一个对象去读取。 3、数据库连接池的设计一般也是采用单例模式，因为数据库连接是一种数据库资源。 4、在Spring中，每个Bean默认就是单例的，这样做的优点是Spring容器可以管理 。 5、在servlet编程中/spring MVC框架，每个Servlet也是单例 /控制器对象也是单例.
在我们项目中，到哪里的场景很显然就是第一个，他就是一个任务管理器。作为一个开源的迁移数据项目，我们需要考虑不单单是每次仅执行一个任务，而是每次执行多个，并且每次可能会并发执行很多的任务，当然随之而来的就是数据安全为题，和任务重复的问题。这个我们后面在做解释

单利模式又分为：懒汉式、饿汉式。为了保证数据的安全，我们这里直接选用了```饿汉式```

具体实现如下：
```java
public class ExecutorServiceUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceUtil.class);

    private static ThreadPoolExecutor executorService;

    private static final int INT_CORE_POOL_SIZE = 50;
    private static final int MAXIMUM_POOL_SIZE = 100;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int WORK_QUEUE_SIZE = 2000;

    static {
        executorService = new ThreadPoolExecutor(
                INT_CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(WORK_QUEUE_SIZE),
                new CommunityThreadFactory(),
                new DataMigrationRejectedExecutionHandler());
    }

    private ExecutorServiceUtil() {
        if(executorService != null){
            throw new DataMigrationException("Reflection call constructor terminates execution!!!");
        }
    }

    public static ExecutorService getInstance() {
        logger.info("queue size: {}", executorService.getQueue().size());
        logger.info("Number of active threads: {}", executorService.getActiveCount());
        logger.info("Number of execution completion threads: {}", executorService.getCompletedTaskCount());
        return executorService;
    }
}
```
> 将我们的线程池进行单利化，为的就是解决任务多发或者高发的问题，有序的控制任务内存和执行。既然提到了多发或者高发，肯定需要解决的就是线程安全问题。这里我们不仅使用了单例，还对部分数据操作的类做了数据安全处理

#### 类关系图
![在这里插入图片描述](https://img-blog.csdnimg.cn/64c6847def1f4bd49de7f5c78471c2ab.png#pic_center)
对关键类都去定义了一个：```@Scope(value = "prototype")```,使用原型作用域，每个任务过来都会生成一个独有的类，这样有效的防止数据共享。

### 事务的管理
在数据迁移过程中，事务的执行是很重要的，如不能保证数据一致，可能在迁移过程中直接导致数据丢失。

项目中也有简单的对事务的应用。由于设计模式的问题，拓展能力很强，代码灵活度也很高。不过接踵而来的就是部分安全代码的问题。比如：事务。当我们独立了插入和删除的时候，我们的事务就被拆分了。那我们需要使用分布式事务，或者事务传播吗？

> 项目中并没有！

![事务](https://img-blog.csdnimg.cn/ad9bcf4ecaf048419b65b8a06523461d.png)
原本设计模式之前，直接包含在一个事务内即可，这里我们使用了设计模式之后，行不通了。

现有的事务逻辑
![事务](https://img-blog.csdnimg.cn/0a62c13fd1f744faa4413b4d7c914281.png)

不难发现，如果insertData出现问题，insertData的事务会回滚对应的插入数据，当时deleteData继续执行，这个时候肯定会来带数据一致性问题。不过这里采用了流程上的控制来解决这个问题。

假若：一个迁移任务只执行一次insertData，deleteData，为了保证数据一致性，我们将这两个流程设计了固定的执行先后顺序，并且，前面一个报错，后面不在继续执行。
![在这里插入图片描述](https://img-blog.csdnimg.cn/6c2182df3b214266909e9deafd970eb6.png)

报错如果出现在删除，那有什么影响，就一个：那就是源库的数据有一部分并未被删除掉，但是肯定是已经迁移到了目标库，这个时候严格意义上来讲，数据的迁移并不影响，仅仅只是多出来一份数据，没有数据安全问题。

### 总结
每个设计方案都不是完美的，都是巧妙的去设计，灵活的去适应。
假若不断迭代，问题肯定会不断出现，新的方案肯定也会不断的替代老方案。

### 拓展
当前项目真正应用应该怎么入手？怎么商用？

