package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import models.Property;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class PropertyDataSource extends GenericDataSource {

    public static final String TAG = PropertyDataSource.class.getSimpleName();

    private Context mContext;
    private RealEstateSQLLiteOpenHelper mRealEstateSqliteHelper;

    public PropertyDataSource(Context context) {

        mContext = context;
        mRealEstateSqliteHelper = new RealEstateSQLLiteOpenHelper(context);
    }

    private SQLiteDatabase open() {
        return mRealEstateSqliteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void delete(Property property) {

        SQLiteDatabase database = open();
        database.beginTransaction();

        //Delete Property
        database.delete(RealEstateSQLLiteOpenHelper.PROPERTY_TABLE,
                String.format("%s=%s", BaseColumns._ID, property.get_id()+""),
                null);
        //Delete RepairItems
        database.delete(RealEstateSQLLiteOpenHelper.REPAIR_ITEMS_TABLE,
                String.format("%s=%s", RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_PROPERTY_ID, property.get_id()+""),
                null);

        database.setTransactionSuccessful();
        database.endTransaction();

    }

    public ArrayList<Property> readProperties() {
        SQLiteDatabase database = open();

        Cursor cursor = database.query(
                RealEstateSQLLiteOpenHelper.PROPERTY_TABLE,
                RealEstateSQLLiteOpenHelper.COLUMNS_PROPERTY,
                null, //selection
                null, //selection args
                null, //group by
                null, //having
                null); //order

        ArrayList<Property> properties = new ArrayList<Property>();
        if(cursor.moveToFirst()) {
            do {
                Property property = new Property(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ADDRESS_LINE_1),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ADDRESS_LINE_2),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_CITY),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_STATE),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ZIP),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ASKING_PRICE),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_BEDROOMS),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_BATHROOMS),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_SQUARE_FOOTAGE),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_YEAR_BUILT),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ADOM),
                        getLongFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_CREATE_DATE),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ARV),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_PROFIT),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_MONEY_COSTS),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_TRANSFER_TAXES),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_CLOSING_COSTS));

                properties.add(property);
            }while(cursor.moveToNext());
        }
        cursor.close();
        close(database);
        return properties;
    }



    public void create(Property property) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues propertyValues = getContentValuesForProperty(property);

        if(property.get_id() == -1) {
            long propertyID = database.insert(RealEstateSQLLiteOpenHelper.PROPERTY_TABLE, null, propertyValues);

            //todo watch this, it might be dangerous
            property.setId((int)propertyID);
        } else {

            database.update(RealEstateSQLLiteOpenHelper.PROPERTY_TABLE,
                    propertyValues,
                    String.format("%s=%d", BaseColumns._ID, property.get_id()), null);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);

        Log.i(TAG, "Property Stored To Database!");

    }

    private ContentValues getContentValuesForProperty(Property property) {
        ContentValues propertyValues = new ContentValues();
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ADDRESS_LINE_1, property.getAddressLine1());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ADDRESS_LINE_2, property.getAddressLine2());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_CITY, property.getCity());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_STATE, property.getState());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ZIP,property.getZip());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ASKING_PRICE, property.getAskingPrice());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_BEDROOMS, property.getBedrooms());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_BATHROOMS, property.getBathrooms());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_YEAR_BUILT, property.getYearBuilt());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_SQUARE_FOOTAGE, property.getSquareFootage());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ADOM, property.getADOM());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_CREATE_DATE, new Date().getTime());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_ARV, property.getARV());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_PROFIT, property.getProfit());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_MONEY_COSTS, property.getMoneyCosts());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_TRANSFER_TAXES, property.getTransferTaxes());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_PROPERTY_CLOSING_COSTS, property.getClosingCosts());
       return propertyValues;
    }
}













