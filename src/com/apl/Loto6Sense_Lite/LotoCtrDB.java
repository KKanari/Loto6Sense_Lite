package com.apl.Loto6Sense_Lite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class LotoCtrDB extends SQLiteOpenHelper{
	
	SQLiteDatabase db = null;
	public LotoCtrDB(Context context) {
		super(context, "Loto6SenseDB", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.beginTransaction();
		try {
			db.execSQL("create table lot6sensetbl " 
					+  "(numCount number primary key, "
					+   "number01 number not null, " 
					+   "number02 number not null, " 
					+   "number03 number not null, " 
					+   "number04 number not null, " 
					+   "number05 number not null, " 
					+   "number06 number not null, " 
					+   "numberB  number not null, " 
					+   "setBall  string not null);");

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	/**
	 * lot6sensetbl�e�[�u���ɑ΂��Ă�INSERT���s���B
	 * @param inHttpDat
	 */
	public void insertDB(String[][] inHttpDat) throws Exception{

		if( inHttpDat != null){

			final SQLiteDatabase dbTmp = getReadableDatabase();
			dbTmp.beginTransaction();
	
			try {
				Integer itemCntMax = 9;
				StringBuffer insertSQL = new StringBuffer("");
				for(int i = 0; i < itemCntMax; i++){
					if( insertSQL.length() > 0){
						insertSQL.append(",");
					}
					insertSQL.append("?");
				}
				
				SQLiteStatement stmt;
				stmt = dbTmp.compileStatement("insert into lot6sensetbl values (" + insertSQL.toString() + ");");
				for (String[] capital : inHttpDat) {
					
					// �f�[�^ ���ڐ� �`�F�b�N 
					if( itemCntMax != capital.length){
						throw new LotoException(LotoException.HTTPFILE_NOITEMCNT);
					}
					
					for( int i = 0; i < capital.length; i++ ){

						if( i != 8 ){
							// �f�[�^ �^ �`�F�b�N */
				            Double.parseDouble(capital[i]);
						}
						
			            // �o�C���h */
						stmt.bindString(i+1, capital[i]);
					}
						
					stmt.executeInsert();
				}			
				dbTmp.setTransactionSuccessful();
			} finally {
				dbTmp.endTransaction();
			}
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}
	
	/** �ŐV�̒��I����50����擾 */
	public Map<Integer, Integer> getDat50 (){
		
		String tmp = "";
		
		int tmpTo = getNewCount(); 
		int tmpFrom = tmpTo - 50 + 1;
		if( tmpFrom <= 0){
			tmpFrom = 1;
		}
		
		StringBuffer sqlAll = new StringBuffer();
		sqlAll.append("select num, count(numCount) from (");
		sqlAll.append("select numCount,number01 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number02 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number03 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number04 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number05 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number06 num from lot6sensetbl ");
		sqlAll.append(") where numCount between " + tmpFrom + " and " + tmpTo);
		sqlAll.append(" group by num ;");

		final SQLiteDatabase dbTmp = getReadableDatabase();
		Cursor c = dbTmp.rawQuery(sqlAll.toString(), null);
		c.moveToFirst();
		CharSequence[] list = new CharSequence[c.getCount()];

		Map<Integer, Integer> rtnMap = getInitMap();
		rtnMap.put(1, 0);
		Integer wkKey = 0;
		Integer wkDat = 0;
		tmp = "";

		for (int i = 0; i < list.length; i++) {

			tmp = String.valueOf(c.getString(0));
			wkKey = Integer.valueOf(tmp).intValue();

			tmp = String.valueOf(c.getString(1));
			wkDat = Integer.valueOf(tmp).intValue();

			rtnMap.put(wkKey, wkDat);
			
			c.moveToNext();
		}
		
		c.close();
		dbTmp.close();
		
		return rtnMap;
	}	
	
	// �{�[���p�f�[�^��ێ�����Map���쐬����B
	private Map<Integer, Integer> getInitMap(){

		Map<Integer, Integer> rtnMap = new HashMap<Integer, Integer>();
		
		for( int i = 1; i <= 43; i++ ){
			rtnMap.put(i, 0);
		}
		
		return rtnMap;
	}

	public Map<Integer, Integer> getDat5 (){
        // DB      
		String tmp = "";		
		int tmpTo = getNewCount(); 
		int tmpFrom = tmpTo - 5 + 1;
		if( tmpFrom <= 0){
			tmpFrom = 1;
		}
		
		StringBuffer sqlAll = new StringBuffer();
		sqlAll.append("select num, count(numCount) from (");
		sqlAll.append("select numCount,number01 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number02 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number03 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number04 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number05 num from lot6sensetbl ");
		sqlAll.append(" union all select numCount,number06 num from lot6sensetbl ");
		sqlAll.append(") where numCount between " + tmpFrom + " and " + tmpTo);
		sqlAll.append(" group by num ;");

		final SQLiteDatabase dbTmp = getReadableDatabase();
		Cursor c = dbTmp.rawQuery(sqlAll.toString(), null);
		c.moveToFirst();
		CharSequence[] list = new CharSequence[c.getCount()];
		
		Map<Integer, Integer> rtnMap = getInitMap();
		Integer wkKey = 0;
		Integer wkDat = 0;
		tmp = "";

		for (int i = 0; i < list.length; i++) {

			tmp = String.valueOf(c.getString(0));
			wkKey = Integer.valueOf(tmp).intValue();

			tmp = String.valueOf(c.getString(1));
			wkDat = Integer.valueOf(tmp).intValue();

			rtnMap.put(wkKey, wkDat);
			
			c.moveToNext();
		}
		
		c.close();
		dbTmp.close();
		
		return rtnMap;
	}	

	/** 
	 * �ŐV�̒��I�񐔂��擾 
	 * */
	public int getNewCount(){
        // DB      
		final SQLiteDatabase dbTmp = getReadableDatabase();
		
		// �ŐV�̒��I�񐔂��擾
		String sql = "select max(numCount) from lot6sensetbl;";
		Cursor c = dbTmp.rawQuery(sql, null);
		c.moveToFirst();
		CharSequence[] list = new CharSequence[c.getCount()];
		String rtnDat = "0";
		for (int i = 0; i < list.length; i++) {
			rtnDat = c.getString(0);
			c.moveToNext();
		}
		c.close();
		dbTmp.close();
		
		if( rtnDat == null ){
			rtnDat = "0";
		}
		
		return Integer.valueOf(rtnDat).intValue();
	}
	

	/** 
	 * �ŐV�̒��I�񐔂��擾 
	 * */
	public int getBigPoint( Integer inNum1,
							Integer inNum2,
							Integer inNum3,
							Integer inNum4,
							Integer inNum5,
							Integer inNum6
							){
        // DB      
		final SQLiteDatabase dbTmp = getReadableDatabase();
		
		// �ŐV�̒��I�񐔂��擾
		String sql = "select numCount from lot6sensetbl where ";
		sql = sql + " number01 = " + inNum1;
		sql = sql + " and number02 = " + inNum2;
		sql = sql + " and number03 = " + inNum3;
		sql = sql + " and number04 = " + inNum4;
		sql = sql + " and number05 = " + inNum5;
		sql = sql + " and number06 = " + inNum6 + ";";
		
		Cursor c = dbTmp.rawQuery(sql, null);
		c.moveToFirst();
		CharSequence[] list = new CharSequence[c.getCount()];
		String rtnDat = "";
		for (int i = 0; i < list.length; i++) {
			rtnDat = c.getString(0);
			c.moveToNext();
		}
		c.close();
		dbTmp.close();
		
		Integer rtn = 0;
		if( rtnDat.length() > 0){
			rtn = Integer.valueOf(rtnDat).intValue();
		}
		return rtn;
	}

	/** �ŐV�̒��I���ʂ��擾 */
	public ArrayList<LotoBallSet> getDatAll (){
		
		StringBuffer sqlAll = new StringBuffer();
		sqlAll.append("select numCount,number01,number02,number03,number04,number05,number06 from lot6sensetbl;");

		final SQLiteDatabase dbTmp = getReadableDatabase();
		Cursor c = dbTmp.rawQuery(sqlAll.toString(), null);
		c.moveToFirst();
		CharSequence[] list = new CharSequence[c.getCount()];
		
		ArrayList<LotoBallSet> setList = new ArrayList<LotoBallSet>();

		for (int i = 0; i < list.length; i++) {
			LotoBallSet oneSet = new LotoBallSet();
			
			oneSet.setLotCount(Integer.valueOf(c.getString(0)));
			
			for( int ii=1; ii < c.getColumnCount(); ii++){
				oneSet.setBallNumber(Integer.valueOf(c.getString(ii)));
			}			
			setList.add(oneSet);
			c.moveToNext();
		}
		
		c.close();
		dbTmp.close();
		
		return setList;
	}	
}
