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
 * �`��p�̃N���X
 */
class LotoBallView extends View {
    private boolean pViewEndFlg = false;
    private int DEF_LINE_MAX = 5;
    
    /** VIEW�̏�� */
    public static int DEF_MODE_INIT = 0;	// �������
    public static int DEF_MODE_BALLVIEW = 1;	// ���I�ԍ��\��
    private int pMode = DEF_MODE_INIT;
   
    /** �摜��x���W */
    int y;
    
    /** ���o�ԍ��\���\���C�� */
    private int pNowLine = 1;
    
    /** �{�[�� */
    private LotoBallList pBalls = null;
    
    private int pX_Start = 20;
    private int pY_Start = 15;
    private int pY_kankaku = 50;
    
    private boolean mAnimationFlg = true;
    
	// ���̃��C�����\�������^�C�~���O(Max255)
	private int lineDisp = 100;
    
    /** ���I�ԍ� */
    private ArrayList<LotoBallSet> pSenceBalls = new ArrayList<LotoBallSet>();
   
    /**
     * �R���X�g���N�^
     * @param c
     */
    public LotoBallView(Context c, LotoBallList inBalls) {
        super(c);

        /* �C�x���g���擾�ł���悤��Focus��L���ɂ��� */
        setFocusable(true);
       
        /* Resource�C���X�^���X�̐��� */
        Resources res = this.getContext().getResources();
       
        /* �摜�̐ݒ� */        
        pBalls = inBalls;
        for( int i=1; i <= LotoBallList.getBallMAX(); i++){
        	LotoBall ballTmp = pBalls.getBall(i);
        	ballTmp.setArgb(0);
        	ballTmp.setBitmap(BitmapFactory.decodeResource(res, ballTmp.getBallImgID()));
        }
        
        pViewEndFlg = false;
        
        /* x,y���W�̏����� */
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
     * �`�揈��
     */
    protected void onDraw(Canvas canvas) {
    	
    	int tmpMaxSize = 0;
	    drawBase(canvas);

	    if(pMode == DEF_MODE_BALLVIEW){
	        
	        // �{�[���`��
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
     * �`��{�[���ݒ� 
     */
    public void setSenceBalls(ArrayList<LotoBallSet> inSenceBalls,int inStartArgb){

    	pSenceBalls.clear();

    	// ���I�ԍ�(ball)�N���X���R�s�[����Ƌ��ɁA�e�ԍ��̕\������ݒ肷��B
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
     * �`��{�[��
     */
    public ArrayList<LotoBallSet> getSenceBalls(){
    	return pSenceBalls;
    }
    
    /** 
     * �`��{�[���ݒ� 
     */
    public void displayChgBall(){

    	int setY = 0;

    	// ���I�ԍ�(ball)�N���X���R�s�[����Ƌ��ɁA�e�ԍ��̕\������ݒ肷��B
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
     * ��{���C�A�E�g
     */
    private void drawBase(Canvas canvas){
        
    	// ���C�����J�n���W
    	int lineX_Left  = 5;
    	// ���C���E�I�����W
    	int lineX_Right = 10;
    	// ���C������������
    	int lineY_Ctr = 45;
    	
    	// �w�i�F�ݒ�
        canvas.drawColor(Color.BLACK);   
    	
        // Message �̕`��
        Paint textPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint2.setTextSize(16);
        textPaint2.setColor( Color.rgb(150, 150, 150));
        
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(16);
        textPaint.setColor( Color.WHITE);
        
        // Line�̕`��
        Paint linePaint2 = new Paint();
        linePaint2.setColor( Color.rgb(150, 150, 150));

        Paint linePaint = new Paint();
        linePaint.setColor( Color.WHITE);
        
        int now_y =0;

        // ��ʊ�{���C�A�E�g�ݒ�
        for(int i = 1; i <= DEF_LINE_MAX; i++){
	        now_y = y + (i-1) * pY_kankaku + lineY_Ctr ; 
	        
	        // ����̔ԍ�
	        canvas.drawText(getLeftTitle(i),0,now_y+1,textPaint);    
	        canvas.drawText(getLeftTitle(i),0,now_y+2,textPaint2);    
	        
	        // ����
	        canvas.drawLine(lineX_Left, now_y, getWidth() - lineX_Right, now_y, linePaint);
	        canvas.drawLine(lineX_Left, now_y+1, getWidth() - lineX_Right, now_y+1, linePaint2);
        }
    }
    
    /** �I���t���O */
    public boolean getViewEndFlg(){
    	return pViewEndFlg;
    }
    
    /**
     * �{�[��
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
     * ����@�ԍ��̐ݒ�
     */
    private String getLeftTitle(int line){
    	
    	String rtnStr = "";
    	rtnStr = String.valueOf(line);
    	return rtnStr;
    }

    /**
     * �v�Z�����p���\�b�h
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
     * �v�Z�����p���\�b�h
     */
    public void clear(){
        pSenceBalls = new ArrayList<LotoBallSet>();
    	this.pMode = DEF_MODE_INIT;
        this.invalidate();
    }

    /** ��ʃT�C�Y���ύX���ꂽ�Ƃ��ɌĂяo����郁�\�b�h */
    
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
    	displayChgBall();
    }
}
