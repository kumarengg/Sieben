package de.baumann.sieben.pause;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import at.juggle.sieben.SoundPool;
import de.baumann.sieben.MainActivity;
import de.baumann.sieben.R;
import de.baumann.sieben.helper.OnSwipeTouchListener;
import de.baumann.sieben.helper.TTSManager;
import de.baumann.sieben.helper.UserSettingsActivity;
import de.baumann.sieben.workouts.MainActivity4;


public class Pause3 extends AppCompatActivity {

    private TextView textView;
    private ProgressBar progressBar;
    private TTSManager ttsManager = null;
    private ImageView imageView;

    private boolean isPaused = false;
    private boolean isCanceled = false;
    private long timeRemaining = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);

        imageView = (ImageView) findViewById(R.id.imageView);
        assert imageView != null;
        imageView.setImageResource(R.drawable.a04);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.pau_3);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        assert progressBar != null;
        progressBar.setRotation(180);

        textView = (TextView) this.findViewById(R.id.timer);

        ttsManager = new TTSManager();
        ttsManager.init(this);

        long millisInFuture = 10000;
        long countDownInterval = 100;


        //Initialize a new CountDownTimer instance
        new CountDownTimer(millisInFuture,countDownInterval){
            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Pause3.this);
            public void onTick(long millisUntilFinished){
                //do something in every tick
                if(isPaused || isCanceled)
                {
                    cancel();
                }
                else {
                    textView.setText(String.valueOf(millisUntilFinished / 1000));
                    int progress = (int) (millisUntilFinished/100);
                    progressBar.setProgress(progress);
                    timeRemaining = millisUntilFinished;
                }
            }
            public void onFinish(){

                if (sharedPref.getBoolean ("beep", false)){
//                    final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
//                    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                    SoundPool.playWhistle(getApplicationContext());
                }

                if (sharedPref.getBoolean ("tts", false)){
                    String text = getResources().getString(R.string.act_4);
                    ttsManager.initQueue(text);
                }

                progressBar.setProgress(0);
                Intent intent_in = new Intent(Pause3.this, MainActivity4.class);
                startActivity(intent_in);
                overridePendingTransition(0, 0);
                finishAffinity();
            }
        }.start();

        imageView.setOnTouchListener(new OnSwipeTouchListener(Pause3.this) {
            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Pause3.this);
            public void onSwipeTop() {
                isPaused = false;
                isCanceled = false;

                long millisInFuture = timeRemaining;
                long countDownInterval = 100;

                new CountDownTimer(millisInFuture, countDownInterval){
                    public void onTick(long millisUntilFinished){
                        if(isPaused || isCanceled)
                        {
                            cancel();
                        }
                        else {
                            textView.setText(String.valueOf(millisUntilFinished / 1000));
                            int progress = (int) (millisUntilFinished/100);
                            progressBar.setProgress(progress);
                            timeRemaining = millisUntilFinished;
                        }
                    }
                    public void onFinish(){

                        if (sharedPref.getBoolean ("beep", false)){
//                            final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
//                            tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                            SoundPool.playWhistle(getApplicationContext());
                        }

                        if (sharedPref.getBoolean ("tts", false)){
                            String text = getResources().getString(R.string.act_4);
                            ttsManager.initQueue(text);
                        }

                        progressBar.setProgress(0);
                        Intent intent_in = new Intent(Pause3.this, MainActivity4.class);
                        startActivity(intent_in);
                        overridePendingTransition(0, 0);
                        finishAffinity();
                    }
                }.start();
                if (sharedPref.getBoolean ("tts", false)){
                    String text = getResources().getString(R.string.sn_weiter);
                    ttsManager.initQueue(text);
                }
                Snackbar.make(imageView, R.string.sn_weiter, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            public void onSwipeRight() {
                isCanceled = true;

                if (sharedPref.getBoolean ("tts", false)){
                    if (sharedPref.getBoolean("act3", false)) {
                        String text = getResources().getString(R.string.pau_2);
                        ttsManager.initQueue(text);
                    } else if (sharedPref.getBoolean("act2", false)) {
                        String text = getResources().getString(R.string.pau);
                        ttsManager.initQueue(text);
                    } else if (sharedPref.getBoolean("act1", false)) {
                        String text = getResources().getString(R.string.act);
                        ttsManager.initQueue(text);
                    } else {
                        String text = getResources().getString(R.string.sn_first);
                        ttsManager.initQueue(text);
                    }
                }

                if (sharedPref.getBoolean("act3", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause2.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else if (sharedPref.getBoolean("act2", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else if (sharedPref.getBoolean("act1", false)) {
                    Intent intent_in = new Intent(Pause3.this, MainActivity.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else {
                    Snackbar.make(imageView, R.string.sn_first, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            public void onSwipeLeft() {
                isCanceled = true;

                if (sharedPref.getBoolean ("tts", false)){

                    if (sharedPref.getBoolean("act5", false)) {
                        String text = getResources().getString(R.string.pau_4);
                        ttsManager.initQueue(text);
                    } else  if (sharedPref.getBoolean("act6", false)) {
                        String text = getResources().getString(R.string.pau_5);
                        ttsManager.initQueue(text);
                    } else  if (sharedPref.getBoolean("act7", false)) {
                        String text = getResources().getString(R.string.pau_6);
                        ttsManager.initQueue(text);
                    } else  if (sharedPref.getBoolean("act8", false)) {
                        String text = getResources().getString(R.string.pau_7);
                        ttsManager.initQueue(text);
                    } else  if (sharedPref.getBoolean("act9", false)) {
                        String text = getResources().getString(R.string.pau_8);
                        ttsManager.initQueue(text);
                    } else  if (sharedPref.getBoolean("act10", false)) {
                        String text = getResources().getString(R.string.pau_9);
                        ttsManager.initQueue(text);
                    } else  if (sharedPref.getBoolean("act11", false)) {
                        String text = getResources().getString(R.string.pau_10);
                        ttsManager.initQueue(text);
                    } else  if (sharedPref.getBoolean("act12", false)) {
                        String text = getResources().getString(R.string.pau_11);
                        ttsManager.initQueue(text);
                    } else {
                        String text = getResources().getString(R.string.sn_last);
                        ttsManager.initQueue(text);
                    }
                }

                if (sharedPref.getBoolean("act5", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause4.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                }else if (sharedPref.getBoolean("act6", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause5.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else if (sharedPref.getBoolean("act7", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause6.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else if (sharedPref.getBoolean("act8", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause7.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else if (sharedPref.getBoolean("act9", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause8.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                }else if (sharedPref.getBoolean("act10", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause9.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else if (sharedPref.getBoolean("act11", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause10.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else if (sharedPref.getBoolean("act12", false)) {
                    Intent intent_in = new Intent(Pause3.this, Pause11.class);
                    startActivity(intent_in);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                } else {
                    Snackbar.make(imageView, R.string.sn_last, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            public void onSwipeBottom() {
                if (sharedPref.getBoolean ("tts", false)){
                    String text = getResources().getString(R.string.sn_pause);
                    ttsManager.initQueue(text);
                }
                isPaused = true;
                Snackbar.make(imageView, R.string.sn_pause, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent_in = new Intent(Pause3.this, UserSettingsActivity.class);
            startActivity(intent_in);
            overridePendingTransition(0, 0);
            isCanceled = true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        isCanceled = true;
        finishAffinity();
    }
}
