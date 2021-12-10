package uk.ac.ed.inf;

import com.mapbox.geojson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Output {

    DatabaseContent databaseContent = new DatabaseContent();

    private void createDeliveriesTab() throws SQLException {
        databaseContent.dropIfExists("deliveries");
        databaseContent.statement.execute(
                "create table deliveries(" +
                        "orderNo char(8), " +
                        "deliveredTo varchar(19), " +
                        "costInPence int)" );
    }

    public void deliveriesTable() throws SQLException {
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

    private void insertFlights() throws SQLException {
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

    public void createGeoJsonFile(){
        try (FileWriter outputFile = new FileWriter(
                "drone-" + App.getArgs()[0] + "-" + App.getArgs()[1] + "-" + App.getArgs()[2] + ".geojson")){
            outputFile.write(createFeatCollection(Movement.moveDrone()).toJson());
            outputFile.close();
            System.out.println("File created successfully");
        } catch (SQLException | IOException throwables) {
            System.out.println("An error occured!");
            throwables.printStackTrace();
        }
    }




}
