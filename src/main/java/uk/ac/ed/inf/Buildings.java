package uk.ac.ed.inf;

import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import java.util.List;

public class Buildings {

    public static List<Polygon> noFlyZones = WebServerData.getNoFlyZones();

    public static List<Point> landMarks = WebServerData.getLandMarks();

    private static int findOrientation(Point x, Point y, Point z){

        double value = ((z.longitude() - y.longitude()))*(y.latitude()
                - x.latitude())-((z.latitude() - y.latitude())*(y.longitude() - x.longitude()));
        if (value == 0.0){
            return 0;
        }
        return (value >0.0) ? 1:2;
    }

    public static boolean isOnLine(Point p, Point x, Point y){

        return (p.longitude() >= Math.min(x.longitude(), y.longitude())
                && p.longitude() <= Math.max(x.longitude(), y.longitude())
                && p.latitude() >= Math.min(x.latitude(), y.latitude())
                && p.latitude() <= Math.max(x.latitude(), y.latitude()));
    }

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
