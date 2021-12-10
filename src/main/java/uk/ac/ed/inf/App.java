package uk.ac.ed.inf;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static String[] savedArgs = new String[7]; //stored arguments from command line

    public static String[] getArgs(){
        return savedArgs;
    }

    public static Date getDate(){
        String dateString = getArgs()[0] + "-" + getArgs()[1] + "-" +getArgs()[2];
        return Date.valueOf(dateString);
    }

    public static void main( String[] args ) throws SQLException {

        savedArgs = args;

        Output output = new Output();
        output.deliveriesTable();
        output.flightPathTable();
        output.createGeoJsonFile();
    }
}
