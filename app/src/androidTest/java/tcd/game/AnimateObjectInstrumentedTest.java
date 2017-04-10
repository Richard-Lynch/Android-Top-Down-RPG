package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
/**
 * Created by david on 27/03/17.
 */

public class AnimateObjectInstrumentedTest {
    // Test Constructor
    Context testContext;
    AnimateObject testAnimateObject;

    @Test
    public void constructor_isCorrect() throws Exception {
        testAnimateObject = new AnimateObject(testContext, "testString", GameObject.GameObjectTypes.NPC, 40, 40, 30, 30);

        assertEquals(testAnimateObject.getHealth(), 100);
        assertEquals(testAnimateObject.getSkill(), 1);
        assertEquals(testAnimateObject.getStrength(), 10);
    }

    @Test
    public void setHealth_isCorrect() throws Exception {
        testAnimateObject = new AnimateObject(testContext, "testString", GameObject.GameObjectTypes.NPC, 40, 40, 30, 30);

        // Test normal data
        testAnimateObject.setHealth(10);
        assertEquals(testAnimateObject.getHealth(), 10);

        // Test negative data
        testAnimateObject.setHealth(-10);
        assertEquals(testAnimateObject.getHealth(), -10);

        // Test null data
        testAnimateObject.setHealth(0);
        assertEquals(testAnimateObject.getHealth(), 0);
    }

    @Test
    public void setSkill_isCorrect() throws Exception {
        testAnimateObject = new AnimateObject(testContext, "testString", GameObject.GameObjectTypes.NPC, 40, 40, 30, 30);

        // Test normal data
        testAnimateObject.setSkill(10);
        assertEquals(testAnimateObject.getSkill(), 10);

        // Test negative data -- Issue: Should negative skill value be allowed?
        testAnimateObject.setSkill(-10);
        assertEquals(testAnimateObject.getSkill(), -10);

        // Test null data -- Issue: Should null skill value be allowed?
        testAnimateObject.setSkill(0);
        assertEquals(testAnimateObject.getSkill(), 0);
    }

    @Test
    public void setStrength_isCorrect() throws Exception {
        testAnimateObject = new AnimateObject(testContext, "testString", GameObject.GameObjectTypes.NPC, 40, 40, 30, 30);

        // Test normal data
        testAnimateObject.setStrength(10);
        assertEquals(testAnimateObject.getStrength(), 10);

        // Test negative data -- Issue: Should negative strength value be allowed?
        testAnimateObject.setStrength(-10);
        assertEquals(testAnimateObject.getStrength(), -10);

        // Test null data -- Issue: Should null strength value be allowed?
        testAnimateObject.setStrength(0);
        assertEquals(testAnimateObject.getStrength(), 0);
    }
}
