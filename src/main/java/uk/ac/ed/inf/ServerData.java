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

/**
 * Represents data retrieved from web server
 */
public class ServerData {

    /** An HTTP client to be used for all communications with web server */
    private static final HttpClient client = HttpClient.newHttpClient();

    /** Menus data retrieved from web server */
    public static List<Menus> retrievedMenus = getMenus();

    /**
     * Gets information of items for sale and their respective shops from web server
     * @return List<Menus></Menus> representing a list of all information on items for sale
     */
    public static List<Menus> getMenus(){
        Menus menus = new Menus("localhost", "9898");

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

    public static void main(String[] args){

    }

    public static Words getLocation(String location){
        String[] loc = location.split("\\."); //split location into 3 parts
        Words word = new Words();

        var request = HttpRequest.newBuilder().uri(URI.create("http://localhost:"
                + App.getArgs()[6]+"/words/"+loc[0]+loc[1]+loc[2]+"/details.json")).build();

        try{
            // get the response object of class HttpResponse<String>
            var response = client.send(request, BodyHandlers.ofString());

            word = new Gson().fromJson(response.body(), Words.class);
        }
        catch (IOException | InterruptedException e){
            e.printStackTrace();
        }
        return word;
    }

}
