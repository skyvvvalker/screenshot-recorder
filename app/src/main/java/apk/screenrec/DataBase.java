package apk.screenrec;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Records";
    private static final int DATABASE_VERSION = 1;

    static final String RECORD_TABLE_NAME = "records";
    static final String RECORD_COL_ID = "_id";
    static final String RECORD_COL_TIME = "time";
    static final String RECORD_COL_NAME = "name";
    static final String RECORD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + RECORD_TABLE_NAME + " (" +
            RECORD_COL_ID + " INTEGER PRIMARY KEY, " +
            RECORD_COL_TIME + " DATETIME, " +
            RECORD_COL_NAME + " TEXT" +
            ");";
    static final String RECORD_DROP_TABLE = "DROP TABLE IF EXISTS " + RECORD_TABLE_NAME;

    DataBase(Context context) {
        //
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        if (database != null) {
            database.execSQL(RECORD_CREATE_TABLE);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase database) {
        if (database != null) {
            database.execSQL(RECORD_CREATE_TABLE);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        if (database != null) {
            if (oldVersion != newVersion) {
                database.execSQL(RECORD_DROP_TABLE);
                database.execSQL(RECORD_CREATE_TABLE);
            }
        }
    }
}
