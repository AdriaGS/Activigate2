package fr.cnrs.ipal.activigate2.HAR;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by adria on 22/3/18.
 */

public class SendBuffer extends AsyncTask<String, Void, String> {

    Utils utils = new Utils();

    @Override
    protected String doInBackground(String... urls) {

        return  UploadHAR.upload(urls[0], urls[1]);
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        if(!result.equals("Null")){
            ArrayList<String> jsonRecord = utils.getJson2Send();
            if(jsonRecord.size() > 0) {
                jsonRecord.remove(jsonRecord.size() - 1);
                utils.setJson2Send(jsonRecord);
            }
            utils.setCanSend(true);
        }
    }

}
