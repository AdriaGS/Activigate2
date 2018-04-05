package fr.cnrs.ipal.activigate2.Logger;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.cnrs.ipal.activigate2.MyApplication;

public class Logger {

    public static void appendLog(String text)
    {
        String filename = "Log.txt";
        File logFile = new File(MyApplication.instance.getFilesDir(), filename);
        Log.d("Filepath", MyApplication.instance.getFilesDir().toString());
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
