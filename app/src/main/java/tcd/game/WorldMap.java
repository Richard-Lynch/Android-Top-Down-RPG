package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Created by stefano on 24/02/17.
 */

public class WorldMap {

    private static final String TAG = "WorldMap";
    private DatabaseHelper databaseHelper;

    private Bitmap map;
    private Bitmap tileSet;
    private final static int GRID_SIZE = 32;
    private int drawSize;

    private short[][] tileIDS;
    private final static int[][] oldTileIDS =
            {
                    {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
                    {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5},
                    {5, 1, 3, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 4, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 5},
                    {5, 1, 1, 3, 1, 1, 1, 1, 1, 2, 2, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 1, 4, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5},
                    {5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5, 5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5},
                    {5, 3, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 1, 1, 1, 1, 4, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 5, 5, 1, 1, 1, 1, 4, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 5},
                    {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 1, 4, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5},
                    {5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5, 5, 1, 1, 1, 1, 1, 1, 3, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 5},
                    {5, 3, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 1, 1, 1, 1, 4, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 5, 5, 1, 1, 1, 1, 4, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 5},
                    {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 3, 1, 1, 4, 1, 1, 1, 1, 1, 1, 5},
                    {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
            };

    WorldMap(Context context, int canvasWidth, int canvasHeight) {
        Toast.makeText(context,"Test",Toast.LENGTH_LONG).show();
        Log.d(TAG,"Starting");
        // These will come from else where
        drawSize = canvasWidth / 32;

        // Parse text file tile map
        // Write each tile to a 2d array (incl size as header in text file)
        // During iteration, hash each tile ID and if not in hash table add it to an array(list) of tiles (objs null but Id) to be queried from db
        // Query all of those tiles from DB and create array containing results
        // Then use below code as normal

        //Read text from file

        int width,height;
        Hashtable<Short,Tile>tilesRequiredHashTable = new Hashtable<>();
        String IDSFromDB = "";

        try {
            // Read first line to get width and height
             BufferedReader br = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("map.csv")));
            String line = br.readLine();
            String[] vals = line.split(",");
            width = Integer.valueOf(vals[0]);
            height = Integer.valueOf(vals[1]);
            Log.d(TAG,width + " " + height);

/*            // Create 2D array containing tiles
            tileIDS = new short[height][width];

            // Fill it
            int i=0;
            while ((line = br.readLine()) != null) {
                String[] ids = line.split(",");
                for(int j=0;i<ids.length;j++){
                    short currentID = Short.valueOf(ids[j]);

                    // put current tile into 2d array
                    tileIDS[i][j] = currentID;

                    // if not already in required hash table add it
                    if(!tilesRequiredHashTable.contains(currentID)){
                        tilesRequiredHashTable.put(currentID,new Tile(currentID));
                        IDSFromDB += String.valueOf(currentID)+",";
                    }
                }
                i++;
            }*/
            br.close();
        }
        catch (IOException e) {
            Log.d(TAG,"Error");
            Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        // Go to Database and Get relevant rows
        databaseHelper = new DatabaseHelper(context);
/*        IDSFromDB = IDSFromDB.substring(0,IDSFromDB.length()-1);
        String query = " SELECT * FROM " + DatabaseHelper.TILES_TABLE + " WHERE ID IN (" + IDSFromDB + " )";
        Log.d(TAG,query);*/
        //Toast.makeText(context,databaseHelper.tableToString(DatabaseHelper.TILES_TABLE),Toast.LENGTH_LONG).show();
        //ArrayList<String[]> rows = databaseHelper.getRows(query, DatabaseHelper.TILES_TABLE,null);
        //Toast.makeText(context,rows.toString(),Toast.LENGTH_LONG).show();

/*
        Tile[] tilesNeeded = new Tile[rows.size()];

        int i=0;
        for(String[] row : rows){
            //Log.d(TAG, Arrays.toString(row));
            tilesNeeded[i] = new Tile(row);
            i++;
        }


        Hashtable<Integer,Tile> tileHashtable = new Hashtable<>();
        for(Tile tile:tilesNeeded){
            tileHashtable.put(tile.ID,tile);
        }

        // Get required tilesets (may need to do this one at a time to avoid oom)
        tileSet = BitmapFactory.decodeResource(context.getResources(), R.drawable.pokemon_tileset);


        map = Bitmap.createBitmap(canvasWidth,canvasHeight,null);
        Canvas c = new Canvas(map);
        Rect src;
        Rect dest = new Rect(0,0,0,0);
        for(int row = 0; row< oldTileIDS.length; row++){
            for(int col = 0; col< oldTileIDS[0].length; col++){
                Tile t = tileHashtable.get(oldTileIDS[row][col]);
                src = t.getSourceRect();
                dest.set(col* drawSize,row* drawSize,(col* drawSize)+ drawSize,(row* drawSize)+ drawSize);
                c.drawBitmap(tileSet,src,dest,null);
            }
        }*/

    }

    public void drawFrame(Canvas canvas, Rect src) {
        canvas.drawBitmap(map,src,new Rect(0,0,canvas.getWidth(),canvas.getHeight()),null);

    }


    class Tile {
        short ID, row,col,spanX,spanY;
        String convenienceName, sheet;
        boolean collidable;

        public Tile(short ID){
            this.ID = ID;
        }

        public Tile(String[] row){
            this.ID = Short.valueOf(row[0]);
            this.convenienceName = row[1];
            this.row = Short.valueOf(row[2]);
            this.col = Short.valueOf(row[3]);
            this.spanX = Short.valueOf(row[4]);
            this.spanY = Short.valueOf(row[5]);
            this.sheet = row[6];
            this.collidable = Boolean.valueOf(row[7]);
        }

        public Rect getSourceRect(){
            return new Rect(col*GRID_SIZE,row*GRID_SIZE,(col*GRID_SIZE)+(spanX*GRID_SIZE),(row*GRID_SIZE)+(spanY*GRID_SIZE));
        }
    }

}
