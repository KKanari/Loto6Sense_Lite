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
        
        // �ݒ��ʒ�`
        addPreferencesFromResource(R.xml.pref); 
        
        // �\����
        lPref = (ListPreference)findPreference(LotoConst.INI_KEY_SENSE_CNT);
        lPref.setSummary(getSummaryMsg(lPref.getValue()));
        lPref.setOnPreferenceChangeListener(this);

        // �\���X�s�[�h
        lPref = (ListPreference)findPreference(LotoConst.INI_KEY_ANIME_SPEED);
        lPref.setSummary(getSummaryMsg(lPref.getValue()));
        lPref.setOnPreferenceChangeListener(this);

        // �\�z���@
        lPref = (ListPreference)findPreference(LotoConst.INI_KEY_SENSE_TYPE);
        lPref.setSummary(getSummaryMsg(lPref.getValue()));
        lPref.setOnPreferenceChangeListener(this);
    }
    
    /** 
     * �{�^���N���b�N ��
     */
    class PreferenceClick implements OnPreferenceClickListener {

		@Override
		public boolean onPreferenceClick(Preference preference) {
			return false;
		}
    };
    
    /** 
     * �l�I����
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
	 * Summary���b�Z�[�W�̍쐬
	 * @param inPara1
	 * @return
	 */
	private String getSummaryMsg(Object inPara1){
		return "�w" + inPara1.toString() + "�x��I�����Ă��܂��B";
	}
}


    