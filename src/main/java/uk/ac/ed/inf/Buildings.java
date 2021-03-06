package uk.ac.ed.inf;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import java.util.List;

/**
 * Represents the area of the flight
 */
public class Buildings {

    /**Represents the areas the drone is not to touch*/
    public static List<Polygon> noFlyZones = WebServerData.getNoFlyZones();

    /**Represents the Point the drone can travel to when it diverts from a noFlyZone*/
    public static List<Point> landMarks = WebServerData.getLandMarks();

    /**
     * Determines if three Points are collinear or not
     * @param x Represents the first point
     * @param y Represents the second point
     * @param z Represents the third point
     * @return int 1 means the points are collinear and 2 means they are not
     */
    private static int findOrientation(Point x, Point y, Point z){

        double value = ((z.longitude() - y.longitude()))*(y.latitude()
                - x.latitude())-((z.latitude() - y.latitude())*(y.longitude() - x.longitude()));
        if (value == 0.0){
            return 0;
        }
        return (value >0.0) ? 1:2;
    }

    /**
     * Determines whether a point is on a line
     * @param p Represents the point in question
     * @param x Represents the first point of line xy
     * @param y Represent the last point of line xy
     * @return boolean True if p is on line xy
     */
    public static boolean isOnLine(Point p, Point x, Point y){

        return (p.longitude() >= Math.min(x.longitude(), y.longitude())
                && p.longitude() <= Math.max(x.longitude(), y.longitude())
                && p.latitude() >= Math.min(x.latitude(), y.latitude())
                && p.latitude() <= Math.max(x.latitude(), y.latitude()));
    }

    /**
     * Determines if two lines intersect
     * @param x1 First point of line x1y1
     * @param y1 Last point of line  x1y1
     * @param x2 First point of line x2y2
     * @param y2 Last point of line x2y2
     * @return boolean true if lines x1y1 and x2y2 intersect
     */
    private static boolean linesIntersect(Point x1, Point y1, Point x2, Point y2){
        // Find the four orientations needed for general and special cases
        int o1 = findOrientation(x1,y1,x2);
        int o2 = findOrientation(x1,y1,y2);
        int o3 = findOrientation(x2,y2,x1);
        int o4 = findOrientation(x2,y2,y1);

        //General case
        if (o1!=o2 && o3!=o4){
            return true;
        }

        if (o1==0 && isOnLine(x2,x1,y1)){
            return true;
        }
        if (o2==0 && isOnLine(y2,x1,y1)){
            return true;
        }
        if (o3==0 && isOnLine(x1,x2,y2)){
            return true;
        }
        return o4 == 0 && isOnLine(y1, x2, y2);
    }

    /**
     * Determines whether a line intersects a polygon
     * @param x First point of line xy
     * @param y Last point of line xy
     * @param polygon Polygon in question
     * @return boolean true if line xy intersects the perimeter of the polygon
     */
    private static boolean lineIntersectsPolygon(Point x, Point y, Polygon polygon){

        LineString polygonPerimeter = polygon.outer();
        var pointsOnPerimeter = polygonPerimeter.coordinates();
        int n = pointsOnPerimeter.size();
        for (int i=0; i<n; i++){
            if (i < n-1){
                if (linesIntersect(x,y,pointsOnPerimeter.get(i),pointsOnPerimeter.get(i+1))){
                    return true;
                }
                else {
                    continue;
                }
            }
            if (i==n-1){
                if (linesIntersect(x,y,pointsOnPerimeter.get(i),pointsOnPerimeter.get(0))){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean lineIntersectsZone(Point x, Point y){
        for (var zone : noFlyZones){
            if (lineIntersectsPolygon(x,y,zone)) {
                return true;
            }
        }
        return false;
    }

}
