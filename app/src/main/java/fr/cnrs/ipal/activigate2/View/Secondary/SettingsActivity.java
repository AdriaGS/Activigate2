package fr.cnrs.ipal.activigate2.View.Secondary;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fr.cnrs.ipal.activigate2.HAR.Utils;
import fr.cnrs.ipal.activigate2.MyApplication;
import fr.cnrs.ipal.activigate2.R;
import fr.cnrs.ipal.activigate2.View.FitbitActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String HAR_PREFERENCES = "HAR_Preferences";

    EditText username;
    EditText pass;
    EditText houseIDET;

    TextView wronguspw;
    Button applyChanges;

    SharedPreferences sharedPreferences;

    Utils utils = new Utils();

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
                        if (username.equals("admin") && pass.equals("1234")) {
                            return;
                        } else {
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

        houseIDET = findViewById(R.id.houseID);
        applyChanges = findViewById(R.id.applyChanges);

    }

    public void onSettingsSaved(View view) {
        String houseID = houseIDET.getText().toString();
        utils.setHouseID(houseID);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(houseIDET.getWindowToken(), 0);
        houseIDET.setFocusable(false);
        save();
        finish();

    }

    private void save() {
        sharedPreferences = MyApplication.instance.getSharedPreferences(HAR_PREFERENCES, FitbitActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("houseId", utils.getHouseID());
        editor.commit();
    }
}
