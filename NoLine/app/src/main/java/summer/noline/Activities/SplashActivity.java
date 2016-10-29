package summer.noline.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import summer.noline.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer=new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    Log.e("SplashActivity", "thread: "+e.toString());
                }
                finally {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };
        timer.start();
    }

}
