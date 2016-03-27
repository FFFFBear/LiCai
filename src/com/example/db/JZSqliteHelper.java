package com.example.db;

import com.example.bean.JZshouru;
import com.example.bean.JZzhichu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class JZSqliteHelper extends SQLiteOpenHelper {

    public static final String ZHICHU = "ZHICHU";// 支出
	public static final String SHOURU = "SHOURU";// 收入
	public Context context;
	public JZSqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL("CREATE TABLE IF NOT EXISTS " + 
				ZHICHU + "(" + "ID" + " integer primary key," + 
				JZzhichu.ZC_ITEM + " varchar," + 
				JZzhichu.ZC_YEAR + " Integer," + 
				JZzhichu.ZC_MONTH + " Integer," + 
				JZzhichu.ZC_WEEK + " Integer," + 
				JZzhichu.ZC_DAY + " Integer," + 
				JZzhichu.ZC_COUNT + " REAL," + 
				JZzhichu.ZC_BEIZHU + " varchar" + ");");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + 
				SHOURU + "(" + "ID" + " integer primary key," + 
				JZshouru.SR_ITEM + " varchar," + 
				JZshouru.SR_YEAR + " Integer," + 
				JZshouru.SR_MONTH + " Integer," + 
				JZshouru.SR_WEEK + " Integer," + 
				JZshouru.SR_DAY + " Integer," + 
				JZshouru.SR_COUNT + " REAL," + 
				JZshouru.SR_BEIZHU + " varchar" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + ZHICHU);
			db.execSQL("DROP TABLE IF EXISTS" + SHOURU);
		onCreate(db);
	}

	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn) {
		try {
			db.execSQL("ALTER TABLE " + ZHICHU + " CHANGE " + oldColumn + " " + newColumn + " " + typeColumn);
			db.execSQL("ALTER TABLE " + SHOURU + " CHANGE " + oldColumn + " " + newColumn + " " + typeColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}