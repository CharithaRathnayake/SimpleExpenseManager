package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "180527V";
    private static final int DATABASE_VERSION = 7;
    //tables
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String TABLE_TRANSACTION = "transactions";

    //keys
    public static final String COL_ACCOUNT_NO = "accountNo";
    public static final String COL_BANK_NAME = "bankName";
    public static final String COL_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String COL_BALANCE = "balance";
    private static final String COL_TRANSACTION_ID = "id";
    public static final String COL_EXPENSE_TYPE = "expenseType";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_DATE = "date";

    //account
    private static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE " + TABLE_ACCOUNT + "("
            + COL_ACCOUNT_NO + " TEXT PRIMARY KEY," + COL_BANK_NAME + " TEXT,"
            + COL_ACCOUNT_HOLDER_NAME + " TEXT," + COL_BALANCE + " REAL" + ")";

    //transaction
    private static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "("
            + COL_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_DATE + " TEXT," + COL_ACCOUNT_NO + " TEXT,"
            + COL_EXPENSE_TYPE + " TEXT," + COL_AMOUNT + " REAL," + "FOREIGN KEY(" + COL_ACCOUNT_NO +
            ") REFERENCES "+ TABLE_ACCOUNT +"(" + COL_ACCOUNT_NO + ") )";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_ACCOUNT + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_TRANSACTION + "'");

        // recreation
        onCreate(db);
    }
}