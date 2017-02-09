package tcd.game;

import android.content.Context;
import android.graphics.BitmapFactory;

/**
 * Created by stefano on 04/02/17.
 */

public class InanObject extends GameObject {


    InanObject(Context context, String s, int canvasWidth, int canvasHeight){
        super(context,s, GameObjectTypes.INANOBJECT, canvasWidth, canvasHeight);
        setSprite(BitmapFactory.decodeResource(context.getResources(),R.drawable.inan_default));
    }

    @Override
    public void update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type){

    }

}
