package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;


/**
 * Created by stefano on 04/02/17.
 */

public class GameObject {
    private String TAG = "GameObject";
    private static int totalGameObjs = 0;
    private int id;
    private String name;
    private int posX,posY,velX,velY;
    private Rect collisionBox;
    private Rect cropBox;
    private Rect drawBox;
    private boolean collisionOn;
    private boolean movable;
    private Bitmap spriteMap;
    private Context context;

    private int colWidth, colHeight, cropWidth, cropHeight, drawWidth,drawHeight;
    private int animationModeIndex, animationLoopIndex;
    // loop = width multiple for crop
    // mode = height ""
    private int canvasWidth, canvasHeight;


    GameObject(){
        id = totalGameObjs;
        totalGameObjs ++;
        name = "un-named";

    }


    GameObject(Context context, String s, int canvasWidth, int canvasHeight){
        Log.d(TAG,"Gameobj constructor");
        this.id = totalGameObjs;
        totalGameObjs++;
        name = s;
        this.context = context;
        Log.d(TAG,"used context");

        spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.truck);
        collisionBox = new Rect(0,0,60,60);
        drawBox = collisionBox;
        cropBox = null;
        animationLoopIndex = 0;
        animationModeIndex = 0;
        posX = 0;
        posY = 0;
        velX = 0;
        velY = 0;
        drawWidth = 60;
        drawHeight = 60;
        cropWidth = 60;
        cropHeight = 60;
        colWidth = 60;
        colHeight = 60;

        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        Log.d(TAG,"fin Gameobj constructor");
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    private void move(){
        this.posX += this.velX;
        this.posY += this.velY;
    }

    private void unmove(){
        this.posX -= this.velX;
        this.posY -= this.velY;
    }


    public void update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type){

        //TODO: Update animation draw frame if required

        move();
        if(posX  >= canvasWidth){
            posX = 0;
        }else{return;}

        for(int i=0;i<players.length;i++){
            if(this.collision(players[i])){
                if(players[i].getID() != this.getID()){
                    unmove();
                    return;
                }
            }
        }

        for(int i=0;i<npcs.length;i++){
            if(this.collision(npcs[i])){
                if(npcs[i].getID() != this.getID()){
                    unmove();
                    return;
                }
            }
        }

        for(int i=0;i<inanObjects.length;i++){
            if(this.collision(inanObjects[i])){
                if(inanObjects[i].getID() != this.getID()){
                    unmove();
                    return;
                }
            }
        }

    }

    private boolean collision(GameObject gameObject){
        return false;
    }

    public void drawFrame(Canvas canvas){
        canvasHeight = canvas.getHeight();
        canvasWidth = canvas.getWidth();
//        canvas.drawBitmap(spriteMap,
//                null,
//                new Rect(posX,posY,posX + drawWidth,posY + drawWidth),
//                null);
        canvas.drawBitmap(spriteMap,
                new Rect(animationLoopIndex * cropWidth,animationModeIndex * cropHeight,animationLoopIndex * cropWidth + cropWidth,animationModeIndex * cropHeight + cropHeight),
                new Rect(posX,posY,posX + drawWidth,posY + drawWidth),
                null);
//        Rect src = new Rect(animationLoopIndex * cropWidth,animationModeIndex * cropHeight,animationLoopIndex * cropWidth + cropWidth,animationModeIndex * cropHeight + cropHeight)
//        Rect src = new Rect(0,0,60,60);
//
//        canvas.drawBitmap(spriteMap,null, src, null);
    }

    public int getID(){
        return this.id;
    }

    public enum GameObjectTypes {
        PLAYER,
        NPC,
        INANOBJECT,
        TOTAL
    }
}
