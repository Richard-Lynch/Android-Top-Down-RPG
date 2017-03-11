package tcd.game;

import android.content.Context;
import android.graphics.BitmapFactory;

import java.util.Map;

/**
 * Created by stefano on 04/02/17.
 */

public class InanObject extends GameObject {


    InanObject(Context context, String s, int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        super(context,s, GameObjectTypes.INANOBJECT, canvasWidth, canvasHeight, mapWidth, mapHeight);
        //setSprite(BitmapFactory.decodeResource(context.getResources(),R.drawable.inan_default));
    }

    @Override
    public int update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap){
        return super.update(players, npcs, inanObjects, id, type, colMap, objMap);
    }

}
