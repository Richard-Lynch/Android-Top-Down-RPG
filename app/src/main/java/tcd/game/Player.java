package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by stefano on 04/02/17.
 */

public class Player extends GameObject {


    Player(Context context, String s,int canvasWidth, int canvasHeight){
        super(context,s, GameObjectTypes.PLAYER ,canvasWidth, canvasHeight);
    }


}
