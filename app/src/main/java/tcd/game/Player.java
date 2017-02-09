package tcd.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
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
        //if we've waited long enough for an update
        loops--;
        if(loops <= 0){
            animationSpeed = 10;//reset the animation speed to default ( we may need a different speed for walking/blinking
            //if we're moving set the corresponding row and also set the "facing" value, which is used when we become stationary
            if(velX == 1){
                animationRowIndex = GameObjectAnimationDirection.MOVING_RIGHT.ordinal();//move to the correct row ( animation )
                facing = GameObjectAnimationDirection.FACING_RIGHT; //set facing direction
                maxAnimationColIndex = GameObjectAnimationMaxIndex.MOVING_RIGHT.ordinal();  //set the number of sprites in the animation
            } else if(velX == -1){
                animationSpeed = 10;
                animationRowIndex = GameObjectAnimationDirection.MOVING_LEFT.ordinal();
                facing = GameObjectAnimationDirection.FACING_LEFT;
                maxAnimationColIndex = GameObjectAnimationMaxIndex.MOVING_LEFT.ordinal();
            }else if(velY == 1){
                animationSpeed = 10;
                animationRowIndex = GameObjectAnimationDirection.MOVING_DOWN.ordinal();
                facing = GameObjectAnimationDirection.FACING_DOWN;
                maxAnimationColIndex = GameObjectAnimationMaxIndex.MOVING_DOWN.ordinal();
            } else if(velY == -1){
                animationRowIndex = GameObjectAnimationDirection.MOVING_UP.ordinal();
                facing = GameObjectAnimationDirection.FACING_UP;
                maxAnimationColIndex = GameObjectAnimationMaxIndex.MOVING_UP.ordinal();
            }
            //if we're not moving we're just chillin and blinking and stuff
            else if(facing == GameObjectAnimationDirection.FACING_RIGHT){
                animationRowIndex = facing.ordinal();//use the facing direction to set the animation row
                maxAnimationColIndex = GameObjectAnimationMaxIndex.FACING_RIGHT.ordinal();  //get the number of sprites in that row
            } else if(facing == GameObjectAnimationDirection.FACING_LEFT){
                animationRowIndex = facing.ordinal();
                maxAnimationColIndex = GameObjectAnimationMaxIndex.FACING_LEFT.ordinal();
            } else if(facing == GameObjectAnimationDirection.FACING_DOWN){
                animationRowIndex = facing.ordinal();
                maxAnimationColIndex = GameObjectAnimationMaxIndex.FACING_DOWN.ordinal();
            } else if(facing == GameObjectAnimationDirection.FACING_UP){
                animationRowIndex = facing.ordinal();
                maxAnimationColIndex = GameObjectAnimationMaxIndex.FACING_UP.ordinal();
            }

            //below we increment the animationColIndex which is the current sprite


            //does nice slow blinking cus we're awesome as heck
            //if we're blinking it will stop iterating for a while on the open eye sprite
            if(animationRowIndex < 4 && animationColIndex == 0){
                blink -=1;
                if(blink <= 0){
                    blink = blink_speed;
                    animationColIndex++;
                }
            }else{  //otherwise it'l iterate to the next sprite at the normal speed
                animationColIndex++;
            }

            //loops back to the start of the animation
            if(animationColIndex >= maxAnimationColIndex){
                animationColIndex = 0;
            }
            //resets animation timer ( loop )
            loops = animationSpeed;
        }
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
            if (isRight_pressed() && velX !=1) {
                Log.d(TAG,"Right");
                this.setVelX(1);
                this.setVelY(0);
            }
            if (isLeft_pressed() && velX !=-1) {
                Log.d(TAG,"Left");
                this.setVelX(-1);
                this.setVelY(0);
            }
            if (isDown_pressed() && velY !=1) {
                Log.d(TAG,"Down");
                this.setVelY(1);
                this.setVelX(0);
            }
            if (isUp_pressed() && velY !=-1) {
                Log.d(TAG,"Up");
                this.setVelY(-1);
                this.setVelX(0);
            }
        }
    }
    @Override
    public void drawFrame(Canvas canvas){
//        Rect temp = new Rect(
//                (animationColIndex * cropWidth) + 25,
//                (animationRowIndex * cropHeight) + 25,
//                (animationColIndex * cropWidth) + cropWidth -25,
//                (animationRowIndex * cropHeight) + cropHeight - 15
//        );

        if(animationRowIndex == 3 && animationColIndex == 1) {
            drawBox.left += 4;
            drawBox.right += 4;
        }

        canvas.drawBitmap(
                spriteMap,
                new Rect(
                        (animationColIndex * cropWidth) + 25,
                        (animationRowIndex * cropHeight) + 25,
                        (animationColIndex * cropWidth) + cropWidth -25,
                        (animationRowIndex * cropHeight) + cropHeight - 15
                ),
                drawBox,
                null
        );

        if(animationRowIndex == 3 && animationColIndex == 1) {
            drawBox.left -= 4;
            drawBox.right -= 4;
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
