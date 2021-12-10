package uk.ac.ed.inf;

/**
 * Represents a location
 */
public class Words {

    /**Country of location*/
    private String country;
    private Square square;
    private String nearestPlace;
    /**The cartesian coordinates of the location*/
    private LngLat coordinates;
    /**The WhatThreeWords string of the location*/
    private String words;
    /**The language of the words in the WhatThreeWords location*/
    private String language;
    private String map;

    /**
     * A subclass that represents the coodinates of the location
     */
    public static class LngLat{
        /**The longitude of the point the location*/
        private double lng;
        /**The latitude of the point*/
        private double lat;

        public double getLng(){
            return this.lng;
        }
        public double getLat(){
            return this.lat;
        }
    }

    private static class Square{
        LngLat southwest;
        LngLat northeast;
    }

    public LngLat getCoordinates(){
        return this.coordinates;
    }

    public String getWords(){
        return this.words;
    }
}
