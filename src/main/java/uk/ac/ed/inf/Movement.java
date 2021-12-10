package uk.ac.ed.inf;

import com.mapbox.geojson.Point;

import java.sql.Date;
import java.sql.SQLException;
import java.util.*;

/**
 * Represents a point on a Cartesian plane
 */
public class Movement {

    static DatabaseContent databaseContent = new DatabaseContent();
    WebServerData serverData = new WebServerData();
    int numOfMoves = 1500;

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

    private Point hover(Point currentPost){
        return nextPosition(currentPost, -999);
    }


    private static Point swerveNoFlyZone(Point currentPost){

        List<Point> landmarks = Buildings.landMarks;
        ArrayList<Double> landmarkDistances = new ArrayList<>();

        for (int i=0; i<landmarks.size(); i++){
            Point landmark = landmarks.get(i);
            double distance = distanceTo(currentPost,landmark);
            landmarkDistances.set(i, distance);
        }
        double minDistance = Collections.min(landmarkDistances);
        int minIndex = landmarkDistances.indexOf(minDistance);

        return landmarks.get(minIndex);
    }


    public static ArrayList<OutputInfo> goToLocation(Point initPost, Point finalDestination, Orders order){
        var currentPost = initPost;
        Point destination = finalDestination;
        int angle = calcPathAngle(currentPost, destination);
        Point nextPosition = nextPosition(currentPost, angle);
        ArrayList<OutputInfo> pathOutputs = new ArrayList<>();

        while (!isCloseTo(currentPost, destination)) {
            if (isConfined(nextPosition)){
                if (!Buildings.lineIntersectsZone(currentPost, nextPosition)) {
                    OutputInfo outputInfo = new OutputInfo(order.orderNo, order.deliverTo, order.costInPence,currentPost.longitude(),
                            currentPost.latitude(), angle, nextPosition.longitude(), nextPosition.latitude());
                    pathOutputs.add(outputInfo);
                }else {
                    Point newDestination = swerveNoFlyZone(currentPost);
                    int newPathAngle = calcPathAngle(currentPost, newDestination);
                    Point newNextPosition = nextPosition(currentPost, newPathAngle);
                    OutputInfo outputInfo = new OutputInfo(order.orderNo,order.deliverTo,order.costInPence,currentPost.longitude(),
                            currentPost.latitude(), newPathAngle, newNextPosition.longitude(), newNextPosition.latitude());
                    pathOutputs.add(outputInfo);
                    destination = newDestination;
                }
                currentPost = nextPosition;
            }
        }
        return pathOutputs;
    }

    public static boolean isIntendedLoc(OutputInfo outputInfo, Point intendedDestination){
        Point finalDestination = Point.fromLngLat(outputInfo.getToLongitude(),outputInfo.getToLatitude());
        return isCloseTo(finalDestination, intendedDestination);
    }

    public static ArrayList<OutputInfo> ifLandMarked(OutputInfo outputInfo, Point intendedDestination, Orders order){
        ArrayList<OutputInfo> nextOutput = new ArrayList<>();

        if (!isIntendedLoc(outputInfo, intendedDestination)){
            Point currentLoc = Point.fromLngLat(outputInfo.getToLongitude(), outputInfo.getToLatitude());
            nextOutput = goToLocation(currentLoc, intendedDestination, order);
        }
        return nextOutput;
    }

    public static ArrayList<OutputInfo> goGetOrder(Point initialLoc, Orders order) {
        ArrayList<OutputInfo> finalOutput = new ArrayList<>();
        ArrayList<Point> shopLocs = order.findShopLocs();

        for (Point shop : shopLocs) {
            getLandMarkedOutputs(order, finalOutput, shop, initialLoc);
        }
        return finalOutput;
    }

    public static ArrayList<OutputInfo> goDeliverOrder(Point initialLoc, Orders order) throws SQLException {
        ArrayList<OutputInfo> finalOutput = new ArrayList<>();
        Point deliveryPoint = order.getDeliveryCoords();

        getLandMarkedOutputs(order, finalOutput, deliveryPoint, initialLoc);

        return finalOutput;
    }

    private static void getLandMarkedOutputs(Orders order, ArrayList<OutputInfo> finalOutput, Point deliveryPoint, Point currentLoc) {
        ArrayList<OutputInfo> initialOutput = goToLocation(currentLoc, deliveryPoint, order);
        finalOutput.addAll(initialOutput);

        int index = initialOutput.size();
        OutputInfo lastOutputInfo = initialOutput.get(index - 1);
        if (!isIntendedLoc(lastOutputInfo, deliveryPoint)) {
            ArrayList<OutputInfo> nextOutput = ifLandMarked(lastOutputInfo, deliveryPoint, order);
            finalOutput.addAll(nextOutput);
        }
    }

    public static Point getLastVisitedPoint(ArrayList<OutputInfo> outputs){
        int currentOutputSize = outputs.size();
        OutputInfo lastOutput = outputs.get(currentOutputSize -1);
        return Point.fromLngLat(lastOutput.getToLongitude(), lastOutput.getToLatitude());
    }

    public static ArrayList<OutputInfo> moveDrone() throws SQLException {
        Point droneStartPoint = Point.fromLngLat(-3.186874, 55.944494);
        ArrayList<OutputInfo> outputs = new ArrayList<>();
        Point currentPost = droneStartPoint;
        Date date = App.getDate();
        ArrayList<Orders> orders = databaseContent.getOrders(date);

        for (int i=0; i<orders.size(); i++){
            Orders order = orders.get(i);

            if (i==0){
                outputs.addAll(goGetOrder(droneStartPoint,order));
                Point pointBeforeDelivery = getLastVisitedPoint(outputs);
                outputs.addAll(goDeliverOrder(pointBeforeDelivery, order));
            }
            else {
                outputs.addAll(goGetOrder(currentPost,order));
                Point pointBeforeDelivery = getLastVisitedPoint(outputs);
                outputs.addAll(goDeliverOrder(pointBeforeDelivery, order));
            }
            currentPost = getLastVisitedPoint(outputs);
        }
        outputs.addAll(goToLocation(currentPost,droneStartPoint, null));

        return outputs;
    }

   public static void main (String[] args){

    }

}


