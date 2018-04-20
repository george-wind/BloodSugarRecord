package george.ni.medicare.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import george.ni.medicare.entity.BloodSugarEntity;


public class MedicareRecordDbHelper extends SQLiteOpenHelper {
    public static final String tableName_BLODD_SUGAR = "blood_sugar";
    public static final String tableName_BLODD_PRESSURE = "blood_pressure";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEST_TIME_MILLIS = "testTimeMills";
    public static final String COLUMN_TEST_YEAR = "TestYear";
    public static final String COLUMN_TEST_MONTH = "TestMonth";
    public static final String COLUMN_TEST_DAY = "TestDay";
    public static final String COLUMN_RESULT = "result";
    public static final String COLUMN_STATE = "state";
    private Map<String, String> tableContent = new HashMap<>();
    private static MedicareRecordDbHelper bloodSugarDbHelper;
    public final static Object dbSync = new Object();
    private static final String DATABASE_NAME = "MEDICARE_RECORD.db";
    private static final int BLOOD_SUGAR_DB_VERISON = 2;//first version，2018/4/12

    public MedicareRecordDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static MedicareRecordDbHelper getInstance(Context context) {
        if (bloodSugarDbHelper == null) {
            synchronized (dbSync) {
                if (bloodSugarDbHelper == null) {
//                    File dir = new File(CollaborationHeart.getUserStorage().getInternalPath("Databases"));
//                    if (!dir.exists()) {
//                        dir.mkdirs();
//                    }
//                    File file = new File(dir, DATABASE_NAME);
//                    if (!file.exists()) {
//                        try {
//                            file.createNewFile();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    bloodSugarDbHelper = new MedicareRecordDbHelper(context, DATABASE_NAME, null, BLOOD_SUGAR_DB_VERISON);
                }
            }
        }
        return bloodSugarDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS ";
        sql += this.tableName_BLODD_SUGAR;
        sql += "(";
        sql += COLUMN_ID;
        sql += " INTEGER PRIMARY KEY,";
        tableContent.put(COLUMN_RESULT, " text");
        tableContent.put(COLUMN_TEST_TIME_MILLIS, " integer");
        tableContent.put(COLUMN_STATE, " integer");
        tableContent.put(COLUMN_TEST_YEAR, " integer");
        tableContent.put(COLUMN_TEST_MONTH, " integer");
        tableContent.put(COLUMN_TEST_DAY, " integer");
        Set set = tableContent.entrySet();
        Iterator iterator = set.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            index++;
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            if (mapEntry.getValue() != null) {
                sql += (String) mapEntry.getKey();
                sql += " ";
                sql += (String) mapEntry.getValue();

                if (index < this.tableContent.size()) {
                    sql += ",";
                }
            }
        }
        sql += ")";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql2 = "CREATE TABLE IF NOT EXISTS ";
        sql2 += this.tableName_BLODD_PRESSURE;
        sql2 += "(";
        sql2 += COLUMN_ID;
        sql2 += " INTEGER PRIMARY KEY,";
        tableContent.put(COLUMN_RESULT, " text");
        tableContent.put(COLUMN_TEST_TIME_MILLIS, " integer");
        tableContent.put(COLUMN_STATE, " integer");
        tableContent.put(COLUMN_TEST_YEAR, " integer");
        tableContent.put(COLUMN_TEST_MONTH, " integer");
        tableContent.put(COLUMN_TEST_DAY, " integer");
        Set set2 = tableContent.entrySet();
        Iterator iterator2 = set2.iterator();
        int index = 0;
        while (iterator2.hasNext()) {
            index++;
            Map.Entry mapEntry = (Map.Entry) iterator2.next();
            if (mapEntry.getValue() != null) {
                sql2 += (String) mapEntry.getKey();
                sql2 += " ";
                sql2 += (String) mapEntry.getValue();

                if (index < this.tableContent.size()) {
                    sql2 += ",";
                }
            }
        }
        sql2 += ")";
        db.execSQL(sql2);
    }

    public List<BloodSugarEntity> loadNormalCheckDatas(String tableName,int start, int count, String year, String month) {
        List<BloodSugarEntity> bloodSugarEntityList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = new String[]{COLUMN_ID, COLUMN_RESULT, COLUMN_TEST_TIME_MILLIS};
        String limit = null;
        String selection = COLUMN_STATE + " =? ";

        ArrayList<String> stringArgsList = new ArrayList<>();
        stringArgsList.add("0");

        if (start != -1 && count != -1) {
            limit = start + "," + count;
        }

        if (!TextUtils.isEmpty(year)) {
            selection += " AND " + COLUMN_TEST_YEAR + " =? ";
            stringArgsList.add(year);
        }
        if (!TextUtils.isEmpty(month)) {
            selection += " AND " + COLUMN_TEST_MONTH + " =? ";
            stringArgsList.add(month);
        }
        String[] stringArgs = new String[stringArgsList.size()];
        stringArgsList.toArray(stringArgs);
        Cursor cursor = null;
        try {
            cursor = database.query(tableName, columns, selection, stringArgs, null, null, COLUMN_TEST_TIME_MILLIS + " DESC", limit);
            while (cursor.moveToNext()) {
                BloodSugarEntity entity = new BloodSugarEntity();
                entity.setId(cursor.getLong(0));
                entity.setResult(cursor.getString(1));
                entity.setTestTimeStamp(cursor.getLong(2));
                bloodSugarEntityList.add(entity);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return bloodSugarEntityList;
    }


    public List<BloodSugarEntity> loadAllCheckRecords(String tableName) {
        List<BloodSugarEntity> bloodSugarEntityList = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = new String[]{COLUMN_ID, COLUMN_RESULT, COLUMN_TEST_TIME_MILLIS, COLUMN_STATE};
        String limit = null;
        String selection = null;
        String[] stringArgs = null;
        Cursor cursor = null;
        try {
            cursor = database.query(tableName, columns, selection, stringArgs, null, null, COLUMN_TEST_TIME_MILLIS + " DESC", limit);
            while (cursor.moveToNext()) {
                BloodSugarEntity entity = new BloodSugarEntity();
                entity.setId(cursor.getLong(0));
                entity.setResult(cursor.getString(1));
                entity.setTestTimeStamp(cursor.getLong(2));
                entity.setState(cursor.getInt(3));
                bloodSugarEntityList.add(entity);
            }
        } catch (Exception ex) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            database.close();
        }
        return bloodSugarEntityList;
    }

    public int deleteCheckDataByLogic(String tableName,BloodSugarEntity entity) {
        entity.setState(1);
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATE, entity.getState());
        return database.update(tableName, contentValues, COLUMN_ID + " =? ", new String[]{entity.getId() + ""});
    }

    public int deleteCheckDataPhysically(String tableName,BloodSugarEntity entity) {
        SQLiteDatabase database = getWritableDatabase();
        return database.delete(tableName, COLUMN_ID + " =? ", new String[]{entity.getId() + ""});
    }

    public long insertCheckData(String tableName,BloodSugarEntity entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_RESULT, entity.getResult());
        contentValues.put(COLUMN_TEST_TIME_MILLIS, entity.getTestTimeStamp());
        contentValues.put(COLUMN_TEST_DAY, entity.getTestDay());
        contentValues.put(COLUMN_TEST_YEAR, entity.getTestYear());
        contentValues.put(COLUMN_TEST_MONTH, entity.getTestMonth());
        contentValues.put(COLUMN_STATE, 0);
        SQLiteDatabase database = getWritableDatabase();
        return database.insert(tableName, null, contentValues);
    }
}
