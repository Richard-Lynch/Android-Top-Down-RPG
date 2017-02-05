package tcd.game;

import android.content.Context;

/**
 * Created by stefano on 04/02/17.
 */

public class InanObject extends GameObject {


    InanObject(Context context, String s, int canvasWidth, int canvasHeight){
        super(context,s, GameObjectTypes.INANOBJECT, canvasWidth, canvasHeight);
    }
}
