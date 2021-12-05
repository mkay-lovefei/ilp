package uk.ac.ed.inf;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Orders {
    DatabaseContent databaseInfo = new DatabaseContent();
    Menus menus = new Menus("localhost", "9898");

    String orderNo;
    String customer;
    ArrayList<String> items;
    Date deliveryDate;
    String deliverTo;
    int costInPence;


    public Orders(String orderNumber, Date deliveryDate){
        this.orderNo = orderNumber;
        this.deliveryDate = deliveryDate;
    }


    public ArrayList<String> findShopLoc(ArrayList<String> items){
        ArrayList<String> shopLoc = new ArrayList<>();
        for (String i : items){
            for (Menus shop : ServerData.retrievedMenus){
                for (Menus.MenuItem menuItem : shop.menu){
                    if (i.equals(menuItem.item))
                        shopLoc.add(shop.location);
                }
            }
        }
        return shopLoc;
    }

    public void setDeliverTo() throws SQLException {
        deliverTo = databaseInfo.getDeliveryLoc(deliveryDate, orderNo);
    }

    public void setOrderItems() throws SQLException {
        items = databaseInfo.getOrderItems(orderNo);
    }

    public void setCustomer() throws SQLException {
        customer = databaseInfo.getCustomer(deliveryDate ,orderNo);
    }

    public void calcDeliveryCost(){
        costInPence = menus.getDeliveryCost(String.valueOf(items));
    }
}

