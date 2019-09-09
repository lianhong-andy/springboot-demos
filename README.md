# Spring Boot Demo


### 分支
- master 分支：基于SpringBoot版本 2.1.0.RELEASE，每个 module 的 parent 依赖根目录下的pom.xml，主要用于管理每个module的依赖版本，方便大家学习

### 开发环境
- **JDK1.8 +**
- **Maven 3.5 +**
- **IntelliJ IDEA ULTIMATE 2018.2 +** (注意：务必使用 IDEA 开发，同时保证安装 `lombok` 插件)
- **mysql 5.7 +** (*尽量5.7版本以上，因为5.7版本加了一些新特性，不向下兼容。本demo里会尽量避免这种不兼容的地方，但还是建议尽量保证5.7版本以上*)


### 运行方式

1. `https://github.com/lianhong-andy/springboot-demos`
2. 使用 IDEA 打开 clone 下来的项目
3. 在 IDEA 中打开项目
4. 在 IDEA 中 Maven Projects 的面板导入根目录下 的 `pom.xml`
5. Maven Projects 找不到的童鞋，可以勾上 View -> Tool Buttons ，然后Maven Projects的面板就会出现在IDEA的右侧
6. 找到各个 module 的 Application 类就可以运行各个 module 了
7. PS：运行各个 module 之前，请先注意有些模块是需要先初始化数据库数据的。