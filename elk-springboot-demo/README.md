### ELK整合Springboot完成应用的日志收集存储和分析的案例

#### 版本信息:
 > Elasticsearch 7.8.0<br>
 > Logstash 7.8.0<br>
 > Kibana 7.8.0<br>
 > spring-boot-starter-parent 2.3.0.RELEASE<br>

#### 项目结构:
```
  ├─src                       源码文件目录
  ├─elk-docker                Docker安装ELK文件
  ├─ELK-Docker.md             Docker中安装ELK文档
  ├─.gitignore                .gitignore文件
  ├─README.md                 README.md文件
  └─pom.xml                   pom文件
 ```

#### 如何运行:
  1. 根据[Elk-Docker.md](./ELK-Docker.md)文件在Docker中启动3个容器
  
  2. 启动成功后浏览器访问`http://[docker主机ip]:5601`进入Kibana的Web控制台
  
  3. 配置logback-spring.xml的`<destination>[docker主机ip]:4560</destination>`的ip,确保日志能通过
     logstash收集并存储到Elasticsearch
  
  4. 启动SpringBoot应用, 访问`http://localhost:8080/test/outputLog`输出一些日志
  
  5. 在Kibana的Web控制台,定义Elasticsearch的Create Index Patterns输入springboot-logs-2020.06.20
     创建过滤条件索引, 如下图所示:
     <img src="https://upload-images.jianshu.io/upload_images/14511997-a7d72702154c8b84.png"/>
     
  6. 创建完成后, 回到Kibana的Discover即可监控日志, 如下图所示:
     <img src="https://upload-images.jianshu.io/upload_images/14511997-2d7b1ccd3386207c.png"/>
     