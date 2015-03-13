package database;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import ind.habanero.realestatecalculator.ParseApplication;
import ind.habanero.realestatecalculator.R;
import models.BasicInformation;

/**
 * Created by stridentbean on 2/21/2015.
 */
public class ParseCloudStorage {
    public static final String TAG = ParseCloudStorage.class.getSimpleName();

    private boolean DEBUGGING;
    private final Context mContext;
    private ParseApplication mApplication;
    private boolean isContextEstablished;


    //TODO make a singleton
    public ParseCloudStorage(Context context) {

        mContext = context;
        DEBUGGING = false;
        isContextEstablished = false;

    }

    public void turnOnDebuggingMode() {
        DEBUGGING = true;
        if(isContextEstablished == false) {
            mApplication = new ParseApplication();
            isContextEstablished = true;
        }
    }

    public void store(final BasicInformation basicInformation) {

        if(basicInformation.getId() == "") {
            //insert
            final ParseObject userInformation = new ParseObject(mContext.getResources().getString(R.string.TABLE_USER_INFORMATION));
            insert(userInformation, basicInformation);
        } else {
            //update
            update(basicInformation);
        }
    }

    private void update(BasicInformation basicInformation) {
        updateAddress(basicInformation);
        updateBasicInformation(basicInformation);
    }

    private void updateBasicInformation(final BasicInformation basicInformation) {
        ParseQuery query = ParseQuery.getQuery(mContext.getResources().getString(R.string.TABLE_USER_INFORMATION));
        query.getInBackground(basicInformation.getId(), new GetCallback() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                if( e == null) {
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_FIRST_NAME), basicInformation.getFirstName());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_LAST_NAME), basicInformation.getLastName());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_BUSINESS_NAME), basicInformation.getLLC());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_PHONE), basicInformation.getPhone());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_EMAIL), basicInformation.getEmail());

                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.i(TAG, "saved basic info to basicInformation: " + parseObject.getObjectId());
                            //store to local with the ID
                            Gson gson = new Gson();
                            StorageUtility.save(gson.toJson(basicInformation), mContext.getString(R.string.basic_information_location), mContext);
                        }
                    });
                } else {
                    Log.e(TAG, "ERROR: " + e.getMessage() + " For: " + basicInformation.getId());
                }
            }
        });
    }

    private void updateAddress(final BasicInformation basicInformation) {
        ParseQuery query = ParseQuery.getQuery(mContext.getResources().getString(R.string.TABLE_ADDRESS));
        query.getInBackground(basicInformation.getAddressId(), new GetCallback() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_ADDRESS_LINE_1), basicInformation.getAddressLine1());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_ADDRESS_LINE_2), basicInformation.getAddressLine2());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_CITY), basicInformation.getCity());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_SATE), basicInformation.getState());
                    parseObject.put(mContext.getResources().getString(R.string.COLUMN_ZIP), basicInformation.getZip());

                    parseObject.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            // Handle success or failure here ...

                            Log.i(TAG, "saved basic info to address: " + parseObject.getObjectId());
                        }
                    });

                } else {
                    Log.e(TAG, "ERROR: " + e.getMessage() + " For: " + basicInformation.getId());
                }
            }
        });
    }

    private void insert(final ParseObject parseObject, final BasicInformation basicInformation) {
        parseObject.put(mContext.getResources().getString(R.string.COLUMN_FIRST_NAME), basicInformation.getFirstName());
        parseObject.put(mContext.getResources().getString(R.string.COLUMN_LAST_NAME), basicInformation.getLastName());
        parseObject.put(mContext.getResources().getString(R.string.COLUMN_BUSINESS_NAME), basicInformation.getLLC());
        parseObject.put(mContext.getResources().getString(R.string.COLUMN_PHONE), basicInformation.getPhone());
        parseObject.put(mContext.getResources().getString(R.string.COLUMN_EMAIL), basicInformation.getEmail());
        ParseUser currentUser = ParseUser.getCurrentUser();
        parseObject.put(mContext.getResources().getString(R.string.COLUMN_USER_ID), currentUser);

        final ParseObject address = new ParseObject(mContext.getResources().getString(R.string.TABLE_ADDRESS));
        address.put(mContext.getResources().getString(R.string.COLUMN_ADDRESS_LINE_1), basicInformation.getAddressLine1());
        address.put(mContext.getResources().getString(R.string.COLUMN_ADDRESS_LINE_2), basicInformation.getAddressLine2());
        address.put(mContext.getResources().getString(R.string.COLUMN_CITY), basicInformation.getCity());
        address.put(mContext.getResources().getString(R.string.COLUMN_SATE), basicInformation.getState());
        address.put(mContext.getResources().getString(R.string.COLUMN_ZIP), basicInformation.getZip());
        address.put(mContext.getResources().getString(R.string.COLUMN_BASIC_INFORMATION_ID), parseObject);


        if (DEBUGGING) {
            try {
                //Do not use saveInBackground in while testing. The async stuff doesn't work properly
                address.save();
                basicInformation.setId(parseObject.getObjectId());
                Log.i(TAG, "SAVED: " + parseObject.getObjectId());
            } catch (ParseException e) {
                e.printStackTrace();

            }
        } else {
            address.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    // Handle success or failure here ...
                    basicInformation.setId(parseObject.getObjectId());
                    Log.i(TAG, "saved basic info to parse: " + parseObject.getObjectId());

                    basicInformation.setAddressId(address.getObjectId());
                    Log.i(TAG, "saved basic info to address: " + address.getObjectId());

                    //store to local with the ID
                    Gson gson = new Gson();
                    StorageUtility.save(gson.toJson(basicInformation), mContext.getString(R.string.basic_information_location), mContext);
                }
            });

        }
    }

    public void turnOffDebuggingMode() {
        DEBUGGING = false;
    }
}
