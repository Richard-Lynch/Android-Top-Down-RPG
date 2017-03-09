package tcd.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.Map;

/**
 * Created by Richard on 09/02/2017.
 */

// Edited by Rowan 5th March
// changed drawFrame method and edited spritesheet so that we don't have to shuffle that one frame back + forth



public class AnimateObject extends GameObject{

    private static final String TAG = "Player";
    AnimateObject(Context context, String s, GameObjectTypes type, int canvasWidth, int canvasHeight){
        super(context,s, type ,canvasWidth, canvasHeight);
        health = 100;
        skill = 1;
        strength = 10;
        //TODO: Adjust player velocities properly (aspect ratio of device?)

    }

    @Override
    public void update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap) {

//        super.update(players, npcs, inanObjects, id, type, colMap, objMap);
        //if we've waited long enough for an update
        loops--;
        if(loops <= 0){
//            animationSpeed = 10;//reset the animation speed to default ( we may need a different speed for walking/blinking
            //if we're moving set the corresponding row and also set the "facing" value, which is used when we become stationary
            if(velX == 1){
                animationRowIndex = GameObjectAnimationDirection.MOVING_RIGHT.ordinal();//move to the correct row ( animation )
                facing = GameObjectAnimationDirection.FACING_RIGHT; //set facing direction
                maxAnimationColIndex = GameObjectAnimationDirection.MOVING_RIGHT.getVal();  //set the number of sprites in the animation
            } else if(velX == -1){
                animationSpeed = 40;
                animationRowIndex = GameObjectAnimationDirection.MOVING_LEFT.ordinal();
                facing = GameObjectAnimationDirection.FACING_LEFT;
                maxAnimationColIndex = GameObjectAnimationDirection.MOVING_LEFT.getVal();
            }else if(velY == 1){
                animationSpeed = 40;
                animationRowIndex = GameObjectAnimationDirection.MOVING_DOWN.ordinal();
                facing = GameObjectAnimationDirection.FACING_DOWN;
                maxAnimationColIndex = GameObjectAnimationDirection.MOVING_DOWN.getVal();
            } else if(velY == -1){
                animationRowIndex = GameObjectAnimationDirection.MOVING_UP.ordinal();
                facing = GameObjectAnimationDirection.FACING_UP;
                maxAnimationColIndex = GameObjectAnimationDirection.MOVING_UP.getVal();
            }
            //if we're not moving we're just chillin and blinking and stuff
            else if(facing == GameObjectAnimationDirection.FACING_RIGHT){
                animationRowIndex = facing.ordinal();//use the facing direction to set the animation row
                maxAnimationColIndex = GameObjectAnimationDirection.FACING_RIGHT.getVal();  //get the number of sprites in that row
            } else if(facing == GameObjectAnimationDirection.FACING_LEFT){
                animationRowIndex = facing.ordinal();
                maxAnimationColIndex = GameObjectAnimationDirection.FACING_LEFT.getVal();
            } else if(facing == GameObjectAnimationDirection.FACING_DOWN){
                animationRowIndex = facing.ordinal();
                maxAnimationColIndex = GameObjectAnimationDirection.FACING_DOWN.getVal();
            } else if(facing == GameObjectAnimationDirection.FACING_UP){
                animationRowIndex = facing.ordinal();
                maxAnimationColIndex = GameObjectAnimationDirection.FACING_UP.getVal();
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
        super.update(players, npcs, inanObjects, id, type, colMap, objMap);

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

    @Override
    public void drawFrame(Canvas canvas){
        canvas.drawBitmap( //Draw the sprite to the canvas
                dividedSpriteMap[animationRowIndex][animationColIndex], null,
                drawBox,
                null
        );
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

}
