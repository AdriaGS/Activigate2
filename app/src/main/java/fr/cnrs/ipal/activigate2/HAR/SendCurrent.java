package fr.cnrs.ipal.activigate2.HAR;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by adriagil on 6/3/18.
 */

public class SendCurrent extends AsyncTask<String, Void, String> {

    Utils utils = new Utils();

    @Override
    protected String doInBackground(String... urls) {

        return  UploadHAR.upload(urls[0], urls[1]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("Null")){
            utils.onSending(true);
            Log.d("HTTP POST Result", "Data was Sent!");
        }
        else {
            utils.onSending(false);
            Log.d("HTTP POST Result", "Data was not Sent, Buffering!");
        }
    }
}
