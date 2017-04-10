package tcd.game;

/**
 * Created by david on 07/04/17.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


public class MonsterInstrumentedTest {
    private Monster testMonster;
    private Context testContext = InstrumentationRegistry.getTargetContext();
    private Bitmap  testSprite;

    @Test
    public void constructor_rectangle_isCorrect() throws Exception {
        //assertEquals(testMonster.getBitmap(), BitmapFactory.decodeResource(testContext.getResources(),R.drawable.truck))

        // Test rectangle with normal data
        testMonster = new Monster(testContext, 20, 30);
        assertEquals(testMonster.getRect(), new Rect(20,30,20+50,30+50));

        // Test rectangle with negative data
        testMonster = new Monster(testContext, -20, -30);
        assertEquals(testMonster.getRect(), new Rect(-20,-30,-20+50,-30+50));

        // Test rectangle with null data
        testMonster = new Monster(testContext, 0, 0);
        assertEquals(testMonster.getRect(), new Rect(0,0,0+50,0+50));
    }

    @Test
    public void constructor_sprite_isCorrect() throws Exception {
        // This test fails

        // Test sprite loading
        testMonster = new Monster(testContext, 20, 20);
        testSprite = BitmapFactory.decodeResource(testContext.getResources(),R.drawable.truck);
        assertEquals(testMonster.getBitmap(), testSprite);
    }

}
