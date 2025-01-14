package csu.web.mypetstore.persistence;
import java.sql.*;
public class DBUtil {
    private static final String DRIVER =  "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://rm-cn-jfj41vu5u0001bjo.rwlb.rds.aliyuncs.com:3306/mypetstore";
    private static final String USERNAME = "web";
    private static final String PASSWORD = "Web*1111";
    public static Connection getConnection() {
        Connection connection = null;
        try{
            Class.forName(DRIVER);
            connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);
        }catch(Exception e){
            e.printStackTrace();
        }
        return connection;
    }
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try{
                connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try{
                statement.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public static void closePreparedStatement(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try{
                preparedStatement.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try{
                resultSet.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs){
        try {
            if(conn != null)
                conn.close();
            if(ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
