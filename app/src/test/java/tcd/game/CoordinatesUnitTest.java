package tcd.game;
/**
 * Created by david on 06/04/17.
 */

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoordinatesUnitTest {
    private Coordinates testCoordinates;

    @Test
    public void constructor_isCorrect() throws Exception {
        // Test normal data
        testCoordinates = new Coordinates(24, 55);
        assertEquals(testCoordinates.getX(), 24);
        assertEquals(testCoordinates.getY(), 55);

        // Test null data
        testCoordinates = new Coordinates(0, 0);
        assertEquals(testCoordinates.getX(), 0);
        assertEquals(testCoordinates.getY(), 0);

        // Test negative data
        testCoordinates = new Coordinates(-3, -4);
        assertEquals(testCoordinates.getX(), -3);
        assertEquals(testCoordinates.getY(), -4);
    }

    @Test
    public void set_isCorrect() throws Exception {
        testCoordinates = new Coordinates(1, 1);

        // Test normal data
        testCoordinates.setX(4);
        assertEquals(testCoordinates.getX(), 4);
        testCoordinates.setY(4);
        assertEquals(testCoordinates.getY(), 4);

        // Test null data
        testCoordinates.setX(0);
        assertEquals(testCoordinates.getX(), 0);
        testCoordinates.setY(0);
        assertEquals(testCoordinates.getY(), 0);

        // Test negative data
        testCoordinates.setX(-55);
        assertEquals(testCoordinates.getX(), -55);
        testCoordinates.setY(-66);
        assertEquals(testCoordinates.getY(), -66);
    }

    @Test
    public void equals_isCorrect() throws Exception {
        Coordinates SecondTestCoordinates = new Coordinates(1, 1);
        testCoordinates = new Coordinates(1, 1);

        // Test normal data

        SecondTestCoordinates.setX(5);
        SecondTestCoordinates.setY(88);

        testCoordinates.setX(5);
        testCoordinates.setY(88);
        assertEquals(testCoordinates.equals(SecondTestCoordinates), true);

        testCoordinates.setX(99);
        testCoordinates.setY(2123);
        assertEquals(testCoordinates.equals(SecondTestCoordinates), false);

        // Test null data

        SecondTestCoordinates.setX(0);
        SecondTestCoordinates.setY(0);

        testCoordinates.setX(0);
        testCoordinates.setY(0);
        assertEquals(testCoordinates.equals(SecondTestCoordinates), true);

        testCoordinates.setX(99);
        testCoordinates.setY(2123);
        assertEquals(testCoordinates.equals(SecondTestCoordinates), false);

        // Test negative data

        SecondTestCoordinates.setX(-213);
        SecondTestCoordinates.setY(-123);

        testCoordinates.setX(-213);
        testCoordinates.setY(-123);
        assertEquals(testCoordinates.equals(SecondTestCoordinates), true);

        testCoordinates.setX(99);
        testCoordinates.setY(2123);
        assertEquals(testCoordinates.equals(SecondTestCoordinates), false);
    }

    @Test
    public void hashCode_isCorrect() {
        testCoordinates = new Coordinates(4, 4);

        // Test normal data
        testCoordinates.setX(4);
        testCoordinates.setY(5);
        assertEquals(testCoordinates.hashCode(), 405);

        // Test null data
        testCoordinates.setX(0);
        testCoordinates.setY(0);
        assertEquals(testCoordinates.hashCode(), 0);

        // Test negative data
        testCoordinates.setX(-1);
        testCoordinates.setY(-44);
        assertEquals(testCoordinates.hashCode(), -144);
    }
}
