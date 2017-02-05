package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;


public class GameMode {
    private final static String TAG = "GameMode";

    // Context is passed ot GameObjects for access to drawable resources
    private Context context;
    private int canvasWidth, canvasHeight;

    // Map should probably have its own class for Reading from file etc
    private Bitmap map;

    // Declare Arrays of our GameObjects
    private InanObject inanObjs[];
    private NPC npcs[];
    private Player players[];

    // Probably should have OpenWorld and Battle Classes  which extend this so dont need flag
    // private boolean battle;


    //TODO: Get Number of NPCS/Inans/Players from map class and initialize arrays accordingly or create data-structure



    /***********************************************************************************
     * Constructors
     ***********************************************************************************/

    /**
     * Creates a Game Mode
     * Must be initialized using gameMode.initialize()
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
    GameMode(Context context, int canvasWidth, int canvasHeight){
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
     * @param screenWidth
     * @param screenHeight
     */
    public void initialize(int screenWidth,int screenHeight){
        this.canvasWidth = screenWidth;
        this.canvasHeight = screenHeight;
        init(0);
    }



    private void init(double levelID){

        map = BitmapFactory.decodeResource(context.getResources(),R.drawable.background);
        players = new Player[1];
        npcs = new NPC[1];
        inanObjs = new InanObject[1];


        players[0] = new Player(context,"Donal", canvasWidth, canvasHeight);
        npcs[0] = new NPC(context,"Frank",canvasWidth, canvasHeight);
        inanObjs[0] = new InanObject(context,"House",canvasWidth,canvasHeight);
        players[0].setPosX(0);
        players[0].setPosY(50);
        players[0].setVelX(5);
        players[0].setVelY(0);
        players[0].setSprite(BitmapFactory.decodeResource(context.getResources(),R.drawable.player_default));

        npcs[0].setPosX(100);
        npcs[0].setPosY(150);
        npcs[0].setVelX(6);
        npcs[0].setVelY(0);

        inanObjs[0].setPosX(400);
        inanObjs[0].setPosY(150);
        inanObjs[0].setVelX(0);
        inanObjs[0].setVelY(0);

    }

    /**
     * Calls update methods for all game objects
     */
    public void update(){

        // Update Player positions
        for(int i=0;i<players.length;i++) {
            players[i].update(players, npcs, inanObjs, players[i].getID(), GameObject.GameObjectTypes.PLAYER);
        }

        // Update NPC positions
        for(int i=0;i<npcs.length;i++){
            npcs[i].update(players,npcs,inanObjs,npcs[i].getID(),GameObject.GameObjectTypes.NPC);
        }

        // Update Inanimate Object Positions
        for(int i=0;i<inanObjs.length;i++){
            inanObjs[i].update(players,npcs,inanObjs,inanObjs[i].getID(), GameObject.GameObjectTypes.INANOBJECT);
        }
    }


    /**
     * Calls draw method for all game objects
     * @param canvas
     */
    public void drawFrame(Canvas canvas){

        // Drawing Map -- should be else where possibly
        canvas.drawBitmap(map,null, new Rect(0,0,canvas.getWidth(),canvas.getHeight()), null);

        // Draw InanimateObjects
        for(int i=0;i<inanObjs.length;i++){
            inanObjs[i].drawFrame(canvas);
        }

        // Draw Npcs
        for(int i=0;i<npcs.length;i++){
            npcs[i].drawFrame(canvas);
        }

        // Draw Players
        for(int i=0;i<players.length;i++) {
            players[i].drawFrame(canvas);
        }
    }
}
