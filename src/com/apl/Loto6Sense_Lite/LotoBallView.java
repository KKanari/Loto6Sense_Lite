package com.apl.Loto6Sense_Lite;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * 描画用のクラス
 */
class LotoBallView extends View {
    private boolean pViewEndFlg = false;
    private int DEF_LINE_MAX = 5;
    
    /** VIEWの状態 */
    public static int DEF_MODE_INIT = 0;	// 初期状態
    public static int DEF_MODE_BALLVIEW = 1;	// 抽選番号表示
    private int pMode = DEF_MODE_INIT;
   
    /** 画像のx座標 */
    int y;
    
    /** 抽出番号表示可能ライン */
    private int pNowLine = 1;
    
    /** ボール */
    private LotoBallList pBalls = null;
    
    private int pX_Start = 20;
    private int pY_Start = 15;
    private int pY_kankaku = 50;
    
    private boolean mAnimationFlg = true;
    
	// 次のラインが表示されるタイミング(Max255)
	private int lineDisp = 100;
    
    /** 抽選番号 */
    private ArrayList<LotoBallSet> pSenceBalls = new ArrayList<LotoBallSet>();
   
    /**
     * コンストラクタ
     * @param c
     */
    public LotoBallView(Context c, LotoBallList inBalls) {
        super(c);

        /* イベントが取得できるようにFocusを有効にする */
        setFocusable(true);
       
        /* Resourceインスタンスの生成 */
        Resources res = this.getContext().getResources();
       
        /* 画像の設定 */        
        pBalls = inBalls;
        for( int i=1; i <= LotoBallList.getBallMAX(); i++){
        	LotoBall ballTmp = pBalls.getBall(i);
        	ballTmp.setArgb(0);
        	ballTmp.setBitmap(BitmapFactory.decodeResource(res, ballTmp.getBallImgID()));
        }
        
        pViewEndFlg = false;
        
        /* x,y座標の初期化 */
        y = pY_Start;

        pMode = DEF_MODE_INIT;
        
    }
    
    public void setLineCnt(int inLineCnt){
    	DEF_LINE_MAX = inLineCnt;
    }

    public void setAnimationFlg(Boolean inFlg){
    	mAnimationFlg = inFlg;
    }
    
    /**
     * 描画処理
     */
    protected void onDraw(Canvas canvas) {
    	
    	int tmpMaxSize = 0;
	    drawBase(canvas);

	    if(pMode == DEF_MODE_BALLVIEW){
	        
	        // ボール描画
	        drawBall(canvas);
	        
	        tmpMaxSize = pSenceBalls.size();
	        
	        if( DEF_LINE_MAX < pSenceBalls.size()){
	        	tmpMaxSize = DEF_LINE_MAX;
	        }
	        
	        if( pNowLine <= tmpMaxSize ){
		        LotoBallSet balls = pSenceBalls.get(pNowLine-1);
		        if( balls.getBall(0).getArgb() >= lineDisp ){
		        	if( pNowLine < pSenceBalls.size() ){
		        		pNowLine++;
		        	}
		        }
	        }
	        
	        if( pNowLine >= tmpMaxSize ){
	        	LotoBallSet balls = pSenceBalls.get(tmpMaxSize-1);
		        if( balls.getBall(0).getArgb() >= 255 ){
		        	pViewEndFlg = true;
		        }
	        }
    	}
    	
    }

    /** 
     * 描画ボール設定 
     */
    public void setSenceBalls(ArrayList<LotoBallSet> inSenceBalls,int inStartArgb){

    	pSenceBalls.clear();

    	// 抽選番号(ball)クラスをコピーすると共に、各番号の表示情報を設定する。
        for(int line = 0; line < inSenceBalls.size(); line++){        		
        	LotoBallSet oneBallList = inSenceBalls.get(line);
        	LotoBallSet newBallList = new LotoBallSet();        	
	        for(int ballcnt = 0; ballcnt < oneBallList.getSize(); ballcnt++){	        	
	        	LotoBall ball = (LotoBall)oneBallList.getBall(ballcnt).copy();
		       	ball.setArgb(inStartArgb);
		       	newBallList.setBall(ball);		       	
	        }
	        pSenceBalls.add(newBallList);
        }
        displayChgBall();
    }

    /** 
     * 描画ボール
     */
    public ArrayList<LotoBallSet> getSenceBalls(){
    	return pSenceBalls;
    }
    
    /** 
     * 描画ボール設定 
     */
    public void displayChgBall(){

    	int setY = 0;

    	// 抽選番号(ball)クラスをコピーすると共に、各番号の表示情報を設定する。
        for(int line = 0; line < pSenceBalls.size(); line++){        		
        	LotoBallSet oneBallList = pSenceBalls.get(line);
        	LotoBallSet newBallList = new LotoBallSet();
		        
        	setY = y + line * pY_kankaku;
        	
        	int setX = pX_Start;
        	
	        for(int ballcnt = 0; ballcnt < oneBallList.getSize(); ballcnt++){	      	
	        	LotoBall ball = (LotoBall)oneBallList.getBall(ballcnt);
	        	
		       	ball.setX(setX);
		       	ball.setY(setY);
		       	newBallList.setBall(ball);

	            int kankakuX = (getWidth() - pX_Start - ball.getImageWidth() * 6 ) / 6 ; 
	            setX = setX + ball.getImageWidth() + kankakuX;
		       	
	        }
        }
    }
    
    /**
     * 基本レイアウト
     */
    private void drawBase(Canvas canvas){
        
    	// ライン左開始座標
    	int lineX_Left  = 5;
    	// ライン右終了座標
    	int lineX_Right = 10;
    	// ラインを下線調整
    	int lineY_Ctr = 45;
    	
    	// 背景色設定
        canvas.drawColor(Color.BLACK);   
    	
        // Message の描画
        Paint textPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint2.setTextSize(16);
        textPaint2.setColor( Color.rgb(150, 150, 150));
        
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(16);
        textPaint.setColor( Color.WHITE);
        
        // Lineの描画
        Paint linePaint2 = new Paint();
        linePaint2.setColor( Color.rgb(150, 150, 150));

        Paint linePaint = new Paint();
        linePaint.setColor( Color.WHITE);
        
        int now_y =0;

        // 画面基本レイアウト設定
        for(int i = 1; i <= DEF_LINE_MAX; i++){
	        now_y = y + (i-1) * pY_kankaku + lineY_Ctr ; 
	        
	        // 左列の番号
	        canvas.drawText(getLeftTitle(i),0,now_y+1,textPaint);    
	        canvas.drawText(getLeftTitle(i),0,now_y+2,textPaint2);    
	        
	        // 下線
	        canvas.drawLine(lineX_Left, now_y, getWidth() - lineX_Right, now_y, linePaint);
	        canvas.drawLine(lineX_Left, now_y+1, getWidth() - lineX_Right, now_y+1, linePaint2);
        }
    }
    
    /** 終了フラグ */
    public boolean getViewEndFlg(){
    	return pViewEndFlg;
    }
    
    /**
     * ボール
     */
    private void drawBall(Canvas canvas){
    	
    	int wkNowLine = pNowLine;
    	if( wkNowLine > this.pSenceBalls.size()){
    		wkNowLine = this.pSenceBalls.size();
    	}
    	
        for(int i = 1; i <= wkNowLine; i++){
        	LotoBallSet oneBallSet = this.pSenceBalls.get(i-1);
	       	for(int ballcnt = 0; ballcnt < oneBallSet.getSize(); ballcnt++){
	       		LotoBall ball = oneBallSet.getBall(ballcnt);
	       		
	       		int wkArgb = ball.getArgb() + 10;
    	       	ball.setArgb(wkArgb);
	            if( wkArgb > 255 ){
	            	ball.setArgb(255);
	            }
	       		
	            ball.onDraw(canvas);
	       	}
        }
    }

    /**
     * 左列　番号の設定
     */
    private String getLeftTitle(int line){
    	
    	String rtnStr = "";
    	rtnStr = String.valueOf(line);
    	return rtnStr;
    }

    /**
     * 計算処理用メソッド
     */
    public void start(){
    	
    	if( mAnimationFlg ){
    		pNowLine = 1;
    	}else{
    		pNowLine = pSenceBalls.size();
    	}
		pViewEndFlg = false;
    	this.pMode = DEF_MODE_BALLVIEW;
    }
    
    /**
     * 計算処理用メソッド
     */
    public void clear(){
        pSenceBalls = new ArrayList<LotoBallSet>();
    	this.pMode = DEF_MODE_INIT;
        this.invalidate();
    }

    /** 画面サイズが変更されたときに呼び出されるメソッド */
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
    	displayChgBall();
    }
}
