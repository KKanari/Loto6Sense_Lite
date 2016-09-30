package com.apl.Loto6Sense_Lite;

import java.util.ArrayList;
import android.content.Context;

/**
 * 1�`43�̃{�[����ێ�����B
 * @author kms2
 *
 */
public class LotoBallList {

	private static int ballCnt = 43;
    private final ArrayList<LotoBall> ballList = new ArrayList<LotoBall>();

	public LotoBallList(Context inContext) {
		
        // ���l�Ɣz��ԍ������킹�邽�߂�	dummy
        ballList.add(new LotoBall(inContext,0));
        
        for( int i = 1; i <= ballCnt; i++){
        	LotoBall ballTmp = new LotoBall(inContext,i);
            ballList.add(ballTmp);
        }
	}
	
	public static int getBallMAX(){
		return ballCnt;
	}
	
	// Ball Class�擾
	public LotoBall getBall(int num) {
		LotoBall rtnBall = null;
		if( num <= ballCnt ){
			rtnBall = ballList.get(num);
		}
		return rtnBall;
	}	
	
}
