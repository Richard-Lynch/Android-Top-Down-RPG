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

public class GameMode {
    private final static String TAG = "GameMode";
    private Context context;
    private Bitmap map;
    private InanObject inanObjs[];
    private NPC npcs[];
    private Player players[];
    private int canvasWidth, canvasHeight;


    // Init vals?

    private boolean battle;

    GameMode(Context context, int canvasWidth, int canvasHeight){
        this.context = context;
//
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        init(0);
    }

    private void init(double levelID){
        //mapID = 0;
        map = BitmapFactory.decodeResource(context.getResources(),R.drawable.background);
        players = new Player[1];
        npcs = new NPC[1];
        inanObjs = new InanObject[1];


        players[0] = new Player(context,"Donal", canvasWidth, canvasHeight);
        npcs[0] = new NPC(context,"Frank",canvasWidth, canvasHeight);
        inanObjs[0] = new InanObject(context,"House",canvasWidth,canvasHeight);
        battle = false;
        players[0].setPosX(50);
        players[0].setPosY(50);
        players[0].setVelX(5);
        players[0].setVelY(0);

        npcs[0].setPosX(100);
        npcs[0].setPosY(150);
        npcs[0].setVelX(6);
        npcs[0].setVelY(0);

        inanObjs[0].setPosX(150);
        inanObjs[0].setPosY(250);
        inanObjs[0].setVelX(7);
        inanObjs[0].setVelY(0);

    }

    public void update(){
        Log.d(TAG, "got to update");
        for(int i=0;i<players.length;i++) {
            players[i].update(players, npcs, inanObjs, players[i].getID(), GameObject.GameObjectTypes.PLAYER);
        }

        for(int i=0;i<npcs.length;i++){
            npcs[i].update(players,npcs,inanObjs,npcs[i].getID(),GameObject.GameObjectTypes.NPC);
        }

        for(int i=0;i<inanObjs.length;i++){
            inanObjs[i].update(players,npcs,inanObjs,inanObjs[i].getID(), GameObject.GameObjectTypes.INANOBJECT);
        }
        Log.d(TAG, "left update");

    }

    public void drawFrame(Canvas canvas){
        Log.d(TAG, "got to draw");
//        canvas.drawBitmap(map,null,new Rect(0,0,canvas.getWidth(),canvas.getHeight()),null);
        Rect src = new Rect(0,0,canvas.getWidth(),canvas.getHeight());

        canvas.drawBitmap(map,null, src, null);
        Log.d(TAG,"Passed drw");
//        inanObjs[0].drawFrame(canvas);

        for(int i=0;i<inanObjs.length;i++){
            inanObjs[i].drawFrame(canvas);
        }

        for(int i=0;i<npcs.length;i++){
            npcs[i].drawFrame(canvas);
        }

        for(int i=0;i<players.length;i++) {
            players[i].drawFrame(canvas);
        }
        Log.d(TAG, "left draw");
    }
}
