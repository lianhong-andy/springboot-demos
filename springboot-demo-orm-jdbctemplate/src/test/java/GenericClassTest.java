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
    private Map<String,Number> collection;

    public static void main(String[] args) {
        try {
            Class<?> clzz = GenericClassTest.class;
            Field collection = clzz.getDeclaredField("collection");
            Type genericType = collection.getGenericType();
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type firstGenericType = parameterizedType.getActualTypeArguments()[0];
            System.out.println("firstGenericType = " + firstGenericType);
            Type secondGenericType = parameterizedType.getActualTypeArguments()[1];
            System.out.println("secondGenericType = " + secondGenericType);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGenericClean() {
        /**
         * 泛型的主要好处就是让编译器保留参数的类型信息，执行类型检查，执行类型转换（casting）操作，
         * 编译器保证了这些类型转换（casting）的绝对无误。
         */
        //演示泛型擦除
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


    public void method(List<String> list) {

    }

    /**
     * 编译出错,这两个方法不属于重载,由于类型的擦除,使得这两个方法的参数列表的参数均为List类型,
     * 这就相当于同一个方法被声明了两次,编译自然无法通过了
     * @param list
     */
    /*public void method(List<Integer> list) {

    }*/

    @Test
    public void test3(){
/*

        List<?> list = null;
        List<Object> list1 =null;
        List<Object> list11 =null;
        List<String> list2 = null;

        list1 = list2; // 编译出错
        list2 = list1; // 编译出错

        list1 = list11; // 编译通过
        list = list1;  // 编译通过
        list = list2;  // 编译通过
*/

    }

}
