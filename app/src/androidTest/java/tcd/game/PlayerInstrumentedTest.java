package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;
/**
 * Created by david on 01/04/17.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlayerInstrumentedTest {
    Player testPlayer;
    Context testContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void t1_constructor_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        assertEquals(testPlayer.getHealth(), 100);
        assertEquals(testPlayer.getSkill(), 1);
        assertEquals(testPlayer.getStrength(), 10);

        // Testing initial values of attributes
        assertEquals(testPlayer.isA_pressed(), false);
        assertEquals(testPlayer.isB_pressed(), false);
        assertEquals(testPlayer.isUp_pressed(), false);
        assertEquals(testPlayer.isDown_pressed(), false);
        assertEquals(testPlayer.isRight_pressed(), false);
        assertEquals(testPlayer.isLeft_pressed(), false);
    }

    @Test
    public void t2_setHealth_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        // Test normal data
        testPlayer.setHealth(10);
        assertEquals(testPlayer.getHealth(), 10);

        // Test negative data
        testPlayer.setHealth(-10);
        assertEquals(testPlayer.getHealth(), -10);

        // Test null data
        testPlayer.setHealth(0);
        assertEquals(testPlayer.getHealth(), 0);
    }

    @Test
    public void t3_setSkill_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        // Test normal data
        testPlayer.setSkill(10);
        assertEquals(testPlayer.getSkill(), 10);

        // Test negative data -- Issue: Should negative skill value be allowed?
        testPlayer.setSkill(-10);
        assertEquals(testPlayer.getSkill(), -10);

        // Test null data -- Issue: Should null skill value be allowed?
        testPlayer.setSkill(0);
        assertEquals(testPlayer.getSkill(), 0);
    }

    @Test
    public void t4_setStrength_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        // Test normal data
        testPlayer.setStrength(10);
        assertEquals(testPlayer.getStrength(), 10);

        // Test negative data -- Issue: Should negative strength value be allowed?
        testPlayer.setStrength(-10);
        assertEquals(testPlayer.getStrength(), -10);

        // Test null data -- Issue: Should null strength value be allowed?
        testPlayer.setStrength(0);
        assertEquals(testPlayer.getStrength(), 0);
    }

    @Test
    public void t5_setAPressed_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setA_pressed(true);
        assertEquals(testPlayer.isA_pressed(), true);
        testPlayer.setA_pressed(false);
        assertEquals(testPlayer.isA_pressed(), false);
    }

    @Test
    public void t6_setBPressed_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setB_pressed(true);
        assertEquals(testPlayer.isB_pressed(), true);
        testPlayer.setB_pressed(false);
        assertEquals(testPlayer.isB_pressed(), false);
    }

    @Test
    public void t7_setUpPressed_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setUp_pressed(true);
        assertEquals(testPlayer.isUp_pressed(), true);
        testPlayer.setUp_pressed(false);
        assertEquals(testPlayer.isUp_pressed(), false);
    }

    @Test
    public void t8_setDownPressed_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setDown_pressed(true);
        assertEquals(testPlayer.isDown_pressed(), true);
        testPlayer.setDown_pressed(false);
        assertEquals(testPlayer.isDown_pressed(), false);
    }

    @Test
    public void t9_setLeftPressed_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setLeft_pressed(true);
        assertEquals(testPlayer.isLeft_pressed(), true);
        testPlayer.setLeft_pressed(false);
        assertEquals(testPlayer.isLeft_pressed(), false);
    }

    @Test
    public void t10_setRightPressed_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setRight_pressed(true);
        assertEquals(testPlayer.isRight_pressed(), true);
        testPlayer.setRight_pressed(false);
        assertEquals(testPlayer.isRight_pressed(), false);
    }

    @Test
    public void t11_setAllButFalse_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setA_pressed(true);
        testPlayer.setB_pressed(true);

        testPlayer.setAllButFalse();

        assertEquals(testPlayer.isA_pressed(), false);
        assertEquals(testPlayer.isB_pressed(), false);
    }

    @Test
    public void t12_setAllVelFalse_isCorrect() throws Exception {
        testPlayer = new Player(testContext, "testString", 40, 40, 30, 30);

        testPlayer.setUp_pressed(true);
        testPlayer.setDown_pressed(true);
        testPlayer.setLeft_pressed(true);
        testPlayer.setRight_pressed(true);

        testPlayer.setAllVelFalse();

        assertEquals(testPlayer.isUp_pressed(), false);
        assertEquals(testPlayer.isDown_pressed(), false);
        assertEquals(testPlayer.isLeft_pressed(), false);
        assertEquals(testPlayer.isRight_pressed(), false);
    }
}
