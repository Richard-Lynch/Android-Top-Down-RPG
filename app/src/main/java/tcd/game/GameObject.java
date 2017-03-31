package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

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
    private static int totalGameObjs = 1;

    /** Unique ID for each GameObject */
    protected int id;

    // Added by Stefano -- required by Map class
    // May need two IDS as id tracks what GameObject they are
    // DatabaseID tracks there ID in a relevant table in database
    protected short databaseID;


    /** Application Context to access Drawable Resources */
    protected Context context;

    // GameObject properties
    protected String name;
    protected Bitmap spriteMap;
    protected Bitmap [][]dividedSpriteMap; // 2-d array containing a bitmap for each frame of animation

    protected int velX,velY;
    protected int speed;

    protected boolean hasEvent;

    protected int eventID;

    public String getEventText() {
        return eventText;
    }

    public void setEventText(String eventText) {
        this.eventText = eventText;
    }

    protected String eventText;

    protected int health;
    protected boolean IsAlive;




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

    protected int cells_wide = 32;
    protected int cells_tall = 18;

    protected boolean moving;
    protected int goalX, goalY;
    protected int deltaX, deltaY;
    protected int gridSize, gridWide,gridHeight;
    protected int gridX, gridY;
    protected boolean gridUnset;
    protected boolean collided;




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
//    protected int colWidth;
//    protected int colHeight;
//    protected int colWidthBuffer;
//    protected int colHeightBuffer;

    public int getDrawWidth(){
        return this.drawWidth;
    }
    public int getDrawHeight(){
        return  this.drawHeight;
    }

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
    GameObject(Context context, String name, GameObjectTypes type,int canvasWidth, int canvasHeight,int mapWidth, int mapHeight){
        this.id = totalGameObjs;
        totalGameObjs++;
        canvasRect = new Rect(0,0,canvasWidth,canvasHeight);
        mapRect = new Rect(0,0,mapWidth,mapHeight);
        this.name = name;
        this.context = context;
        // Loading default Sprites for each GameObject if not passed in
        if(type == GameObjectTypes.PLAYER){
//            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.player_default);
            IsAlive = true;
            health = 100;
            spritesWide = 10;
            spritesTall = 8;
            drawScaleFactor = 0.25f;
            this.setSprite(BitmapFactory.decodeResource(context.getResources(),R.drawable.player_default));
            facing = GameObjectAnimationDirection.FACING_RIGHT;
        } else if (type == GameObjectTypes.INANOBJECT){
//            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.inan_default);
            health = -1;
            spritesWide = 1;
            spritesTall = 1;
            drawScaleFactor = 0.5f;
            this.setSprite(BitmapFactory.decodeResource(context.getResources(),R.drawable.inan_default));
            facing = GameObjectAnimationDirection.FACING_DOWN;
        } else if(type == GameObjectTypes.NPC){
//            spriteMap = BitmapFactory.decodeResource(context.getResources(),R.drawable.npc_default);
            IsAlive = true;
            health = 100;
            spritesWide = 10;
            spritesTall = 8;
            drawScaleFactor = 0.25f;
            this.setSprite(BitmapFactory.decodeResource(context.getResources(),R.drawable.npc_default));
            facing = GameObjectAnimationDirection.FACING_DOWN;
        }

//hard coded constants ( may be passed from map read, but probably the same for all sprites ( maybe not for Inan? )
        animationColIndex = 0;
        maxAnimationColIndex = 0;
        animationRowIndex = 0;
        loops = 0;
        animationSpeed = 16;
        blink_speed = 16;


        //grid
        // should really call a draw width here
        //TODO should call a setDraw method here
        //TODO idealy we want to do something with aspect ratio here

        drawWidth = canvasWidth/cells_wide;
        //drawHeight = canvasHeight/cells_tall;
        drawHeight = drawWidth;

        gridWide = (int)Math.round((double)mapWidth/(double)drawWidth);
        gridHeight = (int)Math.round((double)mapHeight/(double)drawWidth);

        gridSize = drawWidth;

        //gridSize = mapWidth/cells_wide;

        collided  = false;

        setCrop(spriteMap.getWidth()/spritesWide,spriteMap.getHeight()/spritesTall); //sets the sprite width and height variables

        drawBox = new Rect (0,0, drawWidth, drawHeight);

        velX = 0;
        velY = 0;
        this.moving = false;
        speed = 12;

    }


    public Bitmap[][] setSprite(Bitmap bitmap){
        this.spriteMap = bitmap;
        dividedSpriteMap = new Bitmap[spritesTall][spritesWide]; //This 2-d array will store the split up frames from the sprite sheet

        // Divide up sprite sheet into 2d array of Bitmap objects for each individual sprite
        for (int i = 0; i < spritesTall; i++){
            for (int j = 0; j < spritesWide; j++){
                dividedSpriteMap[i][j] = Bitmap.createBitmap(bitmap, j*bitmap.getWidth()/spritesWide, i*bitmap.getHeight()/spritesTall, bitmap.getWidth()/spritesWide, bitmap.getHeight()/spritesTall);
            }
        }
        return dividedSpriteMap;
    }

    public void setCrop(int width, int height){
        this.cropWidth = width;
        this.cropHeight = height;
    }

    public void setGridPos(int posX, int posY) {
        this.setGridX(posX);
        this.setGridY(posY);
    }

    public void setGridX(int posX) {
        this.gridX = posX;
        this.drawBox.left = posX*drawWidth;
        this.drawBox.right = this.drawBox.left + drawWidth;
    }

    public void setGridY(int posY) {
        this.gridY = posY;
        this.drawBox.top = posY*drawHeight;
        this.drawBox.bottom = this.drawBox.top + drawHeight;
    }



    public void setVelX(int velX) {
        if(this.velX != velX && !this.moving){
            if(!this.collided){
                this.loops = 0;
            }
            if(velX != 0){
                this.moving = true;
                this.gridUnset = true;
            }

            this.velX = velX;

            this.deltaX = drawWidth*velX;
            this.deltaY = drawHeight*velY;

            this.goalX = this.drawBox.left + drawWidth*velX;
            this.goalY = this.drawBox.top + drawHeight*velY;
        }
    }

    public void setVelY(int velY) {
        if(this.velY != velY && !this.moving){
            if(!this.collided){
                this.loops = 0;
            }
            if(velY != 0){
                this.moving = true;
                this.gridUnset = true;
            }
            this.velY = velY;

            this.deltaX = drawWidth*velX;
            this.deltaY = drawHeight*velY;

            this.goalX = this.drawBox.left + drawWidth*velX;
            this.goalY = this.drawBox.top + drawHeight*velY;
        }
    }

    public int getPosX(){
        return drawBox.left;
    }

    public int getPosY(){
        return drawBox.top;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
        this.eventText = "Default Text";
        this.hasEvent = true;
    }

    /**Called when an event is activated on this game object.
     *
     * @param type: 0 = Object been attacked, 1 = object been interacted with
     * @param attack_power  If the event is an attack, this is the power of it
     * @returns 0 = health has been decreased, -1 = Object does not have event, otherwise returns the event ID associated with the object
     */
    public int OnEvent(int type, int attack_power){
            switch (type){

                //Event is an attack on the object
                case 0:
                    this.health = this.health - attack_power;
                    Log.d(TAG, "health decreased by " + attack_power);
                    return 0;

                //Event is an interaction
                case 1:
                    if(this.hasEvent){
                        return this.id;
                    }
                    else {
                        return -1;
                    }
                default:
                    return -1;

            }
    }

    public Coordinates getCoordinates(){
        return new Coordinates(this.gridX, this.gridY);
    }

    private void move(){
        if(moving){
            if((abs(deltaX) <= abs(velX*speed) && abs(deltaY) <= abs(velY*speed))) {    //prevents overshoot
                drawBox.offset(deltaX, deltaY);
                deltaX = deltaY = 0;
                this.moving = false;
                setVelX(0);
                setVelY(0);

            }else{
                drawBox.offset(velX*speed, velY*speed);
                deltaX -= velX*speed;
                deltaY -= velY*speed;
            }
        }
    }

    private int directionX(){
        if( facing == GameObjectAnimationDirection.FACING_RIGHT){
            return 1;
        } else if(facing == GameObjectAnimationDirection.FACING_LEFT){
            return -1;
        } else {
            return 0;
        }
    }

    private int directionY(){
        if(facing == GameObjectAnimationDirection.FACING_DOWN) {
            return 1;
        } else if(facing == GameObjectAnimationDirection.FACING_UP){
            return -1;
        } else {
            return 0;
        }
    }

    private int action(Map<Integer, GameObject> objMap,Map<Integer, Integer> colMap,int EventType){
        int dirX = directionX();
        int dirY = directionY();

        int IdOfObject = colMap.get(new Coordinates(this.gridX + dirX, this.gridY + dirY).hashCode());

        switch (EventType){
            case 0:
                return (objMap.get(IdOfObject)).OnEvent(0, 50);
            case 1:
                return (objMap.get(IdOfObject)).OnEvent(1, 0);
            default:
                return -1;
        }
    }


    public int update(Player players[], NPC npcs[], InanObject inanObjects[], int id, GameObjectTypes type, Map<Integer, Integer> colMap, Map<Integer, GameObject> objMap){
        //move object by velocity
        if(health < 1 && type != GameObjectTypes.INANOBJECT){
            if(IsAlive) {
                IsAlive = false;
                colMap.remove((new Coordinates(this.gridX, this.gridY).hashCode()));
                objMap.remove(id);
                hasEvent = false;
            }
        }
        if(this.moving){
            if(gridUnset){
                if((gridX+velX < 0 || gridX+velX >= gridWide) || (gridY+velY < 0 || gridY+velY >= gridHeight)){
                    this.moving = false;
                    deltaX = deltaY = 0;
                    this.collided = true;
                    setVelX(0);
                    setVelY(0);

                    Log.d(TAG, "gone off the map");
                }
                else if(colMap.get(new Coordinates(this.gridX+this.velX, this.gridY+this.velY).hashCode()) == (null)){
                    this.collided = false;
                    Log.d(TAG, "no item in front of me! setting new grid Pos");
                    colMap.put(new Coordinates(this.gridX,this.gridY).hashCode(), null);
                    this.gridX += velX;
                    this.gridY += velY;
                    Log.d(TAG, "now im at "+ gridX + " " + gridY);
                    colMap.put(new Coordinates(this.gridX,this.gridY).hashCode(), this.getID());
                    Log.d(TAG, "I stored my grid pos");
                    move();
                }else if(colMap.get(new Coordinates(this.gridX+velX, this.gridY+velY).hashCode()) != this.getID()){
                    this.collided = true;
                    Log.d(TAG, "oh DAMN item in front of me! not moving");
                    moving = false;
                    setVelX(0);
                    setVelY(0);
                }
                gridUnset = false;
            }
            else{
                Log.d(TAG, "just moving");
                move();
            }

            if(collided){
                collided = false;
                return -1;
            }
        }
        else if(players[0].isA_pressed() && colMap.get(new Coordinates(this.gridX+directionX(), this.gridY+directionY()).hashCode()) != (null)) {

            Log.d(TAG, "Event ID returned to main" + action(objMap,colMap,1));
            return action(objMap,colMap,1); //Returns the event ID to run

        }
        else if(players[0].isB_pressed() && colMap.get(new Coordinates(this.gridX+directionX(), this.gridY+directionY()).hashCode()) != (null)) {

            Log.d(TAG, "Attack has happened, returning 0");
            return action(objMap, colMap, 0); //Returns 0 to signify that health has been decreased on object
        }
            //Log.d(TAG, "returned -1 no event happened");
            return -1; //If no event happened, -1 returned
    }



    public void drawFrame(Canvas canvas,int offset_x,int offset_y){
        //put this in game mode -> change drawbox -> translations
        Rect temp_drawBox = new Rect(drawBox);
        temp_drawBox.offset(-offset_x,-offset_y);
        if((temp_drawBox.left >= 0 && temp_drawBox.right <= canvas.getWidth()) &&  (temp_drawBox.top >= 0 && temp_drawBox.bottom <= canvas.getHeight()) ) {
            canvas.drawBitmap(
                    dividedSpriteMap[animationRowIndex][animationColIndex],
                    null,
                    temp_drawBox,
                    null
            );
        }
    }

    public int getAnimationRowIndex(){
        return animationRowIndex;
    }

    public int getAnimationColIndex(){
        return animationColIndex;
    }

    public int getID(){
        return this.id;
    }

    public int getHealth() {
        return health;
    }
    public boolean isAlive() {return IsAlive;}

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

    // Added by Stefano (required by WorldMap class)
    public void setInfo(String[] s){
        this.name = s[1];
    }

    public void setDatabaseID(short databaseID){
        this.databaseID = databaseID;
    }

    public short getDatabaseID(){
        return databaseID;
    }

}
