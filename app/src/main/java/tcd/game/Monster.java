package tcd.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.Image;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Example Class to see how it ties in to GameFragment
 */

public class Monster {

    private static final String TAG = "Monster";
    private Rect rect;
    private int width,height,speedX,speedY;
    private Bitmap sprite;
    private Context context;

    /**
     *
     * @param ctx Application Context to access Resources
     * @param startX stating x position
     * @param startY starting y  position
     */
    Monster(Context ctx,int startX,int startY){
        this.context = ctx;
        this.speedX = 10;
        this.speedY = 10;
        this.width = 50;
        this.height = 50;
        this.rect = new Rect(startX,startY,startX+width,startY+height);
        loadSprite();
    }

    private void loadSprite(){
        sprite = BitmapFactory.decodeResource(context.getResources(),R.drawable.truck);
    }

    public void update(){
        if(rect.left > 600){
            rect.left = 0;
            rect.right = width;
        } else {
            rect.left += speedX;
            //rect.top += 10;
            rect.right = rect.left + width;
            //rect.bottom = rect.top + height;
        }
    }

    public Bitmap getBitmap(){
        return sprite;
    }

    public Rect getRect(){      //TODO: LOL @ method name
        return rect;
    }

    public void drawFrame(Canvas canvas){
        Rect srcRect = new Rect(0,0,50,60);
        canvas.drawBitmap(sprite,srcRect,rect,null);
    }
}
