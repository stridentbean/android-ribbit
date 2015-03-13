package ind.habanero.realestatecalculator.Java;

import android.provider.Settings;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;

import database.ParseCloudStorage;
import ind.habanero.realestatecalculator.ParseApplication;
import ind.habanero.realestatecalculator.R;
import models.BasicInformation;

/**
 * Created by stridentbean on 2/21/2015.
 */
public class ParseCloudStorageTest extends AndroidTestCase {

    public static final String TAG = ParseCloudStorageTest.class.getSimpleName();

    private RenamingDelegatingContext mContext;
    private BasicInformation mMichael;
    private ParseApplication mApplication;
    private ParseCloudStorage mParseCloudStorage;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext = new RenamingDelegatingContext(getContext(), "test_");
        mMichael = BasicInformationTest.getMichael();

        mParseCloudStorage = new ParseCloudStorage(mContext);
        mParseCloudStorage.turnOnDebuggingMode();
    }

    public void testBasicInformationCreate() {
        mParseCloudStorage.store(mMichael);

        assertTrue(!mMichael.getId().equals(""));
    }

    public void testBasicInformationUpdate() {

        mParseCloudStorage.store(mMichael);
        String firstId = mMichael.getId();

        mMichael.setLLC("TestLLC");
        mParseCloudStorage.store(mMichael);

        assertTrue(mMichael.getId().equals(firstId));

    }

    public void tearDown() throws Exception{

        mParseCloudStorage.turnOffDebuggingMode();
        mParseCloudStorage = null;
        super.tearDown();
    }
}
