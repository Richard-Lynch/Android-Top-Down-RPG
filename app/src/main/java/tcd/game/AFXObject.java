package tcd.game;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by david on 17/03/17.
 */

enum AFX_ID {
    AFX_mario_coin,
    AFX_ouch_1
}

public class AFXObject {
    private SoundPool soundPool;
    private AudioAttributes audioAttributes;
    private final int MAX_STREAM = 10;

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
    }

    public void playAFX (Context context, AFX_ID input_ID) {
        switch (input_ID) {
            case AFX_mario_coin:
                soundPool.load(context, R.raw.mario_coin, 1);
                break;
            case AFX_ouch_1:
                soundPool.load(context, R.raw.ouch_1, 1);
                break;
        }
    }
}
