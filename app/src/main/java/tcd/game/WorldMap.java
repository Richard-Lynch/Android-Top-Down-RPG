package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by stefano on 24/02/17.
 */

public class WorldMap {
    private Random random;
    private Bitmap tileSet;
    private final static int GRID_SIZE = 32;
    private final static int DRAW_SIZE = 50;
    private Rect[] tilesUsed;
    private final static int[][] tileIDS =
                    {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                    {0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 0},
                    {0, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                    {0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 0},
                    {0, 1, 1, 3, 1, 1, 1, 1, 1, 2, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 0},
                    {0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                    {0, 1, 4, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                    {0, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 0},
                    {0, 3, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                    {0, 1, 1, 1, 1, 4, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 0},
                    {0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            };


    WorldMap(Context context) {
        random = new Random();
        tileSet = BitmapFactory.decodeResource(context.getResources(), R.drawable.pokemon_tileset);
        tilesUsed = new Rect[5];
        tilesUsed[0] = new Rect(0, 2 * GRID_SIZE, GRID_SIZE, 3 * GRID_SIZE);
        tilesUsed[1] = new Rect(GRID_SIZE, GRID_SIZE, 2 * GRID_SIZE, 2 * GRID_SIZE);

        tilesUsed[2] = new Rect(2 * GRID_SIZE, GRID_SIZE, 3 * GRID_SIZE, 2 * GRID_SIZE);

        tilesUsed[3] = new Rect(GRID_SIZE, 0, 2 * GRID_SIZE, GRID_SIZE);
        tilesUsed[4] = new Rect(2 * GRID_SIZE, 3 * GRID_SIZE, 3 * GRID_SIZE, 4 * GRID_SIZE);

    }

    public void drawFrame(Canvas canvas) {
        // canvas.drawBitmap(tileSet, new Rect(0,64,32,96),new Rect(0,0,100,100),null);

        for (int row = 0; row < tileIDS.length; row++) {
            for (int col = 0; col < tileIDS[0].length; col++) {
                canvas.drawBitmap(tileSet, tilesUsed[tileIDS[row][col]], new Rect(col * DRAW_SIZE, row * DRAW_SIZE, col * DRAW_SIZE + DRAW_SIZE, row * DRAW_SIZE + DRAW_SIZE), null);

            }
        }
//
//        for(int i=0;i<10;i++){
//            Log.d("MAP",String.valueOf(i));
//            for(int j=0;j<20;j++){
//                Log.d("MAP",String.valueOf(j));
//                if(tileIDS[i][j] == 0){
//                   canvas.drawBitmap(tileSet,tilesUsed[0],new Rect(i*GRID_SIZE,j*GRID_SIZE,i*GRID_SIZE + GRID_SIZE, j*GRID_SIZE + GRID_SIZE),null);
//                } else if (tileIDS[i][j] == 1){
//                    canvas.drawBitmap(tileSet,tilesUsed[1],new Rect(i*GRID_SIZE,j*GRID_SIZE,i*GRID_SIZE + GRID_SIZE, j*GRID_SIZE + GRID_SIZE),null);
//                } else {
//                    canvas.drawBitmap(tileSet,tilesUsed[2],new Rect(i*GRID_SIZE,j*GRID_SIZE,i*GRID_SIZE + GRID_SIZE, j*GRID_SIZE + GRID_SIZE),null);
//                }
//            }
//        }

    }

}
