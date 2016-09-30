package com.apl.Loto6Sense_Lite;

import java.util.ArrayList;

/**
 * 抽選用番号ボールを1セット(ボール6個)で管理する。
 * @author kms2
 *
 */
public class LotoBallSet {
	
	/** 抽選回 */
	private int pLoto_count = 0;

	/** 抽選番号 */
	private ArrayList<LotoBall> pBallList = null;

	/** 抽選番号 */
	private ArrayList<Integer> pNumberList = null;
	
	/**
	 * 
	 */
	public LotoBallSet(){
		pBallList = new ArrayList<LotoBall>();
		pNumberList = new ArrayList<Integer>();
	}

	/**
	 * 抽選回
	 */
	public void setLotCount(Integer inLoto_count){
		pLoto_count = inLoto_count;
	}
	public Integer getLotCount(){
		return pLoto_count;
	}

	/**
	 * 
	 */
	public void setBallNumber(Integer inNumber){
		pNumberList.add(inNumber);
	}
	
	/**
	 * 
	 */
	public ArrayList<Integer> getBallNumbers(){
		return pNumberList;
	}
	
	/**
	 * 
	 */
	public void setBall(LotoBall inBall){
		pBallList.add(inBall);
		pNumberList.add(inBall.getNumber());
	}

	/**
	 * 
	 */
	public LotoBall getBall(Integer inCnt){
		LotoBall rtnBall = null;
		if( pBallList.size() > inCnt ){
			rtnBall = pBallList.get(inCnt);
		}
		return rtnBall;
	}
	
	/**
	 * 
	 */
	public int getSize(){
		return pBallList.size();
	}

	/**
	 * 
	 */
	public boolean chkBall(int inBallNumber){
		
		boolean rtnVal = false;
		if( pNumberList.indexOf(inBallNumber) > -1 ){
			rtnVal = true;
		}
		
		return rtnVal;
	}

	/**
	 * 
	 */
	public void removeBall(int inBallNumber){
		pNumberList.remove(pNumberList.indexOf(inBallNumber));
		for( int i=0; i<pBallList.size(); i++){
			if( inBallNumber == pBallList.get(i).getNumber()){
				pBallList.remove(i);
				break;
			}
		}
	}
}
