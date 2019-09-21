# Annotation的用法
参考文章：[https://www.cnblogs.com/CoderHong/p/9503159.html]

## @Retention
|@Retention取值 | 注解保留到什么阶段
|  ----  | ----  |
|RetentionPolicy.SOURCE	 | 只在代码中保留，在字节码文件中就删除了 |
|RetentionPolicy.CLASS	 | 只在代码和字节码文件中都保留 |
|RetentionPolicy.CLASS	 | 只在代码和字节码文件中都保留 |
|RetentionPolicy.RUNTIME | 所有阶段都保留 |


## @Target
|@Target取值 | 作用目标|
|  ----  |  ----  |
|ElementType.TYPE	|  作用在类、接口等上面|
|ElementType.METHOD	|  作用在方法上面|
|ElementType.FIELD	|  作用在字段上面|
