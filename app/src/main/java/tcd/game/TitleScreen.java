package tcd.game;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.os.Handler;

public class TitleScreen extends AppCompatActivity {

    public ProgressBar progressBar;
    int progressStatus;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        progressBar = (ProgressBar) findViewById(R.id.splash_progress);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void startGameClk(View view) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        new Thread(new Runnable() {
            public void run() {

                // Start a new thread starting the game

                handler.post(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(getBaseContext(), Game.class);
                        startActivity(intent);
                    }
                });

                // This way we can execute the cycle below while the game is loading

                while (progressStatus + 10 < 100) {
                    progressStatus += 5;

                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);

                            // Progress is updated every 50ms, however this
                            // is not reflected on the screen since that the
                            // view is not updated. Couldn't find a way to force
                            // the update, postInvalidate/invalidate are not working.
                            // I'm missing something.
                        }
                    });

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}