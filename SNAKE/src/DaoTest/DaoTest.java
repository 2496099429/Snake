package DaoTest;


import org.junit.Assert;
import org.junit.Test;
import snake.Dao;

/**
 * 测试Dao中的增删查改
 */

public class DaoTest {

    /**
     * 测试添加功能
     */

    @Test
    public void Insert() {
        String username = "张三";
        String password = "8848";
        Assert.assertEquals(true, Dao.InsertOperator(username, password));

    }

    /**
     * 测试删除功能
     */
    @Test
    public void DeleteTest() {
        String username = "张三";
        Assert.assertEquals(true, Dao.DelUser(username));
    }

    /**
     * 测试查询功能
     */
    @Test
    public void SelectTest() {
        String username = "张三";
        Assert.assertEquals(true, Dao.SelectUserLogon(username));
    }

    /**
     * 测试修改功能
     */

    @Test
    public void Update() {
        String username = "张三";
        String password = "2143";
        Assert.assertEquals(true, Dao.UpDateUser(username, password));
    }


}
