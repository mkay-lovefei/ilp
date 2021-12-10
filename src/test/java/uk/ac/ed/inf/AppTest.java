package uk.ac.ed.inf;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
/** public class AppTest
{
    /**
     * Rigorous Test :-)
     */

    /**private static final String VERSION = "1.0.5";
    private static final String RELEASE_DATE = "September 28, 2021";

    private final Movement appletonTower = new Movement(-3.186874, 55.944494);
    private final Movement businessSchool = new Movement(-3.1873,55.9430);
    private final Movement greyfriarsKirkyard = new Movement(-3.1928,55.9469);

    @Test
    public void testIsConfinedTrueA(){
        assertTrue(appletonTower.isConfined());
    }

    @Test
    public void testIsConfinedTrueB(){
        assertTrue(businessSchool.isConfined());
    }

    @Test
    public void testIsConfinedFalse(){
        assertFalse(greyfriarsKirkyard.isConfined());
    }

    private boolean approxEq(double d1, double d2) {
        return Math.abs(d1 - d2) < 1e-12;
    }

    @Test
    public void testDistanceTo(){
        double calculatedDistance = 0.0015535481968716011;
        assertTrue(approxEq(appletonTower.distanceTo(businessSchool), calculatedDistance));
    }

    @Test
    public void testCloseToTrue(){
        Movement alsoAppletonTower = new Movement(-3.186767933982822, 55.94460006601717);
        assertTrue(appletonTower.closeTo(alsoAppletonTower));
    }


    @Test
    public void testCloseToFalse(){
        assertFalse(appletonTower.closeTo(businessSchool));
    }


    private boolean approxEq(Movement l1, Movement l2) {
        return approxEq(l1.lng, l2.lng) &&
                approxEq(l1.lat, l2.lat);
    }

    @Test
    public void testAngle0(){
        Movement nextPosition = appletonTower.nextPosition(0);
        Movement calculatedPosition = new Movement(-3.186724, 55.944494);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle20(){
        Movement nextPosition = appletonTower.nextPosition(20);
        Movement calculatedPosition = new Movement(-3.186733046106882, 55.9445453030215);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle50(){
        Movement nextPosition = appletonTower.nextPosition(50);
        Movement calculatedPosition = new Movement(-3.186777581858547, 55.94460890666647);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle90(){
        Movement nextPosition = appletonTower.nextPosition(90);
        Movement calculatedPosition = new Movement(-3.186874, 55.944644);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle140(){
        Movement nextPosition = appletonTower.nextPosition(140);
        Movement calculatedPosition = new Movement(-3.1869889066664676, 55.94459041814145);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle190(){
        Movement nextPosition = appletonTower.nextPosition(190);
        Movement calculatedPosition = new Movement(-3.1870217211629517, 55.94446795277335);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle260(){
        Movement nextPosition = appletonTower.nextPosition(260);
        Movement calculatedPosition = new Movement(-3.18690004722665, 55.944346278837045);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle300(){
        Movement nextPosition = appletonTower.nextPosition(300);
        Movement calculatedPosition = new Movement(-3.186799, 55.94436409618943);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle350(){
        Movement nextPosition = appletonTower.nextPosition(350);
        Movement calculatedPosition = new Movement(-3.1867262788370483, 55.94446795277335);
        assertTrue(approxEq(nextPosition, calculatedPosition));
    }

    @Test
    public void testAngle999(){
        // The special junk value -999 means "hover and do not change position"
        Movement nextPosition = appletonTower.nextPosition(-999);
        assertTrue(approxEq(nextPosition, appletonTower));
    }

    @Test
    public void testMenusOne() {
        // The webserver must be running on port 9898 to run this test.
        Menus menus = new Menus("localhost", "9898");
        int totalCost = menus.getDeliveryCost(
                "Ham and mozzarella Italian roll"
        );
        // Don't forget the standard delivery charge of 50p
        assertEquals(230 + 50, totalCost);
    }

    @Test
    public void testMenusTwo() {
        // The webserver must be running on port 9898 to run this test.
        Menus menus = new Menus("localhost", "9898");
        int totalCost = menus.getDeliveryCost(
                "Ham and mozzarella Italian roll",
                "Salami and Swiss Italian roll"
        );
        // Don't forget the standard delivery charge of 50p
        assertEquals(230 + 230 + 50, totalCost);
    }

    @Test
    public void testMenusThree() {
        // The webserver must be running on port 9898 to run this test.
        Menus menus = new Menus("localhost", "9898");
        int totalCost = menus.getDeliveryCost(
                "Ham and mozzarella Italian roll",
                "Salami and Swiss Italian roll",
                "Flaming tiger latte"
        );
        // Don't forget the standard delivery charge of 50p
        assertEquals(230 + 230 + 460 + 50, totalCost);
    }

    @Test
    public void testMenusFourA() {
        // The webserver must be running on port 9898 to run this test.
        Menus menus = new Menus("localhost", "9898");
        int totalCost = menus.getDeliveryCost(
                "Ham and mozzarella Italian roll",
                "Salami and Swiss Italian roll",
                "Flaming tiger latte",
                "Dirty matcha latte"
        );
        // Don't forget the standard delivery charge of 50p
        assertEquals(230 + 230 + 460 + 460 + 50, totalCost);
    }

    @Test
    public void testMenusFourB() {
        // The webserver must be running on port 9898 to run this test.
        Menus menus = new Menus("localhost", "9898");
        int totalCost = menus.getDeliveryCost(
                "Flaming tiger latte",
                "Dirty matcha latte",
                "Strawberry matcha latte",
                "Fresh taro latte"
        );
        // Don't forget the standard delivery charge of 50p
        assertEquals(4 * 460 + 50, totalCost);
    }


    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}*/
