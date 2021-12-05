package uk.ac.ed.inf;

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

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
