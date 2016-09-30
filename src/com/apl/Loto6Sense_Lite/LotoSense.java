package com.apl.Loto6Sense_Lite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.content.Context;

public class LotoSense {
	
	private Random gRand = null;
	private int senseType = 0;
	private static Integer SENSETYPE_OLDDAT = 0;
	private static Integer SENSETYPE_RANDOM = 1;
	
	/** �{�[���X�e�[�^�X */
	private Map<Integer,Integer> pBallStatus = new HashMap<Integer, Integer>();
	private static Integer DEF_STATUS_NORMAL  = 0;
	private static Integer DEF_STATUS_BUN5_01 = 1;
	private static Integer DEF_STATUS_BUN50_01 = 10;

	/** DB�f�[�^ */
	// ����5��f�[�^
	private Map<Integer, Integer> pGetDBDat5 = null;
	
	// ����50��f�[�^
	private Map<Integer, Integer> pGetDBDat50 = null;
	
	private ArrayList<LotoGrp>    pLotoGrpList = null;
	
	private String[] pHoldList = null;
	

	/** �R���e�L�X�g */
	public LotoSense(Context con, Random inRand, LotoSdLog inSdLog) {
		this.gRand = inRand;
	}

	/** DB�f�[�^�ݒ� */
	public void setGetDBDat5(Map<Integer, Integer> inGetDBDat){
		pGetDBDat5 = inGetDBDat;
	}

	/** DB�f�[�^�ݒ� */
	public void setGetDBDat50(Map<Integer, Integer> inGetDBDat){
		pGetDBDat50 = inGetDBDat;
	}

	/** 
	 * �{�[���X�e�[�^�X������ 
	 *
	 **/
	private void initStatus(){
		for( int i=0; i <= LotoBallList.getBallMAX(); i++){
			pBallStatus.put(i, DEF_STATUS_NORMAL);
		}
	}

	/** 
	 * �Œ萔��
	 *
	 **/
	public void setHoldList(String[] inHoldList){
		pHoldList = inHoldList;
	}
	
	/** ����5�񕪐͑��� */
	private void bunseki5(){
		if(!pGetDBDat5.isEmpty()){
			// ����5�񒆁A3��ȏ㓖�I�ԍ��ɂȂ��Ă���ԍ��͑ΏۊO�B
			for( int i = 1; i <= pGetDBDat5.size(); i++){
				Integer senceCnt = pGetDBDat5.get(i);
				if( senceCnt >= 3 ){
					pBallStatus.put(i, DEF_STATUS_BUN5_01);
				}
			}
		}
	}

	/** ����50�񕪐͑��� */
	public void bunseki50(){
		if(!pGetDBDat50.isEmpty()){
			Integer intTmp = 0;
			// ����50�񒆁A���I�ԍ���3��ȉ��̔ԍ���ێ�
			ArrayList<Integer> threeDownList = new ArrayList<Integer>();
			
			// 50�񕪂̓��I�ԍ��������ɁA1�`43�̔ԍ��̒����璊�I�ԍ��ΏۊO�̔ԍ������肷��B	
			for( int i = 1; i <= pGetDBDat50.size(); i++){
				
				Integer senceCnt = pGetDBDat50.get(i);
	
				// ����50�񒆁A11��ȏ㓖�I�ԍ��ɂȂ��Ă���ԍ��͑ΏۊO�B
				if( senceCnt >= 11){
					pBallStatus.put(i, DEF_STATUS_BUN50_01);
				}
	
				// ����50�񒆁A���I�ԍ���3��ȉ��̔ԍ��́A�P�̂݃����_���̑ΏۂƂ��đ��͑ΏۊO�B
				if( senceCnt <= 3 ){
					threeDownList.add(i);
					pBallStatus.put(i, DEF_STATUS_BUN50_01);
				}
			}
	
			// 3��ȉ��̔ԍ��́A�P�̂݃����_��
			Integer okNum = gRand.nextInt(threeDownList.size());
			intTmp =  threeDownList.get(okNum);
			pBallStatus.put(intTmp, DEF_STATUS_NORMAL);
		}
	}

	/** 
	 * ���I�ԍ��̗\�z���ʂ��A1�Z�b�g(�{�[��6��)�擾
	 * @param  Balls    �{�[�����X�g
	 * @return ArrayList<Ball>  ���o�����{�[���N���X���X�g
	 * */
	/** �\�z���o */
	public LotoBallSet getNumber(LotoBallList balls){

		LotoBallSet rtnBallSet = new LotoBallSet(); 
		// �\�z���ʕێ����X�g
		ArrayList<Integer> ballList = new ArrayList<Integer>();
		
		initStatus();
		
		if( senseType == SENSETYPE_OLDDAT){
			
			// ����50
			bunseki50();
	
			// ����5
			bunseki5();
			
			pLotoGrpList = new ArrayList<LotoGrp>();
			
			// �O���[�v�P�ʂ̃N���X�쐬
			for( int i = 0; i < LotoGrp.gGROUP.length; i++ ){
				LotoGrp lotoGrpTmp = new LotoGrp(i);
				pLotoGrpList.add(lotoGrpTmp);
			}
	
			// �O���[�v�P�ʂ̃N���X�ɗL���Ȑ�����ݒ�
			for( int i = 1; i < pBallStatus.size(); i++ ){
				if(pBallStatus.get(i) == DEF_STATUS_NORMAL){
					for( int ii = 0; ii < LotoGrp.gGROUP.length; ii++ ){
						LotoGrp lotoGrpTmp = pLotoGrpList.get(ii);
						if( lotoGrpTmp.searchBall(i) ){
							lotoGrpTmp.setBallEnable(i,true);
						}
					}
				}
			}
			
			// A�`G�O���[�v����A���I�ԍ���I������O���[�v��(4 or 5)�������_���Ō��肷��B
			Integer groupNum = 4 + gRand.nextInt(2);
			
			// �O���[�v�t�H�[�}�b�g�擾
			Integer[][] grpFormatList = LotoGrp.getGrpFormat(groupNum);
			Integer[] grpFormat = grpFormatList[gRand.nextInt(grpFormatList.length)];
			
			Boolean ErrFlg = false;
			
			ArrayList<Integer> grpTmp = new ArrayList<Integer>();

			Integer[] setFormat = new Integer[grpFormat.length];
			for( int i=0; i < grpFormat.length; i++){
				setFormat[i] = grpFormat[i];
			}
			
			// HOLD ����
			if( pHoldList != null){
				for( int i=0 ; i<pHoldList.length ; i++){
					String  strBall = pHoldList[i];
					Integer intBall = Integer.valueOf(strBall).intValue();
					ballList.add(intBall);
		
					for( int ii = 0; ii < pLotoGrpList.size(); ii++ ){
						LotoGrp lotoGrpTmp = pLotoGrpList.get(ii);
						if( lotoGrpTmp.searchBall(intBall) ){
							lotoGrpTmp.setBallSelect(intBall, true);
							
							if( grpTmp.indexOf(ii) < 0){
								grpTmp.add(ii);
							}
							
							break;
						}
					}
				}
				int nowSelCnt = pHoldList.length;
				if( grpTmp.size() >= 3 && nowSelCnt >= 3){
					for( int i=0; i < grpFormat.length; i++){
						setFormat[i] = grpFormat[grpFormat.length - 1 - i];
					}
				}
			}
			
			//setFormat.length
			int formatCnt = 0;
			while(ballList.size() < 6){

                // �O���[�v
                LotoGrp lotoGrpTmp = getOkGroup(pLotoGrpList,setFormat[formatCnt]);
    
                if( lotoGrpTmp == null){
                	// �ΏۃO���[�v�����݂��Ȃ��ꍇ
                    ErrFlg = true;
                }else{
                    // 1�O���[�v������̒��o�{�[��
                    for( int ballCnt=lotoGrpTmp.getSelectBallCnt(); ballCnt < setFormat[formatCnt]; ballCnt++){
                    	
                        if( ballList.size() < 6){
                            LotoBallBase lotoBallBaseTmp = getOneBall(lotoGrpTmp);
                            if( lotoBallBaseTmp != null){
                                ballList.add(lotoBallBaseTmp.getNumber());
                                // �O���[�v�̐����I���t���O���n�m
                                lotoGrpTmp.setBallSelect(lotoBallBaseTmp.getNumber(), true);
                                // �O���[�v�̗L�������t���O���n�e�e
                                lotoGrpTmp.setBallEnable(lotoBallBaseTmp.getNumber(), false);
                            }else{
                            	// �ΏۃO���[�v�����݂��Ȃ��ꍇ
                                ErrFlg = true;
                                break;
                            }
                        }else{                        	
                            break;
                        }
                        
                    }
                    formatCnt = formatCnt + 1;
	            }
                
                if( ErrFlg ){
                	break;
                }
			}
			            
            if( ErrFlg ){
                ArrayList<Integer> etcNumber = new ArrayList<Integer>();
                for( int i = 1; i < pBallStatus.size(); i++ ){
                    if( ballList.indexOf(i) == -1 ){
                        etcNumber.add(i);
                    }
                }
                
                for(int i = ballList.size() + 1; i <= 6; i++){
                    Integer idx = gRand.nextInt(etcNumber.size());
                    ballList.add(etcNumber.get(idx));
                    etcNumber.remove(etcNumber.indexOf(etcNumber.get(idx)));
                }
            }
				
		}else if(senseType == SENSETYPE_RANDOM){

			// HOLD ����
			if( pHoldList != null){
				for( int i=0 ; i<pHoldList.length ; i++){
					String  strBall = pHoldList[i];
					Integer intBall = Integer.valueOf(strBall).intValue();
					ballList.add(intBall);
				}
			}
			
			ArrayList<Integer> etcNumber = new ArrayList<Integer>();
			for( int i = 1; i < pBallStatus.size(); i++ ){
				if( ballList.indexOf(i) == -1 ){
					etcNumber.add(i);
				}
			}
			
			for(int i = ballList.size() + 1; i <= 6; i++){
				Integer idx = gRand.nextInt(etcNumber.size());
				ballList.add(etcNumber.get(idx));
				etcNumber.remove(etcNumber.indexOf(etcNumber.get(idx)));
			}
		}
		
		// sort
		ballList = funSort(ballList);
		
		// �o�^
		for( int i = 0; i < ballList.size(); i++ ){
			LotoBall tmpBall = balls.getBall(ballList.get(i));
			rtnBallSet.setBall(tmpBall);
		}
				
		return rtnBallSet;
	}

	/** �L���ȃO���[�v�ԍ�����P�̃O���[�v�ԍ��������_���Œ��o���� */
	private LotoGrp getOkGroup(ArrayList<LotoGrp> inLotoGrpList, Integer inBallOkCnt){
		
		LotoGrp lotoGrpRtn = null;
		
		ArrayList<LotoGrp> tmpGrpList = new ArrayList<LotoGrp>();

		// �L���ȃO���[�v�𒊏o����B
		for( int i = 0; i < inLotoGrpList.size(); i++ ){
			
			LotoGrp lotoGrpTmp = inLotoGrpList.get(i);
			
			if( lotoGrpTmp.getEnable()){
				if( lotoGrpTmp.getBallEnableCnt() >= inBallOkCnt){
					tmpGrpList.add(lotoGrpTmp);
				}
			}
		}

		if(tmpGrpList.size() > 0){
			
			// �O���[�v�I��			
			Integer intTmp = gRand.nextInt(tmpGrpList.size());
			lotoGrpRtn = tmpGrpList.get(intTmp);		
			lotoGrpRtn.setEnable(false);
		}
		
		return lotoGrpRtn;
	}
	
	/** 
	 * �{�[���ԍ��擾 
	 * @param  inGrpIdx �O���[�v�ԍ�
	 * @return Integer  ���o�����{�[���ԍ�
	 * */
	private LotoBallBase getOneBall(LotoGrp inLotoGrp){
		
		ArrayList<LotoBallBase> ballBaseListTmp = inLotoGrp.getGroupOkBalls();
		LotoBallBase ballBaseTmp = null;
		
		if( ballBaseListTmp.size() > 0 ){
			Integer intTmp = gRand.nextInt(ballBaseListTmp.size());
			ballBaseTmp = ballBaseListTmp.get(intTmp);
		}
		
		return ballBaseTmp;
	}

	/** �z��\�[�g */
	private ArrayList<Integer> funSort(ArrayList<Integer> inBallList){
		Collections.sort(inBallList, new Comparator<Integer>() {
			public int compare(Integer tc1, Integer tc2) {
				if (tc1 < tc2) {
					return -1;
				} else if (tc1 >= tc2) {
					return 1;
				}
				return 0;
			}
		});
		return inBallList;
	}
	
	public void setSenseType(int inSenseType){
		senseType = inSenseType;
	}
}
