package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class GameMode {
    private final static String TAG = "GameMode";

    // Context is passed ot GameObjects for access to drawable resources
    private Context context;
    private int canvasWidth, canvasHeight;
    private int MapWidth;
    private int MapHeight;
    private Paint paint;

    // Map should probably have its own class for Reading from file etc
    private Bitmap map;
    private Bitmap speechbox;
    private WorldMap worldMap;
    public int offset_x;
    public int offset_y;
    private Map<Integer, Bitmap[][]> SpriteMaps = new HashMap<Integer, Bitmap[][]>(5);


    // Declare Arrays of our GameObjects
    private InanObject inanObjs[];
    private NPC[] npcs;
    private Player players[];
    private Map<Integer, GameObject> ObjMap = new HashMap<Integer, GameObject>(200);
    private Map<Integer, Integer> PosMap = new HashMap<Integer, Integer>(200);

    private int CurrentEventID;
    private boolean EventActivated;

  // Declare mediaplayer for Music (soundpool buffer is too small for larger files)
    private MediaPlayer mediaPlayer;

    // Declare objects for sound effects
    private SoundPool soundPool;
    private AudioAttributes audioAttributes;
    private int SP_ID_MarioCoin;
    private int SP_ID_DoomGate;
    private final int MAX_STREAM = 10;




    // Probably should have OpenWorld and Battle Classes  which extend this so dont need flag
    // private boolean battle;


    //TODO: Get Number of NPCS/Inans/Players from map class and initialize arrays accordingly or create data-structure

    //TODO: Set a default velocity and multiply this by 1, 0 or -1 etc to move more than one pixel at a time?

    /***********************************************************************************
     * Constructors
     ***********************************************************************************/

    /**
     * Creates a Game Mode
     * Must be initialized using gameMode.initialize()
     *
     * @param context application context
     */
    GameMode(Context context){
        this.context = context;
    }


    /**
     * Creates and Initializes a Game Mode
     * @param context application context
     * @param canvasWidth screen width
     * @param canvasHeight screen height
     */
    GameMode(Context context, int canvasWidth, int canvasHeight) {
        this.context = context;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        init(0);
    }


    /***********************************************************************************
     * Methods
     ***********************************************************************************/

    /**
     * Initializes a GameMode with size of screen. This is called from event fragment once the
     * view has been created and the dimensions are available
     *
     * @param screenWidth
     * @param screenHeight
     */
    public void initialize(int screenWidth, int screenHeight) {
        Log.d(TAG, "in initialise gamemode");

        this.canvasWidth = screenWidth;
        this.canvasHeight = screenHeight;
        this.EventActivated =false;

        init(0);
    }


    private void init(double levelID){
        worldMap = new WorldMap(context,canvasWidth,canvasHeight);
        speechbox = BitmapFactory.decodeResource(context.getResources(), R.drawable.truck);
        map = worldMap.getMap();
        MapWidth = map.getWidth();
        MapHeight = map.getHeight();

        //map = BitmapFactory.decodeResource(context.getResources(),R.drawable.map_default);
        players = new Player[1];
        npcs = worldMap.getNpcs();
        SpriteMaps.put(GameObject.GameObjectTypes.NPC.ordinal(),npcs[0].dividedSpriteMap);

//        inanObjs = new InanObject[1];
        inanObjs = worldMap.getInanObjects();
        SpriteMaps.put(GameObject.GameObjectTypes.INANOBJECT.ordinal(),inanObjs[0].dividedSpriteMap);

        Log.d(TAG,"initing gamemode");

        players[0] = new Player(context,"Donal", canvasWidth, canvasHeight, map.getWidth(), map.getHeight());
        //inanObjs[0] = new InanObject(context,"House",canvasWidth,canvasHeight);
        players[0].setGridPos(3,2);
        players[0].setSprite(BitmapFactory.decodeResource(context.getResources(),R.drawable.player_default));
        SpriteMaps.put(GameObject.GameObjectTypes.PLAYER.ordinal(),players[0].dividedSpriteMap);

        ObjMap.put(players[0].getID(), players[0]);
        PosMap.put(players[0].getCoordinates().hashCode(), players[0].getID());
        Player test = (Player) ObjMap.get(players[0].getID());
        int testint = ObjMap.get(players[0].getID()).getID();
        Log.d(TAG, "Real Player ID:" + players[0].getID());
        Log.d(TAG, "Test Player ID:" + test.getID());
        Log.d(TAG, "Ref Player ID:" + testint);
        Log.d(TAG, "Player now im at " + players[0].gridX + " " + players[0].gridY);
        Log.d(TAG, "Player now im at " + test.gridX + " " + test.gridY);
        Coordinates pl = new Coordinates(players[0].gridX, players[0].gridY);
        Log.d(TAG, "Player coords " + pl.getX() + " " + pl.getY());
        int testPOS = PosMap.get(pl.hashCode());
        Log.d(TAG, "Player pos map id: " + testPOS + " pos: " + ObjMap.get(testPOS).gridX + " " + ObjMap.get(testPOS).gridY);

//        npcs[0].setPosX(700);
//        npcs[0].setPosY(300);
        npcs[0].setGridPos(4, 8);
        npcs[0].setVelX(1);
        npcs[0].setVelY(0);


        // Add all npcs to hash maps
        for(int i=0;i<npcs.length;i++){
            ObjMap.put(npcs[i].getID(),npcs[i]);
            PosMap.put(npcs[i].getCoordinates().hashCode(),npcs[i].getID());
            npcs[i].setVelX(1);
            npcs[i].setVelY(0);
        }
/*
        ObjMap.put(npcs[0].getID(), npcs[0]);
        PosMap.put(npcs[0].getCoordinates().hashCode(), npcs[0].getID());
        NPC testn = (NPC) ObjMap.get(npcs[0].getID());
        int testintn = ObjMap.get(npcs[0].getID()).getID();

        Log.d(TAG,"Real NPC ID:" + npcs[0].getID());
        Log.d(TAG,"Test NPC ID:" + testn.getID());
        Log.d(TAG,"Ref NPC ID:" + testintn);
        Log.d(TAG,"NPC now im at "+ npcs[0].gridX + " " + npcs[0].gridY);
        Log.d(TAG,"NPC now im at "+ testn.gridX + " " + testn.gridY);
*/

        for(int i=0;i<inanObjs.length;i++){
            ObjMap.put(inanObjs[i].getID(),inanObjs[i]);
            PosMap.put(inanObjs[i].getCoordinates().hashCode(),inanObjs[i].getID());
        }
//        inanObjs[0].setPosX(600);
//        inanObjs[0].setPosY(20);

//        inanObjs[0].setGridPos(5,4);
//        inanObjs[0].setVelX(0);
//        inanObjs[0].setVelY(0);
//        ObjMap.put(inanObjs[0].getID(), inanObjs[0]);
//        PosMap.put(inanObjs[0].getCoordinates().hashCode(), inanObjs[0].getID());
//        InanObject testi = (InanObject) ObjMap.get(inanObjs[0].getID());
//        int testinti = ObjMap.get(inanObjs[0].getID()).getID();
//        Log.d(TAG,"Real inan ID:" + inanObjs[0].getID());
//        Log.d(TAG,"Test inan ID:" + testi.getID());
//        Log.d(TAG,"Ref inan ID:" + testinti);
//        Log.d(TAG,"inan now im at "+ inanObjs[0].gridX + " " + inanObjs[0].gridY);
//        Log.d(TAG,"inan now im at "+ testi.gridX + " " + testi.gridY);

        // Initialise mediaplayer
        mediaPlayer = MediaPlayer.create(context, R.raw.doom_gate);
        mediaPlayer.start();

        // Initialise soundpool for sound effects
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(MAX_STREAM)
                .build();

        // Define the method that is called when a file is loaded to the soundPool
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });

        SP_ID_MarioCoin = soundPool.load(context, R.raw.mario_coin, 1);
        SP_ID_MarioCoin = soundPool.load(context, R.raw.mario_coin, 1);
        SP_ID_MarioCoin = soundPool.load(context, R.raw.mario_coin, 1);
        SP_ID_MarioCoin = soundPool.load(context, R.raw.mario_coin, 1);
        SP_ID_MarioCoin = soundPool.load(context, R.raw.mario_coin, 1);


        //Updating Camera offsets
        offset_x = players[0].getPosX() + players[0].getDrawWidth()/2 - canvasWidth/2;
        offset_y = players[0].getPosY() + players[0].getDrawHeight()/2 - canvasHeight/2;
        Log.d(TAG,"POSX: " +players[0].getPosX());
        Log.d(TAG,"POSY: " + players[0].getPosY());
        Log.d(TAG,"PW: " + players[0].getDrawWidth()/2 ) ;
        Log.d(TAG,"PH: " + players[0].getDrawHeight()/2 ) ;
        Log.d(TAG, "CanvasW: " + canvasWidth/2);
        Log.d(TAG,"CanvasH: " + canvasHeight/2);


        if(offset_y <= 0)
        {
            offset_y = 0;
        }
        if(offset_x <= 0)
        {
            offset_x = 0;
        }
        if(offset_y + canvasHeight >= MapHeight)
        {
            offset_y = MapHeight - canvasHeight;
        }
        if(offset_x + canvasWidth >= MapWidth)
        {
            offset_x = MapWidth - canvasWidth;
        }


        Log.d(TAG,"oy: " + offset_y);
        Log.d(TAG,"ox: " + offset_x);

    }

    /**
     * Calls update methods for all game objects
     */
    public void update() {

        if (!EventActivated) {

            // Update Player positions
            for (int i = 0; i < players.length; i++) {
                CurrentEventID = players[i].update(players, npcs, inanObjs, players[i].getID(), GameObject.GameObjectTypes.PLAYER, PosMap, ObjMap);
                if (CurrentEventID == 10) {
                    EventActivated = true;
                }
            }

            // Update NPC positions
            for (int i = 0; i < npcs.length; i++) {
                npcs[i].update(players, npcs, inanObjs, players[i].getID(), GameObject.GameObjectTypes.NPC, PosMap, ObjMap);

            }


            // Update Inanimate Object Positions
//        for(int i=0;i<inanObjs.length;i++){
//            inanObjs[i].update(players, npcs, inanObjs, players[i].getID(), GameObject.GameObjectTypes.INANOBJECT, PosMap, ObjMap);

//        for(int i=0;i<inanObjs.length;i++){
//            inanObjs[i].update(players, npcs, inanObjs, players[i].getID(), GameObject.GameObjectTypes.INANOBJECT, PosMap, ObjMap);
//        }

            //Updating Camera offsets
            offset_x = players[0].getPosX() + players[0].getDrawWidth() / 2 - canvasWidth / 2;
            offset_y = players[0].getPosY() + players[0].getDrawHeight() / 2 - canvasHeight / 2;


            if (offset_y <= 0) {
                offset_y = 0;
            }

            if (offset_x <= 0) {
                offset_x = 0;
            }

            if (offset_y + canvasHeight > MapHeight) {
                Log.d(TAG, "shit");
                offset_y = MapHeight - canvasHeight;
            }

            if (offset_x + canvasWidth > MapWidth) {
                offset_x = MapWidth - canvasWidth;
            }
            //}

            Log.d(TAG, "oy: " + offset_y);
            Log.d(TAG, "ox: " + offset_x);
        }

    }
    void Rel_bit(){

    }
    /**
     * Calls draw method for all game objects
     *
     * @param canvas
     */
    public void drawFrame(Canvas canvas) {
    // can also pass a proameter in here as well as canvas
        //TODO: make a camera class, and tidy up! And sort out col with edge of map!

        Rect Camera = new Rect(offset_x,offset_y,((canvas.getWidth())/MapWidth)*map.getWidth(),
                ((canvas.getHeight())/MapHeight)*map.getHeight());

        canvas.drawBitmap(map, new Rect(
                (int)Math.round(((double)offset_x/(double)MapWidth)*(double)map.getWidth()),
                (int)Math.round(((double)offset_y/(double)MapHeight)*(double)map.getHeight()),
                (int)Math.round((((double)canvas.getWidth())/(double)MapWidth)*(double)map.getWidth()+((double)offset_x/(double)MapWidth)*(double)map.getWidth()),
                (int)Math.round((((double)canvas.getHeight())/(double)MapHeight)*(double)map.getHeight()+((double)offset_y/(double)MapHeight)*(double)map.getHeight())),
                new Rect(0,0, canvas.getWidth(), canvas.getHeight()), null
        );
        Log.d(TAG, "map.get:" + map.getWidth());
        Log.d(TAG, "MapWidth:" + MapWidth);
        Log.d(TAG, "MapHeight:" + MapHeight);
        Log.d(TAG, "canvas.getW:" + canvas.getWidth());
        Log.d(TAG, "offsetX:" + offset_x);
        Log.d(TAG, "div: " + (offset_x/MapWidth));
        Log.d(TAG, "(int)div: " + (int)(offset_x/MapWidth));
        Log.d(TAG, "(int)round.div: " + (int)Math.round(((double)offset_x/(double)MapWidth)));

        Log.d(TAG,"1:" + Math.round((offset_x/MapWidth)*map.getWidth()));
        Log.d(TAG,"2:" + Math.round((offset_y/MapHeight)*map.getHeight()));
        Log.d(TAG,"3:" + Math.round(((canvas.getWidth())/MapWidth)*map.getWidth()+(offset_x/MapWidth)*map.getWidth()));
        Log.d(TAG,"4:" + Math.round(((canvas.getHeight())/MapHeight)*map.getHeight()+(offset_y/MapHeight)*map.getHeight()));


        // Drawing Map -- should be else where possibly

        //canvas.drawBitmap(map,null, new Rect(0,0,canvas.getWidth(),canvas.getHeight()), null);
        //worldMap.drawFrame(canvas,new Rect(0,0,canvasWidth,canvasHeight));

        // Draw InanimateObjects
        for(int i=0;i<inanObjs.length;i++){
            //inanObjs[i].drawFrame(canvas,offset_x,offset_y);
            Rect temp_drawBox = new Rect(inanObjs[i].drawBox);
            temp_drawBox.offset(-offset_x,-offset_y);
            if((temp_drawBox.left >= 0 && temp_drawBox.right <= canvas.getWidth()) &&  (temp_drawBox.top >= 0 && temp_drawBox.bottom <= canvas.getHeight()) ) {
                canvas.drawBitmap(
                        SpriteMaps.get(GameObject.GameObjectTypes.INANOBJECT.ordinal())[inanObjs[i].getAnimationRowIndex()][inanObjs[i].getAnimationColIndex()],
                        null,
                        temp_drawBox,
                        null
                );
            }
        }

        // Draw Npcs
        for(int i=0;i<npcs.length;i++){
            if(npcs[i].getHealth() > 0) {
                npcs[i].drawFrame(canvas,offset_x,offset_y);
            }

        }

        // Draw Players
        for(int i=0;i<players.length;i++) {
            players[i].drawFrame(canvas,offset_x,offset_y);
            //Rect temp_drawBox = new Rect(players[i].drawBox);
//            temp_drawBox.offset(-offset_x,-offset_y);
//            if((temp_drawBox.left >= 0 && temp_drawBox.right <= canvas.getWidth()) &&  (temp_drawBox.top >= 0 && temp_drawBox.bottom <= canvas.getHeight()) ) {
//                canvas.drawBitmap(
//                        SpriteMaps.get(GameObject.GameObjectTypes.PLAYER.ordinal())[players[i].getAnimationRowIndex()][players[i].getAnimationColIndex()],
//                        null,
//                        temp_drawBox,
//                        null
//                );
//            }

        }
        if (EventActivated) {
            canvas.drawBitmap(speechbox, null, new Rect(0, canvas.getHeight() - 400, canvas.getWidth() - 300, canvas.getHeight()), null);
        }
    }




    public Player getPlayer(){
        return players[0];
    }

    public boolean isEventActivated() {return EventActivated;}
    public void setEventActivated(boolean eventActivated) {EventActivated = eventActivated;}
}
