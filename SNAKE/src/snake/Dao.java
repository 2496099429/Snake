package snake;

import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 数据库操作
 */

public class Dao {
    private static Connection conn = null;
    private static PreparedStatement pstmt = null;
    private static ResultSet rs = null;

    public Dao() {
    }

    /**
     * 插入排行榜数据
     * @param username 用户名
     * @param length 最终长度
     * @param score 最终分数
     */
    public static void Insert(String username,int length,int score){

        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into rankt(username,length0,score) values (?,"+length+","+score+")";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


    /**
     * 向数据库中添加用户信息
     * @param username 用户名
     * @param password 密码
     * @return 添加结果
     */

    public static boolean InsertOperator(String username,String password){//添加数据

        boolean status = false;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into users(username,password) values(?,?) ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            int n = pstmt.executeUpdate();
            if(n!=0)
                status = true;
            conn.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        JDBCUtils.closeAll(conn,pstmt);
        return status;
    }

    /**
     *在数据库读取用户信息到ArrayLst中
     * @return 用户信息
     */

    public static  ArrayList<Users> SelectUser(){

        ArrayList<Users> list = new ArrayList<>();

        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "select * from users order by id asc";
            pstmt = conn.prepareStatement(sql);
            rs =pstmt.executeQuery();
            while(rs.next()){
                Users users = new Users();
                users.setId(rs.getInt("id"));
                users.setUsername(rs.getString("username"));
                users.setPassword(rs.getString("password"));
                list.add(users);
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return list;
    }


    /**
     * 验证用户账号面是否正确
     * @param username 用户名
     * @param password 密码
     * @return 查询结果
     */

    public static boolean SelectUserLogon(String username, String password){//查询玩家信息用于登录

        boolean status = false;

        try{
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "select * from users where username = ? and password = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            rs = pstmt.executeQuery();
            if (rs.next()){
                status = true;
            }
            conn.commit();
        }catch(Exception e){
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        JDBCUtils.closeAll(conn,pstmt,rs);

        return status;
    }

    /**
     * 查询用户是否存在
     * @param username 用户名
     * @return 查询结果
     */
    public static boolean SelectUserLogon(String username){//查询玩家是否存在


        boolean status = false;

        try{
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "select * from users where username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            rs = pstmt.executeQuery();
            if (rs.next()){
                status = true;
            }
            conn.commit();
        }catch(Exception e){
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        JDBCUtils.closeAll(conn,pstmt,rs);

        return status;
    }

    /**
     * 注册时连接数据库插入数据
     * @param username 用户名
     * @param password 密码
     * @return 修改结果
     */

    public static boolean UserRegister(String username, String password){//查询玩家信息用于注册

        boolean status = false;
        if (!SelectUserLogon(username)){
            if(InsertOperator(username,password))
            status = true;
        }

        return status;
    }

    /**
     * 删除用户信息,同时删除该用户的排行榜数据
     * @param username 要删除的用户名
     * @return 修改结果
     */

    public static boolean DelUser(String username,String password){//删除数据

        boolean status = false;
        if (SelectUserLogon(username,password)){


        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "delete from users where username = ?";
            DelRank(username);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            int n = pstmt.executeUpdate();
            if(n!=0)
            status = true;
            conn.commit();

        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        JDBCUtils.closeAll(conn,pstmt);
        }


        return status;
    }

    /**
     * 删除排行榜信息
     * @param username 用户名
     * @return 删除结果
     */
    public static boolean DelRank(String username){//删除数据
        Connection conn = null;
        PreparedStatement pstmt = null;

        boolean status = false;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "delete from rankt where username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            int n = pstmt.executeUpdate();
            if(n!=0)
            status = true;
            conn.commit();

        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        JDBCUtils.closeAll(conn,pstmt);


        return status;
    }


    /**
     * 删除用户信息
     * @param username 用户名
     * @return 删除结果
     */
    public static boolean DelUser(String username){//删除数据
        Connection conn = null;
        PreparedStatement pstmt = null;

        boolean status = false;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "delete from users where username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            int n = pstmt.executeUpdate();
            if(n!=0)
            status = true;
            conn.commit();

        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        JDBCUtils.closeAll(conn,pstmt);


        return status;
    }

    /**
     * 修改用户信息方法
     * @param username 用户名
     * @param password 密码
     * @return 修改结果
     */
    public static boolean UpDateUser(String username,String password){//修改密码


        boolean status = false;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "update users set password = ? where username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,password);
            pstmt.setString(2,username);
            int n = pstmt.executeUpdate();
            if(n!=0)
            status = true;
            conn.commit();
        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        }
        JDBCUtils.closeAll(conn,pstmt);


        return status;
    }


}
