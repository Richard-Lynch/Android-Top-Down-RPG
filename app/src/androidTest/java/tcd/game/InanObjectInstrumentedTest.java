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
 * Created by david on 10/04/17.
 */

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InanObjectInstrumentedTest {
    InanObject testInanObject;
    Context testContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void t1_constructor_isCorrect() throws Exception {
        testInanObject = new InanObject(testContext, "testString_t1_constuctor_isCorrect", 40, 40, 30, 30);
        // Base methods
        assertEquals(testInanObject.getSpanX(), 0);
        assertEquals(testInanObject.getSpanY(), 0);

        // Inherited methods
        assertEquals(testInanObject.isAlive(), false);
        assertEquals(testInanObject.getHealth(), -1);

        assertEquals(testInanObject.getID(), 1);
    }

    @Test
    public void t2_setSpan_isCorrect() throws Exception {
        testInanObject = new InanObject(testContext, "testString_t2_setSpan_isCorrect", 40 ,40, 30, 30);

        // Test normal data
        testInanObject.setSpan(330, 440);
        assertEquals(testInanObject.getSpanX(), 330);
        assertEquals(testInanObject.getSpanY(), 440);

        // Test negative data
        testInanObject.setSpan(-450, -60);
        assertEquals(testInanObject.getSpanX(), -450);
        assertEquals(testInanObject.getSpanY(), -60);

        // Test null data
        testInanObject.setSpan(0, 0);
        assertEquals(testInanObject.getSpanX(), 0);
        assertEquals(testInanObject.getSpanY(), 0);
    }
}
