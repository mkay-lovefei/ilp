package uk.ac.ed.inf;

import com.mapbox.geojson.Point;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Move {

    /**A new instance of the data retrieved from the database server to access the class' non-static methods and fields*/
    static DatabaseContent databaseContent = new DatabaseContent();
    /**A new instance of the data retrieved from the web server to access the class' non-static methods and fields */
    WebServerData serverData = new WebServerData();


    /**
     * Checks if a given point is within the drone confinement area
     * @return True if the point is confined or False otherwise
     */
    public static boolean isConfined(Point x){
        boolean withinLongRange = x.longitude()>-3.192473 && x.longitude()<-3.184319;
        boolean withinLatRange =  x.latitude()>55.942617 && x.latitude()<55.946233;
        return withinLongRange && withinLatRange;
    }

    /**
     * Calculates Pythagorean distance between point p and point q
     * @param p LongLat object representing the p to which the distance of this p is to be calculated
     * @return A double representing the Pythagorean distance
     */
    public static double distanceTo(Point p, Point q){
        double x1 = p.longitude();
        double y1 = p.latitude();
        double x2 = q.longitude();
        double y2 = q.latitude();

        double squaredLongDifference = (x1-x2)*(x1-x2);
        double squaredLatDifference = (y1- y2)*(y1- y2);
        double sumOfSquaredDifference = squaredLongDifference + squaredLatDifference;

        return Math.sqrt(sumOfSquaredDifference);
    }

    /**
     * Checks if point p is close to point q
     * @param p LongLat object representing the first point
     * @param q LongLat object representing the second point
     * @return True if p is close to q or False otherwise
     */
    public static boolean isCloseTo(Point p, Point q){
        double distance = distanceTo(p,q);
        return distance < 0.00015;
    }

    /**
     * Calculates the point the drone will be at if it makes a move in a given direction
     * @param currentPost the current position of the drone
     * @param angle An integer representing the direction of move
     *              angle must be a multiple of 10 between 0 and 350
     *              if angle is greater than subtract 350 for new angle
     *              if angle is -999, no movement happens, hence, new position will be initial position
     *              if angle is any other negative number, 350 will be added continuously until it is eventually positive
     *              if angle is not a multiple of 10, it is rounded up to the nearest multiple of 10
     * @return A LongLat object representing the new position
     */
    public static Point nextPosition(Point currentPost, int angle){

        if (angle > 350){
            angle -= 350;
        }
        if (angle == -999){
            return  currentPost;
        }
        while (angle<0){
            angle+=350;
        }

        double a = (Math.round((double)angle/10.0))*10;
        double moveDistance = 0.00015;
        //angle is converted to radians for a more accurate calculation
        double x = moveDistance*Math.cos(Math.toRadians(a));
        double y = moveDistance*Math.sin(Math.toRadians(a));
        double newLong = currentPost.longitude() + x;
        double newLat = currentPost.longitude() + y;

        return Point.fromLngLat(newLong,newLat);
    }

    public static int calcPathAngle(Point initPost, Point destination){

        double r = distanceTo(initPost,destination);
        Point x2y1 = Point.fromLngLat(destination.longitude(),initPost.latitude());
        double x = distanceTo(initPost,x2y1);

        return (int) Math.acos(x/r);
    }

    public static Date getDate() throws ParseException {
        String dateString = App.getArgs()[0] + "-" + App.getArgs()[1] + "-" +App.getArgs()[2];
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date dateUtil = dateFormat.parse(dateString);
        java.sql.Date date = new java.sql.Date(dateUtil.getTime());
        return date;
    }


    public Map<Point, Orders> getAllVisitables(ArrayList<Orders> orders) throws SQLException {
        Map<Point, Orders> allVisitablePoints = new HashMap<>();
        for (Orders order : orders){
            ArrayList<Point> visitablePoints = order.getAllPoints();
            for (int i=0; i<visitablePoints.size(); i++){
                allVisitablePoints.put(visitablePoints.get(i), order);
            }
        }
        return allVisitablePoints;
    }

    public ArrayList<OutputInfo> fly() throws ParseException, SQLException {
        Date date = getDate();
        ArrayList<Orders> orders = databaseContent.getOrders(date);
        Map<Point, Orders> visitables = getAllVisitables(orders);
        ArrayList<Point> pointsToVisit = new ArrayList<>();
        pointsToVisit.addAll(visitables.keySet());
        Point droneStarPoint = Point.fromLngLat(-3.186874, 55.944494);

        for (int i=0; i<pointsToVisit.size(); i++){
            Point finalPoint =
            Point currentPost = droneStarPoint;
            int angle = calcPathAngle(currentPost)
            Point nextPoint =
            int moveLimit = 1500;
            int moveCount = 0;

            while (moveCount < moveLimit){

            }
        }


    }
}
