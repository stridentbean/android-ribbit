package ind.habanero.realestatecalculator;

/**
 * Created by michaelborglin on 3/4/15.
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class CachedFileProvider extends ContentProvider {

    private static final String CLASS_NAME = "CachedFileProvider";

    // The authority is the symbolic name for the provider class
    public static final String AUTHORITY = "ind.habanero.realestatecalculator.gmailattach.provider";

    // UriMatcher used to match against incoming requests
    private UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Add a URI to the matcher which will match against the form
        // 'content://habanero.realestatecalculator.gmailattach.provider/*'
        // and return 1 in the case that the incoming Uri matches this pattern
        uriMatcher.addURI(AUTHORITY, "*", 1);

        return true;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode)
            throws FileNotFoundException {

        String LOG_TAG = CLASS_NAME + " - openFile";

        Log.v(LOG_TAG,
                "Called with uri: '" + uri + "'." + uri.getLastPathSegment());

        // Check incoming Uri against the matcher
        switch (uriMatcher.match(uri)) {

            // If it returns 1 - then it matches the Uri defined in onCreate
            case 1:

                // The desired file name is specified by the last segment of the
                // path
                // E.g.
                // 'content://habanero.realestatecalculator.gmailattach.provider/Test.txt'
                // Take this and build the path to the file
                String fileLocation = getContext().getCacheDir() + File.separator
                        + uri.getLastPathSegment();

                // Create & return a ParcelFileDescriptor pointing to the file
                // Note: I don't care what mode they ask for - they're only getting
                // read only
                ParcelFileDescriptor pfd = ParcelFileDescriptor.open(new File(
                        fileLocation), ParcelFileDescriptor.MODE_READ_ONLY);
                return pfd;

            // Otherwise unrecognised Uri
            default:
                Log.v(LOG_TAG, "Unsupported uri: '" + uri + "'.");
                throw new FileNotFoundException("Unsupported uri: "
                        + uri.toString());
        }
    }

    // //////////////////////////////////////////////////////////////
    // Not supported / used / required for this example
    // //////////////////////////////////////////////////////////////

    @Override
    public int update(Uri uri, ContentValues contentvalues, String s,
                      String[] as) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String s, String[] as) {
        return 0;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentvalues) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String s, String[] as1,
                        String s1) {
        return null;
    }

    public static void createCachedFile(Context context, String fileName,
                                        ByteArrayOutputStream content) throws IOException {

        File cacheFile = new File(context.getCacheDir() + File.separator
                + fileName);
        cacheFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(cacheFile);
        fos.write(content.toByteArray());
        fos.close();
    }

    public static Intent getSendEmailIntent(Context context, String email,
                                            String subject, String body, String fileName) {

        final Intent emailIntent = new Intent(
                android.content.Intent.ACTION_SEND);

        //Explicitly only use Gmail to send
        //emailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");

        emailIntent.setType("message/rfc822");

        //Add the recipients
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { email });

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

        /*String uriText =
                "mailto:" + email +
                        "?subject=" + URLEncoder.encode(subject) +
                        "&body=" + URLEncoder.encode(body);

        //add data for android on board mail app
        Uri uri = Uri.parse(uriText);
        emailIntent.setData(uri);*/

        //Add the attachment by specifying a reference to our custom ContentProvider
        //and the specific file of interest
        emailIntent.putExtra(
                Intent.EXTRA_STREAM,
                Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/"
                        + fileName));

        return emailIntent;



    }
}