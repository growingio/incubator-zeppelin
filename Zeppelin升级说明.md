
# Zeppelin升级

主要修改点：

* 1 用户执行任务的审计日志
* 2 在Form中支持`shell`,`JS`脚本
* 3 初始化时 Helium下载太慢

## 一、编译源码

```
mvn clean package -DskipTests -Dcheckstyle.skip   -Pspark-2.0 -Phadoop-2.4 -Pr -Pscala-2.11
```

```
mvn package -DskipTests -Dcheckstyle.skip  -Pspark-2.0 -Phadoop-2.4 -Pr -Pscala-2.11
```

执行：

```
./bin/zeppelin-daemon.sh start
```

### 采坑

* webpack升级到`webpack@^3.0.0`
* 本地安装R语言环境，`brew install R`
* 安装R包`evaluate`: `进入R  >install.packages("evaluate")`

## 二、修改源代码

### 1. 日志审计功能

相较之前版本的更改，更为简单。新版本的zeppelin在执行单元`Paragraph`中新增了`AuthenticationInfo`信息，这样`NOTE`,`AuthenticationInfo`,`ScriptText`这些执行信息都是全的。可以很方便的进行日志审计。

* 将`Paragraph`执行日志独立到`PO-YYYY-MM-DD.log`中，每天一个文件。(`op`是`paragraph-operation`的缩写)
* 日志格式:
  * 运行一个`Paragraph`
   `GIO Paragraph Start: paragraph_id: {}  interpreter: {} info:[note_name: {} , user: {} , roles: {}]`
  * 处理Script：
      `GIO Paragraph RUN: paragraph_id:{}  script:{} interpreter:{} info:[note_name:{},user:{},roles:{}]`
  * 执行成功:
    `"GIO Paragraph {}: paragraph_id: {}  info:[note_name:{},user:{},roles:{}]"`
  * 执行失败:
  	`GIO Paragraph {}: paragraph_id: {}  exception: {}, result: {},info:[note_name:{},user:{},roles:{}]`

### 2. 在Form表单中执行`shell`和`js`脚本

* `form`中满足以重音符包围，且以sh开头的为shell脚本
* `form`中满足以重音符包围，且以js开头的为js脚本
* 用户点击运行之后，将form中满足要求的内容，作为脚本执行之后，将结果字符串作为输入执行线面流程。
  
#### tips:坑

Mac 中shell脚本和linux不同。
如取昨天日期

* `Linux`: `date +%Y-%m-%d --date="-1 day"` 或者 `date -d '-1 day' '+%Y-%m-%d'`  
* `Mac`:   `date -v-1d +%Y-%m-%d`

### 3. Helium下载太慢，改为本地配置

* 在conf目录下的zeppelin-site.xml中进行配置。值为相对conf的文件目录。如果不进行配置则默认值为helium-registry.json。 JSON可以手动从[更新](https://s3.amazonaws.com/helium-package/helium.json)下载更新。

```
    <property>
        <name>zeppelin.helium.local.json.register</name>
        <value>helium_registry.json</value>
        <description>The directory of helium-registry.json relative to conf</description>
    </property>
    
```

### 4. 修改部分界面显示
