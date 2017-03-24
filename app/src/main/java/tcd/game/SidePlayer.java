package tcd.game;

import android.content.Context;
import android.util.Log;

import java.util.Map;

import static java.lang.Math.abs;

/**
 * Created by davide on 24/03/2017.
 */

public class SidePlayer extends Player {

    private static final String TAG = "SidePlayer";

    SidePlayer(Context context, String s,int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        super(context, s, canvasWidth, canvasHeight, mapWidth, mapHeight);

    }

    public void setVelY(int velY) {

        if(this.moving || velY == 1 || deltaY != 0 || this.velY != 0)
            return;

        // I like positive velocities when starting
        this.velY = velY * -1000;
        this.deltaY = 0;
        //this.deltaY = drawHeight*velY*1;

/*
        if(this.velY != velY && !this.moving){
            if(!this.collided){
                this.loops = 0;
            }
            if(velY != 0){
                this.moving = true;
                this.gridUnset = true;
            }
            this.velY = 1;

            //this.deltaX = drawWidth*velX;
            //this.deltaY = drawHeight*velY*1;

            //this.goalX = this.drawBox.left + drawWidth*velX;
            //this.goalY = this.drawBox.top + drawHeight*velY*2;
        }

        return;
*/
        /*if(this.moving || this.velY != 0 || velY != 1)
            return;

        this.moving = true;
        this.gridUnset = true;

        this.velY = 1;*/
    }

    public void move(Map<Integer, Integer> colMap){
        if(moving){
            if((abs(deltaX) <= abs(velX*speed)/* && abs(deltaY) <= abs(velY*speed)*/)) {    //prevents overshoot
                drawBox.offset(deltaX, 0);
                deltaX = 0;
                this.moving = false;
                setVelX(0);
                //setVelY(0);

            } else {

                drawBox.offset(velX*speed, 0);
                deltaX -= velX*speed;
            }
        }

        // negative or positive vel, it always needs to end up at 0.
        // this should allow all cases, including peak of parabola
        if(velY != 0 || deltaY > 0) {
            velY -= 50;

            // freefall limit
            if(velY < -1000)
                velY = -1000;

            /*if (velY < 0 && deltaY <= 0) {
                // if deltaY is < 0 someone changed it outside of this thread, compensate for it:
                // Race condition fixed...
                //if(deltaY < 0)
                //    drawBox.offset(0, -deltaY);
                deltaY = 0;
                velY = 0;
            }*/

            // Somehow localGridX needs to be used because grid is a multiple of 10
            // with an offset depending on init position... Why, I have no idea.
            // Oh, btw, Y is fine, so using gridY for that.
            int localGridX = (this.gridX / 10) + 9;

            int offset = (int) (velY * 0.020); //0.020
            int grid_before = deltaY / drawWidth;
            int grid_after = (deltaY + offset) / drawWidth;
            int grid_diff = grid_after - grid_before;



            //if(deltaY == 0 && offset > 0)
            //    grid_diff++;

//            if(velY == 0 && deltaY > 0)
//                this.gridY++;

            if(offset > drawWidth / 2)
            {
                Log.d(TAG, "FATAL: TOO FAST");
            }

            // Calculate next grid position for Y
            int localGridY = gridY - grid_diff + 0;

            // We can magically punch trough solids, but only in one direction.
            if (velY > 0 || (colMap.get(new Coordinates(localGridX, localGridY).hashCode()) == (null) || colMap.get(new Coordinates(localGridX, localGridY).hashCode()) == this.getID())) {
                Log.d(TAG, "Not colliding " + " X: " + localGridX + " Y: " + localGridY);

                this.gridY -= grid_diff;

                if(grid_diff != 0)
                {
                    Log.d(TAG, "Grid diff: " + grid_diff);
                }

                Log.d(TAG, "Offset: " + (-offset));
                drawBox.offset(0, -offset);
                deltaY += offset;
            }
            else if (velY < 0) {

                // We are now simply going down one pixel at the time until when we hit the solid.
                // Also needs to limit descend ratio to speed set above

                offset = -1;
                grid_after = (deltaY + offset) / drawWidth;
                grid_before = deltaY / drawWidth;
                grid_diff = grid_after - grid_before;
                localGridY = gridY - grid_diff + 0;

                if (colMap.get(new Coordinates(localGridX, localGridY).hashCode()) != (null) && colMap.get(new Coordinates(localGridX, localGridY).hashCode()) != this.getID()) {
                    // Solid hit, stop cycling to here
                    velY = 0;
                    deltaY = 0;

                    Log.d(TAG, "COLLIDED!" + " X: " + localGridX + " Y: " + localGridY);

                    drawBox.offset(0, -drawWidth + 1);
                    //drawBox.offset(0, -drawWidth + 1);
                    //drawBox.offset(0, -drawWidth);
                    //deltaY += -drawWidth;
//                    this.gridY -= grid_diff;

                    //velY = 0;
                    //deltaY = 0;


                } else {
                    Log.d(TAG, "Offset: " + (-offset));

                    drawBox.offset(0, -offset);
                    deltaY += offset;
                    this.gridY -= grid_diff;

                    if(grid_diff != 0)
                    {
                        Log.d(TAG, "Grid diff: " + grid_diff);
                    }

                    Log.d(TAG, "Close to colliding" + " X: " + localGridX + " Y: " + localGridY);

                }

/*
                while(offset < 0) {
                    int local_offset = -1;
                    offset -= local_offset;
                    grid_before = deltaY / drawWidth;
                    grid_after = (deltaY + local_offset) / drawWidth;
                    grid_diff = grid_after - grid_before;
                    localGridY = gridY - grid_diff;

                    if (colMap.get(new Coordinates(localGridX, localGridY).hashCode()) != (null)) {
                        // Solid hit, stop cycling to here
                        velY = 0;
                        deltaY = 0;
                    }

                    this.gridY -= grid_diff;

                    if(velY == 0)
                        return;

                    drawBox.offset(0, -local_offset);

                    deltaY += offset;
                }
*/
                //deltaY += offset;


            }/*
            else
            {
                velY = 0;
            }*/




            int a = 1;
            a++;
        }
    }

    public int update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap) {

        change_velocity();

        //if(gridUnset) {
            /*if((gridX+velX < 0 || gridX+velX >= gridWide) ) {
                this.moving = false;
                deltaX = deltaY = 0;
                this.collided = true;
                setVelX(0);
                setVelY(0);

                Log.d(TAG, "gone off the map");
            } else {*/
                //ignoring collisions for now
                this.collided = false;
                this.gridX += velX;


                //this.gridX += velX;
                //this.gridY += velY;



        //velY -= velY * (0.2);
                /*if(velY == 1)
                    velY == 100;*/
               // this.gridY += velY;

                move(colMap);
            //}

        //}

        return -1; //If no event happened, -1 returned
    }
}
