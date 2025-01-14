package csu.web.mypetstore.persistence.impl;

import csu.web.mypetstore.domain.LineItem;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.persistence.LineItemDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LineItemDaoImpl implements LineItemDao {
    private static final String GetLineItemsByOrderId = "SELECT ORDERID,LINENUM AS lineNumber,ITEMID,QUANTITY,UNITPRICE FROM LINEITEM WHERE ORDERID = ?";
    private static final String InsertLineItem = "INSERT INTO LINEITEM (ORDERID, LINENUM, ITEMID, QUANTITY, UNITPRICE) VALUES (?, ?, ?, ?, ?)";
    @Override
    public List<LineItem> getLineItemsByOrderId(int orderId) {
        List<LineItem> lineItemList = new ArrayList<LineItem>();
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GetLineItemsByOrderId);
            preparedStatement.setInt(1,orderId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next())
            {
                LineItem lineItem = new LineItem();
                lineItem.setOrderId(rs.getInt(1));
                lineItem.setLineNumber(rs.getInt(2));
                lineItem.setItemId(rs.getString(3));
                lineItem.setQuantity(rs.getInt(4));
                lineItem.setUnitPrice(rs.getBigDecimal(5));
                lineItemList.add(lineItem);
            }
            DBUtil.closeAll(connection,preparedStatement,rs);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return lineItemList;
    }

    @Override
    public void insertLineItem(LineItem lineItem) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(InsertLineItem);
            preparedStatement.setInt(1,lineItem.getOrderId());
            preparedStatement.setInt(2,lineItem.getLineNumber());
            preparedStatement.setString(3,lineItem.getItemId());
            preparedStatement.setInt(4,lineItem.getQuantity());
            preparedStatement.setBigDecimal(5,lineItem.getUnitPrice());
            preparedStatement.executeUpdate();
            DBUtil.closeAll(connection,preparedStatement,null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}