package theekransje.douaneapp.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import theekransje.douaneapp.Domain.APITask;

/**
 * Created by Sander on 5/24/2018.
 */

////DB Waar persistente data wordt opgeslagen alvorens het verzenden.


public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final String name = "DouaneApp";
    private static final int version = 2;

    public DBHelper(Context context) {
        super(context, name, null, version);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: call");
        db.execSQL(APITask.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: call");
        db.execSQL("DROP TABLE " + name );
        onCreate(db);
    }

    public void insertTask(APITask apiTask){
        Log.d(TAG, "insertTask: called");
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(APITask.COLUMN_OBJECT, apiTask.getTaskAsJSON().toString());
        Log.d(TAG, "insertTask: INSERTING");
        db.insert(APITask.TABLE_NAME,null,values);

        Log.d(TAG, "insertTask inserted:  " + apiTask.getTaskAsJSON().toString());

        db.close();
    }

    public ArrayList<APITask> getAllTasks(){
     //   Log.d(TAG, "getAllTasks: called");
        
        ArrayList<APITask> r = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + APITask.TABLE_NAME+ " ORDER BY " +
                APITask.COLUMN_ID + " DESC";
        
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try{
                    APITask a = new APITask(new JSONObject(cursor.getString(cursor.getColumnIndex(APITask.COLUMN_OBJECT))));
                    a.setId(cursor.getString(cursor.getColumnIndex(APITask.COLUMN_ID)));
                    r.add(a);

                    Log.d(TAG, "getAllTasks: Task in API" + a.getEndpoint() + "  " + a.getApiMethod().toString() + "  " + a.getJSONOBJECT().toString());
                    Log.d(TAG, "getAllTasks: RETURNING SIZE " + r.size());
                }catch (Exception e){
                    e.printStackTrace();
                }
                
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list

        return r;

    }


    public void removeTask(APITask apiTask){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + APITask.TABLE_NAME + " WHERE " + APITask.COLUMN_ID + " = " + apiTask.getId()  +";");

        Log.d(TAG, "remoteTask: DELETE FROM DB");
        db.close();
    }
}
