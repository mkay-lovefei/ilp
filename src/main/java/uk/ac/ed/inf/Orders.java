package uk.ac.ed.inf;

import com.mapbox.geojson.Point;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Represents an order
 * */
public class Orders {
    DatabaseContent databaseInfo = new DatabaseContent();
    WebServerData serverData = new WebServerData();

    /**A String that uniquely identifies the order*/
    String orderNo;
    /**A String that uniquely identifies a person making an order*/
    String customer;
    /**The items ordered in this order*/
    ArrayList<String> items;
    Date deliveryDate;
    /**The WhatThreeWords location the order is to be delivered at*/
    String deliverTo;
    int costInPence;

/**
 * Creates a new order with a specified orderNumber and deliveryDate
 * @param orderNumber A String that represents the order number of the order
 * @param deliveryDate an SQL Date object that represents the delivery date of the order
 */
    public Orders(String orderNumber, Date deliveryDate){
        this.orderNo = orderNumber;
        this.deliveryDate = deliveryDate;
    }

/**
 * Gets the cartesian Points representing the location of the shops this order is made from
 * @return ArrayList<Point></Point> that represents the locations of the shops. May have one or two points.
 * */
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

    /**
     * gets all visitable locations of an order
     * @return ArrayList<Point></Point> Represents all the points the drone is to visit for an order
     */
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

    /**
     * Gets the cartesian coordinates that represent delivery location
     * @return Point representing the location of the delivery location
     */
    public Point getDeliveryCoords() throws SQLException {
        deliverTo = databaseInfo.getDeliveryLoc(deliveryDate, orderNo);
        return serverData.getLocation(deliverTo);
    }

}

