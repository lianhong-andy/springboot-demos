# 配置文件demo
### 工具类Hutool的wiki:[http://hutool.mydoc.io/]

### 如何在不同的环境指定不同的配置文件？
#### 配置文件可在yml中指定环境变量，从而决定读取哪个配置文件
```yaml
server:
  port: 8081
  servlet:
    context-path: /demo
```
## maven依赖
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-demo</artifactId>
        <groupId>com.andy</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>springboot-demo-properties</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>springboot-demo-properties</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--
        在 META-INF/additional-spring-configuration-metadata.json 中配置
        可以去除 application.yml 中自定义配置的红线警告，并且为自定义配置添加 hint 提醒
         -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>

        <!-- 热部署 -->
        <dependency>
            <groupId> org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <finalName>spring-boot-demo-properties</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>


</project>
```
## additional-spring-configuration-metadata.json

> 位置： src/main/resources/META-INF/additional-spring-configuration-metadata.json

```json
{
	"properties": [
		{
			"name": "application.name",
			"description": "Default value is artifactId in pom.xml.",
			"type": "java.lang.String"
		},
		{
			"name": "application.version",
			"description": "Default value is version in pom.xml.",
			"type": "java.lang.String"
		},
		{
			"name": "developer.name",
			"description": "The Developer Name.",
			"type": "java.lang.String"
		},
		{
			"name": "developer.website",
			"description": "The Developer Website.",
			"type": "java.lang.String"
		},
		{
			"name": "developer.qq",
			"description": "The Developer QQ Number.",
			"type": "java.lang.String"
		},
		{
			"name": "developer.phone-number",
			"description": "The Developer Phone Number.",
			"type": "java.lang.String"
		}
	]
}
```