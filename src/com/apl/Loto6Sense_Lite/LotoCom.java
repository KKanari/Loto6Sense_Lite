package com.apl.Loto6Sense_Lite;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LotoCom {
	/**
	 * �J�n��������I�������܂ł̌o�ߓ������擾���܂��B
	 * @param start �J�n����
	 * @param end �I������
	 * @return �J�n��������I�������܂ł̌o�ߓ���
	 */
	public static int getDays(Date start, Date end) {
		
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(start);
		long lStart = (calStart.getTime()).getTime();
 
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(end);
		long lEnd = (calEnd.getTime()).getTime();
 
		long result = (lEnd - lStart) / (1000*60*60*24);
		return new Long(result).intValue();
	}

	/**
	 * 
	 * @param inDate
	 * @return
	 */
	public static String strFromDate(Date inDate) {
        SimpleDateFormat sdfTmp = new SimpleDateFormat("yyyy/MM/dd");  
        return sdfTmp.format(inDate);
	}
	
	/**
	 * 
	 * @param inDate
	 * @return
	 */
	public static Date dateFromStr(String inDat) {
		Date rtnDat = null;
        try {
            SimpleDateFormat sdfTmp = new SimpleDateFormat("yyyy/MM/dd");  
            rtnDat = sdfTmp.parse(inDat);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}        
		return rtnDat;
	}
	
    /**
     * ���l�`�F�b�N
     * double �ɕϊ��ł����Ȃ������񂪓n���ꂽ�ꍇ�� false ��Ԃ��܂��B
     * @param str �`�F�b�N������
     * @return �����̕����񂪐��l�ł���ꍇ true ��Ԃ��B
     */
    public static boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    /** 
     * �N�����擾 
     */
    public static String getYYYYMM(){

	    Calendar cal1 = Calendar.getInstance();    // �I�u�W�F�N�g�̐���
	    int year = cal1.get(Calendar.YEAR);        // ���݂̔N���擾
	    int month = cal1.get(Calendar.MONTH) + 1;  // ���݂̌����擾
	    int day = cal1.get(Calendar.DATE);         // ���݂̓����擾
	    
	    StringBuffer dow = new StringBuffer();
	    dow.append(String.valueOf(year) + "/" + String.valueOf(month)+ "/" + String.valueOf(day));
	    
	    dow.append("(");
	    switch (cal1.get(Calendar.DAY_OF_WEEK)) {  // ���݂̗j�����擾
	      case Calendar.SUNDAY:    
	    	  dow.append("Sun"); 
	    	  break;
	      case Calendar.MONDAY:    
	    	  dow.append("Mon"); 
	    	  break;
	      case Calendar.TUESDAY:   
	    	  dow.append("Tue"); 
	    	  break;
	      case Calendar.WEDNESDAY: 
	    	  dow.append("Wed"); 
	    	  break;
	      case Calendar.THURSDAY:  
	    	  dow.append("Thu"); 
	    	  break;
	      case Calendar.FRIDAY:    
	    	  dow.append("Fri"); 
	    	  break;
	      case Calendar.SATURDAY:  
	    	  dow.append("Sat"); 
	    	  break;
	    }
	    dow.append(")");

	    return dow.toString();
    }
}
