package com.apl.Loto6Sense_Lite;

import com.apl.Loto6Sense_Lite.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LotoOptHold extends Activity {
	
	private final int ID_BALLVIEW = 1;
	private final int ID_SELECTBALLVIEW = 2;
	private final int ID_SCROLL = 3;
	private LotoProperty mProperty = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // �v���p�e�B�t�@�C���ǂݍ���
        mProperty = new LotoProperty(LotoOptHold.this);
        
        // HOLD�����̔��f
        String[] holdBall = mProperty.getHoldList();
        LotoBallSet tmpBallSet = new LotoBallSet();
        if( holdBall != null){
	        for( int i=0; i < holdBall.length; i++){
	        	if( holdBall.length > 0 ){
		        	LotoBall tmpBall = new LotoBall(getApplication(), Integer.valueOf(holdBall[i]).intValue());
		        	tmpBallSet.setBall(tmpBall);
	        	}
	        }
        }
        
        // ���C�����C�A�E�g
        orgLinearLayout layoutMain = new orgLinearLayout(this);
        layoutMain.setOrientation(LinearLayout.VERTICAL);
        layoutMain.setBackgroundColor(Color.BLACK);
        layoutMain.setGravity(Gravity.CENTER | Gravity.TOP);
        layoutMain.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, LotoConst.FP));
        layoutMain.setFocusable(true);
        setContentView(layoutMain);
                
        // ���x��
        TextView tv = new TextView(getApplication());
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setText(getString(R.string.lbl_hold_info));
        tv.setTextSize(16);
        layoutMain.addView(tv);
        
        // �I���{�[���\��VIEW
        selectNowView selBallView = new selectNowView( getApplication(),tmpBallSet );
        selBallView.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, 100));
        selBallView.setId(ID_SELECTBALLVIEW);
        layoutMain.addView(selBallView);
        
        // �X�N���[��VIEW
        ScrollView scrollv = new ScrollView(getApplication());
        scrollv.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, 230));
        scrollv.setId(ID_SCROLL);
        layoutMain.addView(scrollv);
        layoutMain.setScr(scrollv);

        // �{�[���\�����C��VIEW
        LinearLayout layoutBall = new LinearLayout(this);
        layoutBall.setOrientation(LinearLayout.VERTICAL);
        layoutBall.setGravity(Gravity.CENTER | Gravity.TOP);
        layoutBall.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, LotoConst.FP));
        scrollv.addView(layoutBall);
        
        // �{�[���`��VIEW
        selectListView mView = new selectListView( getApplication(),tmpBallSet,selBallView);
        mView.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, 2450));
        mView.setId(ID_BALLVIEW);
        layoutBall.addView(mView);
        selBallView.setBallListView(mView);

        // ���C�����C�A�E�g
        LinearLayout layoutBottom = new LinearLayout(this);
        layoutBottom.setOrientation(LinearLayout.VERTICAL);
        layoutBottom.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        layoutBottom.setLayoutParams(
        		new LinearLayout.LayoutParams(LotoConst.FP, LotoConst.FP));
        layoutMain.addView(layoutBottom);
    }    

    @Override
    public void onStart() {
        super.onStart();
        selectNowView selView = (selectNowView)findViewById(ID_SELECTBALLVIEW);
		selView.invalidate();
    }
}

/**
 * ��ʃT�C�Y�ύX���ɐ������X�g�̃T�C�Y��ύX����ׂ̃��C�A�E�g
 * @author kms2
 */

class orgLinearLayout extends LinearLayout {
	private ScrollView pScr = null;
	
	/**
	 * 
	 * @param context
	 */
	public orgLinearLayout(Context context) {
		super(context);
	}
	
	/**
	 * �T�C�Y�ύX���ɉe���̂���X�N���[���o�[
	 * @param inScr
	 */
	public void setScr(ScrollView inScr){
		pScr = inScr;
	}

    /** ��ʃT�C�Y���ύX���ꂽ�Ƃ��ɌĂяo����郁�\�b�h */    
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
    }
    /**
     * �`�揈��
     */
   	@Override
    protected void onDraw(Canvas canvas) {
    	pScr.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, getHeight() - 150));
    }
}

/**
 * �`��p�̃N���X
 */
class selectListView extends View {

    /** VIEW�̏�� */
    private selectNowView mLinkView = null;
    
    /** �{�[�� */
    private LotoBallList mBalls = null;
    
    /** ���I�ԍ� */
    private	LotoBallSet mSelBallSet = new LotoBallSet();
    
    private Context mContext = null;
   
    /**
     * �R���X�g���N�^
     * @param c
     */
    public selectListView(Context c, LotoBallSet inBallSet, selectNowView inSelectBallView) {
        super(c);
        
        mContext = c;
        
        /* �摜�̐ݒ� */        
        mBalls = new LotoBallList(c);
        mLinkView = inSelectBallView;
        mSelBallSet = inBallSet;
        
        for( int i=1; i <= LotoBallList.getBallMAX(); i++){
        	LotoBall ballTmp = mBalls.getBall(i);  	        	
        	// �����x
        	if(mSelBallSet.getBallNumbers().indexOf(ballTmp.getNumber())>-1){
            	ballTmp.setArgb(LotoBall.TOUCH_ARGB_ON);
        	}else{
        		ballTmp.setArgb(LotoBall.TOUCH_ARGB_OFF);
        	}
        }
    }

    /**
     * �`�揈��
     */
    public void displayChgBall() {
        int pointX = 10;
        int pointY = 10;
        
        for( int i=1; i <= LotoBallList.getBallMAX(); i++){
        	
        	LotoBall ballTmp = mBalls.getBall(i);  	
        	
        	// X
        	pointX = (getWidth() - ballTmp.getImageWidth()) / 2;
        	ballTmp.setX(pointX);
        	ballTmp.setY(pointY);
        	
        	// Y
    		pointY = pointY + ballTmp.getImageHeight() + 15;
        }       
    }

    /**
     * �`�揈��
     */
    protected void onDraw(Canvas canvas) {
	    for(int ballcnt = 0; ballcnt < LotoBallList.getBallMAX(); ballcnt++){
	    	LotoBall ball = mBalls.getBall(ballcnt+1);
	        ball.onDraw(canvas);
	    }    	
    }

    /**
     * �����𖢑I�����ꂽ��Ԃɂ���
     */
    public void ballOFF(int inNumber, LotoBallSet inSelBallSet){
    	LotoBall tmpBall = mBalls.getBall(inNumber);
    	tmpBall.setArgb(LotoBall.TOUCH_ARGB_OFF);
		mSelBallSet.removeBall(tmpBall.getNumber());
    	inSelBallSet = mSelBallSet;
    	invalidate();
    	updateFile();
    }

    /**
     * ������I�����ꂽ��Ԃɂ���
     */
    public void ballON(int inNumber){
   		if( mSelBallSet.getSize() < 6){
	    	LotoBall tmpSelect = mBalls.getBall(inNumber);
	    	tmpSelect.setArgb(LotoBall.TOUCH_ARGB_ON);
	  	    LotoBall newBall = (LotoBall)mBalls.getBall(inNumber).copy();
	  	    mSelBallSet.setBall(newBall);
	    	updateFile();
   		}else{
			mBalls.getBall(inNumber).setArgb(LotoBall.TOUCH_ARGB_OFF);
   		}
    }
    
    /**
     * 
     */
    private void updateFile(){

  	    StringBuffer holdList = new StringBuffer("");
  	    for( int i=0; i< mSelBallSet.getSize(); i++){
  	    	if( holdList.length() > 0 ){
  	    		holdList.append(",");
			}
			holdList.append(mSelBallSet.getBall(i).getNumber());
  	    }
		
        SharedPreferences pref2 = PreferenceManager.getDefaultSharedPreferences(mContext); 
        SharedPreferences.Editor editor2=pref2.edit();
        editor2.putString("hold_ball",holdList.toString());
        editor2.commit();
    }

    /**
     * 
     */
   	@Override
   	public boolean onTouchEvent(MotionEvent event){
   		switch(event.getAction()){
   		
	   		case MotionEvent.ACTION_DOWN:
	   			for( int i = 1; i <= LotoBallList.getBallMAX(); ++i ) {
					if( mBalls.getBall(i).onTouchEvent(event) ){		
						LotoBall tmpSelect = (LotoBall)mBalls.getBall(i);
						
						if( mSelBallSet.chkBall(tmpSelect.getNumber())){
							ballOFF(tmpSelect.getNumber(), mSelBallSet);
						}else{
							ballON(mBalls.getBall(i).getNumber());
						}
			   	   		mLinkView.setBallSet(mSelBallSet);
			   	   		mLinkView.invalidate();
			   			invalidate();
					}
	   			}
			break;
   		case MotionEvent.ACTION_UP:
   			break;
   		case MotionEvent.ACTION_MOVE:
   			break;
   		case MotionEvent.ACTION_OUTSIDE:
   			break;
   		}
		return true;
   	}

    /** ��ʃT�C�Y���ύX���ꂽ�Ƃ��ɌĂяo����郁�\�b�h */    
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
    	displayChgBall();
    }
}

/**
 * �㕔�̑I���{�[���`��
 * @author kms2
 */
class selectNowView extends View {

    /** �\������{�[�����X�g */
    private LotoBallSet mBallSet = null;
    
    /** �֘AVIEW*/
    private selectListView mLinkView = null;
    
    /** Base Ball */
    private LotoBall mBaseBall = null;
    
    /** Max */
    private int mSelectBallMax = 6;
   
    /**
     * �R���X�g���N�^
     * @param c
     */
    public selectNowView(Context c, LotoBallSet inBallSet) {
        super(c);
        mBallSet = inBallSet;
        
        // �x�[�X�擾
        mBaseBall = new LotoBall(c, 1);
    }
    
    /**
     * �{�[���I��pVIEW��ݒ肷��B
     * @param inBallListView
     */
    public void setBallListView(selectListView inBallListView){
    	mLinkView = inBallListView;
    }

    /**
     * �`��p�̃��X�g���擾����B
     * @return
     */
    public LotoBallSet getBallSet(){
    	return mBallSet;
    }
    
    /**
     * �{�[���ݒ�
     * @param inBallSet
     */
    public void setBallSet(LotoBallSet inBallSet){
    	mBallSet = inBallSet;
    }
    
    /**
     * �`�揈��
     */
    protected void onDraw(Canvas canvas) {

    	// �����`��p�̃��C���y�C���g
        Paint linePaintMain = new Paint();
        linePaintMain.setColor( Color.rgb(150, 150, 150));

    	// �����`��p�̃T�u�y�C���g
        Paint linePaintSub = new Paint();
        linePaintSub.setColor(Color.rgb(100, 100, 100));
        
		// ���Ԋu
    	int kankakuX = (getWidth() - mBaseBall.getImageWidth() * mSelectBallMax) / 10;
        
        // ����
    	int wkY_Base = 20;
    	int wkX_Base = kankakuX * 2;
    	int wkX_S = wkX_Base;
    	int wkX_E = wkX_S+mBaseBall.getImageWidth();
    	int wkY   = wkY_Base+mBaseBall.getImageHeight() + 2;
    	
    	// �����\���ӏ��̃A���_�[���C��
    	for(int i=0 ; i < mSelectBallMax ; i++){
            canvas.drawLine(wkX_S + 10, wkY, wkX_E - 10, wkY, linePaintMain);
            canvas.drawLine(wkX_S + 10, wkY+1, wkX_E - 10, wkY+1, linePaintSub);
            wkX_S = wkX_E + kankakuX;
            wkX_E = wkX_S+mBaseBall.getImageWidth();
    	}
        
    	// �����\��
    	if( mBallSet != null){
        	if( mBallSet.getSize() > 0){
        		
		    	// �J�n�ʒu
		    	int setX = wkX_Base;
		    	
		    	// �{�[���`��
		    	for(int i = 0 ; i< mBallSet.getSize(); i++){
		    		LotoBall ball = mBallSet.getBall(i);
		    		ball.setX(setX);
		    		ball.setY(wkY_Base);
		    		ball.onDraw(canvas);
		    		setX = setX + ball.getImageWidth() + kankakuX;
		    	}
        	}
    	}
    }

    /**
     * 
     */
   	@Override
   	public boolean onTouchEvent(MotionEvent event){
   		
   		switch(event.getAction()){
   			case MotionEvent.ACTION_DOWN:
   				if( mBallSet != null){
		   			for( int i = 0; i < mBallSet.getSize(); ++i ) {
		   				if( mBallSet.getBall(i).onTouchEvent(event) ){
		   					
		   					// �I�����ꂽ�����ɑ΂��鏈��
		   					int delBallNumber = mBallSet.getBall(i).getNumber();
		   					
		   					// OFF
		   					mLinkView.ballOFF(delBallNumber,mBallSet);
		   					break;
		   				}
		   			}
		   			invalidate();
   				}
				break;
	   		case MotionEvent.ACTION_UP:
				break;
	   		case MotionEvent.ACTION_MOVE:
	   			break;
   		}
		return true;
   	}
}

