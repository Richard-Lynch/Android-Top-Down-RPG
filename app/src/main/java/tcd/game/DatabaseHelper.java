package tcd.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DBHelper";

    static final int DATABASE_VERSION = 1;  //probably never need this
    static final String DATABASE_NAME = "Game_Name_Needed.db";
    static final String TILES_TABLE = "Tiles";
    static final String NPCS_TABLE = " Npcs";

    Context ctx;


    //Queries to create the required tables
    private static final String[] creationQueries = {
            "CREATE TABLE " + TILES_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Row INTEGER, Col INTEGER, SpanX INTEGER, SpanY INTEGER," +
                    "Spritesheet TEXT, Collidable Boolean)",
            "CREATE TABLE " + NPCS_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, SpiteSheet TEXT)"
    };

    //Constructor
    DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
        this.ctx = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Runs only when app is first opened

        //Toast.makeText(ctx,"Recreating DB",Toast.LENGTH_LONG).show();
        // Iterate through table creation queries
        for (String query : creationQueries) {
            db.execSQL(query);
        }

        // Note ID auto increments so isn't needed
        String[][] sampleTiles ={
//                Name,         row, col, spanx, spanY,       tile sheet,    collision
                { "Grass",      "1", "1", "1", "1",    "pokemon_tileset.png", "0"},
                { "Sand",       "62", "7", "1", "1",   "pokemon_tileset.png", "0"},
                { "Rose",       "2", "2", "1", "1",    "pokemon_tileset.png", "0"},
                { "Sign",       "0", "3", "1", "1",    "pokemon_tileset.png", "1"},
                { "Weed",       "2", "1", "1", "1",    "pokemon_tileset.png", "0"},
                { "Tree",       "3", "7", "1", "2",    "pokemon_tileset.png", "1"},
                { "Rock",       "23", "5", "3", "3",   "pokemon_tileset.png", "1"},
                { "Pond",       "34", "0", "2", "2",   "pokemon_tileset.png", "0"},
                { "RoofTip",    "8", "0", "4", "1",    "pokemon_tileset.png", "1"},
                { "RoofMid",    "9", "0", "4", "2",    "pokemon_tileset.png", "1"},
                { "Building",   "11", "0", "4", "2",   "pokemon_tileset.png", "1"},
                { "BuildingSide","47", "7", "1", "5",  "pokemon_tileset.png", "1"},
                { "Sandpit",    "26", "3", "3", "3",  "pokemon_tileset.png", "0"},
                { "Mailbox",    "29", "7", "1", "2",  "pokemon_tileset.png", "1"},
                { "Lamppost",   "45", "7", "1", "2",  "pokemon_tileset.png", "1"},
                { "Lake",       "52", "0", "3", "3",  "pokemon_tileset.png", "0"},
                { "Sand",       "62", "7", "1", "1",  "pokemon_tileset.png", "0"},
                { "SandFade",   "31", "6", "1", "1",  "pokemon_tileset.png", "0"},
                { "RockToBeach","19", "0", "3", "3",  "pokemon_tileset.png", "0"}

        };


        String[][] sampleNPCS = {
//                Name          Spritesheet
                {"VonNeuyman",  "npc_1.png"},
                {"Dijkstra",    "npc_2.png"},
                {"Donall",      "npc_3.png"},
                {"Null-Man",    "npc_4.png"},
                {"AntonG",      "npc_5.png"}
        };


        // Generate initial values in db - these will come from elsewhere
        for (String[] vals : sampleTiles){
            String query = "INSERT INTO " + TILES_TABLE + " (Name, Row, Col, SpanX, SpanY, Spritesheet, Collidable) VALUES (";
            for(String val : vals){
                query += "'" + val + "',";
            }
            query = query.substring(0,query.length()-1);
            query += ")";
            Log.d(TAG,query);
            db.execSQL(query);

        }

        for(String[] vals : sampleNPCS){
            String query = "INSERT INTO " + NPCS_TABLE + " (Name, SpiteSheet) VALUES (";
            for(String val : vals){
                query += "'" + val + "',";
            }
            query = query.substring(0,query.length()-1);
            query += ")";
            db.execSQL(query);

        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //changing database structure or something like that -- can maintain old DB
        // delete current tables in order to create new one
        // db.execSQL("DROP TABLE IF EXISTS " + OBJECT_TABLE);
        // onCreate(db);   //recall on create (which would be changed)
    }


    /**
     * Add row to DB without ID - lazy hacky way to get number of fields
     */

    void addRow(String tableName, String[] values) {
        //Grab column names -- Extra read query for every write query though
        String[] columns = getColsFromTable(tableName);
        //Write to DB
        ContentValues contentValues = new ContentValues();  //Helper class for generating key/value pairs for db queries
        if (columns.length -1 != values.length) {
            // -1 as we don't pass in value for ID which is AI
            Toast.makeText(ctx, "#Columns must match #values\n"+columns.length+" columns in db, " + values.length+" provided", Toast.LENGTH_LONG).show();
            return;
        }

        for (int i = 0; i < columns.length-1; i++) {
            //the +- 1 here compensates for skipping ID column which is AI
            contentValues.put(columns[i+1], values[i]);
        }
        SQLiteDatabase db = getWritableDatabase();      //db = database we are going to write to
        db.insert(tableName, null, contentValues);      // takes table name, null, content values object as above
        db.close();
    }

    /** Add multiple rows to DB */
    void addRows(String tableName, String[][] rows){
        for(String[] vals : rows){
            addRow(tableName,vals);
        }
    }

    /** Gets column names from table */
    String[] getColsFromTable(String tableName){
        //Inefficient lazy way of getting column names so we don't need to pass them in each time
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(tableName,null,null,null,null,null,null);
        String[] columns = c.getColumnNames();
        c.close();
        return columns;
    }


    //TODO: Return Hashmap of string arrays to ID keys
    /** Executes Query and returns rows as  arraylist of string arrays */
    ArrayList<String[]> getRows(String query, String tableName, String[] args){
        SQLiteDatabase db = getReadableDatabase();
        String[] colNames = getColsFromTable(tableName);
        ArrayList<String[]> results = new ArrayList<>();
//      Cursor used to point to elements in db
        Cursor c = db.rawQuery(query, args);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(colNames[0])) != null){
                String[] row = new String[colNames.length];
                for(int i=0;i<colNames.length;i++){
                    row[i] = c.getString(c.getColumnIndex(colNames[i]));
                }
                results.add(row);
            }
            c.moveToNext();
        }
        db.close();
        c.close();
        return results;
    }

    int countRows(String query, String[] args) {
        SQLiteDatabase db = getReadableDatabase();

        int count = 0;

        Cursor c = db.rawQuery(query, args);
        if(c.getCount() > 0) {
            c.moveToFirst();
            count = c.getInt(0);
        }
        c.close();

        return count;
    }



    //  Delete Rows from table
    public void deleteAllRows(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE * FROM " +tableName);
    }

    public void deleteDB(){
        this.ctx.deleteDatabase(DATABASE_NAME);
    }

    //  Display Table as string
    String tableToString(String tableName){
        String dbAsString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+tableName;
        String[] colNames = getColsFromTable(tableName);
//      Cursor used to point to elements in db
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(colNames[0])) != null){
                for(int i=0;i<colNames.length;i++){
                    dbAsString += c.getString(c.getColumnIndex(colNames[i]));
                    dbAsString += ",";
                }
            }
            dbAsString += "\n";
            c.moveToNext();
        }
        db.close();
        c.close();
        return dbAsString;
    }


}

