package tcd.game;


import android.content.Context;

/**
 * Created by stefano on 04/02/17.
 */

public class NPC extends AnimateObject {




    NPC(Context context, String s,int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        super(context,s, GameObjectTypes.NPC, canvasWidth, canvasHeight, mapWidth, mapHeight);

    }

}