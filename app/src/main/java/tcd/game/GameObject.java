package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Map;

import static java.lang.Math.abs;

// TODO: Discuss with team
// GameObject.Context and Canvas width and heights could be static and set somewhere in game mode
// to avoid having to pass it in to all of our constructors?

/** Fair bit of redundancy in this class but its all commented out in case we want to use it
 * TODO: Discuss with team
 */

// added by Rowan 5th March
// Changes to GameObjectAnimationMaxIndex and addition of dividedSpriteMap
// GameObjectAnimationMaxIndex removed since can just get ordinal of GameObjectAnimationDirection


public class GameObject {
    protected String TAG = "GameObject";

    // Used for generating GameObjects IDS
    private static int totalGameObjs = 0;

    /** Unique ID for each GameObject */
    protected int id;


    /** Application Context to access Drawable Resources */
    protected Context context;

    // GameObject properties
    protected String name;
    protected Bitmap spriteMap;
    protected Bitmap [][]dividedSpriteMap; // 2-d array containing a bitmap for each frame of animation

    protected int velX,velY;
    protected int speed;

    /** Box used for updating positions and collision checking */
    protected Rect collisionBox;
    protected Rect drawBox;

    /** Width of Screen */
//    protected int canvasWidth;

    /** Height of Screen */
//    protected int canvasHeight;

    /** Equivalant rectangle to screen size */
    protected Rect canvasRect;

    /** Rectangle the size of the map */
    protected Rect mapRect;

    /** Flag indicating whether object can be passed through */
    protected boolean collisionOn;

    /** Flag indicating whether this GameObject can move (May be redundant) */
    protected boolean movable;

    protected boolean moving;
    protected int goalX, goalY;
    protected int deltaX, deltaY;
    protected int gridSize;
    protected int gridX, gridY;




    /** SpriteMap row index */
    protected int animationRowIndex;
    protected int maxAnimationRowIndex;

    /** SpriteMap Col index */
    protected int animationColIndex;
    protected int maxAnimationColIndex;

    protected int loops;
    protected int animationSpeed;

    protected int spritesWide;
    protected int spritesTall;

    protected int blink;
    protected int blink_speed;

//    protected Paint colour;

    protected GameObjectAnimationDirection facing;

    /** Width of SpriteMap column (width each individual sprite) */
    protected int cropWidth;

    /** Height of SpriteMap row (height of each individual sprite) */
    protected int cropHeight;

    /** Used if we want to make collision (and thus draw) boxes smaller than crop boxes */
    protected float drawScaleFactor;
    protected float colScaleFactor;


    /** Calculated by cropHeight and scallFactor */
    protected int drawWidth;
    protected int drawHeight;

    /** Fixed  */
    protected int colWidth;
    protected int colHeight;
    protected int colWidthBuffer;
    protected int colHeightBuffer;


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
        this.gridSize = 50;
        // Loading default Sprites for each GameObject if not passed in
        if(type == GameObjectTypes.PLAYER){
            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.player_default);
            spritesWide = 10;
            spritesTall = 8;
            drawScaleFactor = 0.25f;
            colScaleFactor = 0.8f;
            facing = GameObjectAnimationDirection.FACING_RIGHT;
        } else if (type == GameObjectTypes.INANOBJECT){
            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.inan_default);
            spritesWide = 1;
            spritesTall = 1;
            drawScaleFactor = 0.5f;
            colScaleFactor = 0.8f;
            facing = GameObjectAnimationDirection.FACING_DOWN;
        } else if(type == GameObjectTypes.NPC){
            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.npc_default);
            spritesWide = 10;
            spritesTall = 8;
            drawScaleFactor = 0.25f;
            colScaleFactor = 0.8f;
            facing = GameObjectAnimationDirection.FACING_DOWN;
        }

        setCrop(spriteMap.getWidth()/spritesWide,spriteMap.getHeight()/spritesTall); //sets the sprite width and height variables
        setDraw(cropWidth, cropHeight, drawScaleFactor);
        setCol(drawWidth, drawHeight, colScaleFactor);


        animationColIndex = 0;
        maxAnimationColIndex = 0;
        animationRowIndex = 0;
        loops = 0;
        animationSpeed = 25;
        blink_speed = 25;

        dividedSpriteMap = new Bitmap[spritesTall][spritesWide]; //This 2-d array will store the split up frames from the sprite sheet

        // Divide up sprite sheet into 2d array of Bitmap objects for each individual sprite
        for (int i = 0; i < spritesTall; i++){
            for (int j = 0; j < spritesWide; j++){
                dividedSpriteMap[i][j] = Bitmap.createBitmap(spriteMap, j*spriteMap.getWidth()/spritesWide, i*spriteMap.getHeight()/spritesTall, spriteMap.getWidth()/spritesWide, spriteMap.getHeight()/spritesTall);
            }
        }

        collisionBox = new Rect(0,0,colWidth,colHeight);
        drawBox = new Rect (0,0, drawWidth, drawHeight);

        velX = 0;
        velY = 0;
        this.moving = false;
        speed = 6;

    }

    public void setSprite(Bitmap bitmap){
        this.spriteMap = bitmap;
    }

    public void setCrop(int width, int height){
        this.cropWidth = width;
        this.cropHeight = height;
    }

    public void setDraw(int width, int height, float scaleFactor){
//        this.drawWidth = width;
//        this.drawHeight = height;
        this.drawWidth = Math.round(width * scaleFactor);
        this.drawHeight = Math.round(height * scaleFactor);
    }

    public void setCol(int width, int height, float scaleFactor){
        this.colWidth = Math.round(width * scaleFactor);
        this.colHeight = Math.round(height * scaleFactor);
        this.colWidthBuffer = Math.round( ( this.drawWidth - this.colWidth ) / 2);
        this.colHeightBuffer = Math.round( ( this.drawHeight - this.colHeight ) / 2);

    }

    public void setPosX(int posX) {
        this.drawBox.left = posX;
        this.drawBox.right = posX + drawWidth;

        this.collisionBox.left = posX + colWidthBuffer;
        this.collisionBox.right = posX + colWidth + colWidthBuffer;
    }

    public void setPosY(int posY) {
        this.drawBox.top = posY;
        this.drawBox.bottom = posY + drawHeight;

        this.collisionBox.top = posY + colHeightBuffer;
        this.collisionBox.bottom = posY + colHeight + colHeightBuffer;
    }

    public void setGridPos(int posX, int posY) {
        this.drawBox.left = posX*gridSize;
        this.drawBox.right = this.drawBox.left + drawWidth;
        this.drawBox.top = posY*gridSize;
        this.drawBox.bottom = this.drawBox.top + drawHeight;

        this.collisionBox.left = this.drawBox.left + colWidthBuffer;
        this.collisionBox.right = this.collisionBox.left + colWidth;
        this.collisionBox.top = this.drawBox.top + colHeightBuffer;
        this.collisionBox.bottom = this.collisionBox.top + colHeight;
    }

    public void setGrifX(int posX) {
        gridX = posX;
        this.drawBox.left = posX*gridSize;
        this.drawBox.right = this.drawBox.left + drawWidth;

        this.collisionBox.left = this.drawBox.left + colWidthBuffer;
        this.collisionBox.right = this.collisionBox.left + colWidth;
    }

    public void setGrifY(int posY) {
        gridY = posY;
        this.drawBox.top = posY*gridSize;
        this.drawBox.bottom = this.drawBox.top + drawHeight;

        this.collisionBox.top = this.drawBox.top + colHeightBuffer;
        this.collisionBox.bottom = this.collisionBox.top + colHeight;
    }

    public void setVelX(int velX) {
        if(this.velX != velX && !this.moving){
            this.loops = 0;
            this.moving = true;
            this.velX = velX;

            this.deltaX = gridSize*velX;
            this.deltaY = gridSize*velY;

//            this.goalX = this.drawBox.left + this.deltaX;
//            this.goalY = this.drawBox.top + this.deltaY;
            this.goalX = this.drawBox.left + gridSize*velX;
            this.goalY = this.drawBox.top + gridSize*velY;
            gridX += velX;
        }
    }

    public void setVelY(int velY) {
        if(this.velY != velY && !this.moving){
            this.loops = 0;
            this.moving = true;
            this.velY = velY;

            this.deltaX = gridSize*velX;
            this.deltaY = gridSize*velY;

            this.goalX = this.drawBox.left + gridSize*velX;
            this.goalY = this.drawBox.top + gridSize*velY;
            gridY += velY;

        }
    }

    private void move(){
//||(abs(goalX-drawBox.left) > abs(25*velX) || (abs(goalY-drawBox.top) > abs(25*velY) ))
        if(moving){
            if((abs(deltaX) <= abs(velX*speed) && abs(deltaY) <= abs(velY*speed))) {
                drawBox.offset(deltaX, deltaY);
                collisionBox.offset(deltaX, deltaY);
                deltaX = deltaY = 0;
                velX = velY = 0;
                this.moving = false;

            }else{
                drawBox.offset(velX*speed, velY*speed);
                collisionBox.offset(velX*speed, velY*speed);
                deltaX -= velX*speed;
                deltaY -= velY*speed;
            }
//            drawBox.left += velX*speed;
//            drawBox.right += velX*speed;
//            drawBox.top += velY*speed;
//            drawBox.bottom += velY*speed;
//
//            collisionBox.left += velX*speed;
//            collisionBox.right += velX*speed;
//            collisionBox.top += velY*speed;
//            collisionBox.bottom += velY*speed;
        }
    }

    private void unmove(){

        velX = -velX;
        velY = -velY;
        drawBox.offset(velX*speed, velY*speed);
        collisionBox.offset(velX*speed, velY*speed);
        velX = 0;
        velY = 0;
        this.moving = false;
//
//        drawBox.left -= velX*speed;
//        drawBox.right -= velX*speed;
//        drawBox.top -= velY*speed;
//        drawBox.bottom -= velY*speed;
//
//        collisionBox.left -= velX*speed;
//        collisionBox.right -= velX*speed;
//        collisionBox.top -= velY*speed;
//        collisionBox.bottom -= velY*speed;
    }

    public void update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Player, Integer> objMap){
        //move object by velocity
        if()
        move();

        //check for collision
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
        canvas.drawBitmap(
                dividedSpriteMap[animationRowIndex][animationColIndex], null,
                drawBox,
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

    public enum GameObjectAnimationDirection { //NB This only applies to NPC and Player Spritesheets
        FACING_DOWN(3),
        FACING_LEFT(3),
        FACING_UP(1),
        FACING_RIGHT(3),
        MOVING_DOWN(10),
        MOVING_LEFT(10),
        MOVING_UP(10),
        MOVING_RIGHT(10),
        TOTAL(0);

        private int value;

        GameObjectAnimationDirection(int value) {
            this.value = value;
        }

        public int getVal(){return value;} // to return the values in the brackets
    }
}
