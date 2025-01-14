package csu.web.mypetstore.persistence.impl;

import csu.web.mypetstore.domain.UserActivity;
import csu.web.mypetstore.persistence.UserActivityDAO;
import csu.web.mypetstore.persistence.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static csu.web.mypetstore.persistence.DBUtil.getConnection;

public class UserActivityDaoImpl implements UserActivityDAO {
    @Override
    public void logUserActivity(int userId, String action, String productId) {
        String sql = "INSERT INTO user_activity_log (user_id, action, product_id) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.setString(2, action);
            statement.setObject(3, productId, Types.INTEGER); // productId 可能为空
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUserActivity(UserActivity userActivity) {
        String sql = "INSERT INTO user_activity_log (user_id, action, item_id, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userActivity.getUserId());
            pstmt.setString(2, userActivity.getAction());
            pstmt.setObject(3, userActivity.getItemId());
            pstmt.setTimestamp(4, new Timestamp(userActivity.getTimestamp().getTime()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserActivity> getUserActivitiesByUserId(String userId) {
        List<UserActivity> activities = new ArrayList<>();
        String sql = "SELECT * FROM user_activity_log WHERE user_id = ? ORDER BY timestamp DESC";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserActivity activity = new UserActivity();
                activity.setUserId(resultSet.getString("user_id"));
                activity.setAction(resultSet.getString("action"));
                activity.setItemId(resultSet.getString("item_id"));
                activity.setTimestamp(resultSet.getTimestamp("timestamp"));

                activities.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 处理异常
        }

        return activities;
    }
}
