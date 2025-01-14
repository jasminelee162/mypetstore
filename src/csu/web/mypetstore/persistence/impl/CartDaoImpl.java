package csu.web.mypetstore.persistence.impl;


import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.domain.Cart;
import csu.web.mypetstore.domain.CartItem;
import csu.web.mypetstore.persistence.CartDao;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.service.CatalogService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CartDaoImpl implements CartDao {

    private static final String GetCart = "select itemId,quantity from cart where userId = ?";
    private static final String AddItemToCart = "insert into cart (userId,itemId,quantity) values (?,?,?)";
    private static final String DeleteCart = "delete from cart where userId = ?";
    private static final String UpdateCart = "update cart set quantity = ? where userId = ? and itemId = ?";
    private static final String RemoveItemFromCart = "delete from cart where userId = ? and itemId = ?";
    @Override
    public Cart getCart(String username) {
        Cart cart = null;
        CatalogService catalogService = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GetCart);
            preparedStatement.setString(1,username);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs != null)
            {
                cart = new Cart();
                catalogService = new CatalogService();
            }
            while (rs.next())
            {
                cart.addItem(catalogService.getItem(rs.getString(1)),catalogService.isItemInStock(rs.getString(1)));
                cart.setQuantityByItemId(rs.getString(1),rs.getInt(2));
            }
            DBUtil.closeAll(connection,preparedStatement,rs);
        }catch (Exception e){
            e.printStackTrace();
        }
        return cart;
    }

    @Override
    public void updateCart(Cart cart, Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UpdateCart);
            for (int i = 0; i < cart.getNumberOfItems(); i++)
            {
                preparedStatement.setInt(1,cart.getCartItemList().get(i).getQuantity());
                preparedStatement.setString(2,account.getUsername());
                preparedStatement.setString(3,cart.getCartItemList().get(i).getItem().getItemId());
                preparedStatement.executeUpdate();
            }
            DBUtil.closeAll(connection,preparedStatement,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addItemToCart(Account account, CartItem cartItem) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(AddItemToCart);
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,cartItem.getItem().getItemId());
            preparedStatement.setInt(3,cartItem.getQuantity());
            preparedStatement.executeUpdate();
            DBUtil.closeAll(connection,preparedStatement,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeCart(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DeleteCart);
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.executeUpdate();
            DBUtil.closeAll(connection,preparedStatement,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeItemFromCart(Account account,String itemId) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(RemoveItemFromCart);
            preparedStatement.setString(1,account.getUsername());
            preparedStatement.setString(2,itemId);
            preparedStatement.executeUpdate();
            DBUtil.closeAll(connection,preparedStatement,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}