
package database;

/**
 * Created by michaelborglin on 2/11/15.
 */

        import android.database.sqlite.SQLiteOpenHelper;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.provider.BaseColumns;
        import android.util.Log;

/**
 * Created by Evan Anger on 8/17/14.
 */
public class RealEstateSQLLiteOpenHelper extends SQLiteOpenHelper {

    public static final String TAG = RealEstateSQLLiteOpenHelper.class.getSimpleName();

    private static final String DB_NAME = "realEstate.db";
    private static final int DB_VERSION = 1;

    //Tables
    public static final String PROPERTY_TABLE = "PROPERTY";
    public static final String REPAIR_ITEMS_TABLE = "REPAIR_ITEMS";

    //Columns
    public static final String COLUMN_PROPERTY_ADDRESS_LINE_1 = "ADDRESS_LINE_1";
    public static final String COLUMN_PROPERTY_ADDRESS_LINE_2 = "ADDRESS_LINE_2";
    public static final String COLUMN_PROPERTY_CITY = "CITY";
    public static final String COLUMN_PROPERTY_STATE = "STATE";
    public static final String COLUMN_PROPERTY_ZIP = "ZIP";
    public static final String COLUMN_PROPERTY_ASKING_PRICE = "ASKING_PRICE";
    public static final String COLUMN_PROPERTY_BEDROOMS = "BEDROOMS";
    public static final String COLUMN_PROPERTY_BATHROOMS = "BATHROOMS";
    public static final String COLUMN_PROPERTY_YEAR_BUILT = "YEAR_BUILT";
    public static final String COLUMN_PROPERTY_ADOM = "ADOM";
    public static final String COLUMN_CREATE_DATE = "CREATE_DATE";
    public static final String COLUMN_PROPERTY_SQUARE_FOOTAGE = "SQUARE_FOOTAGE";
    public static final String COLUMN_PROPERTY_ARV = "ARV";
    public static final String COLUMN_PROPERTY_PROFIT = "PROFIT";
    public static final String COLUMN_PROPERTY_MONEY_COSTS = "MONEY_COSTS";
    public static final String COLUMN_PROPERTY_TRANSFER_TAXES = "TRANSFER_TAXES";
    public static final String COLUMN_PROPERTY_CLOSING_COSTS = "CLOSING_COSTS";
    public static final String[] COLUMNS_PROPERTY = new String[]
            {BaseColumns._ID,COLUMN_PROPERTY_ADDRESS_LINE_1
        ,COLUMN_PROPERTY_ADDRESS_LINE_2,COLUMN_PROPERTY_CITY,COLUMN_PROPERTY_STATE, COLUMN_PROPERTY_ZIP,COLUMN_PROPERTY_ASKING_PRICE,COLUMN_PROPERTY_BEDROOMS
                    ,COLUMN_PROPERTY_BATHROOMS,
                    COLUMN_PROPERTY_YEAR_BUILT
                    ,COLUMN_PROPERTY_ADOM
                    , COLUMN_CREATE_DATE,
                    COLUMN_PROPERTY_SQUARE_FOOTAGE,
                    COLUMN_PROPERTY_ARV,
                    COLUMN_PROPERTY_PROFIT,
                    COLUMN_PROPERTY_MONEY_COSTS,
                    COLUMN_PROPERTY_TRANSFER_TAXES,
                    COLUMN_PROPERTY_CLOSING_COSTS
            };

    public static final String COLUMN_REPAIR_ITEMS_AMOUNT = "AMOUNT";
    public static final String COLUMN_REPAIR_ITEMS_PRICE = "PRICE";
    public static final String COLUMN_REPAIR_ITEMS_SWITCHED = "SWITCHED";
    public static final String COLUMN_REPAIR_ITEMS_TYPE = "TYPE";
    public static final String COLUMN_REPAIR_ITEMS_PROPERTY_ID = "PROPERTY_ID";
    public static final String[] COLUMNS_REPAIR_ITEM = new String[] {
            BaseColumns._ID,
            COLUMN_REPAIR_ITEMS_AMOUNT,
                COLUMN_REPAIR_ITEMS_PRICE,
                COLUMN_REPAIR_ITEMS_SWITCHED,
                COLUMN_REPAIR_ITEMS_TYPE,
                COLUMN_REPAIR_ITEMS_PROPERTY_ID
    };

    private static final String CREATE_PROPERTY =
            "CREATE TABLE " + PROPERTY_TABLE + "("
                    + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_PROPERTY_ADDRESS_LINE_1 +" TEXT," +
                    COLUMN_PROPERTY_ADDRESS_LINE_2 + " TEXT," +
                    COLUMN_PROPERTY_CITY +" TEXT," +
                    COLUMN_PROPERTY_STATE + " TEXT," +
                    COLUMN_PROPERTY_ZIP +" TEXT," +
                    COLUMN_PROPERTY_ASKING_PRICE + " INTEGER," +
                    COLUMN_PROPERTY_BEDROOMS +" INTEGER," +
                    COLUMN_PROPERTY_BATHROOMS + " INTEGER," +
                    COLUMN_PROPERTY_SQUARE_FOOTAGE + " INTEGER," +
                    COLUMN_PROPERTY_YEAR_BUILT +" INTEGER," +
                    COLUMN_PROPERTY_ADOM + " INTEGER," +
                    COLUMN_CREATE_DATE + " INTEGER," +
                    COLUMN_PROPERTY_ARV + " INTEGER," +
                    COLUMN_PROPERTY_PROFIT + " INTEGER," +
                    COLUMN_PROPERTY_MONEY_COSTS + " INTEGER," +
                    COLUMN_PROPERTY_TRANSFER_TAXES + " INTEGER," +
                    COLUMN_PROPERTY_CLOSING_COSTS + " INTEGER)";

    private static final String CREATE_REPAIR_ITEM =
            "CREATE TABLE " + REPAIR_ITEMS_TABLE + "(" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_REPAIR_ITEMS_PROPERTY_ID + " INTEGER," +
                    COLUMN_REPAIR_ITEMS_AMOUNT + " STRING," +
                    COLUMN_REPAIR_ITEMS_PRICE + " INTEGER," +
                    COLUMN_REPAIR_ITEMS_SWITCHED + " INTEGER," +
                    COLUMN_REPAIR_ITEMS_TYPE + " STRING, " +
                    getForeignString(COLUMN_REPAIR_ITEMS_PROPERTY_ID, BaseColumns._ID)
                    + ")";

     private static final String DROP_PROPERTY = "DROP TABLE IF EXISTS " + PROPERTY_TABLE +";";
    private static final String DROP_REPAIR_ITEMS = "DROP TABLE IF EXISTS " + REPAIR_ITEMS_TABLE +";";

    public RealEstateSQLLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PROPERTY);
        Log.i(TAG, CREATE_PROPERTY);
        sqLiteDatabase.execSQL(CREATE_REPAIR_ITEM);
        Log.i(TAG, CREATE_REPAIR_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                sqLiteDatabase.execSQL(DROP_PROPERTY);
                sqLiteDatabase.execSQL(DROP_REPAIR_ITEMS);
                Log.i(TAG, "Database Recreated");
                //sqLiteDatabase.execSQL(ALTER_ADD_CREATE_DATE);
        }

    }

    private static String getForeignString(String column, String foreignKey) {
        return "FOREIGN KEY(" + column + ") REFERENCES " + PROPERTY_TABLE + "(" + foreignKey + ")";
    }

}


