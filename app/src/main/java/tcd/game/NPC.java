package tcd.game;


import android.content.Context;
import android.util.Log;

import java.util.Map;

/**
 * Created by stefano on 04/02/17.
 */

public class NPC extends AnimateObject {

    private int PosIniX;
    private int PosIniY;
    private int PosX;
    private int PosFinalX;
    private int PosFinalY;
    private float PosY;
   // private float slope;
    private int ini=0;
    private int DIRECTION_X = 1;
    private int DIRECTION_Y  = 0;
   // private int delta;
    //private float tmp_float_y;


    NPC(Context context, String s,int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        super(context,s, GameObjectTypes.NPC, canvasWidth, canvasHeight, mapWidth, mapHeight);
    }

    @Override
    public int update(Player[] players, NPC[] npcs, InanObject[] inanObjects, int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap) {
        if(ini==0)
        {
            PosIniX=this.gridX;
            PosIniY=this.gridY;
            PosFinalX=20;
            PosFinalY=-10;
            ini++;
            PosX=PosIniX;
            PosY=PosIniY;
             //slope=(float) ((float)(PosFinalY-PosIniY))/((float)(PosFinalX-PosIniX));
        }
        if(PosX!=PosFinalX || PosY != PosFinalY)
        {
                this.setVelX(this.get_directionX());
                PosX=PosX+this.DIRECTION_X;
                this.setVelY(this.get_directionY());
            PosY=PosY+this.get_directionY();
            }
        if (PosX==PosFinalX) {
            this.swap_directions();
            this.ini++;
        }
        if(this.PosY==this.PosFinalY && this.PosX==this.PosFinalX)
        {
            this.moving=false;
        }

           // this.setVelY(this.get_directionY());
           // PosY= (float) (slope*(PosX-PosIniX))+PosIniY;
            //this.gridY=Math.round(PosY);



        return super.update(players, npcs, inanObjects, id, type, colMap, objMap);
    }

    private int get_directionX()
    {
        if(PosX<=PosFinalX) {
            return this.DIRECTION_X;
        }
        else if(PosX>PosFinalX) {
            return -(this.DIRECTION_X);
        }
        return 0;
    }
    private int get_directionY()
    {
        if (PosY<=PosFinalY)
            return this.DIRECTION_Y;
        else if(PosY>PosFinalY)
        {
            return -(this.DIRECTION_Y);
        }
        return 0;
    }
    private void swap_directions()
    {
        this.DIRECTION_Y=1;
        this.DIRECTION_X=0;
    }
}