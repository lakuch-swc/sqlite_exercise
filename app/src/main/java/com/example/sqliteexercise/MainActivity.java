package com.example.sqliteexercise;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText nameEditText;
    EditText emailEditText;
    EditText idEditText;
    String strName;
    String strEmail;
    String strID;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues values;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        idEditText = findViewById(R.id.idEditText);
        dbHelper = new DBHelper(MainActivity.this);
        values = new ContentValues();
    }

    public void onButtonClick(View view) {

        strName = nameEditText.getText().toString();
        strEmail = emailEditText.getText().toString();
        strID = idEditText.getText().toString();
        database = dbHelper.getWritableDatabase();

        switch(view.getId()) {
            case R.id.addButton:
                Log.d("Debug", "Add ---------------------------------------------------");
                values.put("name", strName);
                values.put("email", strEmail);
                long rowID = database.insert("myTable", null, values);
                Log.d("Debug", "Added Entry " + rowID);
                break;
            case R.id.readButton:
                Log.d("Debug", "Read --------------------------------------------------");
                database = dbHelper.getReadableDatabase();
                cursor = database.query("myTable", null, null, null, null, null, null);
                int cID = cursor.getColumnIndex("id");
                int nameColumn = cursor.getColumnIndex("name");
                int emailColumn = cursor.getColumnIndex("email");
                while(cursor.moveToNext()) {
                    Log.d("Debug", "id = " + cursor.getInt(cID));
                    Log.d("Debug", "name = " + cursor.getString(nameColumn));
                    Log.d("Debug", "email = " + cursor.getString(emailColumn));
                }
                cursor.close();
                break;
            case R.id.updateButton:
                Log.d("Debug", "Update ------------------------------------------------");
                values.put("name", strName);
                values.put("email", strEmail);
                if (!strID.equals("")) {
                    database.update("myTable", values, "id=?", new String[]{strID});
                    Log.d("Debug", "Entry " + strID + " updated to: name - " + strName + " email - " + strEmail);
                }
                else {
                    Log.d("Debug", "Missing id input");
                }
                break;
            case R.id.clearButton:
                Log.d("Debug", "Clear -------------------------------------------------");
                if (!strID.equals("")) {
                    database.delete("myTable", "id=" + strID, null);
                    Log.d("Debug", "Entry " + strID + " deleted.");
                }
                else {
                    Log.d("Debug", "Missing id input");
                }
                break;
            case R.id.getCountButton:
                Log.d("Debug", "Count -------------------------------------------------");
                cursor = database.rawQuery("SELECT COUNT(*) FROM myTable", null);
                cursor.moveToFirst();
                Log.d("Debug", "Rows count = " + cursor.getInt(0));
                cursor.close();
                break;
            default:
                break;
        }
        dbHelper.close();
    }
}
