package uk.ac.ed.inf;

public class OutputInfo {

    private final String orderNo;
    private final String deliveredTo;
    private final int costInPence;
    private final double fromLongitude;
    private final double fromLatitude;
    private final int angle;
    private final double toLongitude;
    private final double toLatitude;


    public OutputInfo(String orderNo, String deliveredTo, int costInPence, double fromLongitude, double fromLatitude,
                      int angle, double toLongitude, double toLatitude){
        this.orderNo = orderNo;
        this.deliveredTo = deliveredTo;
        this.costInPence = costInPence;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
    }

    public String getOrderNo(){
        return orderNo;
    }

    public String getDeliveredTo(){
        return deliveredTo;
    }

    public int getCostInPence(){
        return costInPence;
    }

    public double getFromLongitude(){
        return fromLongitude;
    }

    public double getFromLatitude(){
        return fromLatitude;
    }

    public int getAngle(){
        return angle;
    }

    public double getToLongitude(){
        return toLongitude;
    }

    public double getToLatitude(){
        return toLatitude;
    }

}

