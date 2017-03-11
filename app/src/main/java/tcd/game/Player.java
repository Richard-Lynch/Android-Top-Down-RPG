package tcd.game;

import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Created by stefano on 04/02/17.
 */

public class Player extends AnimateObject {

    private static final String TAG = "Player";
    //Flags
    private boolean up_pressed = false;
    private boolean down_pressed = false;
    private boolean left_pressed = false;
    private boolean right_pressed = false;

    //TODO decide what these do
    private boolean A_pressed = false;
    private boolean B_pressed = false;

    //Stats
    private int health;
    private int skill;
    private int strength;

    //constructor
    Player(Context context, String s,int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        super(context,s, GameObjectTypes.PLAYER ,canvasWidth, canvasHeight, mapWidth, mapHeight);
        //TODO: Adjust player velocities properly (aspect ratio of device?)
    }

    //add change velocity to update and call super
    @Override
    public void update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap){
        change_velocity();
        super.update(players, npcs, inanObjects, id, type,colMap,objMap);
    }

    //change the velocity based on button presses
    private void change_velocity(){
        int number_of_presses = press_check();
        if(number_of_presses > 1 || number_of_presses == 0)  {
            this.setVelY(0);
            this.setVelX(0);
            setAllVelFalse();
        }
        else {
            if (right_pressed && velX !=1) {
                Log.d(TAG,"Right");
                this.setVelX(1);
                this.setVelY(0);
            }
            if (left_pressed && velX !=-1) {
                Log.d(TAG,"Left");
                this.setVelX(-1);
                this.setVelY(0);
            }
            if (down_pressed && velY !=1) {
                Log.d(TAG,"Down");
                this.setVelY(1);
                this.setVelX(0);
            }
            if (up_pressed && velY !=-1) {
                Log.d(TAG,"Up");
                this.setVelY(-1);
                this.setVelX(0);
            }
        }
    }
    //checks for double press
    private int press_check(){
        int counter = 0;

        if(up_pressed)
            counter++;
        if(down_pressed)
            counter++;
        if(right_pressed)
            counter++;
        if(left_pressed)
            counter++;

        return counter;
    }
    //on a double press, clear all flags
    public void setAllVelFalse(){
        down_pressed = false;
        left_pressed = false;
        right_pressed = false;
        up_pressed = false;
    }

    //Setters and getters for flags
    public boolean isUp_pressed() {
        return up_pressed;
    }
    public void setUp_pressed(boolean up_pressed) {
        this.up_pressed = up_pressed;
    }
    public boolean isDown_pressed() {
        return down_pressed;
    }
    public void setDown_pressed(boolean down_pressed) {
        this.down_pressed = down_pressed;
    }
    public boolean isLeft_pressed() {
        return left_pressed;
    }
    public void setLeft_pressed(boolean left_pressed) {
        this.left_pressed = left_pressed;
    }
    public boolean isRight_pressed() {
        return right_pressed;
    }
    public void setRight_pressed(boolean right_pressed) {
        this.right_pressed = right_pressed;
    }
    public boolean isA_pressed() {
        return A_pressed;
    }
    public void setA_pressed(boolean a_pressed) {
        A_pressed = a_pressed;
    }
    public boolean isB_pressed() {
        return B_pressed;
    }
    public void setB_pressed(boolean b_pressed) {
        B_pressed = b_pressed;
    }
}
