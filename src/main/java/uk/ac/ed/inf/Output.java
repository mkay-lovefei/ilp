package uk.ac.ed.inf;

import com.mapbox.geojson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the creation of the output databases, the insertion of their values and
 * the writing of the output file
 */
public class Output {

    /**A new DatabaseContent object from which the needed data will be retrieved*/
    DatabaseContent databaseContent = new DatabaseContent();


    /**
     * Creates a new table for recording information on a day's deliveries
     * @throws SQLException
     */
    private void DeliveriesTab() throws SQLException {
        databaseContent.dropIfExists("deliveries");
        databaseContent.statement.execute(
                "create table deliveries(" +
                        "orderNo char(8), " +
                        "deliveredTo varchar(19), " +
                        "costInPence int)" );
    }


    /**
     * Records all the order numbers, delivery locations and delivery costs of a days deliveries
     * into the deliveries table
     * @throws SQLException
     * @throws ParseException
     */
    public void deliveriesTable() throws SQLException, ParseException {
        ArrayList<OutputInfo> outputInfos = Movement.moveDrone();
        PreparedStatement psOutputInfo = databaseContent.conn.prepareStatement(
                "insert into deliveries values (?, ?, ?)" );

        for (OutputInfo info : outputInfos){
            psOutputInfo.setString(1, info.getOrderNo());
            psOutputInfo.setString(2, info.getDeliveredTo());
            psOutputInfo.setInt(3, info.getCostInPence());
            psOutputInfo.execute();
        }
    }


    /**
     * Creates a new table for recording the behaviour of the drone's flight
     * @throws SQLException
     */
    public void flightPathTable() throws SQLException {
        databaseContent.dropIfExists("flightpath");
        databaseContent.statement.execute(
                "create table flightpath(" +
                        "orderNo char(8), " +
                        "fromLongitude double, " +
                        "fromLatitude double, " +
                        "angle integer, " +
                        "toLongitude double, " +
                        "toLatitude double)" );
    }


    /**
     * Records all the order number, initial position of drone, position of drone after move and
     * the angle of the each move into the flightPath table
     * into the deliveries table
     * @throws SQLException
     * @throws ParseException
     */
    private void insertFlights() throws SQLException, ParseException {
        List<OutputInfo> outputInfos = Movement.moveDrone();
        PreparedStatement psOutputInfo = databaseContent.conn.prepareStatement(
                "insert into flightpath values (?, ?, ?, ?, ?, ?)" );

        for (OutputInfo info : outputInfos){
            psOutputInfo.setString(1, info.getOrderNo());
            psOutputInfo.setDouble(2, info.getFromLongitude());
            psOutputInfo.setDouble(3, info.getFromLatitude());
            psOutputInfo.setInt(4, info.getAngle());
            psOutputInfo.setDouble(5, info.getFromLongitude());
            psOutputInfo.setDouble(6, info.getToLatitude());
            psOutputInfo.execute();
        }
    }

    /**
     * Creates the flightpath of the drone
     * @param outputInfos Representing a list of all the OutputInfo objects that contain all the
     *                    necessary details of a move
     * @return FeatureCollection representing the flightpath of the drone
     * @throws SQLException
     */
    private FeatureCollection createFeatCollection (List<OutputInfo> outputInfos) throws SQLException {
        List<Feature> features = new ArrayList<>(1);

        OutputInfo firstMove = outputInfos.get(0);
        Point startPoint = Point.fromLngLat(firstMove.getFromLongitude(), firstMove.getFromLatitude());
        Point endPoint = Point.fromLngLat(firstMove.getToLongitude(), firstMove.getToLatitude());

        ArrayList<Point> pathPoints = new ArrayList<>();
        pathPoints.add(startPoint);
        pathPoints.add(endPoint);

        for (int i=1; i<outputInfos.size(); i++){
            pathPoints.add(Point.fromLngLat(outputInfos.get(i).getToLongitude(), outputInfos.get(i).getToLatitude()));
        }

        LineString path = LineString.fromLngLats(pathPoints);
        Feature feature = Feature.fromGeometry(path);
        features.add(feature);
        return FeatureCollection.fromFeatures(features);
    }


    /**
     * Creates a GeoJSON file that contains a visual representation of the flightpath
     */
    public void createGeoJsonFile(){
        try (FileWriter outputFile = new FileWriter(
                "drone-" + App.getArgs()[0] + "-" + App.getArgs()[1] + "-" + App.getArgs()[2] + ".geojson")){
            outputFile.write(createFeatCollection(Movement.moveDrone()).toJson());
            outputFile.close();
            System.out.println("File created successfully");
        } catch (SQLException | IOException | ParseException throwables) {
            System.out.println("An error occured!");
            throwables.printStackTrace();
        }
    }

}
