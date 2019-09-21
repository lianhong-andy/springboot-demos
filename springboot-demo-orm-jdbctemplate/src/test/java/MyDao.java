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
