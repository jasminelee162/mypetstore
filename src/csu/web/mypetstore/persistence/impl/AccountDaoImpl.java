package csu.web.mypetstore.persistence.impl;

import csu.web.mypetstore.domain.Account;
import csu.web.mypetstore.persistence.AccountDao;
import csu.web.mypetstore.persistence.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountDaoImpl implements AccountDao {

    private static final String GET_ACCOUNT_BY_USERNAME =
            "SELECT SIGNON.USERNAME,ACCOUNT.EMAIL,ACCOUNT.FIRSTNAME,ACCOUNT.LASTNAME,ACCOUNT.STATUS,ACCOUNT.ADDR1 AS address1," +
                    "ACCOUNT.ADDR2 AS address2,ACCOUNT.CITY,ACCOUNT.STATE,ACCOUNT.ZIP,ACCOUNT.COUNTRY,ACCOUNT.PHONE,PROFILE.LANGPREF AS languagePreference," +
                    "PROFILE.FAVCATEGORY AS favouriteCategoryId,PROFILE.MYLISTOPT AS listOption,PROFILE.BANNEROPT AS bannerOption,BANNERDATA.BANNERNAME " +
                    "FROM ACCOUNT, PROFILE, SIGNON, BANNERDATA WHERE ACCOUNT.USERID = ? AND SIGNON.USERNAME = ACCOUNT.USERID " +
                    "AND PROFILE.USERID = ACCOUNT.USERID AND PROFILE.FAVCATEGORY = BANNERDATA.FAVCATEGORY";

    private static final String GET_ACCOUNT_BY_USERNAME_AND_PASSWORD =
            "SELECT SIGNON.USERNAME,ACCOUNT.EMAIL,ACCOUNT.FIRSTNAME,ACCOUNT.LASTNAME,ACCOUNT.STATUS,ACCOUNT.ADDR1 AS address1," +
                    "ACCOUNT.ADDR2 AS address2,ACCOUNT.CITY,ACCOUNT.STATE,ACCOUNT.ZIP,ACCOUNT.COUNTRY,ACCOUNT.PHONE,PROFILE.LANGPREF AS languagePreference," +
                    "PROFILE.FAVCATEGORY AS favouriteCategoryId,PROFILE.MYLISTOPT AS listOption,PROFILE.BANNEROPT AS bannerOption,BANNERDATA.BANNERNAME " +
                    "FROM ACCOUNT, PROFILE, SIGNON, BANNERDATA WHERE ACCOUNT.USERID = ? AND SIGNON.PASSWORD = ? AND SIGNON.USERNAME = ACCOUNT.USERID " +
                    "AND PROFILE.USERID = ACCOUNT.USERID AND PROFILE.FAVCATEGORY = BANNERDATA.FAVCATEGORY";

    private static final String INSERT_ACCOUNT =
            "INSERT INTO ACCOUNT (EMAIL, FIRSTNAME, LASTNAME, STATUS, ADDR1, ADDR2, CITY, STATE, ZIP, COUNTRY, PHONE, USERID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_PROFILE =
            "INSERT INTO PROFILE (LANGPREF, FAVCATEGORY, USERID, MYLISTOPT, BANNEROPT) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_SIGNON =
            "INSERT INTO SIGNON (PASSWORD,USERNAME) VALUES (?, ?)";

    private static final String UPDATE_ACCOUNT =
            "UPDATE ACCOUNT SET EMAIL = ? ,FIRSTNAME = ? ,LASTNAME = ? ,STATUS = ? ,ADDR1 = ? ,ADDR2 = ? ," +
                    "CITY = ? ,STATE = ? ,ZIP = ? ,COUNTRY = ? ,PHONE = ?  WHERE USERID = ? ";

    private static final String UPDATE_PROFILE =
            "UPDATE PROFILE SET LANGPREF = ?, FAVCATEGORY = ?, MYLISTOPT = ?, BANNEROPT = ? WHERE USERID = ?";

    private static final String UPDATE_SIGNON =
            "UPDATE SIGNON SET PASSWORD = ? WHERE USERNAME = ?";

    private Account resultSetToAccount(ResultSet resultSet) throws Exception{
        Account account = new Account();
        account.setUsername(resultSet.getString("username"));
        //account.setPassword(resultSet.getString("password"));
        account.setEmail(resultSet.getString("email"));
        account.setFirstName(resultSet.getString("firstName"));
        account.setLastName(resultSet.getString("lastName"));
        account.setStatus(resultSet.getString("status"));
        account.setAddress1(resultSet.getString("address1"));
        account.setAddress2(resultSet.getString("address2"));
        account.setCity(resultSet.getString("city"));
        account.setState(resultSet.getString("state"));
        account.setZip(resultSet.getString("zip"));
        account.setCountry(resultSet.getString("country"));
        account.setPhone(resultSet.getString("phone"));
        account.setFavouriteCategoryId(resultSet.getString("favouriteCategoryId"));
        account.setLanguagePreference(resultSet.getString("languagePreference"));
        account.setListOption(resultSet.getInt("listOption") == 1);
        account.setBannerOption(resultSet.getInt("bannerOption") == 1);
        account.setBannerName(resultSet.getString("bannerName"));
        return account;
    }

    @Override
    public Account getAccountByUsername(String username) {
        Account accountResult = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_USERNAME);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                accountResult = this.resultSetToAccount(resultSet);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountResult;
    }

    @Override
    public Account getAccountByUsernameAndPassword(Account account) {
        Account accountResult = null;
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_USERNAME_AND_PASSWORD);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                accountResult = this.resultSetToAccount(resultSet);
            }
            DBUtil.closeResultSet(resultSet);
            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
        return accountResult;
    }

    @Override
    public void insertAccount(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACCOUNT);
            preparedStatement.setString(1, account.getEmail());
            preparedStatement.setString(2, account.getFirstName());
            preparedStatement.setString(3, account.getLastName());
            preparedStatement.setString(4, account.getStatus());
            preparedStatement.setString(5, account.getAddress1());
            preparedStatement.setString(6, account.getAddress2());
            preparedStatement.setString(7, account.getCity());
            preparedStatement.setString(8, account.getState());
            preparedStatement.setString(9, account.getZip());
            preparedStatement.setString(10, account.getCountry());
            preparedStatement.setString(11, account.getPhone());
            preparedStatement.setString(12, account.getUsername());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertProfile(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROFILE);
            preparedStatement.setString(1, account.getLanguagePreference());
            preparedStatement.setString(2, account.getFavouriteCategoryId());
            preparedStatement.setString(3, account.getUsername());
            preparedStatement.setInt(4, account.isListOption() ? 1 : 0);
            preparedStatement.setInt(5, account.isBannerOption() ? 1 : 0);
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertSignon(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SIGNON);
            preparedStatement.setString(1, account.getPassword());
            preparedStatement.setString(2, account.getUsername());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateAccount(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);
            preparedStatement.setString(1, account.getEmail());
            preparedStatement.setString(2, account.getFirstName());
            preparedStatement.setString(3, account.getLastName());
            preparedStatement.setString(4, account.getStatus());
            preparedStatement.setString(5, account.getAddress1());
            preparedStatement.setString(6, account.getAddress2());
            preparedStatement.setString(7, account.getCity());
            preparedStatement.setString(8, account.getState());
            preparedStatement.setString(9, account.getZip());
            preparedStatement.setString(10, account.getCountry());
            preparedStatement.setString(11, account.getPhone());
            preparedStatement.setString(12, account.getUsername());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateProfile(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROFILE);
            preparedStatement.setString(1, account.getLanguagePreference());
            preparedStatement.setString(2, account.getFavouriteCategoryId());
            preparedStatement.setInt(3, account.isListOption() ? 1 : 0);
            preparedStatement.setInt(4, account.isBannerOption() ? 1 : 0);
            preparedStatement.setString(5, account.getUsername());

            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void updateSignon(Account account) {
        try {
            Connection connection = DBUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SIGNON);
            preparedStatement.setString(1, account.getPassword());
            preparedStatement.setString(2, account.getUsername());
            preparedStatement.executeUpdate();

            DBUtil.closePreparedStatement(preparedStatement);
            DBUtil.closeConnection(connection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

