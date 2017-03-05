package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by stefano on 24/02/17.
 */

public class WorldMap {
    Bitmap map;
    private Bitmap tileSet;
    private final static int GRID_SIZE = 32;
    private final static int DRAW_SIZE = 28;
    private Rect[] tilesUsed;
    private final static int[][] tileIDS =
                    {
                            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
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

    private boolean[][] collisionGrid;

    WorldMap(Context context) {


        // Parse text file tile map
        // Write each tile to a 2d array (incl size as header in text file)
        // During iteration, hash each tile ID and if not in hash table add it to an array(list) of tiles (objs null but Id) to be queried from db
        // Query all of those tiles from DB and create array containing results
        // Then use below code as normal


        // Create dummy tiles coming from DB
        Tile[] tilesNeeded = {
                new Tile(0,2,1,1,1,"Weed","pokemon_tileset.png",false),
                new Tile(1,1,1,1,1,"Grass","pokemon_tileset.png",false),
                new Tile(2,11,7,1,1,"Brick","pokemon_tileset.png",false),
                new Tile(3,2,2,1,1,"Rose","pokemon_tileset.png",false),
                new Tile(4,0,3,1,1,"Sign","pokemon_tileset.png",true),
        };

        Hashtable<Integer,Tile> tileHashtable = new Hashtable<>();
        for(Tile tile:tilesNeeded){
            tileHashtable.put(tile.ID,tile);
        }

        // Get required tilesets (may need to do this one at a time to avoid oom)
        tileSet = BitmapFactory.decodeResource(context.getResources(), R.drawable.pokemon_tileset);

        // Create collisionGrid
        collisionGrid = new boolean[tileIDS.length][tileIDS[0].length];

        /*
        tilesUsed = new Rect[5];
        tilesUsed[0] = new Rect(0, 2 * GRID_SIZE, GRID_SIZE, 3 * GRID_SIZE);
        tilesUsed[1] = new Rect(GRID_SIZE, GRID_SIZE, 2 * GRID_SIZE, 2 * GRID_SIZE);

        tilesUsed[2] = new Rect(2 * GRID_SIZE, GRID_SIZE, 3 * GRID_SIZE, 2 * GRID_SIZE);

        tilesUsed[3] = new Rect(GRID_SIZE, 0, 2 * GRID_SIZE, GRID_SIZE);
        tilesUsed[4] = new Rect(2 * GRID_SIZE, 3 * GRID_SIZE, 3 * GRID_SIZE, 4 * GRID_SIZE);

        map = Bitmap.createBitmap(20*32,10*32,null);
        Canvas c = new Canvas(map);
        for (int row = 0; row < tileIDS.length; row++) {
            for (int col = 0; col < tileIDS[0].length; col++) {
                c.drawBitmap(tileSet, tilesUsed[tileIDS[row][col]], new Rect(col * DRAW_SIZE, row * DRAW_SIZE, col * DRAW_SIZE + DRAW_SIZE, row * DRAW_SIZE + DRAW_SIZE), null);
            }
        }
        */

        map = Bitmap.createBitmap(20*32,10*32,null);
        Canvas c = new Canvas(map);
        Rect src = new Rect(0,0,0,0);
        Rect dest = new Rect(0,0,0,0);
        for(int row=0;row<tileIDS.length;row++){
            for(int col=0;col<tileIDS[0].length;col++){
                Tile t = tileHashtable.get(tileIDS[row][col]);
                if(t.collidable){
                    collisionGrid[row][col] = true;
                } else {
                    collisionGrid[row][col] = false;
                }
                src = t.getSourceRect();
                dest.set(col*DRAW_SIZE,row*DRAW_SIZE,(col*DRAW_SIZE)+DRAW_SIZE,(row*DRAW_SIZE)+DRAW_SIZE);
                c.drawBitmap(tileSet,src,dest,null);
            }
        }

    }

    public void drawFrame(Canvas canvas, Rect src) {
        canvas.drawBitmap(map,src,new Rect(0,0,canvas.getWidth(),canvas.getHeight()),null);

    }


    class Tile {
        int ID, row,col,spanX,spanY;
        String convenienceName, sheet;
        boolean collidable;

        public Tile(int ID, int row, int col, int spanX, int spanY, String convenienceName, String sheet, boolean collidable) {
            this.ID = ID;
            this.row = row;
            this.col = col;
            this.spanX = spanX;
            this.spanY = spanY;
            this.convenienceName = convenienceName;
            this.sheet = sheet;
            this.collidable = collidable;
        }

        public Rect getSourceRect(){
            return new Rect(col*GRID_SIZE,row*GRID_SIZE,(col*GRID_SIZE)+GRID_SIZE,(row*GRID_SIZE)+GRID_SIZE);
        }
    }

}
