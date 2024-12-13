### 在Docker中安装ELK (ElasticSearch, Logstash, Kibana)

 1.将项目下的elk-docker文件夹发送到安装docker的linux主机[最小4G内存]
 
 2.修改文件夹所有者`sudo chown -R root:root elk-docker/`
 
 3.修改文件权限`sudo chmod -R 777 elk-docker/`, 确保挂载数据卷时,文件可访问
 
 4.修改最大虚拟内存大小: `vi /etc/sysctl.conf`

```
  vm.max_map_count=655360
```

  执行:`sysctl -p`
 
 5.运行`docker-compose up -d`启动3个容器
 
 6.启动成功后: 访问`http://[docker主机ip]:5601`进入kibana的Web控制台
