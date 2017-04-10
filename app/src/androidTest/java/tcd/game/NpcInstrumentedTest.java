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
public class NpcInstrumentedTest {
    NPC testNPC;
    Context testContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void t1_constructor_isCorrect() throws Exception {
        testNPC = new NPC(testContext, "testString_t1_constuctor_isCorrect", 40, 40, 30, 30);

        // Inherited methods
        assertEquals(testNPC.isAlive(), true);
        assertEquals(testNPC.getHealth(), 100);

        assertEquals(testNPC.getID(), 1);
    }
}
