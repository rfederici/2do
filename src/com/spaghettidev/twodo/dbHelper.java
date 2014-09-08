package com.spaghettidev.twodo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {

	static final String dbName = "notesDB";

	static final String tblNotes = "notesTable";
	
	static final String colID = "_id";
	static final String colContent = "_content";
	static final String colTimestamp = "_timestamp";
	static final String colIsDone = "_done";

	static final String viewEmps = "ViewEmps";
	private Writer writer;

	public dbHelper(Context context) {
		super(context, dbName, null, 3);
		Log.d("DB", "Created the database!");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("DB", db.getPath() + "Making the tables!");
		  
		  db.execSQL("CREATE TABLE " + tblNotes + " (" +
				  colID + " INTEGER PRIMARY KEY, " +
				  colContent + " TEXT, " +
				  colTimestamp + " TEXT, " +
				  colIsDone + " INT)");
		  
		  Log.d("DB", "Tables made!");
	}
	
	@SuppressLint("SimpleDateFormat")
	public void addNote(SQLiteDatabase db, String content) {
		SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
		String timestamp = s.format(new Date());
		content = content.replaceAll("'", "''");
		db.execSQL(String.format("INSERT INTO " + tblNotes + " VALUES (NULL, '%s', %s, %d);", content, timestamp, 0));
	}
	
	public Cursor getNotes(SQLiteDatabase db) {
		return db.query(tblNotes, null, colIsDone + "=0", null, null, null, null);
	}
	
	public boolean exportCSV(SQLiteDatabase db){
        
        try{
        
            Cursor cursor = db.rawQuery("select * from " + tblNotes, null);
            cursor.moveToFirst();
            
            JSONArray vals = new JSONArray();
	        
	        while (!cursor.isAfterLast()){
	        	JSONObject j = new JSONObject();
	        	j.put(colContent, cursor.getString(1));
	        	j.put(colTimestamp, cursor.getString(2));
	        	j.put(colIsDone, cursor.getString(3));
	        	vals.put(j);
	        	cursor.moveToNext();
	        }
	        
	        File outDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "TODO");
	        if (!outDir.isDirectory()) {
	            outDir.mkdir();
	        }
	        File outputFile = new File(outDir, "DBExport.txt");
	        if (outputFile.exists())outputFile.delete();
	        outputFile.createNewFile();
	        
	        writer = new BufferedWriter(new FileWriter(outputFile));
	        writer.write(vals.toString());
	        writer.close();
	        
	        return true;
        }
        catch (Exception e){
            return false;	
        }

	}
	
	public void importCSV (File input, SQLiteDatabase db) {

		try {
			InputStream inputStream = new FileInputStream(input);
	        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	        
	        StringBuilder stringBuilder = new StringBuilder();
	        
	        boolean done = false;
	        while (!done) {
	            final String line = reader.readLine();
	            done = (line == null);
	            
	            if (line != null) {
	                stringBuilder.append(line);
	            }
	        }
	        
	        reader.close();
	        inputStream.close();
			JSONArray ijs = new JSONArray(stringBuilder.toString());
			for (int i = 0; i < ijs.length(); i++){
				JSONObject jso = (JSONObject) ijs.get(i);
				Log.d("JSON", jso.toString());
				db.execSQL(String.format("INSERT INTO " + tblNotes + " VALUES (NULL, '%s', %s, %d);",
						jso.get(colContent).toString().replaceAll("'", "''"),
						jso.get(colTimestamp).toString(),
						Integer.parseInt(jso.get(colIsDone).toString())));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
