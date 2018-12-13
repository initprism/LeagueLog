package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class SummonerDAO extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SummonerManager";

    // Bookmarks table name
    private static final String TABLE_MYSUMMONER = "mySummoner";
    private static final String TABLE_HISTORYSUMMONER = "historySummoner";

    //Table Columns names
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

    public SummonerDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MYSUMMONER_TABLE = "CREATE TABLE " + TABLE_MYSUMMONER + "("
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

        String CREATE_HISTORYSUMMONER_TABLE = "CREATE TABLE " + TABLE_HISTORYSUMMONER + "("
                + KEY_PLATFORM + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_TIER + " TEXT,"
                + KEY_TIERINFO + " TEXT,"
                + KEY_PROFILEICON + " TEXT,"
                + " PRIMARY KEY (" + KEY_NAME + ", " + KEY_PLATFORM + "))";

        db.execSQL(CREATE_MYSUMMONER_TABLE);
        db.execSQL(CREATE_HISTORYSUMMONER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYSUMMONER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORYSUMMONER);

        // Create tables again
        onCreate(db);
    }

    /**
     * MySummoner CRUD
     **/

    // 새로운 MySummonerDTO 함수 추가
    public void addMySummoner(MySummonerDTO mySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, mySummonerDTO.getPlatform());
        values.put(KEY_NAME, mySummonerDTO.getName());
        values.put(KEY_LEVEL, mySummonerDTO.getLevel());
        values.put(KEY_TIER, mySummonerDTO.getTier());
        values.put(KEY_TIERINFO, mySummonerDTO.getTierInfo());
        values.put(KEY_LP, mySummonerDTO.getLp());
        values.put(KEY_WIN, mySummonerDTO.getWins());
        values.put(KEY_LOSS, mySummonerDTO.getLosses());
        values.put(KEY_AVR, mySummonerDTO.getAvr());
        values.put(KEY_PROFILEICON, mySummonerDTO.getProfileIcon());

        // Inserting Row
        db.insert(TABLE_MYSUMMONER, null, values);
        db.close(); // Closing database connection
    }

    // Platform 에 해당하는 MySummoner 객체 가져오기
    public MySummonerDTO getMySummoner(String platform) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MYSUMMONER, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_LEVEL, KEY_TIER, KEY_TIERINFO,
                        KEY_LP, KEY_WIN, KEY_LOSS, KEY_AVR, KEY_PROFILEICON}, KEY_PLATFORM + "=?",
                new String[]{String.valueOf(platform)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;

        MySummonerDTO mySummonerDTO = new MySummonerDTO(
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
        return mySummonerDTO;
    }

    // 모든 MySummoner 정보 가져오기
    public List<MySummonerDTO> getAllMySummoners() {
        List<MySummonerDTO> mySummonerDTOList = new ArrayList<MySummonerDTO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MYSUMMONER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MySummonerDTO mySummonerDTO = new MySummonerDTO();
                mySummonerDTO.setPlatform(cursor.getString(0));
                mySummonerDTO.setName(cursor.getString(1));
                mySummonerDTO.setLevel(cursor.getString(2));
                mySummonerDTO.setTier(cursor.getString(3));
                mySummonerDTO.setTierInfo(cursor.getString(4));
                mySummonerDTO.setLp(cursor.getString(5));
                mySummonerDTO.setWins(cursor.getString(6));
                mySummonerDTO.setLosses(cursor.getString(7));
                mySummonerDTO.setAvr(cursor.getString(8));
                mySummonerDTO.setProfileIcon(cursor.getString(9));
                // Adding contact to list
                mySummonerDTOList.add(mySummonerDTO);
            } while (cursor.moveToNext());
        }

        // return contact list
        return mySummonerDTOList;
    }

    //Contact 정보 업데이트
    public int updateMySummoner(MySummonerDTO mySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, mySummonerDTO.getPlatform());
        values.put(KEY_NAME, mySummonerDTO.getName());
        values.put(KEY_LEVEL, mySummonerDTO.getLevel());
        values.put(KEY_TIER, mySummonerDTO.getTier());
        values.put(KEY_TIERINFO, mySummonerDTO.getTierInfo());
        values.put(KEY_LP, mySummonerDTO.getLp());
        values.put(KEY_WIN, mySummonerDTO.getWins());
        values.put(KEY_LOSS, mySummonerDTO.getLosses());
        values.put(KEY_AVR, mySummonerDTO.getAvr());
        values.put(KEY_PROFILEICON, mySummonerDTO.getProfileIcon());

        // updating row
        return db.update(TABLE_MYSUMMONER, values, KEY_PLATFORM + " = ?",
                new String[]{String.valueOf(mySummonerDTO.getPlatform())});
    }

    // Contact 정보 삭제하기
    public void deleteMySummoner(MySummonerDTO mySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MYSUMMONER, KEY_PLATFORM + " = ?",
                new String[]{String.valueOf(mySummonerDTO.getPlatform())});
        db.close();
    }

    // Contact 정보 숫자
    public int getMySummonersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MYSUMMONER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    /**
     * HistorySummoner CRUD
     */

    // 새로운 HistorySummonerDTO 함수 추가
    public void addHistorySummoner(HistorySummonerDTO historySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, historySummonerDTO.getPlatform());
        values.put(KEY_NAME, historySummonerDTO.getName());
        values.put(KEY_TIER, historySummonerDTO.getTier());
        values.put(KEY_TIERINFO, historySummonerDTO.getTierInfo());
        values.put(KEY_PROFILEICON, historySummonerDTO.getProfileIcon());

        // Inserting Row
        db.insert(TABLE_HISTORYSUMMONER, null, values);
        db.close(); // Closing database connection
    }

    //name 에 해당하는 HistorySummoner 객체 가져오기
    public HistorySummonerDTO getHistorySummoner(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORYSUMMONER, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_TIER, KEY_TIERINFO, KEY_PROFILEICON}, KEY_NAME + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;

        HistorySummonerDTO historySummonerDTO = new HistorySummonerDTO(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
        // return contact
        return historySummonerDTO;
    }

    //name,pf 에 해당하는 HistorySummoner 객체 가져오기
    public HistorySummonerDTO getHistorySummoner(String name, String platform) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORYSUMMONER, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_TIER, KEY_TIERINFO, KEY_PROFILEICON}, KEY_NAME + "=? AND " + KEY_PLATFORM + " =?",
                new String[]{name, platform}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;


        HistorySummonerDTO historySummonerDTO = new HistorySummonerDTO(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
        // return contact
        return historySummonerDTO;
    }

    // 모든 HistorySummoner 정보 가져오기
    public List<HistorySummonerDTO> getAllHistorySummoners() {
        List<HistorySummonerDTO> SummonerList = new ArrayList<HistorySummonerDTO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HISTORYSUMMONER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HistorySummonerDTO summoner = new HistorySummonerDTO();
                summoner.setPlatform(cursor.getString(0));
                summoner.setName(cursor.getString(1));
                summoner.setTier(cursor.getString(2));
                summoner.setTierInfo(cursor.getString(3));
                summoner.setProfileIcon(cursor.getString(4));
                // Adding contact to list
                SummonerList.add(summoner);
            } while (cursor.moveToNext());
        }

        // return contact list
        return SummonerList;
    }

    //Contact 정보 업데이트
    public int updateHistorySummoner(HistorySummonerDTO historySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, historySummonerDTO.getPlatform());
        values.put(KEY_NAME, historySummonerDTO.getName());
        values.put(KEY_TIER, historySummonerDTO.getTier());
        values.put(KEY_TIERINFO, historySummonerDTO.getTierInfo());
        values.put(KEY_PROFILEICON, historySummonerDTO.getProfileIcon());

        // updating row
        return db.update(TABLE_HISTORYSUMMONER, values, KEY_NAME + " = ? and " + KEY_PLATFORM + "=?",
                new String[]{historySummonerDTO.getName(), historySummonerDTO.getPlatform()});
    }

    // Contact 정보 삭제하기
    public void deleteHistorySummoner(HistorySummonerDTO historySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORYSUMMONER, KEY_NAME + " = ?",
                new String[]{String.valueOf(historySummonerDTO.getName())});
        db.close();
    }

    // Contact 정보 숫자
    public int getHistorySummonerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HISTORYSUMMONER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}

