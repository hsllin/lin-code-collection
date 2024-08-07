# springboot-elasticsearch

springboot集成elasticsearch  
1、ES连接客户端使用High Level REST Client  
2、集成swagger接口  
3、集成thymeleaf编写页面，vue2.0页面渲染  
4、需要事先安装elasticsearch  
5、需要事先安装ik分词器，[ik地址](https://github.com/medcl/elasticsearch-analysis-ik)  
6、ik分词检索/搜索关键字自动补全

## 版本

组件框架 | 版本
--- | ----
springboot | 2.1.7.RELEASE
swagger-ui | 2.9.2
elasticsearch-rest-high-level-client | 6.6.0
elasticsearch | 6.6.0
analysis-ik | 6.6.0

列表:  
[http://127.0.0.1:8080/es/search/bookList](http://127.0.0.1:8080/es/search/bookList)  
 ![image](https://raw.githubusercontent.com/keyvin-wei/springboot-elasticsearch/master/src/main/resources/static/img/bookList.png)  

分词搜索/关键字补全演示：  
![image](https://github.com/keyvin-wei/springboot-elasticsearch/blob/master/src/main/resources/static/img/searchVue.gif)