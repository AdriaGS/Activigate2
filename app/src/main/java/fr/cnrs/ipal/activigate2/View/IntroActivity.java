package fr.cnrs.ipal.activigate2.View;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fr.cnrs.ipal.activigate2.R;

public class IntroActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        intent = new Intent(this, MainActivity.class);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(intent);
<<<<<<< HEAD
                finish();
            }
        }, 3000);   //5 seconds

    }

=======
            }
        }, 5000);   //5 seconds

    }

    private void goMainActivity() {

    }
>>>>>>> 063eb9bd0982290fc39ed60ca04fad45cba242bb
}
