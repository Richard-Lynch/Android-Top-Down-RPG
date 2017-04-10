package tcd.game;
/**
 * Created by david on 10/04/17.
 */
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameObjectInstrumentedTest {
    GameObject testGameObject;
    Context testContext = InstrumentationRegistry.getTargetContext();

    // testGameObject = new GameObject();
    // ISSUE: No way to access attributes set by default constructor
    // ISSUE: default constructor does not update the total number of game objects
    // RELATED ISSUE: Child classes of GameObject do not update the total number of game objects

    @Test
    public void t1_constructor_PLAYER_isCorrect() throws Exception {
        // Test regular constructor for PLAYER
        testGameObject = new GameObject(testContext, "testName", GameObject.GameObjectTypes.PLAYER, 40, 40, 30, 30);
        assertEquals(testGameObject.isAlive(), true);
        assertEquals(testGameObject.getHealth(), 100);

        assertEquals(testGameObject.getID(), 1);    // This test will need to be updated if default constructor issues are fixed
    }

    @Test
    public void t2_constructor_INANOBJECT_isCorrect() throws Exception {
        // Test regular constructor for PLAYER
        testGameObject = new GameObject(testContext, "testName", GameObject.GameObjectTypes.INANOBJECT, 40, 40, 30, 30);
        assertEquals(testGameObject.isAlive(), false);
        assertEquals(testGameObject.getHealth(), -1);

        assertEquals(testGameObject.getID(), 2);    // This test will need to be updated if default constructor issues are fixed
    }

    @Test
    public void t3_constructor_NPC_isCorrect() throws Exception {
        // Test regular constructor for PLAYER
        testGameObject = new GameObject(testContext, "testName", GameObject.GameObjectTypes.NPC, 40, 40, 30, 30);
        assertEquals(testGameObject.isAlive(), true);
        assertEquals(testGameObject.getHealth(), 100);

        assertEquals(testGameObject.getID(), 3);    // This test will need to be updated if default constructor issues are fixed
    }
}

/*
public int getAnimationRowIndex(){
        return animationRowIndex;
    }

    public int getAnimationColIndex(){
        return animationColIndex;
    }

    public int getID(){
        return this.id;
    }

    public int getHealth() {
        return health;
    }
    public boolean isAlive() {return IsAlive;}
 */