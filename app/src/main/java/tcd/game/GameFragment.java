package tcd.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.os.Vibrator;

/**
 * This class creates a game fragment that can be embedded into an activity.
 * This class will update all of the objects to be drawn to the n'th frame on a separate thread,
 * while drawing the n-1th frame using the hardware accelerated GUI thread.
 */

public class GameFragment extends Fragment{
    private static final String TAG = "GameFragment";
    private final int TARGET_FPS = 60;

    private GameRenderer gameRenderer;
    private GameMode gameMode;
    private Vibrator Vib;

    // Controls
    private Rect upRect, downRect,leftRect,rightRect, A_rect, B_rect;
    private Rect upRect_area, downRect_area,leftRect_area,rightRect_area, A_rect_area, B_rect_area;
    private Bitmap controlIconUp, controlIconDown, controlIconLeft, controlIconRight, A_Button, B_Button;


    // As the game loop runs on separate thread, it starts before objects get fully initialized
    // We can let it loop but block the updating and drawing until it is ready
    // TODO: This doesn't full work as intended - see try catch for NPE inside gameloop
    private boolean gameInitialized;

    // FPS test
    private long time;
    private int fps;
    private int numCalls;
    private Paint paint;


    /***********************************************************************************
     * Lifecycle Hooks
     ***********************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        gameInitialized = false;
        gameRenderer = new GameRenderer(getActivity(), TARGET_FPS);


        //Fps Monitor
        time = 0;
        paint = new Paint(Color.RED);
        paint.setTextSize(36.0f);
        paint.setTextAlign(Paint.Align.LEFT);

        gameRenderer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = Math.round(event.getX());
                int y = Math.round(event.getY());
                int tolerance = 5;
                Rect touchLoc = new Rect(x,y,x+tolerance,y+tolerance);

                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG,"Moving true");
                    if(gameMode.isEventActivated() == false){
                    if (touchLoc.intersect(upRect_area)) {
                        gameMode.getPlayer().setUp_pressed(true);
                        controlIconUp = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_up_pressed);
                        } else if (touchLoc.intersect(downRect_area)) {
                                gameMode.getPlayer().setDown_pressed(true);
                                controlIconDown = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_down_pressed);
                        } else if(touchLoc.intersect(rightRect_area)){
                                gameMode.getPlayer().setRight_pressed(true);
                                controlIconRight = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_right_pressed);
                         } else if(touchLoc.intersect(leftRect_area)) {
                                gameMode.getPlayer().setLeft_pressed(true);
                                controlIconLeft = BitmapFactory.decodeResource(getResources(), R.drawable.arrow_left_pressed);
                        }
                    }
                        if(touchLoc.intersect(A_rect)){
                            A_Button = BitmapFactory.decodeResource(getResources(), R.drawable.a_button_not);
                            gameMode.getPlayer().setAPressed(true);
                        } else if (touchLoc.intersect(B_rect)){
                            B_Button = BitmapFactory.decodeResource(getResources(), R.drawable.b_button_not);
                            gameMode.getPlayer().setBPressed(true);
                            gameMode.setEventActivated(false);
                    }

                } else if(event.getAction() == MotionEvent.ACTION_UP){
                    Log.d(TAG,"Moving false");
                    gameMode.getPlayer().setAllVelFalse();
                    gameMode.getPlayer().setAllButFalse();
                    restoreBitmaps();;
                    //A_Button = BitmapFactory.decodeResource(getResources(), R.drawable.a_button);
                    //B_Button = BitmapFactory.decodeResource(getResources(),R.drawable.b_button);
                }

                return true;
            }
        });
        return gameRenderer;
    }

    // Gives access to screen dimensions when they are ready
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Initializing: in onActCreate");

        getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d(TAG, "Initializing: in onGlobalLayout");
                int width = getView().getWidth();
                int height = getView().getHeight();
                createControls(width, height);
                initializeGameMode(width, height);
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onPause() {
        Log.d(TAG, "Pausing");
        super.onPause();
        gameRenderer.pause();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "Resuming");
        super.onResume();
        gameRenderer.resume();
    }

    private void restoreBitmaps(){
        controlIconUp = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_up);
        controlIconDown = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_down);
        controlIconLeft = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_left);
        controlIconRight = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_right);

        A_Button = BitmapFactory.decodeResource(getResources(), R.drawable.a_button);
        B_Button = BitmapFactory.decodeResource(getResources(),R.drawable.b_button);
    }

    /***********************************************************************************
     * Controls
     **********************************************************************************/
    private void createControls(int width, int height){
        controlIconUp = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_up);
        controlIconDown = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_down);
        controlIconLeft = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_left);
        controlIconRight = BitmapFactory.decodeResource(getResources(),R.drawable.arrow_right);

        A_Button = BitmapFactory.decodeResource(getResources(), R.drawable.a_button);
        B_Button = BitmapFactory.decodeResource(getResources(),R.drawable.b_button);


        // Size of a single direction button is 1/18th of max screen size,
        // since that device is forced in landscape mode
        int eleSize = (int) (width / 18.0);

        // Elements are drawn a half element away from the screen,
        // in the bottom left corner.
        int eleScreenOffset = (int) (eleSize / 2.0);
        height -= eleScreenOffset;

        // left top right bottom
        downRect = new Rect(eleScreenOffset + eleSize * 1, height - (eleSize * 1), eleScreenOffset + eleSize * 2, height - (eleSize * 0));
        upRect = new Rect(eleScreenOffset + eleSize * 1, height - (eleSize * 3), eleScreenOffset + eleSize * 2, height - (eleSize * 2));
        leftRect = new Rect(eleScreenOffset + eleSize * 0, height - (eleSize * 2), eleScreenOffset + eleSize * 1, height - (eleSize * 1));
        rightRect = new Rect(eleScreenOffset + eleSize * 2, height - (eleSize * 2), eleScreenOffset + eleSize * 3, height - (eleSize * 1));

        downRect_area = new Rect(0, height - (eleSize * 1), eleScreenOffset + eleSize * 4, height - (eleSize * 0));
        upRect_area = new Rect(0, height - (eleSize * 6), eleScreenOffset + eleSize * 4, height - (eleSize * 2));
        leftRect_area = new Rect(0, height - (eleSize * 2), eleScreenOffset + eleSize * 1, height - (eleSize * 1));
        rightRect_area = new Rect(eleScreenOffset + eleSize * 2, height - (eleSize * 2), eleScreenOffset + eleSize * 5, height - (eleSize * 1));

        A_rect = new Rect(width - eleScreenOffset - eleSize * 2, height - (eleSize * 1),width - eleScreenOffset - eleSize * 1, height - (eleSize * 0));
        B_rect = new Rect(width - eleScreenOffset - eleSize * 1, height - (eleSize * 2),width - eleScreenOffset - eleSize * 0, height - (eleSize * 1));
    }

    private void drawControls(Canvas canvas) {
        if(!gameMode.isEventActivated()) {
            canvas.drawBitmap(controlIconUp, null, upRect, null);
            canvas.drawBitmap(controlIconDown, null, downRect, null);
            canvas.drawBitmap(controlIconRight, null, rightRect, null);
            canvas.drawBitmap(controlIconLeft, null, leftRect, null);
        }

        canvas.drawBitmap(A_Button, null, A_rect, null);
        canvas.drawBitmap(B_Button, null, B_rect, null);
    }


    /*
     **********************************************************************************
     * Main Game Methods
     ***********************************************************************************/

    /**
     * Called once on second thread prior to starting game loop.
     *
     * @param context passed from GameRenderer's constructor
     */
    private void setup(Context context) {
        gameMode = new GameMode(context);

        //Fps
        numCalls = 0;
        time = System.nanoTime();
    }


    private void initializeGameMode(int width, int height) {
//        Log.d(TAG,"Initializing GameMode " + width + height);
        gameMode.initialize(width, height);
        gameInitialized = true;
    }


    /**
     * Called on second thread once per game loop tick.
     */
    private void update() {
        // Calculate FPS
        long currentTime = System.nanoTime();
        if (currentTime - time >= 1000000000) {
            fps = numCalls;
            numCalls = 0;
            time = currentTime;
        }

        gameMode.update();
    }

    /**
     * Called on GUI thread using postInvalidate() in game loop.
     * This will run up to once per game loop tick after an update() has been executed.
     * This will depend on whether the GUI thread finishes drawing the current frame to the
     * canvas and resets the volatile drawNeeded boolean prior to second thread starting the
     * update of the next frame.
     *
     * @param canvas: A reference to a canvas present on GUI thread.
     */
    private void drawFrame(Canvas canvas) {
        gameMode.drawFrame(canvas);
        drawControls(canvas);

        // FPS
        numCalls++;
        canvas.drawText("FPS = " + fps, 50f, 50f, paint);
    }


    /***********************************************************************************
     * ******************************* GameRenderer *************************************
     **********************************************************************************/

    /*
     * This inner class contains the game loop and could be separated into its own class
     * at some point.
     * This class handles the updating and rendering of the frames on separate threads.
     */

    class GameRenderer extends View implements Runnable {


        // Thread for updating.
        private Thread updateThread = null;

        // Used to pause/resume game rendering.
        private volatile boolean running = true;

        // Determining if next draw call is ready (previous draw call finished).
        private volatile boolean drawing = false;

        // Frame rate we hope to maintain.
        private int targetFPS;

        // Equivalent time step associated with this frame rate.
        private long targetStepPeriod;

        // Variable to hold how long thread over / under slept (+/-) respectively
        private long overSleepTime;


        /**
         * Creates a GameRenderer, runs setup routine and starts the game loop.
         *
         * @param context   Activity context
         * @param targetFPS The ideal frame rate
         */
        public GameRenderer(Context context, int targetFPS) {
            super(context);
            // Note run method starts on seperate thread once constructor finishes i think
            Log.d(TAG, "GameRenderer Constructor");
            this.targetFPS = targetFPS;
            this.targetStepPeriod = 1000000000 / this.targetFPS;
            setup(context.getApplicationContext());

        }


        @Override
        public void run() {
            Log.d(TAG, "Run Method starting");
            while (!gameInitialized) {
                Log.d(TAG, "Waiting for game to Initialise");
            }
            while (running) {
//                    Log.d(TAG,"GameLoop Starting");
                long starTime = System.nanoTime();

                // Update frame
                try {
                    update();

                    // Check if still drawing on GUI thread
                    if (!drawing) {
                        drawing = true;
                        // Methods: invalidate(), postInvalidate()
                        // If we update ANY view (TextView, View, SurfaceView, CanvasView.. etc)
                        // The GUI thread needs to redraw it
                        // By calling invalidate(), we trigger a redraw of the view on the GUI thread
                        // By calling postInvalidate(), sends message to GUI thread FROM ANOTHER THREAD (which we are on)
                        postInvalidate();
                    }
                } catch (NullPointerException npe) {
                    Log.d(TAG, "NPE while updating " + npe.getMessage());
                }


                long endTime = System.nanoTime();
                long intendedSleepTime = targetStepPeriod - (endTime - starTime);

                // Correct for an oversleeping on previous tick
                intendedSleepTime = intendedSleepTime - overSleepTime;

                if (intendedSleepTime > 0) {
                    try {
                        // Convert intendedSleepTime from ns to ms
                        Thread.sleep(intendedSleepTime / 1000000L);
                        long actualSleepTime = System.nanoTime() - endTime;
                        overSleepTime = actualSleepTime - intendedSleepTime;
                    } catch (InterruptedException e) {
                        Log.d(TAG, "Thread interrupted: " + e.getMessage());
                    }
                } else {
                    // TODO: May be sub optimal to just reset this to 0
                    overSleepTime = 0L;
                }

            }
        }

        /**
         * THIS RUNS ON GUI THREAD
         * Overwriting onDraw() GUI onDraw() for our fragment
         * onDraw() is called whenever a view is updated (which we have triggered with postInvalidate())
         * We are overloading the onDraw() when its passed a reference to our canvas
         *
         * @param canvas Reference to canvas we are drawing on
         */
        @Override
        protected void onDraw(Canvas canvas) {
            drawFrame(canvas);

            drawing = false;
        }


        /**
         * Called from GameFragment lifecycle hook overrides
         */
        public void pause() {
            running = false;
            while (true) {
                try {
                    updateThread.join();
                    return;
                } catch (InterruptedException e) {
                    Log.d(TAG, "Thread interupted onPause(): " + e.getMessage());
                }
            }
        }

        /**
         * Called from GameFragment lifecycle hook overrides
         */
        public void resume() {
            running = true;
            updateThread = new Thread(this);
            updateThread.start();
        }
    }
}
