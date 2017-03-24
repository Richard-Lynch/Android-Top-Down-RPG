package tcd.game;


import android.content.Context;

/**
 * Created by stefano on 04/02/17.
 */

public class NPC extends AnimateObject {


    NPC(Context context, String s,int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        super(context,s, GameObjectTypes.NPC, canvasWidth, canvasHeight, mapWidth, mapHeight);
    }

    public void setVelY(int velY) {

        if(this.velY != velY && !this.moving){
            if(!this.collided){
                this.loops = 0;
            }
            if(velY != 0){
                this.moving = true;
                this.gridUnset = true;
            }
            this.velY = velY;

            this.deltaX = drawWidth*velX;
            this.deltaY = drawHeight*velY;

            this.goalX = this.drawBox.left + drawWidth*velX;
            this.goalY = this.drawBox.top + drawHeight*velY;
        }
    }
}