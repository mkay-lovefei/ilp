package uk.ac.ed.inf;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class App 
{
    private static String[] savedArgs = new String[7]; //stored arguments from command line

    public static String[] getArgs(){
        return savedArgs;
    }



    public static void main( String[] args ) throws SQLException, ParseException {

        savedArgs = args;

        Movement droneMovement = new Movement ();

        ArrayList<OutputInfo> result = droneMovement.moveDrone();

        for (OutputInfo out : result) {
            System.out.println(out);
        }

//        Output output = new Output();
//        output.deliveriesTable();
//        output.flightPathTable();
//        output.createGeoJsonFile();
    }
}
