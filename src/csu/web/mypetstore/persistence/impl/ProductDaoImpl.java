package csu.web.mypetstore.persistence.impl;

import csu.web.mypetstore.domain.Product;
import csu.web.mypetstore.persistence.DBUtil;
import csu.web.mypetstore.persistence.ProductDao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private static final String getProductListByCategoryString=
            "SELECT PRODUCTID,NAME,DESCN as description,CATEGORY as categoryId From PRODUCT WHERE CATEGORY=?";
    private static final String getProductString=
            "SELECT PRODUCTID,NAME,DESCN as description,CATEGORY as categoryId From PRODUCT WHERE PRODUCTID= ?";
    private static final String searchProductListString=
            "SELECT PRODUCTID,NAME,DESCN as description,CATEGORY as categoryId From PRODUCT WHERE lower(name) like ?";
    @Override
    public List<Product> getProductListByCategory(String categoryId) {
        List<Product> products = new ArrayList<>();
        try{
            Connection connection= DBUtil.getConnection();
            PreparedStatement pStatement=connection
                    .prepareStatement(getProductListByCategoryString);
            pStatement.setString(1,categoryId);
            ResultSet resultSet=pStatement.executeQuery();
            while(resultSet.next()){
                Product product=new Product();
                product.setProductId(resultSet.getString("PRODUCTID"));
                product.setName(resultSet.getString("Name"));
                product.setDescription(resultSet.getString("description"));
                product.setCategoryId(resultSet.getString("categoryId"));
                products.add(product);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public Product getProduct(String productId) {
    Product product=null;
    try{
            Connection connection= DBUtil.getConnection();
            PreparedStatement pStatement=connection
                    .prepareStatement(getProductString);
            pStatement.setString(1,productId);
            ResultSet resultSet=pStatement.executeQuery();
            while(resultSet.next()){
                product=new Product();
                product.setProductId(resultSet.getString(1));
                product.setName(resultSet.getString(2));
                product.setDescription(resultSet.getString(3));
                product.setCategoryId(resultSet.getString(4));
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public List<Product> searchProductList(String keywords) {
        List<Product> productList = new ArrayList<Product>();
        try{
            Connection connection= DBUtil.getConnection();
            PreparedStatement pStatement=connection
                    .prepareStatement(searchProductListString);
            pStatement.setString(1,keywords);
            ResultSet resultSet=pStatement.executeQuery();
            while(resultSet.next()){
                Product product=new Product();
                product.setProductId(resultSet.getString(1));
                product.setName(resultSet.getString(2));
                product.setDescription(resultSet.getString(3));
                product.setCategoryId(resultSet.getString(4));
                productList.add(product);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(pStatement);
            DBUtil.closeConnection(connection);
        }catch(Exception e){
            e.printStackTrace();
        }
        return productList;
    }
}
