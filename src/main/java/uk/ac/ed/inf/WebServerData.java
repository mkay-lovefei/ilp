package uk.ac.ed.inf;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

/**
 * Represents data retrieved from web server
 */
public class WebServerData {

    /** An HTTP client to be used for all communications with web server */
    private static final HttpClient client = HttpClient.newHttpClient();

    /** Menus data retrieved from web server */
    public static List<Menus> retrievedMenus = getMenus();



    /**
     * Gets information of items for sale and their respective shops from web server
     * @return List<Menus></Menus> representing a list of all information on items for sale
     */
    public static List<Menus> getMenus(){
        Menus menus = new Menus("localhost", App.getArgs()[3]);

        //build http request and send to client
        // client assumes it is a GET request by default
        var request = HttpRequest.newBuilder().uri(URI.create("http://" + menus.machineName+ ":" + menus.serverPort + "/menus/menus.json")).build();

        //holds menus
        var retrievedMenus = new ArrayList<Menus>();

        try {
            // Response object is of class HttpResponse<String>
           var response = client.send(request, BodyHandlers.ofString());

           Type listType = new TypeToken<ArrayList<Menus>>(){}.getType();

           //obtain menus from body of response
            retrievedMenus = new Gson().fromJson(response.body(), listType);
            }

            catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        return retrievedMenus;
    }


    /**
     * Gets the corresponding cartesian Point of a WhatThreeWords location from the Words folder
     * on the web server
     * @param location Represents a WhatThreeWords location
     * @return Point Represents the cartesian coordinates of the location
     */
    public static Point getLocation(String location){
        String[] loc = location.split("\\."); //split location into 3 parts
        Words word = new Words();

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:"
                + App.getArgs()[3]+"/words/"+loc[0]+ "/" + loc[1] + "/" + loc[2]+"/details.json")).build();

        try{
            // get the response object of class HttpResponse<String>
            var response = client.send(request, BodyHandlers.ofString());

            word = new Gson().fromJson(response.body(), Words.class);
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
       return Point.fromLngLat(word.getCoordinates().getLng(), word.getCoordinates().getLat());
    }

    /**public ArrayList<String> getAllShopLocs(){
        ArrayList<String> shopLocs = new ArrayList<>();
        for (Menus menu : retrievedMenus){
            String locCoords = menu.location;
            shopLocs.add(locCoords);
        }
        return shopLocs;
    }*/


    /**
     * Gets the Polygons that represent the areas that the drone is not
     * supposed to enter
     * @return List<Polygon></Polygon> Represents the list of noFlyZone areas
     */
    static List<Polygon> getNoFlyZones(){
        var noFlyZones = new ArrayList<Polygon>();

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:"
                + App.getArgs()[6]+"/buildings/"+"/no-fly-zones.geojson")).build();

        try{
            // get the response object of class HttpResponse<String>
            var response = client.send(request, BodyHandlers.ofString());

            var featureCollection = FeatureCollection.fromJson(response.body());
            var features = featureCollection.features();
            for (Feature feature : features){
                Polygon zone = (Polygon) feature.geometry();
                noFlyZones.add(zone);
            }
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return noFlyZones;
    }


    /**
     * Gets all the landmarks that the drone could fly in case of a diversion
     * @return List<Point></Point> Represents the list of the cartesian points of the landmarks
     */
    static List<Point> getLandMarks(){
        var landMarks = new ArrayList<Point>();

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:"
                + App.getArgs()[6]+"/buildings/"+"/landmarks.geojson")).build();

        try{
            // get the response object of class HttpResponse<String>
            var response = client.send(request, BodyHandlers.ofString());

            var featureCollection = FeatureCollection.fromJson(response.body());
            var features = featureCollection.features();
            for (Feature feature : features){
                Point landMark = (Point) feature.geometry();
                landMarks.add(landMark);
            }
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return landMarks;
    }
}
