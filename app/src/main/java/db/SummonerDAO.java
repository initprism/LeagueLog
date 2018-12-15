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
    private static final String TABLE_BOOKMARKSUMMONER = "bookmarkSummoner";

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
    private static final String KEY_BOOKMARK = "bookmark";

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
                + KEY_LEVEL + " TEXT,"
                + KEY_PROFILEICON + " TEXT,"
                + KEY_BOOKMARK + " TEXT,"
                + " PRIMARY KEY (" + KEY_NAME + ", " + KEY_PLATFORM + "))";

        String CREATE_BOOKMARKSUMMONER_TABLE = "CREATE TABLE " + TABLE_BOOKMARKSUMMONER + "("
                + KEY_PLATFORM + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_TIER + " TEXT,"
                + KEY_TIERINFO + " TEXT,"
                + KEY_LEVEL + " TEXT,"
                + KEY_PROFILEICON + " TEXT,"
                + " PRIMARY KEY (" + KEY_NAME + ", " + KEY_PLATFORM + "))";

        db.execSQL(CREATE_MYSUMMONER_TABLE);
        db.execSQL(CREATE_HISTORYSUMMONER_TABLE);
        db.execSQL(CREATE_BOOKMARKSUMMONER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYSUMMONER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORYSUMMONER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKMARKSUMMONER);

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
        values.put(KEY_LEVEL, historySummonerDTO.getLevel());
        values.put(KEY_PROFILEICON, historySummonerDTO.getProfileIcon());
        values.put(KEY_BOOKMARK, historySummonerDTO.getBookmark());

        // Inserting Row
        db.insert(TABLE_HISTORYSUMMONER, null, values);
        db.close(); // Closing database connection
    }

    //name 에 해당하는 HistorySummoner 객체 가져오기
    public HistorySummonerDTO getHistorySummoner(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORYSUMMONER, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_TIER, KEY_TIERINFO, KEY_LEVEL, KEY_PROFILEICON, KEY_BOOKMARK}, KEY_NAME + "=?",
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
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        // return contact
        return historySummonerDTO;
    }

    //name,pf 에 해당하는 HistorySummoner 객체 가져오기
    public HistorySummonerDTO getHistorySummoner(String name, String platform) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HISTORYSUMMONER, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_TIER, KEY_TIERINFO, KEY_LEVEL, KEY_PROFILEICON, KEY_BOOKMARK}, KEY_NAME + "=? AND " + KEY_PLATFORM + " =?",
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
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));
        // return contact
        return historySummonerDTO;
    }

    // 모든 HistorySummoner 정보 가져오기
    public ArrayList<HistorySummonerDTO> getAllHistorySummoners() {
        ArrayList<HistorySummonerDTO> SummonerList = new ArrayList<HistorySummonerDTO>();
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
                summoner.setLevel(cursor.getString(4));
                summoner.setProfileIcon(cursor.getString(5));
                summoner.setBookmark(cursor.getString(6));
                // Adding contact to list
                SummonerList.add(summoner);
            } while (cursor.moveToNext());
        }

        // return contact list
        return SummonerList;
    }

    //historySummoner 정보 업데이트
    public int updateHistorySummoner(HistorySummonerDTO historySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, historySummonerDTO.getPlatform());
        values.put(KEY_NAME, historySummonerDTO.getName());
        values.put(KEY_TIER, historySummonerDTO.getTier());
        values.put(KEY_TIERINFO, historySummonerDTO.getTierInfo());
        values.put(KEY_LEVEL, historySummonerDTO.getLevel());
        values.put(KEY_PROFILEICON, historySummonerDTO.getProfileIcon());
        values.put(KEY_BOOKMARK, historySummonerDTO.getBookmark());

        // updating row
        return db.update(TABLE_HISTORYSUMMONER, values, KEY_NAME + " = ? and " + KEY_PLATFORM + "=?",
                new String[]{historySummonerDTO.getName(), historySummonerDTO.getPlatform()});
    }

    //historySummoner 정보 갱신
    public void replaceHistorySummoner(HistorySummonerDTO historySummonerDTO) {

        deleteHistorySummoner(historySummonerDTO);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, historySummonerDTO.getPlatform());
        values.put(KEY_NAME, historySummonerDTO.getName());
        values.put(KEY_TIER, historySummonerDTO.getTier());
        values.put(KEY_TIERINFO, historySummonerDTO.getTierInfo());
        values.put(KEY_LEVEL, historySummonerDTO.getLevel());
        values.put(KEY_PROFILEICON, historySummonerDTO.getProfileIcon());
        values.put(KEY_BOOKMARK, historySummonerDTO.getBookmark());

        // updating row
         // Inserting Row
        db.insert(TABLE_HISTORYSUMMONER, null, values);
        db.close(); // Closing database connection
    }

    // historySummoner 정보 삭제하기
    public void deleteHistorySummoner(HistorySummonerDTO historySummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HISTORYSUMMONER, KEY_NAME + " = ? and " + KEY_PLATFORM + " = ?",
                new String[]{historySummonerDTO.getName(), historySummonerDTO.getPlatform()});
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
      /**
     * BookmarkSummoner CRUD
     */

    // 새로운 BookmarkSummonerDTO 함수 추가
    public void addBookmarkSummoner(BookmarkSummonerDTO bookmarkSummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, bookmarkSummonerDTO.getPlatform());
        values.put(KEY_NAME, bookmarkSummonerDTO.getName());
        values.put(KEY_TIER, bookmarkSummonerDTO.getTier());
        values.put(KEY_TIERINFO, bookmarkSummonerDTO.getTierInfo());
        values.put(KEY_LEVEL, bookmarkSummonerDTO.getLevel());
        values.put(KEY_PROFILEICON, bookmarkSummonerDTO.getProfileIcon());

        // Inserting Row
        db.insert(TABLE_BOOKMARKSUMMONER, null, values);
        db.close(); // Closing database connection
    }

    //name 에 해당하는 BookmarkSummoner 객체 가져오기
    public BookmarkSummonerDTO getBookmarkSummoner(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKMARKSUMMONER, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_TIER, KEY_TIERINFO, KEY_LEVEL, KEY_PROFILEICON}, KEY_NAME + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;

        BookmarkSummonerDTO bookmarkSummonerDTO = new BookmarkSummonerDTO(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));
        // return contact
        return bookmarkSummonerDTO;
    }

    //name,pf 에 해당하는 BookmarkSummoner 객체 가져오기
    public BookmarkSummonerDTO getBookmarkSummoner(String name, String platform) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BOOKMARKSUMMONER, new String[]{KEY_PLATFORM,
                        KEY_NAME, KEY_TIER, KEY_TIERINFO, KEY_LEVEL, KEY_PROFILEICON}, KEY_NAME + "=? AND " + KEY_PLATFORM + " =?",
                new String[]{name, platform}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() == 0)
            return null;


        BookmarkSummonerDTO bookmarkSummonerDTO = new BookmarkSummonerDTO(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));
        // return contact
        return bookmarkSummonerDTO;
    }

    // 모든 BookmarkSummoner 정보 가져오기
    public ArrayList<BookmarkSummonerDTO> getAllBookmarkSummoners() {
        ArrayList<BookmarkSummonerDTO> SummonerList = new ArrayList<BookmarkSummonerDTO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BOOKMARKSUMMONER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BookmarkSummonerDTO summoner = new BookmarkSummonerDTO();
                summoner.setPlatform(cursor.getString(0));
                summoner.setName(cursor.getString(1));
                summoner.setTier(cursor.getString(2));
                summoner.setTierInfo(cursor.getString(3));
                summoner.setLevel(cursor.getString(4));
                summoner.setProfileIcon(cursor.getString(5));
                // Adding contact to list
                SummonerList.add(summoner);
            } while (cursor.moveToNext());
        }

        // return contact list
        return SummonerList;
    }

    //BookmarkSummoner 정보 업데이트
    public int updateBookmarkSummoner(BookmarkSummonerDTO bookmarkSummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, bookmarkSummonerDTO.getPlatform());
        values.put(KEY_NAME, bookmarkSummonerDTO.getName());
        values.put(KEY_TIER, bookmarkSummonerDTO.getTier());
        values.put(KEY_TIERINFO, bookmarkSummonerDTO.getTierInfo());
        values.put(KEY_LEVEL, bookmarkSummonerDTO.getLevel());
        values.put(KEY_PROFILEICON, bookmarkSummonerDTO.getProfileIcon());

        // updating row
        return db.update(TABLE_BOOKMARKSUMMONER, values, KEY_NAME + " = ? and " + KEY_PLATFORM + "=?",
                new String[]{bookmarkSummonerDTO.getName(), bookmarkSummonerDTO.getPlatform()});
    }

    //BookmarkSummoner 정보 갱신
    public void replaceBookmarkSummoner(BookmarkSummonerDTO bookmarkSummonerDTO) {

        deleteBookmarkSummoner(bookmarkSummonerDTO);

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PLATFORM, bookmarkSummonerDTO.getPlatform());
        values.put(KEY_NAME, bookmarkSummonerDTO.getName());
        values.put(KEY_TIER, bookmarkSummonerDTO.getTier());
        values.put(KEY_TIERINFO, bookmarkSummonerDTO.getTierInfo());
        values.put(KEY_LEVEL, bookmarkSummonerDTO.getLevel());
        values.put(KEY_PROFILEICON, bookmarkSummonerDTO.getProfileIcon());

        // updating row
         // Inserting Row
        db.insert(TABLE_BOOKMARKSUMMONER, null, values);
        db.close(); // Closing database connection
    }

    // BookmarkSummoner 정보 삭제하기
    public void deleteBookmarkSummoner(BookmarkSummonerDTO bookmarkSummonerDTO) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKMARKSUMMONER, KEY_NAME + " = ? and " + KEY_PLATFORM + " = ?",
                new String[]{bookmarkSummonerDTO.getName(), bookmarkSummonerDTO.getPlatform()});
        db.close();
    }

    // Contact 정보 숫자
    public int getBookmarkSummonerCount() {
        String countQuery = "SELECT  * FROM " + TABLE_BOOKMARKSUMMONER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}

