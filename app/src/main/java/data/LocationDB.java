package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.text.format.Time;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.LocationData;

/**
 * Created by erevear on 5/16/15.
 */
public class LocationDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "location.db";
    private static final int DATABASE_VERSION = 27;
    private static final String LOCATIONS_TABLE = "Locations";
    public static final String LAT_COLUMN_NAME = "latitude";
    public static final String LON_COLUMN_NAME = "longitude";
    public static final String SPEED_COLUMN_NAME = "speed";
    public static final String HEADING_COLUMN_NAME = "heading";
    public static final String TIME_COLUMN_NAME = "timestamp";
    public static final String USERID_COLUMN_NAME = "userid";
    private Context context;
    private SQLiteDatabase db;

    private SQLiteStatement insertStmt;
    private static final String INSERT = "insert into " + LOCATIONS_TABLE
            + "(userid, latitude, longitude, speed, heading, timestamp) values (?, ?, ?, ?, ?, ?)";

    public LocationDB() {
        super(null, DATABASE_NAME, null, DATABASE_VERSION);
        setContext(context);
    }

    public LocationDB(Context the_context) {
        super(the_context, DATABASE_NAME, null, DATABASE_VERSION);
        context = the_context;
    }


    /**
     * Inserts the id and other information
     * If successful, returns the rowid otherwise -1.
     */
    public boolean insert(String userID, double latitude, double longitude, double speed, double heading) throws SQLException {
        if(db!=null){
            db = this.getWritableDatabase();
        }



        ContentValues contentValues = new ContentValues();
        contentValues.put(USERID_COLUMN_NAME, userID);
        contentValues.put(LAT_COLUMN_NAME, latitude);
        contentValues.put(LON_COLUMN_NAME, longitude);
        contentValues.put(SPEED_COLUMN_NAME, speed);
        contentValues.put(HEADING_COLUMN_NAME, heading);
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        Date date = new Date();
        long time = date.getTime() / 1000;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int millisecond = cal.get(Calendar.MILLISECOND);
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
        Log.d("LocationDB", "time " + time);
        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        contentValues.put(TIME_COLUMN_NAME, Calendar.getInstance().getTimeInMillis() / 1000);
        if(db!=null){
            db.insert(LOCATIONS_TABLE, null, contentValues);
        }

        return true;
    }

    public Cursor getData(String id, long start, long end) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + LOCATIONS_TABLE + " WHERE timestamp BETWEEN ? AND ? AND userid = ?";
        String[] args = {Long.toString(start), Long.toString(end), id};
        Cursor res = db.rawQuery(query, args);
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Locations", null);
        return res;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Locations" +
                "(userid TEXT, latitude TEXT, longitude TEXT, speed TEXT, heading TEXT, timestamp LONG)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Locations");
        onCreate(db);
    }

    /**
     * Delete everything from example
     */
    public void deleteAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(LOCATIONS_TABLE, null, null);
    }

    /**
     * Return an array list of edu.uw.tacoma.mmuppa.cssappwithwebservices.model.Course objects from the
     * data returned from select query on Courses table.
     *
     * @return
     */
    public ArrayList<LocationData> selectAll() {
        ArrayList<LocationData> list = new ArrayList<LocationData>();
        Cursor cursor = this.db.query(LOCATIONS_TABLE, new String[]
                {"latitude", "longitude", "speed", "heading", "timestamp"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {

                LocationData e = new LocationData(cursor.getDouble(0), cursor.getDouble(1),
                        cursor.getDouble(2), cursor.getString(3), cursor.getString(4));
                list.add(e);
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }
//    public void close()
//    {
//        db.close();
//    }

    /**
     * Return the latitude when id is passed.
     * null if no record found.
     *
     * @param id
     * @return
     */
    public Cursor selectByID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from Locations WHERE userid=?", new String[]{id});
        return res;
    }


    public void setContext(Context the_context) {
        this.context = the_context;
    }
}