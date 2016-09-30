package com.apl.Loto6Sense_Lite;

import com.apl.Loto6Sense_Lite.R;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

public class LotoSetting extends PreferenceActivity implements OnPreferenceChangeListener{
	
	private ListPreference lPref = null;

	/**
	 * 
	 */
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);    
        
        // 設定画面定義
        addPreferencesFromResource(R.xml.pref); 
        
        // 表示数
        lPref = (ListPreference)findPreference(LotoConst.INI_KEY_SENSE_CNT);
        lPref.setSummary(getSummaryMsg(lPref.getValue()));
        lPref.setOnPreferenceChangeListener(this);

        // 表示スピード
        lPref = (ListPreference)findPreference(LotoConst.INI_KEY_ANIME_SPEED);
        lPref.setSummary(getSummaryMsg(lPref.getValue()));
        lPref.setOnPreferenceChangeListener(this);

        // 予想方法
        lPref = (ListPreference)findPreference(LotoConst.INI_KEY_SENSE_TYPE);
        lPref.setSummary(getSummaryMsg(lPref.getValue()));
        lPref.setOnPreferenceChangeListener(this);
    }
    
    /** 
     * ボタンクリック 時
     */
    class PreferenceClick implements OnPreferenceClickListener {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			return false;
		}
    };
    
    /** 
     * 値選択時
     */
	@Override
	public boolean onPreferenceChange(Preference arg0, Object arg1) {
		if( arg1 != null ){
			arg0.setSummary(getSummaryMsg(arg1));			
			return true;
		}
		return false;
	}
	
	/**
	 * Summaryメッセージの作成
	 * @param inPara1
	 * @return
	 */
	private String getSummaryMsg(Object inPara1){
		return "『" + inPara1.toString() + "』を選択しています。";
	}
}


    