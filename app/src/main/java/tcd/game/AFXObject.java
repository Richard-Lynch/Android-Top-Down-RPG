package tcd.game;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

import java.util.Timer;


/**
 * Created by david on 17/03/17.
 */

// TODO: Figure out how to limit collision sounds to one per second

enum AFX_ID {
    AFX_mario_coin,
    AFX_ouch_1
}

public class AFXObject {
    private SoundPool soundPool;
    private AudioAttributes audioAttributes;
    private final int MAX_STREAM = 5;
    private long  time_since_ouch;

    public AFXObject () {
        //AudioAttributes audioAttributes;

        // Initialise soundpool for sound effects
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_STREAM)
                    .build();
        }

        // Define the method that is called when a file is loaded to the soundPool
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        });

        // Set time_since_ouch
        time_since_ouch = 0;
    }

    public void playAFX (Context context, AFX_ID input_ID) {
        switch (input_ID) {
            case AFX_mario_coin:
                soundPool.load(context, R.raw.mario_coin, 1);
                break;
            case AFX_ouch_1:
                if (System.currentTimeMillis() - time_since_ouch > 500 || System.currentTimeMillis() < time_since_ouch) {
                    soundPool.load(context, R.raw.ouch_1, 1);
                    time_since_ouch = System.currentTimeMillis();
                }
                break;
        }
    }

    private void countdown (int AFX_countdown) {

    }
}
