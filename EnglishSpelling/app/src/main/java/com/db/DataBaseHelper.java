package com.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;

	private static final String[] FILE_PARTS = { "score.db" };

	private static final String DB_NAME = "score" + DB_VERSION + ".db";

	private SQLiteDatabase myDataBase;

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

//	public void openDataBase() throws SQLException {
//		String myPath = DB_PATH + DB_NAME;
//		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
//				SQLiteDatabase.NO_LOCALIZED_COLLATORS);
//	}

	@Override
	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	private static String getDatabasePath(Context context) {
		Log.i("DREG", "Package ="+context.getPackageName());
		return "/data/data/" + context.getPackageName() + "/databases/";
		
	}

	public static void manageDatabase(Context context) {
		try {
			File pFile = new File(getDatabasePath(context));
			if (pFile.exists()) {
				if (pFile.isDirectory()) {
					File[] files = pFile.listFiles();
					if (files != null && files.length > 0) {
						for (int i = 0; i < files.length; i++) {
							if (files[i].isFile()
									&& (!files[i].getName().equals(DB_NAME))) {
								files[i].delete();
							}
						}
					}
				} else {
					pFile.delete();
					pFile.mkdirs();
				}
			} else {
				pFile.mkdirs();
			}
			File dbFile = new File(pFile, DB_NAME);
			if (!dbFile.exists()) {
				OutputStream myOutput = new FileOutputStream(dbFile);
				byte[] buffer = new byte[1024];
				int length;
				for (int i = 0; i < FILE_PARTS.length; i++) {
					InputStream myInput = context.getAssets().open(
							FILE_PARTS[i]);
					while ((length = myInput.read(buffer)) > 0) {
						myOutput.write(buffer, 0, length);
					}
					myInput.close();
				}
				myOutput.flush();
				myOutput.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
