# 数据脱敏工具
源自
https://juejin.cn/post/7225218026097852472

### ******😼开始******

******

本文主要分享什么是数据脱敏，如何优雅的在项目中运用一个注解实现数据脱敏，为项目进行赋能。希望能给你们带来帮助。😀

### ❓什么是数据脱敏

数据脱敏是一种通过去除或替换敏感数据中的部分信息，以保护数据隐私和安全的技术。其主要目的是确保数据仍然可以在各种场景中使用，同时保护敏感信息，防止数据泄露和滥用。数据脱敏通常用于处理包含个人身份信息和其他敏感信息的数据集，如手机号、姓名、地址、银行卡、身份证号、车牌号等等。

在数据脱敏过程中，通常会采用不同的算法和技术，以根据不同的需求和场景对数据进行处理。例如，对于身份证号码，可以使用掩码算法（masking）将前几位数字保留，其他位用“X”或"\*"代替；对于姓名，可以使用伪造（pseudonymization）算法，将真实姓名替换成随机生成的假名。

下面我讲为大家带来数据脱敏掩码操作，让我们一起学起来吧。🎮

### ❗开胃菜

下面给大家介绍的是使用两种不同的工具类进行数据脱敏，而我们今天的主题使用一个注解解决数据脱敏问题的主要两个工具类。来跟着我学习吧。😼

#### 使用Hutool工具类实现数据掩码

比喻说我们现在要对手机号进行数据脱敏，**前三后四不掩码，其他全部用\*进行掩码**

如下图代码所示，我们定义了一个手机号：17677772345，需要进行数据脱敏。

调用的hutool的信息脱敏工具类。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/23c1063447df4ab7a0d4266fb34130d3~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

我们运行一下看看结果。一个简单的数据脱敏就实现了。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/bd478aefcd794af4863a6968eb1b0853~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

##### Hutool信息脱敏工具类

根据上面的一个Demo，大家可以看到我使用了Hutool的信息脱敏工具类进行对手机号掩码脱敏。那么让我们一起看看Hutool信息脱敏的工具类吧。

官网文档：

[hutool.cn/docs/#/core…](https://link.juejin.cn/?target=https%3A%2F%2Fhutool.cn%2Fdocs%2F%23%2Fcore%2F%25E5%25B7%25A5%25E5%2585%25B7%25E7%25B1%25BB%2F%25E4%25BF%25A1%25E6%2581%25AF%25E8%2584%25B1%25E6%2595%258F%25E5%25B7%25A5%25E5%2585%25B7-DesensitizedUtil "https://hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E4%BF%A1%E6%81%AF%E8%84%B1%E6%95%8F%E5%B7%A5%E5%85%B7-DesensitizedUtil")

看一下官网的介绍，支持多种脱敏数据类型，满足我们大部分需求，如果需要自定义还提供了自定义的方法实现。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/794a96fabaa24603a007932fa542b60a~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

下面是里面定义号的脱敏规则，直接调用就可以实现简单的数据脱敏，这里给大家介绍是因为我们今天要给大家带来的注解实现数据脱敏核心就是利用我们的Hutool提供的工具类实现，支持自定义隐藏。

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/ab83fd7b7e7541158f9201b8e30d5909~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

#### 使用Jackson进行数据序列化脱敏

首先创建一个实体类，此实体类只有一个测试的手机号。

注解的讲解：

@Data：lombok的注解生成get,set等等方法。

@JsonSerialize(using = TestJacksonSerialize.class)：**该注解的作用就是可自定义序列化，可以用在注解上，方法上，字段上，类上，运行时生效等等，根据提供的序列化类里面的重写方法实现自定义序列化。可以看下下面的源码，有兴趣的朋友可以去了解一下，也能解决我们日常开发中很多场景。** 🎈

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/419e30ccddea4bdb99f7635ede410892~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

```ruby
@Data public class TestDTO implements Serializable { /** * 手机号 */ @JsonSerialize(using = TestJacksonSerialize.class) private String phone; }
```

然后创建一个TestJacksonSerialize类实现自定义序列化。

此类主要继承JsonSerializer，因为我们这里需要序列化的类型是String泛型就选择String。**注意如果你使用此注解作用在类上的话，这里就是你要序列化的类。**

重写序列化方法，里面的实现很简单就是调用我们的hutool工具类进行手机号数据脱敏。

```less
public class TestJacksonSerialize extends JsonSerializer<String> { @Override @SneakyThrows public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) { // 使用我们的hutool工具类进行手机号脱敏 jsonGenerator.writeString(DesensitizedUtil.fixedPhone(String.valueOf(str))); } }
```

让我们测试一下吧，因为此注解是运行时生效，我们定义一个接口来测试。🏄

```less
@RestController @RequestMapping("/test") public class TestApi { @GetMapping public TestDTO test(){ TestDTO testDTO = new TestDTO(); testDTO.setPhone("17677772345"); return testDTO; } }
```

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/fff8edbbf49342e3bdde22888101028c~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

可以看到测试成功，经过上面的两个工具类的介绍，联想一下我们怎么通过两个工具类定义一个自己的注解实现数据脱敏呢❓

### 🎐注解实现数据脱敏

**我们考虑一下，工具类现在有了，那么我们怎么去实现一个注解优雅的解决数据脱敏呢？**

请看下文，让我带大家一起学习。🕹️

#### 1️⃣定义一个注解

定义一个Desensitization注解。

@Retention(RetentionPolicy.RUNTIME)：运行时生效。

@Target(ElementType.FIELD)：可用在字段上。

@JacksonAnnotationsInside：此注解可以点进去看一下是一个元注解，主要是用户打包其他注解一起使用。

@JsonSerialize：上面说到过，**该注解的作用就是可自定义序列化，可以用在注解上，方法上，字段上，类上，运行时生效等等，根据提供的序列化类里面的重写方法实现自定义序列化。**

```less
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD) @JacksonAnnotationsInside @JsonSerialize(using = DesensitizationSerialize.class) public @interface Desensitization { /** * 脱敏数据类型，只要在CUSTOMER的时候，startInclude和endExclude生效 */ DesensitizationTypeEnum type() default DesensitizationTypeEnum.CUSTOMER; /** * 开始位置（包含） */ int startInclude() default 0; /** * 结束位置（不包含） */ int endExclude() default 0; }
```

可以看到此注解有三个值，一个是枚举类定义了我们的脱敏数据类型。一个开始位置，一个结束位置。

枚举类待会给大家讲解，如果选择了自定义类型，下面的开始位置，结束位置才生效。

开始结束位置是我们hutool工具提供的自定义脱敏实现需要的参数。__可以看此方法，需要提出一点的是此方法硬编码了掩码值_。如果我们的场景需要其他掩码值的话实现也很简单，把hutool的源码拷出来，代替他的硬编码_，就可以实现，这里就不给大家多废话了，感兴趣的可以评论，私聊我噢，我教你们怎么实现。\*\*

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/119fdb6c592448ef9e97c4c0d33ae700~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

#### 2️⃣创建一个枚举类

此枚举类是我们数据脱敏的类型，包括了大部分场景。以及可以满足我们日常开发咯。

```swift
public enum DesensitizationTypeEnum { //自定义 CUSTOMER, //用户id USER_ID, //中文名 CHINESE_NAME, //身份证号 ID_CARD, //座机号 FIXED_PHONE, //手机号 MOBILE_PHONE, //地址 ADDRESS, //电子邮件 EMAIL, //密码 PASSWORD, //中国大陆车牌，包含普通车辆、新能源车辆 CAR_LICENSE, //银行卡 BANK_CARD }
```

#### 3️⃣创建我们的自定义序列化类

此类是我们数据脱敏的关键。主要是继承了我们的JsonSerializer，实现了我的ContextualSerializer。重写了它俩的方法。

@NoArgsConstructor：Lombok无参构造生成。

@AllArgsConstructor：Lombok有参生成。

ContextualSerializer：**这个类是序列化上下文类，主要是解决我们这个地方获取字段的一些信息，可以看一下源码，他的实现类有很多，Jackson提供的@JsonFormat注解也是实现此类，获取字段的一些信息进行序列化的。有兴趣的朋友可以看一下，多看源码，才能学到Jackson的实现方法，才能有今天我们的实现。**

两个重写的方法解读：

serialize：重写，实现我们的序列化自定义。

createContextual：序列化上下文方法重写，获取我们的字段一些信息进行判断，然后返回实例。具体代码可以看下面代码，都有注释噢。

```scss
@NoArgsConstructor @AllArgsConstructor public class DesensitizationSerialize extends JsonSerializer<String> implements ContextualSerializer { private DesensitizationTypeEnum type; private Integer startInclude; private Integer endExclude; @Override public void serialize(String str, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException { switch (type) { // 自定义类型脱敏 case CUSTOMER: jsonGenerator.writeString(CharSequenceUtil.hide(str,startInclude,endExclude)); break; // userId脱敏 case USER_ID: jsonGenerator.writeString(String.valueOf(DesensitizedUtil.userId())); break; // 中文姓名脱敏 case CHINESE_NAME: jsonGenerator.writeString(DesensitizedUtil.chineseName(String.valueOf(str))); break; // 身份证脱敏 case ID_CARD: jsonGenerator.writeString(DesensitizedUtil.idCardNum(String.valueOf(str), 1, 2)); break; // 固定电话脱敏 case FIXED_PHONE: jsonGenerator.writeString(DesensitizedUtil.fixedPhone(String.valueOf(str))); break; // 手机号脱敏 case MOBILE_PHONE: jsonGenerator.writeString(DesensitizedUtil.mobilePhone(String.valueOf(str))); break; // 地址脱敏 case ADDRESS: jsonGenerator.writeString(DesensitizedUtil.address(String.valueOf(str), 8)); break; // 邮箱脱敏 case EMAIL: jsonGenerator.writeString(DesensitizedUtil.email(String.valueOf(str))); break; // 密码脱敏 case PASSWORD: jsonGenerator.writeString(DesensitizedUtil.password(String.valueOf(str))); break; // 中国车牌脱敏 case CAR_LICENSE: jsonGenerator.writeString(DesensitizedUtil.carLicense(String.valueOf(str))); break; // 银行卡脱敏 case BANK_CARD: jsonGenerator.writeString(DesensitizedUtil.bankCard(String.valueOf(str))); break; default: } } @Override public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException { if (beanProperty != null) { // 判断数据类型是否为String类型 if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) { // 获取定义的注解 Desensitization desensitization = beanProperty.getAnnotation(Desensitization.class); // 为null if (desensitization == null) { desensitization = beanProperty.getContextAnnotation(Desensitization.class); } // 不为null if (desensitization != null) { // 创建定义的序列化类的实例并且返回，入参为注解定义的type,开始位置，结束位置。 return new DesensitizationSerialize(desensitization.type(), desensitization.startInclude(), desensitization.endExclude()); } } return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty); } return serializerProvider.findNullValueSerializer(null); } }
```

#### 4️⃣测试

创建一个测试注解的DTO，此测试如下。

```typescript
@Data public class TestAnnotationDTO implements Serializable { /** * 自定义 */ @Desensitization(type = DesensitizationTypeEnum.CUSTOMER,startInclude = 5,endExclude = 10) private String custom; /** * 手机号 */ @Desensitization(type = DesensitizationTypeEnum.MOBILE_PHONE) private String phone; /** * 邮箱 */ @Desensitization(type = DesensitizationTypeEnum.EMAIL) private String email; /** * 身份证 */ @Desensitization(type = DesensitizationTypeEnum.ID_CARD) private String idCard; }
```

新增测试接口

```java
@GetMapping("/test-annotation") public TestAnnotationDTO testAnnotation(){ TestAnnotationDTO testAnnotationDTO = new TestAnnotationDTO(); testAnnotationDTO.setPhone("17677772345"); testAnnotationDTO.setCustom("111111111111111111"); testAnnotationDTO.setEmail("1433926101@qq.com"); testAnnotationDTO.setIdCard("4444199810015555"); return testAnnotationDTO; }
```

**测试一下看看效果。如下图所示，完美！！！！**

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/7478b09630bb48139b23845a8153a0f6~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

### 项目pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?> <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd"> <modelVersion>4.0.0</modelVersion> <parent> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-parent</artifactId> <version>2.7.10</version> <relativePath/> <!-- lookup parent from repository --> </parent> <groupId>com.jiaqing</groupId> <artifactId>tool-desensitization</artifactId> <version>0.0.1-SNAPSHOT</version> <name>tool-desensitization</name> <description>数据脱敏</description> <properties> <java.version>1.8</java.version> <hutool.version>5.8.5</hutool.version> </properties> <dependencies> <dependency> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter</artifactId> </dependency> <dependency> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-web</artifactId> </dependency> <dependency> <groupId>cn.hutool</groupId> <artifactId>hutool-core</artifactId> <version>${hutool.version}</version> </dependency> <dependency> <groupId>org.projectlombok</groupId> <artifactId>lombok</artifactId> <optional>true</optional> </dependency> <dependency> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-test</artifactId> <scope>test</scope> </dependency> <!--json模块--> <dependency> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-starter-json</artifactId> </dependency> </dependencies> <build> <plugins> <plugin> <groupId>org.springframework.boot</groupId> <artifactId>spring-boot-maven-plugin</artifactId> <configuration> <excludes> <exclude> <groupId>org.projectlombok</groupId> <artifactId>lombok</artifactId> </exclude> </excludes> </configuration> </plugin> </plugins> </build> </project>
```

### 后记

今天给大家带来的是如何实现一个注解进行数据脱敏。

你可以学习到如何使用hutool工具类进行数据脱敏。

你可以学到如何使用@JsonSerialize注解实现自定义序列化。

你可以学到如何使用hutool工具+Jackson实现自己的脱敏注解。

源码：[github.com/hujiaqing78…](https://link.juejin.cn/?target=https%3A%2F%2Fgithub.com%2Fhujiaqing789%2Fspring-boot-study.git "https://github.com/hujiaqing789/spring-boot-study.git")

**如果你有问题或者建议欢迎大家评论区讨论。**

**如对您有用，希望你可以点赞，收藏，评论，您的支持是我最大动力。**

**我们下期再见。**

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/e5960e8830bf4dbd81c0a2c50e4dce72~tplv-k3u1fbpfcp-zoom-in-crop-mark:1512:0:0:0.awebp)

******