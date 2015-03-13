package database;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


/**
 * Created by stridentbean on 2/6/2015.
 */
public class StorageUtility {

    public static final String TAG = StorageUtility.class.getSimpleName();

   /* private File getJson(String filename) {
        File f = File.getFile(filename);
        if (f != null && f.isFile()) {
            String jsonString = FileCache.readFromFile(f);
            Log.i("DEMO", "DATA Read from file is:[ " + jsonString + " ]");
            return f;
        }
    }
    */

    public static void save(String data, String file, Context context){
        try {
            FileOutputStream fOut = context.openFileOutput(file, Activity.MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
            Log.i(TAG, "Data Saved");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.getStackTrace().toString());
            e.printStackTrace();
        }
    }
    public static String read(String file, Context context){
        StringBuilder sb = new StringBuilder();
        try{
            FileInputStream fis = context.openFileInput(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        }catch(Exception e){
            Log.e(TAG, "File Error: " +e.getStackTrace().toString());
            return "";
        }
        Log.i(TAG, sb.toString());
        return sb.toString();
    }

    public static boolean delete(String loc, Context context) {
        boolean deleted = context.deleteFile(loc);
        //File file = new File(loc);
        //boolean deleted = file.delete();
        return deleted;
    }
}
