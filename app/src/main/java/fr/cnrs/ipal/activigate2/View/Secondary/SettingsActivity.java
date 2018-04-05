package fr.cnrs.ipal.activigate2.View.Secondary;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import fr.cnrs.ipal.activigate2.R;

public class SettingsActivity extends AppCompatActivity {

    EditText username;
    EditText pass;
    TextView wronguspw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        LayoutInflater li = LayoutInflater.from(this);
        View prompt = li.inflate(R.layout.login_form, null);

        username = prompt.findViewById(R.id.username);
        pass = prompt.findViewById(R.id.password);
        wronguspw = prompt.findViewById(R.id.wrongLogin);

        AlertDialog.Builder login = new AlertDialog.Builder(this);
        login.setView(prompt);
        login.setCancelable(false);
        login.setMessage(R.string.loginBox)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(username.equals("admin") && pass.equals("1234")){
                            return;
                        }
                        else {
                            wronguspw.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton(R.string.cancelLogin, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        // Create the AlertDialog object and return it
        login.create();
        login.show();

    }
}
