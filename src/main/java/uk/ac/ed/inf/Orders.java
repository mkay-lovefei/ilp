package uk.ac.ed.inf;

import com.mapbox.geojson.Point;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Orders {
    DatabaseContent databaseInfo = new DatabaseContent();
    WebServerData serverData = new WebServerData();
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


    public ArrayList<Point> findShopLocs(){
        ArrayList<Point> shops = new ArrayList<>(2);
        for (String i : items){
            for (Menus shop : WebServerData.retrievedMenus){
                for (Menus.MenuItem menuItem : shop.menu){
                    if (i.equals(menuItem.item))
                      shops.add(WebServerData.getLocation(shop.location));
                }
            }
        }
        return shops;
    }

    //get all visitable locations in a list
    public ArrayList<Point> getAllPoints() throws SQLException {
        ArrayList<Point> visitablePoints = new ArrayList<>();
        Point deliveryPoint = getDeliveryCoords();
        ArrayList<Point> shops = findShopLocs();
        visitablePoints.add(shops.get(0));
        if (shops.size() == 2){
            visitablePoints.add(shops.get(1));
        }
        visitablePoints.add(deliveryPoint);
        return visitablePoints;
    }

    public Point getDeliveryCoords() throws SQLException {
        deliverTo = databaseInfo.getDeliveryLoc(deliveryDate, orderNo);
        return serverData.getLocation(deliverTo);
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

