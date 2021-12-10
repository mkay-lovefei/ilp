package uk.ac.ed.inf;

/**
 * Represents the details of a move
 */
public class OutputInfo {

    private final String orderNo;
    private final String deliveredTo;
    private final int costInPence;
    private final double fromLongitude;
    private final double fromLatitude;
    private final int angle;
    private final double toLongitude;
    private final double toLatitude;


    /**
     * Creates a new move with specified details
     * @param orderNo Represents the order number of the order for which the move was made. Can be null
     *                when the drone is returning to its initial position after all deliveries are done
     * @param deliveredTo Represents the WhatThreeWords location of the delivery point of the order
     * @param costInPence Represents the cost of the order and its delivery
     * @param fromLongitude Represents the longitude of the point from which the move started
     * @param fromLatitude Represents the latitude of the point from which the move started
     * @param angle Represents the angle of the move
     * @param toLongitude Represents the longitude of the point at which the move ended
     * @param toLatitude Represents the latitude of the point at which the move ended
     */
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

