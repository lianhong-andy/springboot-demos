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

