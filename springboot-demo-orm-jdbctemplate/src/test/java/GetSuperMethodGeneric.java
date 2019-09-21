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
