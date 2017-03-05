package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;


/**
 * Created by stefano on 24/02/17.
 */

public class WorldMap {

    private int canvasWidth, canvasHeight;

    private static final String TAG = "WorldMap";
    private DatabaseHelper databaseHelper;

    private Bitmap map;
    private Bitmap tileSet;
    private final static int GRID_SIZE = 32;
    private int drawSize = 26;

    private final static int[][] tileIDS =
                    {
                            {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                            {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                            {5, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5},
                            {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                            {5, 1, 1, 3, 1, 1, 1, 1, 1, 2, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 5},
                            {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5},
                            {5, 1, 4, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5},
                            {5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5},
                            {5, 3, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5},
                            {5, 1, 1, 1, 1, 4, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 5},
                            {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 5},
                            {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                     };

    private boolean[][] collisionGrid;

    WorldMap(Context context, int canvasWidth, int canvasHeight) {
        // These will probably come from camera class
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        drawSize = canvasWidth / 20;

        // Parse text file tile map
        // Write each tile to a 2d array (incl size as header in text file)
        // During iteration, hash each tile ID and if not in hash table add it to an array(list) of tiles (objs null but Id) to be queried from db
        // Query all of those tiles from DB and create array containing results
        // Then use below code as normal


        databaseHelper = new DatabaseHelper(context);
        Toast.makeText(context,databaseHelper.tableToString(DatabaseHelper.TILES_TABLE),Toast.LENGTH_LONG).show();

        ArrayList<String[]> rows = databaseHelper.getRows("SELECT * FROM " + DatabaseHelper.TILES_TABLE, DatabaseHelper.TILES_TABLE,null);
        Toast.makeText(context,rows.toString(),Toast.LENGTH_LONG).show();
//        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);

        Tile[] tilesNeeded = new Tile[rows.size()];

        int i=0;
        for(String[] row : rows){
            Log.d(TAG, Arrays.toString(row));
            tilesNeeded[i] = new Tile(row);
            i++;
        }


        // Create dummy tiles coming from DB
/*        Tile[] tilesNeeded = {
                new Tile(1,"Grass",1,1,1,1,"pokemon_tileset.png",false),
                new Tile(2,"Brick",11,7,1,1,"pokemon_tileset.png",false),
                new Tile(3,"Rose",2,2,1,1,"pokemon_tileset.png",false),
                new Tile(4,"Sign",0,3,1,1,"pokemon_tileset.png",true),
                new Tile(5,"Weed",2,1,1,1,"pokemon_tileset.png",false)
        };*/




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
                c.drawBitmap(tileSet, tilesUsed[tileIDS[row][col]], new Rect(col * drawSize, row * drawSize, col * drawSize + drawSize, row * drawSize + drawSize), null);
            }
        }
        */

        map = Bitmap.createBitmap(canvasWidth,canvasHeight,null);
        Canvas c = new Canvas(map);
        Rect src;
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
                dest.set(col* drawSize,row* drawSize,(col* drawSize)+ drawSize,(row* drawSize)+ drawSize);
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

        public Tile(int ID, String convenienceName, int row, int col, int spanX, int spanY,  String sheet, boolean collidable) {
            this.ID = ID;
            this.row = row;
            this.col = col;
            this.spanX = spanX;
            this.spanY = spanY;
            this.convenienceName = convenienceName;
            this.sheet = sheet;
            this.collidable = collidable;
        }

        public Tile(String[] row){
            this.ID = Integer.valueOf(row[0]);
            this.convenienceName = row[1];
            this.row = Integer.valueOf(row[2]);
            this.col = Integer.valueOf(row[3]);
            this.spanX = Integer.valueOf(row[4]);
            this.spanY = Integer.valueOf(row[5]);
            this.sheet = row[6];
            this.collidable = Boolean.valueOf(row[7]);
        }

        public Rect getSourceRect(){
            return new Rect(col*GRID_SIZE,row*GRID_SIZE,(col*GRID_SIZE)+(spanX*GRID_SIZE),(row*GRID_SIZE)+(spanY*GRID_SIZE));
        }
    }

}
