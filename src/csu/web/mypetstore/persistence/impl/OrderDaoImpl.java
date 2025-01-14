package csu.web.mypetstore.persistence.impl;

import csu.web.mypetstore.domain.Order;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.persistence.OrderDao;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

public class OrderDaoImpl implements OrderDao {
    private static final String GET_ORDER_BY_USERNAME =
            "SELECT BILLADDR1 AS billAddress1,BILLADDR2 AS billAddress2,BILLCITY,BILLCOUNTRY,BILLSTATE,BILLTOFIRSTNAME,BILLTOLASTNAME,BILLZIP," +
                    "SHIPADDR1 AS shipAddress1,SHIPADDR2 AS shipAddress2,SHIPCITY,SHIPCOUNTRY,SHIPSTATE,SHIPTOFIRSTNAME,SHIPTOLASTNAME,SHIPZIP," +
                    "CARDTYPE,COURIER,CREDITCARD,EXPRDATE AS expiryDate,LOCALE,ORDERDATE,ORDERS.ORDERID,TOTALPRICE,USERID AS username,STATUS " +
                    "FROM ORDERS, ORDERSTATUS WHERE ORDERS.USERID = ? AND ORDERS.ORDERID = ORDERSTATUS.ORDERID";

    private static final String GET_ORDER_TEMPLATE_BY_USERNAME_AND_STATUS =
            "SELECT BILLADDR1 AS billAddress1,BILLADDR2 AS billAddress2,BILLCITY,BILLCOUNTRY,BILLSTATE,BILLTOFIRSTNAME,BILLTOLASTNAME,BILLZIP," +
                    "SHIPADDR1 AS shipAddress1,SHIPADDR2 AS shipAddress2,SHIPCITY,SHIPCOUNTRY,SHIPSTATE,SHIPTOFIRSTNAME,SHIPTOLASTNAME,SHIPZIP," +
                    "CARDTYPE,CREDITCARD,EXPRDATE AS expiryDate,STATUS " +
                    "FROM ORDERTEMPLATE WHERE USERID = ? AND STATUS = ? ";

    private static final String GET_ORDER_TEMPLATE_STATUS_BY_USERNAME =
            "SELECT STATUS FROM ORDERTEMPLATE WHERE USERID = ? ";

    private static final String GET_ORDER =
            "SELECT BILLADDR1 AS billAddress1,BILLADDR2 AS billAddress2,BILLCITY,BILLCOUNTRY,BILLSTATE,BILLTOFIRSTNAME,BILLTOLASTNAME,BILLZIP," +
                    "SHIPADDR1 AS shipAddress1,SHIPADDR2 AS shipAddress2,SHIPCITY,SHIPCOUNTRY,SHIPSTATE,SHIPTOFIRSTNAME,SHIPTOLASTNAME,SHIPZIP," +
                    "CARDTYPE,COURIER,CREDITCARD,EXPRDATE AS expiryDate,LOCALE,ORDERDATE,ORDERS.ORDERID,TOTALPRICE,USERID AS username,STATUS " +
                    "FROM ORDERS, ORDERSTATUS WHERE ORDERS.ORDERID = ? AND ORDERS.ORDERID = ORDERSTATUS.ORDERID";


    private static final String INSERT_ORDER =  "INSERT INTO ORDERS (ORDERID, USERID, ORDERDATE, SHIPADDR1, SHIPADDR2, SHIPCITY, SHIPSTATE," +
            "SHIPZIP, SHIPCOUNTRY, BILLADDR1, BILLADDR2, BILLCITY, BILLSTATE, BILLZIP, BILLCOUNTRY," +
            "COURIER, TOTALPRICE, BILLTOFIRSTNAME, BILLTOLASTNAME, SHIPTOFIRSTNAME, SHIPTOLASTNAME," +
            "CREDITCARD, EXPRDATE, CARDTYPE, LOCALE) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_ORDER_TEMPLATE =  "INSERT INTO ORDERTEMPLATE (USERID, SHIPADDR1, SHIPADDR2, SHIPCITY, SHIPSTATE," +
            "SHIPZIP, SHIPCOUNTRY, BILLADDR1, BILLADDR2, BILLCITY, BILLSTATE, BILLZIP, BILLCOUNTRY," +
            "BILLTOFIRSTNAME, BILLTOLASTNAME, SHIPTOFIRSTNAME, SHIPTOLASTNAME," +
            "CREDITCARD, EXPRDATE, CARDTYPE, STATUS) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_ORDER_STATUS =
            "INSERT INTO ORDERSTATUS (ORDERID, LINENUM, TIMESTAMP, STATUS) VALUES (?, ?, ?, ?)";

    private static final String REMOVE_ORDER_TEMPLATE =
            "DELETE FROM ORDERTEMPLATE WHERE USERID = ? AND STATUS = ? ";

    private static final String RENAME_ORDER_TEMPLATE =
            "UPDATE ORDERTEMPLATE SET STATUS = ? WHERE USERID = ? AND STATUS = ?";

    private static final String IS_STATUS_DUPLICATE =
            "SELECT COUNT(*) FROM ORDERTEMPLATE WHERE USERID = ? AND STATUS = ?";

    @Override
    public Order getOrderTemplateByUsernameAndStatus(String username, String status) {
        Order order = new Order();
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_TEMPLATE_BY_USERNAME_AND_STATUS);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,status);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                order.setBillAddress1(resultSet.getString("billAddress1"));
                order.setBillAddress2(resultSet.getString("billAddress2"));
                order.setBillCity(resultSet.getString("billCity"));
                order.setBillCountry(resultSet.getString("billCountry"));
                order.setBillState(resultSet.getString("billState"));
                order.setBillToFirstName(resultSet.getString("billToFirstName"));
                order.setBillToLastName(resultSet.getString("billToLastName"));
                order.setBillZip(resultSet.getString("billZip"));
                order.setShipAddress1(resultSet.getString("shipAddress1"));
                order.setShipAddress2(resultSet.getString("shipAddress2"));
                order.setShipCity(resultSet.getString("shipCity"));
                order.setShipCountry(resultSet.getString("shipCountry"));
                order.setShipState(resultSet.getString("shipState"));
                order.setShipToFirstName(resultSet.getString("shipToFirstName"));
                order.setShipToLastName(resultSet.getString("shipToLastName"));
                order.setShipZip(resultSet.getString("shipZip"));
                order.setCardType(resultSet.getString("cardType"));
                order.setCreditCard(resultSet.getString("creditCard"));
                order.setExpiryDate(resultSet.getString("expiryDate"));
                order.setStatus(resultSet.getString("status"));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public List<Order> getOrdersByUsername(String username) {
        List<Order> orderList = new ArrayList<>();
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_BY_USERNAME);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = new Order();
                order.setBillAddress1(resultSet.getString("billAddress1"));
                order.setBillAddress2(resultSet.getString("billAddress2"));
                order.setBillCity(resultSet.getString("billCity"));
                order.setBillCountry(resultSet.getString("billCountry"));
                order.setBillState(resultSet.getString("billState"));
                order.setBillToFirstName(resultSet.getString("billToFirstName"));
                order.setBillToLastName(resultSet.getString("billToLastName"));
                order.setBillZip(resultSet.getString("billZip"));
                order.setShipAddress1(resultSet.getString("shipAddress1"));
                order.setShipAddress2(resultSet.getString("shipAddress2"));
                order.setShipCity(resultSet.getString("shipCity"));
                order.setShipCountry(resultSet.getString("shipCountry"));
                order.setShipState(resultSet.getString("shipState"));
                order.setShipToFirstName(resultSet.getString("shipToFirstName"));
                order.setShipToLastName(resultSet.getString("shipToLastName"));
                order.setShipZip(resultSet.getString("shipZip"));
                order.setCardType(resultSet.getString("cardType"));
                order.setCourier(resultSet.getString("courier"));
                order.setCreditCard(resultSet.getString("creditCard"));
                order.setExpiryDate(resultSet.getString("expiryDate"));
                order.setLocale(resultSet.getString("locale"));
                order.setOrderDate(resultSet.getDate("orderDate"));
                order.setOrderId(resultSet.getInt("orderId"));
                order.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                order.setUsername(resultSet.getString("username"));
                order.setStatus(resultSet.getString("status"));

                orderList.add(order);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public List<Order> getOrderTemplateStatusByUsername(String username) {
        List<Order> orderList = new ArrayList<>();
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_TEMPLATE_STATUS_BY_USERNAME);
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Order order = new Order();
                order.setStatus(resultSet.getString("status"));

                orderList.add(order);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderList;
    }

    @Override
    public Order getOrder(int orderId) {
        Order order = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                order = new Order();
                order.setBillAddress1(resultSet.getString("billAddress1"));
                order.setBillAddress2(resultSet.getString("billAddress2"));
                order.setBillCity(resultSet.getString("billCity"));
                order.setBillCountry(resultSet.getString("billCountry"));
                order.setBillState(resultSet.getString("billState"));
                order.setBillToFirstName(resultSet.getString("billToFirstName"));
                order.setBillToLastName(resultSet.getString("billToLastName"));
                order.setBillZip(resultSet.getString("billZip"));
                order.setShipAddress1(resultSet.getString("shipAddress1"));
                order.setShipAddress2(resultSet.getString("shipAddress2"));
                order.setShipCity(resultSet.getString("shipCity"));
                order.setShipCountry(resultSet.getString("shipCountry"));
                order.setShipState(resultSet.getString("shipState"));
                order.setShipToFirstName(resultSet.getString("shipToFirstName"));
                order.setShipToLastName(resultSet.getString("shipToLastName"));
                order.setShipZip(resultSet.getString("shipZip"));
                order.setCardType(resultSet.getString("cardType"));
                order.setCourier(resultSet.getString("courier"));
                order.setCreditCard(resultSet.getString("creditCard"));
                order.setExpiryDate(resultSet.getString("expiryDate"));
                order.setLocale(resultSet.getString("locale"));
                order.setOrderDate(resultSet.getDate("orderDate"));
                order.setOrderId(resultSet.getInt("orderId"));
                order.setTotalPrice(resultSet.getBigDecimal("totalPrice"));
                order.setUsername(resultSet.getString("username"));
                order.setStatus(resultSet.getString("status"));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    @Override
    public void insertOrder(Order order) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER);
            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setString(2, order.getUsername());
            preparedStatement.setDate(3, new java.sql.Date(order.getOrderDate().getTime()));
            preparedStatement.setString(4, order.getShipAddress1());
            preparedStatement.setString(5, order.getShipAddress2());
            preparedStatement.setString(6, order.getShipCity());
            preparedStatement.setString(7, order.getShipState());
            preparedStatement.setString(8, order.getShipZip());
            preparedStatement.setString(9, order.getShipCountry());
            preparedStatement.setString(10, order.getBillAddress1());
            preparedStatement.setString(11, order.getBillAddress2());
            preparedStatement.setString(12, order.getBillCity());
            preparedStatement.setString(13, order.getBillState());
            preparedStatement.setString(14, order.getBillZip());
            preparedStatement.setString(15, order.getBillCountry());
            preparedStatement.setString(16, order.getCourier());
            preparedStatement.setBigDecimal(17, order.getTotalPrice());
            preparedStatement.setString(18, order.getBillToFirstName());
            preparedStatement.setString(19, order.getBillToLastName());
            preparedStatement.setString(20, order.getShipToFirstName());
            preparedStatement.setString(21, order.getShipToLastName());
            preparedStatement.setString(22, order.getCreditCard());
            preparedStatement.setString(23, order.getExpiryDate());
            preparedStatement.setString(24, order.getCardType());
            preparedStatement.setString(25, order.getLocale());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertOrderTemplate(Order order) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_TEMPLATE);
            preparedStatement.setString(1, order.getUsername());
            preparedStatement.setString(2, order.getShipAddress1());
            preparedStatement.setString(3, order.getShipAddress2());
            preparedStatement.setString(4, order.getShipCity());
            preparedStatement.setString(5, order.getShipState());
            preparedStatement.setString(6, order.getShipZip());
            preparedStatement.setString(7, order.getShipCountry());
            preparedStatement.setString(8, order.getBillAddress1());
            preparedStatement.setString(9, order.getBillAddress2());
            preparedStatement.setString(10, order.getBillCity());
            preparedStatement.setString(11, order.getBillState());
            preparedStatement.setString(12, order.getBillZip());
            preparedStatement.setString(13, order.getBillCountry());
            preparedStatement.setString(14, order.getBillToFirstName());
            preparedStatement.setString(15, order.getBillToLastName());
            preparedStatement.setString(16, order.getShipToFirstName());
            preparedStatement.setString(17, order.getShipToLastName());
            preparedStatement.setString(18, order.getCreditCard());
            preparedStatement.setString(19, order.getExpiryDate());
            preparedStatement.setString(20, order.getCardType());
            preparedStatement.setString(21, order.getStatus());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertOrderStatus(Order order) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ORDER_STATUS);
            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setInt(2, order.getLineItems().size());
            preparedStatement.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.setString(4, order.getStatus());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeOrderTemplate(String username, String status) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_ORDER_TEMPLATE);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, status);
            preparedStatement.executeUpdate();
            DBUtil.closeStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renameOrderTemplate(String username, String status, String newStatus) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(RENAME_ORDER_TEMPLATE);
            preparedStatement.setString(1, newStatus);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, status);
            preparedStatement.executeUpdate();
            DBUtil.closeStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStatusDuplicate(String username, String status) {
        boolean isDuplicate = false;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(IS_STATUS_DUPLICATE);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, status);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isDuplicate = resultSet.getInt(1) > 0;
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closeStatement(preparedStatement);
            DBUtil.closeConnection(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDuplicate;
    }
}
