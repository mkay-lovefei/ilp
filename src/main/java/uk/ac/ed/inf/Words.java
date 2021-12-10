package uk.ac.ed.inf;

public class Words {

    private String country;
    private Square square;
    private String nearestPlace;
    private LngLat coordinates;
    private String words;
    private String language;
    private String map;

    public static class LngLat{
        private double lng;
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
