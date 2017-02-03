package tcd.game;

import android.util.Log;


public class GameLoop implements Runnable {
    /**
     * Creates a new thread which runs the game loop
     * TODO: Split GameLoop into 2 threads: one rendering current frame and one updating next frame
     */
    private static final String TAG = "GameLoop";

    // FPS we hope to maintain
    private int targetFPS;

    // Timestep equivelant
    private long targetStepPeriod;

    // Thread that will do our drawing
    private Thread renderThread;

    // Volatile: can be changed by other threads -- (generally not cached as it can be changed unpredictably)
    private volatile boolean running;


    /**
     * Creates but does not start a game loop
     * @param fps desired frame rate
     */
    public GameLoop(int fps){
        this.targetFPS = fps;
        this.targetStepPeriod = 1000000000/targetFPS;    //nano seconds for some reason
        this.renderThread = new Thread(this);
    }

    /**
     * Starts GameLoop
     */
    public void start(){
        this.running = true;
    }



    private void update(){
        // TODO: Update Canvas
    }

    private void draw(){
        // TODO: Redraw Canvas
    }


    // Method that thread will execute
    @Override
    public void run() {
        Log.d(TAG,"Starting run..");

        // How long we over/under slept (+/- respectively)
        long overSleepTime = 0L;

        // Try/Catch used here as threads may not sleep and throw an exception
        try{
            while(running){

                // Update, draw and find time elapsed
                long startTime = System.nanoTime();
                update();
                draw();
                long endTime = System.nanoTime();

                // intendedSleepTime is targetStepPeriod - time taken to update and draw
                long intendedSleepTime = (targetStepPeriod - (endTime-startTime));

                //However if we got delayed last cycle by eg 2ms --> overSleepTime = 2ms
                //Then we don't want to sleep for full 5ms as we are technically behind schedule --> sleep for 3ms
                intendedSleepTime = intendedSleepTime  - overSleepTime;

                if(intendedSleepTime>0) {
                    // Sleep thread (ns)
                    Thread.sleep(intendedSleepTime / 1000000L);

                    // TODO: Drop redundant variable (left in for clarity for now)
                    // Thread now awoken: see how long we ACTUALLY slept for
                    long actualSleepTime = (System.nanoTime() - endTime);

                    // Calculate how long we over/under slept (+/-)
                    overSleepTime = actualSleepTime - intendedSleepTime;
                    //Log.d(TAG,String.valueOf(overSleepTime));

                } else {
                    overSleepTime = 0L;
                    //TODO: I think this shouldn't be reset here but could be wrong
                }
            }
        } catch (InterruptedException e){
            Log.d(TAG,"Error in sleeping thread");
        }
    }

    public void pause() {
        Log.d(TAG,"Pausing Loop..");
        running = false;
        while(true) {
            try {
                renderThread.join();            // kills thread, maybe should just stop thread
//                renderThread.join(100L);      // attempts to kill thread for 100ms before giving up
                return;
            } catch (InterruptedException e){
                Log.d(TAG,"Couldn't join thread..");
            }
        }
    }

    public void resume(){
        Log.d(TAG,"Resuming Loop..");
        running = true;

        // Create and start new thread
        renderThread = new Thread(this);
        renderThread.start();
    }
}
