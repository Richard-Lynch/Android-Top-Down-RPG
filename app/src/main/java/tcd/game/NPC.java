package tcd.game;


import android.content.Context;

/**
 * Created by stefano on 04/02/17.
 */

public class NPC extends GameObject {




    NPC(Context context, String s,int canvasWidth, int canvasHeight){
        super(context,s, GameObjectTypes.NPC, canvasWidth, canvasHeight);
    }
}