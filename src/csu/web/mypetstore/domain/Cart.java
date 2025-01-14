package csu.web.mypetstore.domain;

import csu.web.mypetstore.persistence.DBUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

public class Cart implements Serializable {
    private static final long serialVersionUID = 8329559983943337176L;
    private final Map<String, CartItem> itemMap = Collections.synchronizedMap(new HashMap<String, CartItem>());
    private final List<CartItem> itemList = new ArrayList<CartItem>();
    private static final String AddItemToCart = "insert into cart (userId,itemId,quantity) values (?,?,?)";

    public Iterator<CartItem> getCartItems() {
        return itemList.iterator();
    }

    public List<CartItem> getCartItemList() {
        return itemList;
    }

    public int getNumberOfItems() {
        return itemList.size();
    }

    public Iterator<CartItem> getAllCartItems() {
        return itemList.iterator();
    }

    public boolean containsItemId(String itemId) {
        return itemMap.containsKey(itemId);
    }

    public void addItem(Item item, boolean isInStock) {
        CartItem cartItem = (CartItem) itemMap.get(item.getItemId());
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setItem(item);
            cartItem.setQuantity(0);
            cartItem.setInStock(isInStock);
            itemMap.put(item.getItemId(), cartItem);
            itemList.add(cartItem);
        }
        cartItem.incrementQuantity();
    }
    public void addItemToCart(String userId, Item cartItem) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(AddItemToCart);
            preparedStatement.setString(1,userId);
            preparedStatement.setString(2,cartItem.getItemId());
            preparedStatement.setInt(3,cartItem.getQuantity()+1);
            preparedStatement.executeUpdate();
            DBUtil.closeAll(connection,preparedStatement,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public Item removeItemById(String itemId) {
        CartItem cartItem = (CartItem) itemMap.remove(itemId);
        if (cartItem == null) {
            return null;
        } else {
            itemList.remove(cartItem);
            return cartItem.getItem();
        }
    }
    public void removeItemFromDatabase(String userId, String itemId) {
        try {
            // 获取数据库连接
            Connection connection = DBUtil.getConnection();

            // SQL 查询：删除购物车中指定商品
            String deleteQuery = "DELETE FROM cart WHERE userId = ? AND itemId = ?";

            // 准备删除语句
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, userId);  // 设置 userId
            preparedStatement.setString(2, itemId);  // 设置 itemId

            // 执行删除操作
            preparedStatement.executeUpdate();

            // 关闭连接
            DBUtil.closeAll(connection, preparedStatement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementQuantityByItemId(String itemId) {
        CartItem cartItem = (CartItem) itemMap.get(itemId);
        cartItem.incrementQuantity();
    }
    public void updateItemQuantityInDatabase(String userId, String workingItemId) {
        try {
            // 获取数据库连接
            Connection connection = DBUtil.getConnection();

            // SQL 查询：更新商品数量
            String updateQuery = "UPDATE cart SET quantity = quantity + 1 WHERE userId = ? AND itemId = ?";

            // 准备更新语句
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, userId);  // 设置 userId
            preparedStatement.setString(2, workingItemId);  // 设置 itemId

            // 执行更新操作
            preparedStatement.executeUpdate();

            // 关闭连接
            DBUtil.closeAll(connection, preparedStatement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setQuantityByItemId(String itemId, int quantity) {
        CartItem cartItem = (CartItem) itemMap.get(itemId);
        cartItem.setQuantity(quantity);
    }
    public void updateItemQuantityInDatabase(String userId, String itemId, int newQuantity) {
        try {
            // 获取数据库连接
            Connection connection = DBUtil.getConnection();

            // SQL 更新语句：更新购物车中商品的数量
            String updateQuery = "UPDATE cart SET quantity = ? WHERE userId = ? AND itemId = ?";

            // 准备更新语句
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, newQuantity);  // 设置新的数量
            preparedStatement.setString(2, userId);  // 设置 userId
            preparedStatement.setString(3, itemId);  // 设置 itemId

            // 执行更新操作
            preparedStatement.executeUpdate();

            // 关闭连接
            DBUtil.closeAll(connection, preparedStatement, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public BigDecimal getSubTotal() {
        BigDecimal subTotal = new BigDecimal("0");
        Iterator<CartItem> items = getAllCartItems();
        while (items.hasNext()) {
            CartItem cartItem = (CartItem) items.next();
            Item item = cartItem.getItem();
            BigDecimal listPrice = item.getListPrice();
            BigDecimal quantity = new BigDecimal(String.valueOf(cartItem.getQuantity()));
            subTotal = subTotal.add(listPrice.multiply(quantity));
        }
        return subTotal;
    }
}
