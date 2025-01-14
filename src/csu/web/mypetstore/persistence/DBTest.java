

package csu.web.mypetstore.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBTest {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("数据库连接成功！");

                // 尝试执行一个简单的查询来测试连接
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT VERSION()");
                if (rs.next()) {
                    System.out.println("MySQL版本: " + rs.getString(1));
                }
                DBUtil.closeResultSet(rs);
                DBUtil.closeStatement(stmt);
            } else {
                System.out.println("数据库连接失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据库连接失败，原因是：" + e.getMessage());
        } finally {
            // 确保在 finally 块中关闭连接，以避免资源泄露
            DBUtil.closeConnection(conn);
        }
    }
}