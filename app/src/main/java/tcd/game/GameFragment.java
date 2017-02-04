package tcd.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

/**
 * This class creates a game fragment that can be embedded into an activity.
 * This class will update all of the objects to be drawn to the n'th frame on a separate thread,
 * while drawing the n-1th frame using the hardware accelerated GUI thread.
 * If the drawing process is fast enough, the GUI thread will wait for next frame to be updated.
 */

public class GameFragment extends Fragment {
    private static final String TAG = "GameFragment";
    private final int TARGET_FPS = 60;

    private GameRenderer gameRenderer;
    private int numCalls;
    private Paint paint;
//    public Context context;
    private GameMode gameMode;

    // FPS test
    private long time;
    int fps;


    /***********************************************************************************
     * Lifecycle Hooks
     ***********************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"derp");

        gameRenderer = new GameRenderer(getActivity(), TARGET_FPS);
        Log.d(TAG,"Depr");
        time = 0;

        return gameRenderer;
    }

    @Override
    public void onPause() {
        Log.d(TAG,"Pausing");
        super.onPause();
        gameRenderer.pause();
    }

    @Override
    public void onResume() {
        Log.d(TAG,"Resuming");
        super.onResume();
        gameRenderer.resume();
    }


    /***********************************************************************************
     * Main Game Methods
     ***********************************************************************************/

    /**
     * Called once on second thread prior to starting game loop.
     */
    private void setup(Context context) {
        numCalls = 0;
        paint = new Paint(Color.RED);
        time = System.nanoTime();
        gameMode = new GameMode(context,400,400);
    }





    /**
     * Called on second thread once per game loop tick.
     */
    private void update() {
       long currentTime = System.nanoTime();
        if(currentTime - time > 1000000000){
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
     * @param canvas: A reference to a canvas present on GUI thread.
     */
    private void drawFrame(Canvas canvas) {
        gameMode.drawFrame(canvas);
        numCalls++;
        paint.setTextSize(36.0f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("FPS = " + fps, 50f, 50f, paint);
    }


    /***********************************************************************************
     ******************************** GameRenderer *************************************
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
         * @param context Activity context
         * @param targetFPS The ideal frame rate
         */
        public GameRenderer(Context context, int targetFPS) {
            super(context);
//            this.context = context;
            this.targetFPS = targetFPS;
            this.targetStepPeriod =  1000000000 / this.targetFPS;
            setup(context.getApplicationContext());

        }


        @Override
        public void run() {

            while (running) {
                long starTime = System.nanoTime();

                // Update frame
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
                    // TODO: I don't think this should be reset - Ask group
                    //       We should correct for undersleeping as well as oversleeping
                    //       If we reset this, updates continue ahead of time
                    //       I guess it makes no difference as we wont be drawing the frames anyway
                    
                     overSleepTime = 0L;
                }
            }
        }

        /**
         * THIS RUNS ON GUI THREAD
         * Overwriting onDraw() GUI onDraw() for our fragment
           onDraw() is called whenever a view is updated (which we have triggered with postInvalidate())
           We are overloading the onDraw() when its passed a reference to our canvas
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
                    Log.d(TAG,"Thread interupted onPause(): " + e.getMessage());
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
