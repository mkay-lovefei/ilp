package uk.ac.ed.inf;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseContent {


    static String jdbcString = "jdbc:derby://localhost:1527/derbyDB";
    private static Connection conn;
    static {
        try {
            conn = DriverManager.getConnection(jdbcString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    Statement statement;
    {
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropIfExists(String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, tableName,null);
        if (resultSet.next()){
            statement.execute("drop table tableName");
        }
    }

    private ArrayList<String> getOrderNumbers(Date givenDate) throws SQLException {
        ArrayList<String> orderNumbers = new ArrayList<>();

        final String orderNumQuery = "select orderNo from orders where deliveryDate=(?)";
        PreparedStatement psOrderNumQuery = conn.prepareStatement(orderNumQuery);
        psOrderNumQuery.setDate(1, givenDate);

        // Search for order numbers that correspond to date and add them to a list
        ResultSet rs = psOrderNumQuery.executeQuery();
        while (rs.next()){
            String orderNo = rs.getString("orderNo");
            orderNumbers.add(orderNo);
        }
        return orderNumbers;
    }



    public ArrayList<String> getOrderItems(String orderNo) throws SQLException {
        ArrayList<String> orderItems = new ArrayList<>();

        final String itemsQuery = "select item from orderDetails where orderNo=(?)";
        PreparedStatement psItemsQuery = conn.prepareStatement(itemsQuery);
        psItemsQuery.setString(1, orderNo);

        // find corresponding order items and add them to a list
        ResultSet rs = psItemsQuery.executeQuery();
        while (rs.next()){
            String item = rs.getString("item");
            orderItems.add(item);
        }
        return orderItems;
    }

    public String getDeliveryLoc(Date givenDate, String orderNum) throws SQLException {

        final String orderNumQuery = "select deliverTo from orders where orderNo=(?) and deliveryDate=(?)";
        PreparedStatement psOrderNumQuery = conn.prepareStatement(orderNumQuery);
        psOrderNumQuery.setString(1,orderNum);
        psOrderNumQuery.setDate(1, givenDate);
        ResultSet rs = psOrderNumQuery.executeQuery();
        String deliverTo = rs.getString("deliverTo");

        return deliverTo;
    }

    public String getCustomer (Date givenDate, String orderNum) throws SQLException {

        final String orderNumQuery = "select customer from orders where orderNo=(?) and deliveryDate=(?)";
        PreparedStatement psOrderNumQuery = conn.prepareStatement(orderNumQuery);
        psOrderNumQuery.setString(1,orderNum);
        psOrderNumQuery.setDate(1, givenDate);
        ResultSet rs = psOrderNumQuery.executeQuery();
        String customer = rs.getString("customer");

        return customer;
    }

}
