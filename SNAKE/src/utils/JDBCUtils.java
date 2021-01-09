package utils;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC工具类
 */

public class JDBCUtils {

    private static String url;
    private static String driver;




    static{
        Properties pro = new Properties();
        try {
            URL res = JDBCUtils.class.getResource("jdbc.properties");
            String path = res.getPath();


            pro.load(new FileReader(path));
            url = pro.getProperty("url");
            driver = pro.getProperty("driver");

            Class.forName(driver);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取数据库连接
     * @return Connection对象
     * @throws SQLException 数据库异常
     */
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url);

    }

    /**
     * 关闭资源
     * @param conn 数据库连接对象
     * @param pstmt 预编译对象
     */

    public static void closeAll(Connection conn,PreparedStatement pstmt){
        if(pstmt != null){
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    /**
     * 关闭资源
     * @param conn 连接对象
     * @param pstmt 预编译对象
     * @param rs 读取的资源
     */

    public static void closeAll(Connection conn,PreparedStatement pstmt,ResultSet rs){
        if(pstmt != null){
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 关闭资源
     * @param conn
     * @param pstmt1
     * @param pstmt2
     * @param rs1
     * @param rs2
     */

    public static void closeAll(Connection conn,PreparedStatement pstmt1,PreparedStatement pstmt2,  ResultSet rs1 ,ResultSet rs2){
        if(pstmt1 != null){
            try {
                pstmt1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(pstmt2 != null){
            try {
                pstmt2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(rs1 != null){
            try {
                rs1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(rs2 != null){
            try {
                rs1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 关闭资源
     * @param conn
     * @param pstmt1
     * @param pstmt2
     * @param rs1
     */
    public static void closeAll(Connection conn,PreparedStatement pstmt1,PreparedStatement pstmt2,  ResultSet rs1 ){
        if(pstmt1 != null){
            try {
                pstmt1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(pstmt2 != null){
            try {
                pstmt2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(rs1 != null){
            try {
                rs1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 关闭资源
     * @param conn
     * @param pstmt1
     * @param pstmt2
     */

    public static void closeAll(Connection conn,PreparedStatement pstmt1,PreparedStatement pstmt2){
        if(pstmt1 != null){
            try {
                pstmt1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(pstmt2 != null){
            try {
                pstmt2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    /**
     * 关闭资源
     * @param conn
     * @param pstmt1
     * @param rs1
     * @param rs2
     */

    public static void closeAll(Connection conn,PreparedStatement pstmt1,  ResultSet rs1 ,ResultSet rs2){
        if(pstmt1 != null){
            try {
                pstmt1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(conn != null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        if(rs1 != null){
            try {
                rs1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(rs2 != null){
            try {
                rs1.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}

