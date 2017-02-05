package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

// TODO: Discuss with team
// GameObject.Context and Canvas width and heights could be static and set somewhere in game mode
// to avoid having to pass it in to all of our constructors?

/** Fair bit of redundancy in this class but its all commented out in case we want to use it
 * TODO: Discuss with team
 */
public class GameObject {
    private String TAG = "GameObject";

    // Used for generating GameObjects IDS
    private static int totalGameObjs = 0;

    /** Unique ID for each GameObject */
    private int id;


    /** Application Context to access Drawable Resources */
    private Context context;

    // GameObject properties
    private String name;
    private Bitmap spriteMap;
//    private int posX,posY;

    private int velX,velY;

    /** Box used for updating positions and collision checking */
    private Rect collisionBox;

    /** Width of Screen */
//    private int canvasWidth;

    /** Height of Screen */
//    private int canvasHeight;

    /** Equivalant rectangle to screen size */
    private Rect canvasRect;

    /** Rectangle the size of the map */
    private Rect mapRect;

    /** Flag indicating whether object can be passed through */
    private boolean collisionOn;

    /** Flag indicating whether this GameObject can move (May be redundant) */
    private boolean movable;


    /** SpriteMap row index */
    private int animationRowIndex;

    /** SpriteMap Col index */
    private int animationColIndex;

    /** Width of SpriteMap column (width each individual sprite) */
    private int cropWidth;

    /** Height of SpriteMap row (height of each individual sprite) */
    private int cropHeight;

    /** Used if we want to make collision (and thus draw) boxes smaller than crop boxes */
    private float scaleFactor;

    /** Calculated by cropWidth and scallFactor */
    private int drawWidth;

    /** Calculated by cropHeight and scallFactor */
    private int drawHeight;



    GameObject(){
        name = "Null-Man";

    }

    /**
     * Creates a Game Object which contains methods for updating and drawing frames
     * @param context Application context
     * @param name Name of Object
     * @param type The type of GameObject child
     * @param canvasWidth The width of the screen
     * @param canvasHeight
     */
    GameObject(Context context, String name, GameObjectTypes type,int canvasWidth, int canvasHeight){
        this.id = totalGameObjs;
        totalGameObjs++;
        canvasRect = new Rect(0,0,canvasWidth,canvasHeight);
        mapRect = canvasRect; //for now
        this.name = name;
        this.context = context;

        // Loading default Sprites for each GameObject if not passed in
        if(type == GameObjectTypes.PLAYER){
            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.player_default);
        } else if (type == GameObjectTypes.INANOBJECT){
            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.inan_default);
        } else if(type == GameObjectTypes.NPC){
            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.npc_default);
        }

        setCrop(spriteMap.getWidth(),spriteMap.getHeight(),0.5f);


        animationColIndex = 0;
        animationRowIndex = 0;

        collisionBox = new Rect(0,0,drawWidth,drawHeight);

        velX = 0;
        velY = 0;

//
//        posX = 0;
//        posY = 0;


//        drawBox = collisionBox;
//        cropBox = null;


//        drawWidth = 60;
//        drawHeight = 60;

//        colWidth = 60;
//        colHeight = 60;

    }










    public void setSprite(Bitmap bitmap){
        this.spriteMap = bitmap;
    }


    public void setCrop(int width, int height, float scaleFactor){
        this.cropWidth = width;
        this.cropHeight = height;
        setDrawDimensions(width,height,scaleFactor);
    }


    private void setDrawDimensions(int width, int height, float scaleFactor){
        this.scaleFactor = scaleFactor;
        this.drawWidth = Math.round(width * scaleFactor);
        this.drawHeight = Math.round(height * scaleFactor);;
    }




//    public void setPosX(int posX) {
//        this.posX = posX;
//    }
//
//    public void setPosY(int posY) {
//        this.posY = posY;
//    }
//
//    public void setVelX(int velX) {
//        this.velX = velX;
//    }
//
//    public void setVelY(int velY) {
//        this.velY = velY;
//    }

    public void setPosX(int posX) {
        this.collisionBox.left = posX;
        this.collisionBox.right = posX + drawWidth;
    }

    public void setPosY(int posY) {
        this.collisionBox.top = posY;
        this.collisionBox.bottom = posY + drawHeight;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    private void move(){
//        this.posX += this.velX;
//        this.posY += this.velY;
        collisionBox.left += velX;
        collisionBox.right += velX;
        collisionBox.top += velY;
        collisionBox.bottom += velY;
    }

    private void unmove(){
//        this.posX -= this.velX;
//        this.posY -= this.velY;
        collisionBox.left -= velX;
        collisionBox.right -= velX;
        collisionBox.top -= velY;
        collisionBox.bottom -= velY;

    }


    public void update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type){

        //TODO: Update animation draw frame if required
        

        move();


        for(int i=0;i<players.length;i++){
            if(this.collision(players[i])){
                if(players[i].getID() != this.getID()){
                    unmove();
                    return;
                }
            }
        }

        for(int i=0;i<npcs.length;i++){
            if(this.collision(npcs[i])){
                if(npcs[i].getID() != this.getID()){
                    unmove();
                    return;
                }
            }
        }

        for(int i=0;i<inanObjects.length;i++){
            if(this.collision(inanObjects[i])){
                if(inanObjects[i].getID() != this.getID()){
                    unmove();
                    return;
                }
            }
        }

    }

    private boolean collision(GameObject gameObject){

        //Rectangle intersection
        if(canvasRect.contains(this.collisionBox)) {
            if(this.collisionBox.intersects(gameObject.collisionBox.left,gameObject.collisionBox.top, gameObject.collisionBox.right,gameObject.collisionBox.bottom)) {
                return true;
            } else {
                return false;
            }
        }
        return true;

    }

    public void drawFrame(Canvas canvas){

//        Paint paint = new Paint(Color.RED);
//        canvas.drawRect(collisionBox, paint);

        canvas.drawBitmap(
                spriteMap,
                new Rect(
                        animationColIndex * cropWidth,
                        animationRowIndex * cropHeight,
                        animationColIndex * cropWidth + cropWidth,
                        animationRowIndex * cropHeight + cropHeight
                ),
                collisionBox,
                null
        );
    }

    public int getID(){
        return this.id;
    }

    public enum GameObjectTypes {
        PLAYER,
        NPC,
        INANOBJECT,
        TOTAL
    }
}
