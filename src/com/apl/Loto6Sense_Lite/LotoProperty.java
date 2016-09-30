package com.apl.Loto6Sense_Lite;

import java.util.ArrayList;

import com.apl.Loto6Sense_Lite.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LotoProperty {
	
	private SharedPreferences mPref = null;
	private String mViewCnt   = "";
	private String mViewSpeed = "";
	private String mSenseType = "";
	private String mHoldList = "";
	private String mUpdateDate = "";
	private Context mContext = null;
	
	/**
	 * ����
	 */
	public LotoProperty(Context inContext){
		mContext = inContext;
		mPref = PreferenceManager.getDefaultSharedPreferences(mContext); 
		updatePara();
	}

	/**
	 * �ēǂݍ���
	 */
	public void reLoad(){
		mPref = PreferenceManager.getDefaultSharedPreferences(mContext); 
		updatePara();
	}

	/**
	 * �ǂݍ��݃f�[�^�𔽉f
	 */
	private void updatePara(){
		
	    // DB�X�V���t
		mUpdateDate = mPref.getString(LotoConst.INI_KEY_DB_UPDATE, "");	  
		
		// ���I�ԍ��\����
	    mViewCnt = mPref.getString(LotoConst.INI_KEY_SENSE_CNT, mContext.getString(R.string.def_view_cnt));	

	    // ���I�ԍ��\���X�s�[�h
	    mViewSpeed = mPref.getString(LotoConst.INI_KEY_ANIME_SPEED, mContext.getString(R.string.def_view_speed));	

	    // ���I�ԍ��\�z�v�Z���@
	    mSenseType = mPref.getString(LotoConst.INI_KEY_SENSE_TYPE, mContext.getString(R.string.def_sense_type));	
	    
	    // HOLD����
	    mHoldList = mPref.getString(LotoConst.INI_KEY_HOLD_NUMBER, "");	
	    
	}

	/**
	 * �t�@�C���ۑ�
	 */
	public void save(){

        SharedPreferences.Editor saveEdit=mPref.edit();
        
	    // DB�X�V���t
        saveEdit.putString(LotoConst.INI_KEY_DB_UPDATE,mUpdateDate);
        saveEdit.putString(LotoConst.INI_KEY_SENSE_CNT   ,mViewCnt);
        saveEdit.putString(LotoConst.INI_KEY_ANIME_SPEED ,mViewSpeed);
        saveEdit.putString(LotoConst.INI_KEY_SENSE_TYPE ,mSenseType);
        saveEdit.putString(LotoConst.INI_KEY_HOLD_NUMBER,mHoldList);
        
        saveEdit.commit();
	}

	/**
	 * 
	 * @return
	 */
	public String getUpdateDate() {
		return mUpdateDate;
	}

	/**
	 * 
	 * @param inUpdateDate
	 */
	public void setUpdateDate(String inUpdateDate) {
		mUpdateDate = inUpdateDate;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getViewCnt() {
		return Integer.valueOf(mViewCnt).intValue();
	}

	/**
	 * 
	 * @return
	 */
	public int getViewSpeed() {
		int rtnTmp = 0;
		String[] itemList = mContext.getResources().getStringArray( R.array.loto_speed );

		for(int i=0; i<itemList.length; i++){
			if(itemList[i].equals(mViewSpeed)){	
		    	rtnTmp = itemList.length - i;
		    	break;
			}
		}
		return rtnTmp;
	}

	/**
	 *  
	 * @return
	 */
	public int getSenseType() {
		int rtnTmp = 0;
		String[] itemList = mContext.getResources().getStringArray( R.array.loto_sense );

		for(int i=0; i<itemList.length; i++){
			if(itemList[i].equals(mSenseType)){	
		    	rtnTmp = i;
		    	break;
			}
		}
		return rtnTmp;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getHoldList() {
		String[] rtnTmp = null;
		if( mHoldList.length() > 0){
			rtnTmp = mHoldList.split(",");
		}
		return rtnTmp;
	}

	/**
	 * 
	 * @param inHoldList
	 */
	public void setmHoldList(String inHoldList) {
		mHoldList = inHoldList;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String[]> loadLastViewBallSetList(){
		ArrayList<String[]> rtnDat = new ArrayList<String[]>();
		int intMax = Integer.valueOf(mPref.getString("lastViewBallSetMax", "0")).intValue();	
		for( int i=0; i<intMax;i++){
			String getDat = mPref.getString("lastViewBallSet"+i, "0");	  
			String[] getDatSplit = getDat.split(",");
			rtnDat.add(getDatSplit);
		}
		return rtnDat;
	}
    /**
     * 
     */
    public void saveLastViewBallSetList(ArrayList<LotoBallSet> inBallSetList){

        if( inBallSetList != null){
            SharedPreferences.Editor saveEdit=mPref.edit();	  	    
	  	    for( int i=0; i< inBallSetList.size(); i++){
	  	    	
	  	    	StringBuffer writeBall = new StringBuffer("");
	  	    	
	  	    	LotoBallSet tmpBallSet = inBallSetList.get(i);
	  	    	ArrayList<Integer> tmpList = tmpBallSet.getBallNumbers();
	  	    	for( int ii=0; ii<tmpList.size(); ii++){
	  	    		if( writeBall.length() > 0){
	  	    			writeBall.append(",");
	  	    		}
	  	    		writeBall.append(tmpList.get(ii));
	  	    	}
	  	    	saveEdit.putString("lastViewBallSet"+i,writeBall.toString());
	  	    }
	  	  saveEdit.putString("lastViewBallSetMax",String.valueOf(inBallSetList.size()));
	  	  saveEdit.commit();
        }
    }
}
