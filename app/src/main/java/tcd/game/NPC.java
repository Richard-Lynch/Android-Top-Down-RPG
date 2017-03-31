package tcd.game;


import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
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
    private List<Coordinates> coordinates=new ArrayList<Coordinates>();
    private int numInList=0;
   // private int delta;
    //private float tmp_float_y;


    NPC(Context context, String s,int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        super(context,s, GameObjectTypes.NPC, canvasWidth, canvasHeight, mapWidth, mapHeight);
        Coordinates c1,c2,c3;
        c1=new Coordinates(100,-15);
        c2=new Coordinates(120,-25);
        c3=new Coordinates(130,-35);

        this.coordinates.add(c1);
        this.coordinates.add(c2);
        this.coordinates.add(c3);
    }



    @Override
    public int update(Player[] players, NPC[] npcs, InanObject[] inanObjects, int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap) {

        if(ini==0)
        {   Coordinates coordinate;
            coordinate=coordinates.get(this.numInList);
            if(this.numInList==0) {
                PosIniX = this.gridX;
                PosIniY = this.gridY;
            }
            else{
                PosIniX=coordinates.get(this.numInList-1).getX();
                PosIniY=coordinates.get(this.numInList-1).getY();
            }
            PosFinalX=coordinate.getX();
            PosFinalY=coordinate.getY();
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
        if (PosX==PosFinalX && PosY!=PosFinalY && this.ini==1) {
            this.swap_directions();
            this.ini++;
        }
        if(this.PosY==this.PosFinalY && this.PosX==this.PosFinalX)
        {
            if(coordinates.size()-1==this.numInList)
                this.moving=false;
            else{
                this.numInList=this.numInList+1;
                this.ini=0;
                this.swap_directions();
            }

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
    {   int tmp;
        tmp=this.DIRECTION_Y;
        this.DIRECTION_Y=this.DIRECTION_X;
        this.DIRECTION_X=tmp;
    }
}