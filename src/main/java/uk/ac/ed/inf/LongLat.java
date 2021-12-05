package uk.ac.ed.inf;

/**
 * Represents a point on a Cartesian plane
 */
public class LongLat {

    /** Horizontal component of point */
    public double lng;

    /** Vertical component of point */
    public double lat;

    /**
     * Creates new point with specified horizontal and vertical components
     * @param longitude A double representing the specified horizontal component of the point
     * @param latitude A double represent the specified vertical component of the point
     */
    public LongLat(double longitude, double latitude){
        this.lng = longitude;
        this.lat = latitude;
    }

    /**
     * Checks if this point is within the drone confinement area
     * @return True if the point is confined or False other wise
     */
    public boolean isConfined(){
        boolean withinLongRange = lng>-3.192473 && lng<-3.184319;
        boolean withinLatRange =  lat>55.942617 && lat<55.946233;
        return withinLongRange && withinLatRange;
    }

    /**
     * Calculates Pythagorean distance between this point and a given point
     * @param point LongLat object representing the point to which the distance of this point is to be calculated
     * @return A double representing the Pythagorean distance
     */
    public double distanceTo(LongLat point){
        double x2 = point.lng;
        double y2 = point.lat;
        double squaredLongDifference = (x2-lng)*(x2-lng);
        double squaredLatDifference = (y2-lat)*(y2-lat);
        double sumOfSquaredDifference = squaredLongDifference + squaredLatDifference;

        return Math.sqrt(sumOfSquaredDifference);
    }

    /**
     * Checks if this point is close to a given point
     * @param point LongLat object representing the point to which the closeness of this point it to be checked
     * @return True if this point is close to given point or False otherwise
     */
    public boolean closeTo(LongLat point){
        double distance = distanceTo(point);
        return distance < 0.00015;
    }

    /**
     * Calculates the point the drone will be at if it makes a move in a given direction
     * @param angle An integer representing the direction of move
     *              angle must be a multiple of 10 between 0 and 350
     *              if angle is greater than subtract 350 for new angle
     *              if angle is -999, no movement happens, hence, new position will be initial position
     *              if angle is any other negative number, 350 will be added continuously until it is eventually positive
     *              if angle is not a multiple of 10, it is rounded up to the nearest multiple of 10
     * @return A LongLat object representing the new position
     */
    public static LongLat nextPosition(int angle){
        LongLat position = new LongLat(-3.186874,55.944494);

        if (angle > 350){
            angle -= 350;
        }
        if (angle == -999){
            return  position;
        }
        while (angle<0){
            angle+=350;
        }

        double a = (Math.round((double)angle/10.0))*10;
        double moveDistance = 0.00015;
        //angle is converted to radians for a more accurate calculation
        double x = moveDistance*Math.cos(Math.toRadians(a));
        double y = moveDistance*Math.sin(Math.toRadians(a));
        position.lng += x;
        position.lat += y;
        return position;
    }

    public static void main (String[] args){

        System.out.println(nextPosition(90).lng +" "+ nextPosition(90).lat);

        }
}


