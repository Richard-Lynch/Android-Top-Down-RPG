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
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created by stefano on 24/02/17.
 */

public class WorldMap {

    private static final String TAG = "WorldMap";
    private DatabaseHelper databaseHelper;
    private Context context;

    private Bitmap map;
    private Bitmap tileSet;
    private final static int GRID_SIZE = 32;
    private int drawSize;

    private ArrayList<NPC> npcs;
    private ArrayList<InanObject>inanObjects;

    private short[][] tileIDS;


    WorldMap(Context context, int canvasWidth, int canvasHeight) {
        this.context = context;
        Log.d(TAG,"Starting");
        // These will come from else where
        drawSize = canvasWidth / 32;



        // Store tiles to be read from DB in hash map for easy lookup
        HashMap<Short,Tile> tilesRequiredHashMap = new HashMap<>();

        // String for generating query with vaules of tiles required
        String IDSFromDB = "";
        int rows,cols;
        try {
            // Read first line to get width and height
             BufferedReader br = new BufferedReader(
                    new InputStreamReader(context.getAssets().open("map.csv")));
            String line = br.readLine();
            String[] vals = line.split(",");
            cols = Integer.valueOf(vals[0]);
            rows = Integer.valueOf(vals[1]);


            // Create the 2D array containing tiles
            tileIDS = new short[rows][cols];

            // Fill it
            int i=0;
            while (((line = br.readLine()) != null)) {
                String[] ids = line.split(",");
                // Check if end of block reached (all commas)
                if(i==rows){
                    // Line break read
                    break;
                }
                for(int j=0;j<ids.length;j++){
                    short currentID = Short.valueOf(ids[j]);

                    // put current tile into 2d array
                    tileIDS[i][j] = currentID;

                    // if not already in required hash table add it
                    if(!tilesRequiredHashMap.containsKey(currentID)){
                        //t("Putting " + currentID + " in hash table");
                        tilesRequiredHashMap.put(currentID,new Tile(currentID));
                        IDSFromDB += String.valueOf(currentID)+",";
                    }
                }
                i++;
            }

            // Go to Database and Get relevant rows
            databaseHelper = new DatabaseHelper(context);

            // Trim off last comma
            IDSFromDB = IDSFromDB.substring(0,IDSFromDB.length()-1);

            // Generate Query
            String query = " SELECT * FROM " + DatabaseHelper.TILES_TABLE + " WHERE ID IN (" + IDSFromDB + " ) ORDER BY ID";

            // Save Results
            ArrayList<String[]> results = databaseHelper.getRows(query, DatabaseHelper.TILES_TABLE,null);

            // Iterate over tiles read from DB
            for(String[] s : results){
                // Finish the initalization process of all the tiles required for the map
                Tile tile = tilesRequiredHashMap.get(Short.valueOf(s[0]));
                if(tile != null) {
                    tile.setInfo(s);
                } else {
                    Toast.makeText(context,"Null: id= " + s[0],Toast.LENGTH_SHORT).show();
                }
            }

            // Get required tilesets (may need to do this one at a time to avoid oom)
            // For now just one so
            tileSet = BitmapFactory.decodeResource(context.getResources(), R.drawable.pokemon_tileset);

            // Create our final bitmap
            map = Bitmap.createBitmap(canvasWidth,canvasHeight,null);

            // Create inanObjects and save them
            inanObjects = new ArrayList<>();

            // Draw to it
            Canvas c = new Canvas(map);
            Rect src;
            Rect dest = new Rect(0,0,0,0);
            for(int row = 0; row< tileIDS.length; row++){
                for(int col = 0; col< tileIDS[0].length; col++){
                    Tile tile = tilesRequiredHashMap.get(tileIDS[row][col]);
                    if(tile == null){
                        t("error");
                        break;
                    }
                    if(tile.collidable){
                        InanObject inanObject = new InanObject(context,tile.convenienceName,canvasWidth,canvasHeight);
                        inanObject.setGridPos(col,row);
                        t("Creating inan: (" + col + "," + row + ")");
                        inanObjects.add(inanObject);
                    }
                    src = tile.getSourceRect();
                    dest.set(col* drawSize,row* drawSize,(col* drawSize)+ drawSize,(row* drawSize)+ drawSize);
                    c.drawBitmap(tileSet,src,dest,null);
                }
            }

            // Create Array list of npcs
            npcs = new ArrayList<>();

            // Read from file
            i=0;
            IDSFromDB = "";
            while (((line = br.readLine()) != null)) {
                String[] ids = line.split(",");
                // Check if end of block reached (all commas)
                if(i==rows){
                    // Line break read
                    break;
                }
                for(int j=0;j<ids.length;j++){
                    short currentID = Short.valueOf(ids[j]);

                    if(currentID != 0) {
                        // create npc
                        NPC npc = new NPC(context, null, canvasWidth, canvasHeight);
                        npc.setGridPos(j,i);
                        npc.setDatabaseID(currentID);
                        npcs.add(npc);
                        IDSFromDB += String.valueOf(currentID) + ",";
                    }
                }
                i++;
            }

            // Read all required NPCS from DB
            // Currently just have bame but this can be extended

            // Trim comma
            IDSFromDB = IDSFromDB.substring(0,IDSFromDB.length()-1);
            query = " SELECT * FROM " + DatabaseHelper.NPCS_TABLE + " WHERE ID IN (" + IDSFromDB + ") ORDER BY ID";

            // Save Results
            results = databaseHelper.getRows(query, DatabaseHelper.NPCS_TABLE,null);


            // Parse through the results
            HashMap<Short,String[]> npcHashMap = new HashMap<>();
            for(String[] s : results){
                // Store NPC rows in key value pair (should really be done in DB class)
                npcHashMap.put(Short.valueOf(s[0]),s);
                // Note could search for each NPC which matches this ID here and assign it that way
                // But think iterating over the npcs and getting the info from the hash map would be quicker?
            }

            // Set relevant values from DB to each NPC (may be multiple NPC's with same ID (generic ones or W.E.))
            for(NPC npc : npcs){
                String[] s = npcHashMap.get(npc.getDatabaseID());
                if(s!= null){
                    npc.setInfo(s);
                } else {
                    t("null on " + npc.getDatabaseID());
                }

            }

            br.close();
        }
        catch (IOException e) {
            Log.d(TAG,"Error");
            Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    /**
     * Draws a sub portion of the map Bitmap based on supplied src rectangle
     * @param canvas
     * @param src
     */
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

        public void setInfo(String[] row){
            // skip row[0]  as it is DB id which was set in constructor
            this.convenienceName = row[1];
            this.row = Short.valueOf(row[2]);
            this.col = Short.valueOf(row[3]);
            this.spanX = Short.valueOf(row[4]);
            this.spanY = Short.valueOf(row[5]);
            this.sheet = row[6];
            if(Integer.valueOf(row[7]) == 1){
                this.collidable = true;
            } else {
                this.collidable = false;
            }
        }

        /**
         * Gets the source rectangle to be taken from larger spritesheet for this tile
         * @return Source Rectangle on sprite sheet
         */
        public Rect getSourceRect(){
            return new Rect(col*GRID_SIZE,row*GRID_SIZE,(col*GRID_SIZE)+(spanX*GRID_SIZE),(row*GRID_SIZE)+(spanY*GRID_SIZE));
        }
    }

    /**
     * Get all of the NCPS associated with the map
     * @return array of npcs
     */
    public NPC[] getNpcs(){
        return npcs.toArray(new NPC[npcs.size()]);
    }


    /**
     * Get all of the NCPS associated with the map
     * @return array of npcs
     */
    public InanObject[] getInanObjects(){
        return inanObjects.toArray(new InanObject[inanObjects.size()]);
    }



    //delete me - my logs were broken so everything was toasted
    void t(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }

}
