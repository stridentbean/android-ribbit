package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import models.Property;
import models.RepairItem;

/**
 * Created by michaelborglin on 2/19/15.
 */
public class RepairItemDataSource extends GenericDataSource {
    public static final String TAG = PropertyDataSource.class.getSimpleName();

    private Context mContext;
    private RealEstateSQLLiteOpenHelper mRealEstateSqliteHelper;

    public RepairItemDataSource(Context context) {
        mContext = context;
        mRealEstateSqliteHelper = new RealEstateSQLLiteOpenHelper(context);
    }

    private SQLiteDatabase open() {
        return mRealEstateSqliteHelper.getWritableDatabase();
    }

    private void close(SQLiteDatabase database) {
        database.close();
    }

    public void create(RepairItem repairItem, Property property) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        ContentValues propertyValues = getContentValuesForProperty(repairItem, property);

        if (repairItem.get_id() == -1) {
            long propertyID = database.insert(RealEstateSQLLiteOpenHelper.REPAIR_ITEMS_TABLE, null, propertyValues);
            repairItem.setM_id((int) propertyID);
        } else {

            database.update(RealEstateSQLLiteOpenHelper.REPAIR_ITEMS_TABLE,
                    propertyValues,
                    String.format("%s=%d", BaseColumns._ID, repairItem.get_id()), null);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);

        Log.i(TAG, "Repair Item Stored To Database!");

    }
    private ContentValues getContentValuesForProperty(RepairItem repairItem, Property property) {
        ContentValues propertyValues = new ContentValues();
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_AMOUNT, repairItem.getAmount());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_PRICE, repairItem.getPrice());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_SWITCHED, repairItem.isSwitched());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_TYPE, repairItem.getType().name());
        propertyValues.put(RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_PROPERTY_ID, property.get_id());
        return propertyValues;
    }

    public void create(List<RepairItem> list, Property property) {
        SQLiteDatabase database = open();
        database.beginTransaction();

        for(RepairItem item : list) {
            ContentValues propertyValues = getContentValuesForProperty(item, property);

            if (item.get_id() == -1) {
                long propertyID = database.insert(RealEstateSQLLiteOpenHelper.REPAIR_ITEMS_TABLE, null, propertyValues);
                item.setM_id((int) propertyID);
            } else {

                database.update(RealEstateSQLLiteOpenHelper.REPAIR_ITEMS_TABLE,
                        propertyValues,
                        String.format("%s=%d", BaseColumns._ID, item.get_id()), null);
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        close(database);

        Log.i(TAG, "Repair Items Stored To Database!");

    }

    public ArrayList<RepairItem> getRepairItemsForProperty(Property property) {

        SQLiteDatabase database = open();

        String whereClause = RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_PROPERTY_ID + "=?";
        String[] whereArgs = new String[]{property.get_id() + ""};

        Cursor cursor = database.query(
                RealEstateSQLLiteOpenHelper.REPAIR_ITEMS_TABLE,
                RealEstateSQLLiteOpenHelper.COLUMNS_REPAIR_ITEM,
                whereClause,
                whereArgs,
                null, //group by
                null, //having
                null); //order

        ArrayList<RepairItem> items = new ArrayList<RepairItem>();
        if(cursor.moveToFirst()) {
            do {
                RepairItem item = new RepairItem(getIntFromColumnName(cursor, BaseColumns._ID),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_TYPE),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_PRICE),
                        getStringFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_AMOUNT),
                        getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_SWITCHED));

                Log.i(TAG, "Prop ID: " + getIntFromColumnName(cursor, RealEstateSQLLiteOpenHelper.COLUMN_REPAIR_ITEMS_PROPERTY_ID));
                        items.add(item);
            }while(cursor.moveToNext());
        }
        cursor.close();
        close(database);

        Log.i(TAG, items.size() + " Repair Items retrieved!");
        return items;
    }

}
