## ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]的含义
```markdown
getClass().getGenericSuperclass()返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
然后将其转换ParameterizedType。。
getActualTypeArguments()返回表示此类型实际类型参数的 Type 对象的数组。
[0]就是这个数组中第一个了。。
```
## 简而言之就是获得超类的泛型参数的实际类型。。

## 比如
### 超类
```java
public class GenericDAO<T> {
    private Class<T> entityClass;
    
    protected GenericDAO() {
    Type type = getClass().getGenericSuperclass();
    Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
    this.entityClass = (Class<T>) trueType;
    }
}
```

### 子类
```java
public class OptionManager extends GenericDAO<MSGC_OPTION> {
}
```
### 测试类
```java
public class OracleTest {
    public static void main(String[] args) throws Exception {
        OptionManager manager = new OptionManager();
    }
}
```

```markdown
这样在你new OptionManager();以后
超类里的entityClass就是子类那里的public class OptionManager extends GenericDAO<MSGC_OPTION> 里面的MSGC_OPTION所对应的class对象了..
```

## 参考链接：
https://blog.csdn.net/wangcong2005/article/details/17144665
```java
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lianhong
 * @description
 * @date 2019/9/21 0021下午 3:39
 */
public class GenericClassTest {
    @Test
    public void testGenericClean() {
        /**
         * 泛型的主要好处就是让编译器保留参数的类型信息，执行类型检查，执行类型转换（casting）操作，
         * 编译器保证了这些类型转换（casting）的绝对无误。
         */
        /**演示泛型擦除*/
        /******* 使用泛型类型 *******/
        List<String> list2 = new ArrayList<String>();
        list2.add("value");                 //[类型安全的写入数据] 编译器检查该值,该值必须是String类型才能通过编译
        String str2 = list2.get(0); //[类型安全的读取数据] 不需要手动转换
        System.out.println("str2 = " + str2);

        /******* 不使用泛型类型 *******/
        List list = new ArrayList<>();
        list.add("andy"); //编译器不检查值
        Integer e = (Integer) list.get(0);//需手动强制转换,如转换类型与原数据类型不一致将抛出ClassCastException异常
    }
}

```

```markdown
java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Integer

	at GenericClassTest.testGenericClean(GenericClassTest.java:43)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
```

## 泛型的类型擦除：
### Java 中的泛型只存在于编译期，在将 Java 源文件编译完成 Java 字节代码中是不包含泛型中的类型信息的。
### 使用泛型的时候加上的类型参数，会被编译器在编译的时候去掉。
### 这个过程就称为类型擦除（type erasure）。

```java
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lianhong
 * @description
 * @date 2019/9/21 0021下午 3:39
 */
public class GenericClassTest {
    @Test
    public void testGenericErasure() {
        //演示泛型擦除
        /******* 使用泛型类型 *******/
        List<Integer> list1 = new ArrayList<>();
        System.out.println("list1.getClass() = " + list1.getClass());
        List<String> list2 = new ArrayList<>();
        System.out.println("list2.getClass() = " + list2.getClass());

        System.out.println(list1.getClass() == list2.getClass());
    }
}

```

## 输出结果
```markdown
list1.getClass() = class java.util.ArrayList
list2.getClass() = class java.util.ArrayList
true

```
#
> 在以上代码中定义的 List<String> 和 List<Integer> 等类型，
> 在编译之后都会变成 List，而由泛型附加的类型信息对 JVM 来说是不可见的，所以第一条打印语句输出 true，
> 第二、第三条打印语句都输出 java.util.ArrayList，这都说明 List<String> 和 List<Integer> 的对象使用的都是同一份字节码，运行期间并不存在泛型。

```java
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lianhong
 * @description
 * @date 2019/9/21 0021下午 3:39
 */
public class GenericClassTest {
    public void method(List<String> list) {

    }

    /**
     * 编译出错,这两个方法不属于重载,由于类型的擦除,使得这两个方法的参数列表的参数均为List类型,
     * 这就相当于同一个方法被声明了两次,编译自然无法通过了
     * @param list
     */
    public void method(List<Integer> list) {

    }
}

```

#### 以此类为例，在 cmd 中 编译 GenericsApp.java 得到字节码（泛型已经擦除），然后再反编译这份字节码来看下源码中泛型是不是真的被擦除了：

>从图中可以看出，经反编译后的源码中 method 方法的参数变成了 List 类型，说明泛型的类型是真的被擦除了，字节码文件中不存在泛型，也就是说，运行期间泛型并不存在，它在
 
 编译完成之后就已经被擦除了。
 
 
 泛型类型的子类型：
 
     泛型类型跟其是否是泛型类型的子类型没有任何关系。
 
 
         List<Object> list1;
         List<String> list2;
        
         list1 = list2; // 编译出错
         list2 = list1; // 编译出错
 
 大家都知道，在 Java 中，Object 类是所有类的超类，自然而然的 Object 类是 String 类的超类，按理，将一个 String 类型的对象赋值给一个 Object 类型的对象是可行的，
 
 但是泛型中并不存在这样的逻辑，用更通俗的话说，泛型类型跟其是否子类型没有任何关系。

 >
> 泛型中的通配符（?）：
  
      由于泛型类型与其子类型存在不相关性，那么在不能确定泛型类型的时候，可以使用通配符（?），通配符（?）能匹配任意类型。
      
      
              List<?> list;
              List<Object> list1 = null;
              List<String>  list2 = null;
             
              list = list1;
              list = list2;
 
 
  限定通配符的上界：
  
               ArrayList<? extends Number> collection = null;
               collection = new ArrayList<Number>();
               collection = new ArrayList<Short>();
               collection = new ArrayList<Integer>();
               collection = new ArrayList<Long>();
               collection = new ArrayList<Float>();
               collection = new ArrayList<Double>();
  
   ? extends XX，XX 类是用来限定通配符的上界，XX 类是能匹配的最顶层的类，
   它只能匹配 XX 类以及 XX 类的子类。在以上代码中，Number 类的实现类有：
   
AtomicInteger、AtomicLong、 BigDecimal、 BigInteger、 Byte、 Double、 Float、 Integer、 Long、 Short ，因此以上代码均无错误。

限定通配符的下界：


        ArrayList<? super Integer> collection = null;
       
        collection = new ArrayList<Object>();
        collection = new ArrayList<Number>();
        collection = new ArrayList<Integer>();
        
        
? super XX，XX 类是用来限定通配符的下界，XX 类是能匹配的最底层的类，它只能匹配 XX 类以及 XX 类的超类，在以上代码中，Integer 类的超类有：

Number、Object，因此以上代码均能通过编译无误。

>

### 通过反射获得泛型的实际类型参数：
#### 这个就有点难度了，上面已经说到，泛型的类型参数会在编译完成以后被擦除，那在运行期间还怎么来获得泛型的实际类型参数呢？
#### 这个是有点难度了吧？似乎不可能实现的样子，
#### 其实不然，java.lang.Class 类从 Java 1.5 起（如果没记错的话），提供了一个 getGenericSuperclass() 方法来获取直接超类的泛型类型，这就使得获取泛型的实际类型参数成为


## 父类
```java
package base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author lianhong
 * @description
 * @date 2019/9/21 0021下午 4:27
 */
public class BaseDao<T,P> {
    private Class<T> clzz;

    public BaseDao() {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        for (Type type : types) {
            System.out.println("type = " + type);
        }
        this.clzz = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        System.out.println("clzz = " + clzz);
    }
}


```

## 子类
```java
import base.BaseDao;
import com.andy.orm.jdbctemplate.entity.User;

/**
 * @author lianhong
 * @description
 * @date 2019/9/21 0021下午 4:32
 */
public class MyDao extends BaseDao<User,Long> {
    public MyDao() {
        super();
    }

    public static void main(String[] args) {
        new MyDao();
    }
}

```

### 输出结果
```markdown
type = class com.andy.orm.jdbctemplate.entity.User
type = class java.lang.Long
clzz = class com.andy.orm.jdbctemplate.entity.User
```

>泛型编译后会去参数化（擦拭法），因此无法直接用反射获取泛型的参数类型
可以把泛型用做一个方法的参数类型，方法可以保留参数的相关信息，这样就可以用反射先获取方法的信息
然后再进一步获取泛型参数的相关信息，这样就得到了泛型的实际参数类型
```java
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * @author lianhong
 * @description 获取父类的泛型
 * @date 2019/9/21 0021下午 4:42
 */
public class GetSuperMethodGeneric {

    public void applyCollection(Collection<Number> collection,Map<String,Object> map) {

    }

    @Test
    public void test() throws NoSuchMethodException {
        Class<?> clzz = GetSuperMethodGeneric.class;
        Method method = clzz.getDeclaredMethod("applyCollection", Collection.class,Map.class);//通过反射获取指定方法
        Type[] genericParameterTypes = method.getGenericParameterTypes();//取得泛型类型参数集
        for (Type genericParameterType : genericParameterTypes) {
            ParameterizedType pType = (ParameterizedType) genericParameterType;//将其转成参数化类型,因为在方法中泛型是参数
            Type[] actualTypeArguments = pType.getActualTypeArguments();//取得参数的实际类型
            for (Type actualTypeArgument : actualTypeArguments) {
                System.out.println("actualTypeArgument = " + actualTypeArgument);
            }
        }
    }
}

```

### 输出结果
```markdown
actualTypeArgument = class java.lang.Number
actualTypeArgument = class java.lang.String
actualTypeArgument = class java.lang.Object
```