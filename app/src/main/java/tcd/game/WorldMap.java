package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by stefano on 24/02/17.
 */

public class WorldMap {

    private Bitmap tileSet;
    private final static int GRID_SIZE = 30;
    private Rect[] tilez;
    private final static int[][] tiles =
            {       {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,1,1,1,1,1,1,1,1,2,2,1,1,1,1,1,1,1,1,0},
                    {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
            };


    WorldMap(Context context){
        tileSet = BitmapFactory.decodeResource(context.getResources(),R.drawable.wood_tileset);
        tilez = new Rect[3];
        tilez[0] = new Rect(0,0,16,16);
        tilez[1] = new Rect(80,16,96,32);
        tilez[2] = new Rect(0,16,16,32);


    }

    public void drawFrame(Canvas canvas){
        Random r = new Random();


        for(int row=0;row<tiles.length;row++){
            for(int col=0; col<tiles[0].length;col++){
                Rect src = null;

                canvas.drawBitmap(tileSet,tilez[tiles[row][col]],new Rect(col*GRID_SIZE,row*GRID_SIZE,col*GRID_SIZE + GRID_SIZE, row*GRID_SIZE + GRID_SIZE),null);
            }
        }
//
//        for(int i=0;i<10;i++){
//            Log.d("MAP",String.valueOf(i));
//            for(int j=0;j<20;j++){
//                Log.d("MAP",String.valueOf(j));
//                if(tiles[i][j] == 0){
//                   canvas.drawBitmap(tileSet,tilez[0],new Rect(i*GRID_SIZE,j*GRID_SIZE,i*GRID_SIZE + GRID_SIZE, j*GRID_SIZE + GRID_SIZE),null);
//                } else if (tiles[i][j] == 1){
//                    canvas.drawBitmap(tileSet,tilez[1],new Rect(i*GRID_SIZE,j*GRID_SIZE,i*GRID_SIZE + GRID_SIZE, j*GRID_SIZE + GRID_SIZE),null);
//                } else {
//                    canvas.drawBitmap(tileSet,tilez[2],new Rect(i*GRID_SIZE,j*GRID_SIZE,i*GRID_SIZE + GRID_SIZE, j*GRID_SIZE + GRID_SIZE),null);
//                }
//            }
//        }

    }

}
