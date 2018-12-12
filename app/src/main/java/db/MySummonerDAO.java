package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class MySummonerDAO extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mySummonerManager";

    // Bookmarks table name
    private static final String TABLE_NAME = "mySummoner";

    // Bookmarks Table Columns names

    private static final String KEY_PLATFORM = "platform";
    private static final String KEY_NAME = "name";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_TIER = "tier";
    private static final String KEY_TIERINFO = "tierInfo";
    private static final String KEY_LP = "lp";
    private static final String KEY_WIN = "wins";
    private static final String KEY_LOSS = "losses";
    private static final String KEY_AVR = "avr";
    private static final String KEY_PROFILEICON = "profileIcon";

    public MySummonerDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_PLATFORM + " TEXT PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_LEVEL + " TEXT,"
                + KEY_TIER + " TEXT,"
                + KEY_TIERINFO + " TEXT,"
                + KEY_LP + " TEXT,"
                + KEY_WIN + " TEXT,"
                + KEY_LOSS + " TEXT,"
                + KEY_AVR + " TEXT,"
                + KEY_PROFILEICON + " TEXT " + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    /**
     * CRUD 함수
     */

    // 새로운 MySummoner 함수 추가
    public void addSummoner(MySummoner mySummoner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, mySummoner.getPlatform());
        values.put(KEY_NAME, mySummoner.getName());
        values.put(KEY_LEVEL, mySummoner.getLevel());
        values.put(KEY_TIER, mySummoner.getTier());
        values.put(KEY_TIERINFO, mySummoner.getTierInfo());
        values.put(KEY_LP, mySummoner.getLp());
        values.put(KEY_WIN, mySummoner.getWins());
        values.put(KEY_LOSS, mySummoner.getLosses());
        values.put(KEY_AVR, mySummoner.getAvr());
        values.put(KEY_PROFILEICON, mySummoner.getProfileIcon());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // id 에 해당하는 Contact 객체 가져오기
    public MySummoner getSummoner(String platform) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_LEVEL, KEY_TIER, KEY_TIERINFO,
                        KEY_LP, KEY_WIN, KEY_LOSS, KEY_AVR, KEY_PROFILEICON}, KEY_PLATFORM + "=?",
                new String[]{String.valueOf(platform)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if(cursor.getCount() == 0)
            return null;

        MySummoner mySummoner = new MySummoner(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9));
        // return contact
        return mySummoner;
    }

    // 모든 Contact 정보 가져오기
    public List<MySummoner> getAllSummoners() {
        List<MySummoner> mySummonerList = new ArrayList<MySummoner>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MySummoner mySummoner = new MySummoner();
                mySummoner.setPlatform(cursor.getString(0));
                mySummoner.setName(cursor.getString(1));
                mySummoner.setLevel(cursor.getString(2));
                mySummoner.setTier(cursor.getString(3));
                mySummoner.setTierInfo(cursor.getString(4));
                mySummoner.setLp(cursor.getString(5));
                mySummoner.setWins(cursor.getString(6));
                mySummoner.setLosses(cursor.getString(7));
                mySummoner.setAvr(cursor.getString(8));
                mySummoner.setProfileIcon(cursor.getString(9));
                // Adding contact to list
                mySummonerList.add(mySummoner);
            } while (cursor.moveToNext());
        }

        // return contact list
        return mySummonerList;
    }

    //Contact 정보 업데이트
    public int updateSummoner(MySummoner mySummoner) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, mySummoner.getPlatform());
        values.put(KEY_NAME, mySummoner.getName());
        values.put(KEY_LEVEL, mySummoner.getLevel());
        values.put(KEY_TIER, mySummoner.getTier());
        values.put(KEY_TIERINFO, mySummoner.getTierInfo());
        values.put(KEY_LP, mySummoner.getLp());
        values.put(KEY_WIN, mySummoner.getWins());
        values.put(KEY_LOSS, mySummoner.getLosses());
        values.put(KEY_AVR, mySummoner.getAvr());
        values.put(KEY_PROFILEICON, mySummoner.getProfileIcon());

        // updating row
        return db.update(TABLE_NAME, values, KEY_PLATFORM + " = ?",
                new String[]{String.valueOf(mySummoner.getPlatform())});
    }

    // Contact 정보 삭제하기
    public void deleteSummoner(MySummoner mySummoner) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_PLATFORM + " = ?",
                new String[]{String.valueOf(mySummoner.getPlatform())});
        db.close();
    }

    // Contact 정보 숫자
    public int getBookmarksCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}

