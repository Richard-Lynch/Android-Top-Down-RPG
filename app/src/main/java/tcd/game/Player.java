package tcd.game;

import android.content.Context;
import android.util.Log;

/**
 * Created by stefano on 04/02/17.
 */

public class Player extends GameObject {

    private static final String TAG = "Player";
    Player(Context context, String s,int canvasWidth, int canvasHeight){
        super(context,s, GameObjectTypes.PLAYER ,canvasWidth, canvasHeight);
        health = 100;
        skill = 1;
        strength = 10;
        //TODO: Adjust player velocities properly (aspect ratio of device?)

    }

    @Override
    public void update(Player[] players, NPC[] npcs, InanObject[] inanObjects, int id, GameObjectTypes type) {

        change_velocity();
        super.update(players, npcs, inanObjects, id, type);
    }

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

    public void setAllVelFalse(){
        down_pressed = false;
        left_pressed = false;
        right_pressed = false;
        up_pressed = false;
    }

    private int press_check(){
        int counter = 0;

        if(isUp_pressed())
            counter++;
        if(isDown_pressed())
            counter++;
        if(isRight_pressed())
            counter++;
        if(isLeft_pressed())
            counter++;

        return counter;
    }

    private void change_velocity(){
        int number_of_presses = press_check();
        if(number_of_presses > 1 || number_of_presses == 0)  {
            this.setVelY(0);
            this.setVelX(0);
            setAllVelFalse();
        }
        else {
            if (isRight_pressed()) {
                Log.d(TAG,"Right");
                this.setVelX(1);
                this.setVelY(0);
            }
            if (isLeft_pressed()) {
                Log.d(TAG,"Left");
                this.setVelX(-1);
                this.setVelY(0);
            }
            if (isDown_pressed()) {
                Log.d(TAG,"Down");
                this.setVelY(1);
                this.setVelX(0);
            }
            if (isUp_pressed()) {
                Log.d(TAG,"Up");
                this.setVelY(-1);
                this.setVelX(0);
            }
        }

    }
    //Setters
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
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
