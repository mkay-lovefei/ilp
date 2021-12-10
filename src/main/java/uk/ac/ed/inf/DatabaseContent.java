package uk.ac.ed.inf;

import java.sql.*;
import java.util.ArrayList;

/**
 * Represents the data retrieved from the database server
 */
public class DatabaseContent {

    /**A statement for running various SQL commands against the database*/
    Statement statement;
    public static Connection conn;

    /**
     * A string that specifies the details of the connection with the database server
     */
    static String jdbcString = "jdbc:derby://localhost:1527/derbyDB";

    /**Establishing a connection with the database server*/
    static {
        try {
            conn = DriverManager.getConnection(jdbcString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks if a given table exists in a database and deletes it if it does
     * @param tableName A String representing the name of the table in question
     */
    public void dropIfExists(String tableName) throws SQLException {
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, tableName,null);
        if (resultSet.next()){
            statement.execute("drop table tableName");
        }
    }


    /**
     * Gets all the order numbers for a particular date from the orders table in the database
     * @param givenDate An sql Date object that represents the date in question
     * @return ArrayList<String></String> representing the list of order numbers
     */
    public ArrayList<String> getOrderNumbers(Date givenDate) throws SQLException {
        ArrayList<String> orderNumbers = new ArrayList<>();

        final String orderNumQuery = "select orderNo from orders where deliveryDate=(?)";
        PreparedStatement psOrderNumQuery = conn.prepareStatement(orderNumQuery);
        psOrderNumQuery.setDate(1, givenDate);

        /**Search for order numbers that correspond to date and add them to a list*/
        ResultSet rs = psOrderNumQuery.executeQuery();
        while (rs.next()){
            String orderNo = rs.getString("orderNo");
            orderNumbers.add(orderNo);
        }
        return orderNumbers;
    }


    /***
     * Gets the order items for a particular order from the orderDetails table using
     * using the order number
     * @param orderNo Representing the order number of the order
     * @return ArrayList<String></String> Representing the list of items for the order
     */
    public ArrayList<String> getOrderItems(String orderNo) throws SQLException {
        ArrayList<String> orderItems = new ArrayList<>();

        final String itemsQuery = "select item from orderDetails where orderNo=(?)";
        PreparedStatement psItemsQuery = conn.prepareStatement(itemsQuery);
        psItemsQuery.setString(1, orderNo);

        /** find corresponding order items and add them to a list*/
        ResultSet rs = psItemsQuery.executeQuery();
        while (rs.next()){
            String item = rs.getString("item");
            orderItems.add(item);
        }
        return orderItems;
    }


    /***
     * Gets the WhatThreeWords location of an order from the orders table, using
     * the corresponding delivery date and order number
     * @param givenDate Represents the delivery date of the order
     * @param orderNum Represents the order number of the order
     * @return String Representing the WhatThreeWords location of the delivery point
     */
    public String getDeliveryLoc(Date givenDate, String orderNum) throws SQLException {

        String loc ="";
        final String orderNumQuery = "select deliverTo from orders where orderNo=(?) and deliveryDate=(?)";
        PreparedStatement psOrderNumQuery = conn.prepareStatement(orderNumQuery);
        psOrderNumQuery.setString(1, orderNum);
        psOrderNumQuery.setDate(2, givenDate);
        ResultSet rs = psOrderNumQuery.executeQuery();
        while (rs.next()){
            loc = rs.getString("deliverTo");
        }
        return loc;
    }


    /***
     * Creates a new order with a specified order number and delivery date then assigns the values
     * of the rest of the fields that will be needed for later calculations and outputs
     * @param orderNo Represents the order number of the order
     * @param date Represents the delivery date of the order
     * @return Orders Represents the newly created order
     * @throws SQLException
     */
    private Orders createNewOrder(String orderNo, Date date) throws SQLException {
        Orders order = new Orders(orderNo, date);

        order.deliverTo = getDeliveryLoc(date, orderNo);
        order.items = getOrderItems(orderNo);
        ArrayList<String> orderItems = order.items;
        order.costInPence = Menus.getDeliveryCost(orderItems.toArray(String[]::new));
        return order;
    }


    /***
     * Gets all the orders for a particular date
     * @param date Represents the date in question
     * @return ArrayList<Orders></Orders> Represent a list of all the orders made on the date given
     * each of these Orders objects contains all the details of the order
     * @throws SQLException
     */
    public ArrayList<Orders> getOrders(Date date) throws SQLException {
        ArrayList<String> orderNos = getOrderNumbers(date);
        ArrayList<Orders> orders = new ArrayList<>();

        for (String s : orderNos){
            Orders order = createNewOrder(s,date);
            orders.add(order);
        }
        return orders;
     }

    /**public ArrayList<String> getAllDeliveryLocs() throws SQLException {
        final String delLocQuery = "select deliverTo from orders";
        PreparedStatement psDelLocQuery = conn.prepareStatement(delLocQuery);
        ArrayList<String> DeliveryLocs = new ArrayList<>();
        ResultSet rs = psDelLocQuery.executeQuery();

        while (rs.next()){
            String loc = rs.getString("deliverTo");
            DeliveryLocs.add(loc);
        }
        return DeliveryLocs;
    }*/

}
