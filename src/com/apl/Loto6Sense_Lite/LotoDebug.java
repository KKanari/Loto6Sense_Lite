package com.apl.Loto6Sense_Lite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LotoDebug {

	private LotoSdLog pSdLog = null;
	
	/**
	 * 
	 * @param inSdLog
	 */
	public LotoDebug(LotoSdLog inSdLog){
		pSdLog = inSdLog;
	}

	/**
	 * 
	 * @param inSdLog
	 */
	public void debug_LotoBallSet(LotoBallSet inBallSet){

		StringBuffer sbMain = new StringBuffer("");
		StringBuffer sbBallList = new StringBuffer("");
		
		// ���I��
		sbMain.append("  dai:"+inBallSet.getLotCount());
		
		// ���I�ԍ�
		for( int i=0; i<inBallSet.getSize(); i++ ){
			LotoBall ball = inBallSet.getBall(i);
			if( sbBallList.length() > 0 ){
				sbBallList.append(",");
			}			
			sbBallList.append(ball.getNumber());
		}
		
		pSdLog.put(sbMain.toString() + "[" + sbBallList.toString() + "]");
	}

	/**
	 * 
	 * @param inSdLog
	 */
	public Map<Integer, Integer> debug_ChekLoto(LotoBallSet inBallSet, ArrayList<LotoBallSet> inDbAll){
		
		Map<Integer, Integer> mapToukei = new HashMap<Integer, Integer>();
		mapToukei.put(0, 0);
		mapToukei.put(1, 0);
		mapToukei.put(2, 0);
		mapToukei.put(3, 0);
		mapToukei.put(4, 0);
		mapToukei.put(5, 0);
		mapToukei.put(6, 0);

		ArrayList<LotoBallSet> hitSetList = loto_Result(inBallSet,inDbAll);

		for( int hitCnt=0; hitCnt < hitSetList.size(); hitCnt++){
				
			LotoBallSet hitOne = hitSetList.get(hitCnt);
				
			StringBuffer sbMsg = new StringBuffer("");
			StringBuffer sbBallMsg = new StringBuffer("");

			switch(hitOne.getSize()){
				case 1:
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
				case 5:
				case 6:
					sbMsg.append("    dai:"+hitOne.getLotCount()+"  Hit:" + hitOne.getSize());
	       			int temp = mapToukei.get(hitOne.getSize());
	       			mapToukei.put(hitOne.getSize(), temp+1) ;
					for( int ballCnt = 0; ballCnt < hitOne.getSize(); ballCnt++){
						if( sbBallMsg.length() > 0){
							sbBallMsg.append(",");
						}
						sbBallMsg.append(hitOne.getBall(ballCnt).getNumber());
					}						
					pSdLog.put(sbMsg.toString() + "[" + sbBallMsg.toString() + "]");
					break;
			}
		}
		
		return mapToukei;
	}
	
    /**
     * �ߋ��̒��I���ʂƏƍ�����
     */
    private ArrayList<LotoBallSet> loto_Result(LotoBallSet inBallSet,
    									       ArrayList<LotoBallSet> inAllHisDat){
    	ArrayList<LotoBallSet> oneSetList = new ArrayList<LotoBallSet>();
    	
		for( int line=0; line < inAllHisDat.size(); line++){
			LotoBallSet oneSet = inAllHisDat.get(line);

			LotoBallSet newSet = new LotoBallSet();
			newSet.setLotCount(oneSet.getLotCount());
			for (int i = 0; i < inBallSet.getSize(); i++) {
				LotoBall ball = inBallSet.getBall(i);
				if( oneSet.getBallNumbers().indexOf(ball.getNumber()) > 0 ){
					newSet.setBall(ball);
				}
			}
			
			if(newSet.getSize() > 3){
				oneSetList.add(newSet);
			}
		}
		return oneSetList;
    }    
    
    /** 
     * �l�\���FMap<Integer, Integer>
     * @param inTitle
     * @param inGetDBDat
     */
    public void debug_Map(String inTitle,Map<Integer, Integer> inGetDBDat){
    	//Map�I�u�W�F�N�g�́u�L�[�v�̈ꗗ���擾
    	Set set = inGetDBDat.keySet();

    	//�C�e���[�^�擾
    	Iterator iterator = set.iterator();

    	String logTitle = "---" + inTitle + " (Size:"+inGetDBDat.size()+")---";
		pSdLog.put(logTitle);
    	
    	//�I�u�W�F�N�g���̃f�[�^��S�Ď擾
    	while(iterator.hasNext()){
    		Object  key = iterator.next();
    		Integer val = inGetDBDat.get(key);
    		String msg = "    cnt=" + key + " val=" + String.valueOf(val);
    		pSdLog.put(msg);
    	}
    }

    /******************************************************************
     * DEBUG
     ******************************************************************/

    /*
    // DEBUG
    class ClickListenerDebug implements OnClickListener {
		@Override
		public void onClick(View v) {

			LotoCtrDB tmpCtrDB = new LotoCtrDB(getApplication());
			ArrayList<LotoBallSet> tmpBallSetList =  tmpCtrDB.getDatAll();

			Map<Integer, Integer> mapToukei = null;
			Map<Integer, Integer> mapToukeiALL = new HashMap<Integer, Integer>();
			mapToukeiALL.put(0, 0);
			mapToukeiALL.put(1, 0);
			mapToukeiALL.put(2, 0);
			mapToukeiALL.put(3, 0);
			mapToukeiALL.put(4, 0);
			mapToukeiALL.put(5, 0);
			mapToukeiALL.put(6, 0);
			LotoBallView ballview = (LotoBallView)findViewById(ID_VIEW_BALL);
	        
	        // ���I�ԍ����o
	        ArrayList<LotoBallSet> viewBallList = new ArrayList<LotoBallSet>();
	        int pViewSetCnt = 1000;
	        for( int i=1; i <= pViewSetCnt; i++){
	        	
		        // ���I�ԍ��\�z�����J�n
	        	LotoBallSet aryTmp = mSense.getNumber(mBalls);     
	    		
	    		String titleMsg ="[[" + i + "]]" + aryTmp.getBallNumbers() ;
	    		mSdLog.put(titleMsg);
	    		
        		viewBallList.add(aryTmp);
        		//mDebug.debug_LotoBallSet(aryTmp);
        		mapToukei = mDebug.debug_ChekLoto(aryTmp, tmpBallSetList);
        		
        		for( int ii=0; ii < 7; ii++){
        			mapToukeiALL.put(ii, mapToukeiALL.get(ii) + mapToukei.get(ii));
        		}
	        }

	        StringBuffer kekkaMsg = new StringBuffer("---Total : ");
    		for( int i=0; i < 7; i++){
    			kekkaMsg.append("  [["+i+"]]"+mapToukeiALL.get(i));
    		}

    		mSdLog.put(kekkaMsg.toString());
	        	        
	        // ���o���ʕ\���J�n
	        ballview.setSenceBalls(viewBallList);	        
	        ballview.start();
	        mHandler = new RedrawHandler(ballview, Integer.valueOf(getString(R.string.timer)).intValue());
	        mHandler.start();
	        
	        // Start�{�^����\��
	        v.setVisibility(View.GONE);
	        
	        // Clear�{�^����\��
	        ImageButton imgB = (ImageButton)findViewById(ID_BTN_CLEAR);
	        imgB.setVisibility(View.INVISIBLE);
	        	        
		}
    };

    // DEBUG
    class ClickListenerDebug2 implements OnClickListener {
		@Override
		public void onClick(View v) {

			LotoBallView ballview = (LotoBallView)findViewById(ID_VIEW_BALL);
	        
	        // ���I�ԍ����o
	        ArrayList<LotoBallSet> viewBallList = new ArrayList<LotoBallSet>();
	       
	        LotoCtrDB tmpCtrDB = new LotoCtrDB(getApplication());
	        
	        int pViewSetCnt= 10000;
	        for( int i=0; i < pViewSetCnt; i++){
	        	
	        	if( i % 100 == 0 ){
		    		String titleMsg ="[[" + i + "]]";
	        	}
		        
		        // ���I�ԍ��\�z�����J�n
	        	LotoBallSet aryTmp = mSense.getNumber(mBalls);     
		        
		        // DEBUG SQL�o�[�W���� 1�ʂ̂݌��� 
	        	Integer rtn = tmpCtrDB.getBigPoint(
	        			           aryTmp.getBall(0).getNumber(), 
	        			           aryTmp.getBall(1).getNumber(), 
	        			           aryTmp.getBall(2).getNumber(),
	        			           aryTmp.getBall(3).getNumber(), 
	        			           aryTmp.getBall(4).getNumber(), 
	        			           aryTmp.getBall(5).getNumber());
	        	
	        	if( rtn > 0){
	        		StringBuffer sbMsg = new StringBuffer("");
	        		StringBuffer sbBallMsg = new StringBuffer("");
					sbMsg.append("    dai:"+aryTmp.getLotCount()+"  Hit:" + aryTmp.getSize());
					for( int ballCnt = 0; ballCnt < aryTmp.getSize(); ballCnt++){
						if( sbBallMsg.length() > 0){
							sbBallMsg.append(",");
						}
						sbBallMsg.append(aryTmp.getBall(ballCnt).getNumber());
					}						
					mSdLog.put(sbMsg.toString() + "[" + sbBallMsg.toString() + "]");
	        	}
	        	
	        	// �\�z�ԍ���\���p���X�g�ɐݒ�
	        	if( rtn > 0){
	        		viewBallList.add(aryTmp);
	        		if(viewBallList.size()==5){
	        			break;
	        		}
	        	}
	        }
	       
	        if(viewBallList.size()>0){
		        // ���o���ʕ\���J�n
		        ballview.setSenceBalls(viewBallList);	        
		        ballview.start();
		        mHandler = new RedrawHandler(ballview, 100);
		        mHandler.start();
		        
		        // Start�{�^����\��
		        v.setVisibility(View.GONE);
		        
		        // Clear�{�^����\��
		        ImageButton imgB = (ImageButton)findViewById(ID_BTN_CLEAR);
		        imgB.setVisibility(View.INVISIBLE);
	        }
		}
    };
    */
    
}
