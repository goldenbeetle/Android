package com.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseAccessor {

	public static DataBaseHelper myDbHelper = null;
	public static SQLiteDatabase rdb = null;
	public static SQLiteDatabase wdb = null;

	public static synchronized final void initDB(Context context)
			throws Exception {
		if (myDbHelper == null) {
			myDbHelper = new DataBaseHelper(context);
			rdb = myDbHelper.getReadableDatabase();
			wdb = myDbHelper.getWritableDatabase();
		}
	}

	public static synchronized final void closeDB() {
		if (myDbHelper != null) {
			myDbHelper.close();
			rdb.close();
			wdb.close();
			myDbHelper = null;
			rdb = null;
			wdb = null;
		}
	}

	public static final boolean isTableExists(String tableName) {
		boolean ret = false;
		try {
			String qry = "SELECT COUNT(*) FROM sqlite_master WHERE (type='table') AND (name='"
					+ tableName + "')ORDER BY name;";
			Cursor cursor = rdb.rawQuery(qry, null);
			while (cursor.moveToNext()) {
				if (Integer.parseInt(cursor.getString(0)) > 0) {
					ret = true;
				}
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static final void createScoreTable() {
		try {
			String tblName = "score";
			if (!isTableExists(tblName)) {
				wdb.execSQL("create table if not exists "
						+ tblName
						+ " (id INTEGER primary key autoincrement,playerName text not null,score INTEGER,game_level text not null ,numOfQuestion INTEGER)");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean addScore(String playerName, int score,
			String game_level, int numOfQuestion) {

		try {
			Log.i("DREG", "Player Name= " + playerName);
			String tableName = "score";
			ContentValues values = new ContentValues();
			values.put("playerName", playerName);
			values.put("score", score);
			values.put("game_level", game_level);
			values.put("numOfQuestion", numOfQuestion);
			wdb.insertOrThrow(tableName, null, values);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static List<String[]> getScoreList(String game_level,
			int numOfQuestion) {
		List<String[]> ret = null;
		try {
			String[] selections = null;
			String qry = null;

			qry = "Select playerName,score,id from score where game_level='"
					+ game_level + "' and numOfQuestion=" + numOfQuestion
					+ " ORDER BY score DESC limit 0,5";

			Cursor cursor = wdb.rawQuery(qry, selections);
			ret = new ArrayList<String[]>();
			while (cursor.moveToNext()) {
				String[] row = new String[2];
				row[0] = "" + cursor.getString(0);
				row[1] = cursor.getString(1);
				Log.i("DREG", row[0]);
				Log.i("DREG", row[1]);
				ret.add(row);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static boolean isScoreInTop(String game_level,int numOfQuestion,int score) {
		List<String[]> ret = null;
		boolean retVal = false;
		try {
			String[] selections = null;
			String qry = null;

			qry = "Select playerName,score,id from score where game_level='"
					+ game_level + "' and numOfQuestion=" + numOfQuestion
					+ " ORDER BY score DESC limit 0,5";

			Cursor cursor = wdb.rawQuery(qry, selections);
			ret = new ArrayList<String[]>();
			while (cursor.moveToNext()) {
				String[] row = new String[2];
				row[0] = "" + cursor.getString(0);
				row[1] = cursor.getString(1);
				Log.i("DREG", row[0]);
				Log.i("DREG", row[1]);
				ret.add(row);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ret==null || ret.isEmpty()||ret.size()<5) {
			retVal = true;
		} else {
			for (String[] s: ret)
		    {
		      int dbscore=0;
		      try {
		    	  dbscore=Integer.parseInt(s[1]);
		      }catch(Exception e) {
		    	  dbscore=0; 
		      }
		      if(dbscore<score) {
		    	  retVal = true;
		      }
		    }
		}
		return retVal;
	}
	

	public static int getMaxScore(String game_level, int numOfQuestion) {
		int maxScore = 0;
		try {
			String[] selections = null;
			String qry = null;

			qry = "SELECT MAX(score) from score where game_level='"
					+ game_level + "' and numOfQuestion=" + numOfQuestion;

			Cursor cursor = wdb.rawQuery(qry, selections);
			while (cursor.moveToNext()) {
				maxScore = cursor.getInt(0);
				Log.i("DREG", "Max score =" + maxScore);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return maxScore;
	}

}
