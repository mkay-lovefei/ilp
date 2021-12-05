package uk.ac.ed.inf;

public class Words {

    private String country;
    private Square square;
    private String nearestPlace;
    private LongLat coordinates;
    private String words;
    private String language;
    private String map;

    private static class Square{
        LongLat southwest;
        LongLat northeast;
    }

    public LongLat getCoordinates(){
        return this.coordinates;
    }

    public String getWords(){
        return this.words;
    }
}
