package tcd.game;


import android.content.Context;

/**
 * Created by stefano on 04/02/17.
 */

public class NPC extends GameObject {




    NPC(Context context, String s,int canvasWidth, int canvasHeight){
        super(context,s, GameObjectTypes.NPC, canvasWidth, canvasHeight);

    }

    @Override
    public void update(Player[] players, NPC[] npcs, InanObject[] inanObjects, int id, GameObjectTypes type) {
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
}