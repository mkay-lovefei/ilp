package uk.ac.ed.inf;

import com.mapbox.geojson.Point;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Represents the items for sale in the shops participating in the drone delivery service
 */
public class Menus {

    WebServerData serverData = new WebServerData();

    /** name of shop */
    String name;

    /** location of shop */
    String location;

    /** List of items on shop's menu */
    List<MenuItem> menu;

    /** Name of machine web server is running on */
    String machineName;

    /** The port where web server is running */
    String serverPort;

    /**
     * Creates a new Menus object with a specified machine name and port where web server is to run on
     * @param machineName A String representing name of machine that web server is to run on
     * @param serverPort A String representing the port the web server is to run on
     */
    public Menus (String machineName, String serverPort){
        this.machineName = machineName;
        this.serverPort = serverPort;
    }

    /**
     * Represents a menu item consisting of an item name and the cost of the item in pence
     */
    public static class MenuItem{
        String item;
        int pence;
    }

    /**
     * Gets a list of every shop's menu items from the Menus data retrieved from web server
     * and puts them in item name, item cost pairs
     * @return Map<String, Integer></String,> representing item name, item cost pairs
     */
    public static Map<String, Integer> menuItemList(){
        Map<String,Integer> menuItems = new HashMap<>();
        for(Menus shop : WebServerData.retrievedMenus){
            for (Menus.MenuItem menuItem : shop.menu){
                menuItems.put(menuItem.item,menuItem.pence);
            }
        }
        return menuItems;
    }

    /**
     * Calculates the cost of delivering a number of items, including a fixed delivery
     * charge of 50pence
     * It is assumed that no two shops have the same item, hence no item has varying costs from different shops
     * @param args A variable number of strings representing items to be delivered
     * @return An integer representing the total delivery cost in pence
     */
    public static int getDeliveryCost(String... args){

        int cost = 50;
        for (String item : args){
            if(menuItemList().containsKey(item)){
                cost += menuItemList().get(item);
            }
        }
        return cost;
    }

    public Point getShopCoordinates(){
        return serverData.getLocation(location);
    }
}
