package com.apl.Loto6Sense_Lite;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.http.protocol.UriPatternMatcher;

import com.apl.Loto6Sense_Lite.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDiskIOException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Loto6Sense extends Activity {
	
	/** view ID */
	private final int ID_LAYOUT_MAIN = 1;	//Main layout	
	private final int ID_BTN_START   = 2;	// Start button	
	private final int ID_BTN_CLEAR   = 3;	// Clear button
	private final int ID_VIEW_BALL   = 4;	// View
	
	/** ���j���[ */
    private final int MENU_ID_HOLD    = Menu.FIRST;   
    private final int MENU_ID_SETTING = Menu.FIRST + 1;
    
    /** �x�����b�Z�[�W */
    private StringBuffer mWarningMsg = new StringBuffer("");
    
    /** �G���[���b�Z�[�W */
    private StringBuffer mErrorMsg   = new StringBuffer("");

	/** Start�{�^���摜 */
	private static Drawable mNormalBack; 	//�ʏ펞
	private static Drawable mPushBack; 		//������

	/** �^�C�}�[�p */
	private RedrawHandler mHandler = null;

    /** ���ʏ�� */
    private LotoBallList mBalls = null;
    
    /** resources */
	private Resources mResources = null;
	
	/** �v���p�e�B�t�@�C�� */
	private LotoProperty mProperty = null;
	
	/** �\�z�v�Z���� */
	private LotoSense mSense = null;
    
    /** Log�o�͏��� */
	private LotoSdLog mSdLog = null;    
    
    /** Debug���� */
	private LotoDebug mDebug = null;
    
    /** DB */
	private LotoCtrDB mCtrDB = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);

	    try{
	        // ---------------------
	        // ��������
	        // ---------------------
	        
	        // SD�ւ̃��O�o�͐ݒ�
	        //    debug = ���O�o��  etc = ���O�o�͂��Ȃ�
	        if( getString(R.string.app_mode).equals(LotoConst.APP_MODE_DEBUG)){
	        	mSdLog = new LotoSdLog(true);
	        }else{
	        	mSdLog = new LotoSdLog(false);
	        }
	        
	        // Debug�p
	        mDebug = new LotoDebug(mSdLog);
	        
	        // ---------------------
	        // �A�v���^�C�g���̐ݒ�
	        // ---------------------
	        
	     	// �p�b�P�[�W�̒l�Q��
	     	PackageManager pm = getPackageManager();
	     	PackageInfo info = null;
	     	
	     	// �p�b�P�[�W���ƎQ�Ƃ����������̎w��
	     	info = pm.getPackageInfo("com.apl.Loto6Sense_Lite", 0);
	     	
	     	// PackageInfo.�������ŎQ�Ƃ���
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.custom_layout);
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_layout);

	        final TextView rightText = (TextView) findViewById(R.id.right_text);
	        rightText.setTextColor(Color.WHITE);
	        rightText.setTypeface(Typeface.DEFAULT_BOLD);
	        rightText.setText("v"+info.versionName);

	        final TextView leftText = (TextView) findViewById(R.id.left_text);
	        leftText.setTextColor(Color.WHITE);
	        leftText.setTypeface(Typeface.DEFAULT_BOLD);
	        
	        // ---------------------
	        // ���C�A�E�g�쐬����
	        // ---------------------
	        
	        // main
	        LinearLayout layoutMainTop = new LinearLayout(this);
	        setContentView(layoutMainTop);
	        
	        // scroll
	        ScrollView scrollv = new ScrollView(getApplication());
	        scrollv.setBackgroundColor(Color.BLACK);
	        scrollv.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, LotoConst.FP));
	        layoutMainTop.addView(scrollv);
	                
	        // ���C�����C�A�E�g
	        LinearLayout layoutMain = new LinearLayout(this);
	        layoutMain.setId(ID_LAYOUT_MAIN);
	        layoutMain.setOrientation(LinearLayout.VERTICAL);
	        layoutMain.setGravity(Gravity.CENTER | Gravity.TOP);
	        layoutMain.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, LotoConst.FP));
	        scrollv.addView(layoutMain);
	        
	        // ���t�\�����x��
	        TextView tvToday = new TextView(this);
	        tvToday.setText(LotoCom.getYYYYMM());
	        tvToday.setTextSize(16);
	        tvToday.setTypeface(Typeface.DEFAULT_BOLD);
	        layoutMain.addView(tvToday);
	
	        // �������x��
	        TextView tvInfo = new TextView(this);
	        tvInfo.setText(R.string.lbl_msg01);
	        tvInfo.setTextSize(20);
	        tvInfo.setGravity(Gravity.CENTER);
	        tvInfo.setTypeface(Typeface.DEFAULT_BOLD);
	        layoutMain.addView(tvInfo);	
	        
	        // �X�^�[�g�{�^��
	        ImageButton imgBtnStart = new ImageButton(this);
	    	mResources = getResources();
			mNormalBack = mResources.getDrawable(R.drawable.startbtn);
			imgBtnStart.setBackgroundDrawable(mNormalBack);
			imgBtnStart.setId(ID_BTN_START);
	        layoutMain.addView(imgBtnStart,new LayoutParams(LotoConst.WC, LotoConst.WC));
	        
	        // ���X�i�[�쐬
	        ClickListener clickStartBtn = new ClickListener();
	        imgBtnStart.setOnClickListener(clickStartBtn);
	        TouchListener touchStartBtn = new TouchListener();
	        imgBtnStart.setOnTouchListener(touchStartBtn);
	
	        // �N���A�[ �{�^��
	        ImageButton imgBtnClear = new ImageButton(this);
	    	mResources = getResources();
	    	Drawable drawClear = mResources.getDrawable(R.drawable.clearbtn);
	    	imgBtnClear.setBackgroundDrawable(drawClear);
	    	imgBtnClear.setVisibility(View.GONE);
	    	imgBtnClear.setId(ID_BTN_CLEAR);
	        layoutMain.addView(imgBtnClear,new LayoutParams(LotoConst.WC, LotoConst.WC));
	                
	        // ���X�i�[�쐬
	        ClickListener2 clickClear = new ClickListener2();
	        imgBtnClear.setOnClickListener(clickClear);
	        TouchListener2 touchClear = new TouchListener2();
	        imgBtnClear.setOnTouchListener(touchClear);
	
	        // ---------------------
	        // DB����
	        // ---------------------
	        
	        // DB�ݒ�
	        mCtrDB = new LotoCtrDB(getApplication());
	
	        // ---------------------
	        // HTTP�ʐM
	        // ---------------------
	        
	        httpMain();
	
	        // ---------------------
	        // �v�Z�����N���X����
	        // ---------------------
	        
	        mSense = new LotoSense(getApplication(),new Random(new Random().nextInt()),mSdLog);
	        mSense.setGetDBDat5(mCtrDB.getDat5());
	        mSense.setGetDBDat50(mCtrDB.getDat50());
	
	        // ---------------------
	        // �`��N���X�̃C���X�^���X�𐶐� 
	        // ---------------------
	
	        mBalls = new LotoBallList(this);    
	        LotoBallView mView = new LotoBallView( getApplication(),mBalls);
	        mView.setLayoutParams(new LinearLayout.LayoutParams(LotoConst.FP, 300));
	        mView.setId(ID_VIEW_BALL);
	        layoutMain.addView(mView);

	        // ---------------------
	        // �O��\�������\������
	        // ---------------------

	        // �^�C�}�[����	        
	        mHandler = new RedrawHandler(mView, Integer.valueOf(getString(R.string.dat_timer)).intValue());
	        
	        // �O��\�������擾
	        ArrayList<String[]> getTmpList = mProperty.loadLastViewBallSetList();
	        
	        if( getTmpList.size() > 0){
	        	
	        	// �O��\�����Ă������������݂���ꍇ
		        ArrayList<LotoBallSet> tmpBallSetList = new ArrayList<LotoBallSet>();
		        
	        	// �O��\�����Ă���������\������
		        imgBtnStart.setVisibility(View.GONE);
		        
		        // Clear�{�^����\��
		        imgBtnClear.setVisibility(View.INVISIBLE);
		        
		        for(int i=0; i<getTmpList.size(); i++){
		        	String[] getLine = getTmpList.get(i);
		        	LotoBallSet tmpBallSet = new LotoBallSet();
		        	for(int ii=0; ii<getLine.length; ii++){
		        		int tmpBallNum = Integer.valueOf(getLine[ii]).intValue();
		        		LotoBall tmpBall = new LotoBall(this, tmpBallNum);
		        		tmpBallSet.setBall(tmpBall);
		        	}
		        	tmpBallSetList.add(tmpBallSet);
		        }
		        mView.setSenceBalls(tmpBallSetList,256);
		        mView.setAnimationFlg(false);
		        mView.start();
		        mHandler.start();
	        }
	       
	    }catch(SQLiteDiskIOException e){
	    	System.out.println(e.getStackTrace());
	    	mErrorMsg.append(getString(R.string.msg_error_database));
        }catch(Exception e){
	    	System.out.println(e.getStackTrace());
	    	mErrorMsg.append(getString(R.string.msg_error_system));
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
    	System.out.println(keyCode);
    	
    	if( keyCode == 4 ){
            // �擾
            LotoBallView tmpView = (LotoBallView)findViewById(ID_VIEW_BALL);
            ArrayList<LotoBallSet> getBalls = tmpView.getSenceBalls();
            mProperty.saveLastViewBallSetList(getBalls);
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
    /**
     * 
     */
    @Override
    public void onStart() {
        super.onStart();

        // �G���[����
        if( mErrorMsg.length() > 0 ){
        	dialogMsg(LotoConst.RTN_CODE_ERROR,mErrorMsg.toString());
	    }else{
	        
		    try{
		        // ---------------------
		        // �v���p�e�B�t�@�C���ǂݍ���
		        // ---------------------
	
		    	// �V�K�ɓǂݒ���
		        mProperty = new LotoProperty(this);
		        
		        // HOLD����
			    if( mProperty.getHoldList() != null){
			        mSense.setHoldList(mProperty.getHoldList());
			    }else{
			    	mSense.setHoldList(null);
			    }
		
			    // �v�Z���@
		        mSense.setSenseType(mProperty.getSenseType());
		        
		        // �\����
		        LotoBallView tmpView = (LotoBallView)findViewById(ID_VIEW_BALL);
		        tmpView.setLineCnt(mProperty.getViewCnt());
		
		        // �{�[���\���̈�̃T�C�Y����
		        tmpView.setLayoutParams(
		        		new LinearLayout.LayoutParams(LotoConst.FP, 15+ mProperty.getViewCnt() * 50));
		
		        // �\���X�s�[�h
		        mHandler.setSleep((this.getResources().getStringArray( 
		        		R.array.loto_speed).length - mProperty.getViewSpeed()) * 5);
		        
		    }catch(Exception e){
		    	System.out.println(e.getStackTrace());
		    	mErrorMsg.append(getString(R.string.msg_error_system));	    	
		    }
	        
	        // �G���[����
	        if( mErrorMsg.length() > 0 ){
	        	dialogMsg(LotoConst.RTN_CODE_ERROR,mErrorMsg.toString());
	        	mErrorMsg.setLength(0);
	        }else if( mWarningMsg.length() > 0 ){
	        	dialogMsg(LotoConst.RTN_CODE_WARNING,mWarningMsg.toString());
	        	mWarningMsg.setLength(0);
	        }
	    }
    }    
    
    /** 
     * �_�C�A���O�{�b�N�X��\��
     */
    private void dialogMsg(int inMsgType,String inMsg){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        if( inMsgType == LotoConst.RTN_CODE_ERROR ){
    	    // error
    	    alertDialogBuilder.setIcon( android.R.drawable.ic_delete);
            alertDialogBuilder.setTitle("system error");
    	    
        	// OK���ɏ������I�����܂��B
	        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {    
		       	@Override    
		       	public void onClick(DialogInterface dialog, int which) {    
		       		finish();
		       		}
	       	});

        }else if( inMsgType == LotoConst.RTN_CODE_WARNING ){
    	    // warning
    	    alertDialogBuilder.setIcon( android.R.drawable.ic_menu_info_details);
            alertDialogBuilder.setTitle("warning");
    	    
        	// OK���ɏ������p�����܂��B
	        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {    
		       	@Override    
		       	public void onClick(DialogInterface dialog, int which) {    
		       		}
	       	});
        }
        
        alertDialogBuilder.setMessage(inMsg);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * @throws Exception  
     *  
     */
    private void httpSub(LotoCtrDB inCtrDB) throws Exception{
        
        // HTTP�ʐM�Ńf�[�^���擾        
        String wkURL= getString(R.string.url_file);
        String[][] wkHttpDatSp = null;
        try{
            String wkHttpDat = LotoHttp.http2str(this,wkURL);    
            
            if( wkHttpDat.length() == 0 ){
            	throw new LotoException(LotoException.HTTPFILE_SIZE0);
            }
            
            String[] wkHttpDatSp1 = wkHttpDat.split(LotoConst.KAIGYOU); 
            wkHttpDat = wkHttpDat.replaceAll(LotoConst.KAIGYOU, ",");
            
            int wkNowMax = inCtrDB.getNewCount();
            if( wkHttpDatSp1.length - wkNowMax > 0){
	            wkHttpDatSp = new String[wkHttpDatSp1.length - wkNowMax][];
	            int wkDatSpCnt = 0;
	            for( int i=wkNowMax; i < wkHttpDatSp1.length; i++){
	            	if( wkHttpDatSp1[i].length() > 0){
	            		wkHttpDatSp[wkDatSpCnt] = wkHttpDatSp1[i].split(",");
	            		wkDatSpCnt++;
	            	}
	            }
            }
            
            /** 
             * String[] �� ArrayList�ϊ�
             * String[] array=(String[])list.toArray(new String[0]);
             * String str[] 	= {"a","b","c","d","e","f","g","h"};
             * ArrayList list 	= new ArrayList(Arrays.asList(str));
             */
            
            inCtrDB.insertDB(wkHttpDatSp);	

        }catch(Exception e){
        	throw e;
        }
    }

    /** 
     * 
     */
    private void httpMain() throws Exception{

        // ���I�ԍ��X�V�������擾
        Date wkToDat = new Date();    
        Date wkFromDat = null;
        
        // �v���p�e�B�t�@�C���ǂݍ���
        // �����̂ݎg�p
        mProperty = new LotoProperty(this);
        wkFromDat = LotoCom.dateFromStr(mProperty.getUpdateDate());
        
		if( httpChk(wkFromDat,wkToDat) ){
			
			// HTTP�֘A�̃G���[���́A�������p������B
			try{
				httpSub(mCtrDB);
				mProperty.setUpdateDate(LotoCom.strFromDate(wkToDat));
	            mProperty.save();
	            
			}catch(FileNotFoundException e){
				// URL�̃t�@�C�������G���[
				mWarningMsg.append(getString(R.string.msg_warning_nofile));
				
			}catch(UnknownHostException e){
				// �T�[�o�ڑ��G���[
				mWarningMsg.append(getString(R.string.msg_warning_nofile));
				
			}catch(NumberFormatException e){
				// �s���f�[�^�@������f�[�^�����݂���
				mWarningMsg.append(getString(R.string.msg_warning_nofile));

			}catch(LotoException e){
				
				if( e.subExceptionNum == LotoException.HTTPFILE_NOITEMCNT ){
					// �s���f�[�^ HTTP�t�@�C���̍��ڐ����ُ�
					mWarningMsg.append(getString(R.string.msg_warning_nofile));

				}else if( e.subExceptionNum == LotoException.HTTPFILE_SIZE0 ){
					// �s���f�[�^ HTTP�t�@�C���̃T�C�Y���ُ�
					mWarningMsg.append(getString(R.string.msg_warning_nofile));
				}
			}
		}
    }

    /** 
     * DB�X�V�^�C�~���O���ǂ�����Ԃ�
     * @param  inFrom  ���t�P
     * @param  inTo    ���t�Q
     * @return boolean ture=�X�V�^�C�~���O false=����ȊO
     */
    private boolean httpChk(Date inFrom,Date inTo){
    	
    	boolean rtnVal = false;
    	int longDay = 0;
    	
    	if( getString(R.string.app_mode).equals(LotoConst.APP_MODE_FREE)){
        	rtnVal = false;
    	}else if( inFrom == null){
        	rtnVal = true;
    	}else{
	    	
	        longDay = LotoCom.getDays(inFrom, inTo);
	    	
	        // �o�ߓ�����7���ȓ��̂�
	        if( longDay <= 7 ){
		    	
//2010/02/16 �C��
//			    Calendar calFrom = Calendar.getInstance();	    
//			    calFrom.set(inFrom.getYear(), inFrom.getMonth()-1, inFrom.getDay());
//			    int fromWeekNo = calFrom.get(Calendar.DAY_OF_WEEK);
//		
//			    Calendar calTo = Calendar.getInstance();	    
//			    calTo.set(inTo.getYear(), inTo.getMonth()-1, inTo.getDay());
//			    int toWeekNo = calTo.get(Calendar.DAY_OF_WEEK);
//			    
//			    // �ؗj�����߂��Ă��邩�ǂ���
//			    if(fromWeekNo <= Calendar.THURSDAY && Calendar.THURSDAY < toWeekNo ){
//			    	rtnVal = true;
//			    }
	        	
	        	// 2010/02/16 �C���F�j�����擾���邽�߂ɃJ�����_�[���g�p����
			    Calendar calFrom = Calendar.getInstance();
			    calFrom.setTime(inFrom);
			    
			    // 2010/02/16 �C���FDB�X�V���t�̎��̓�����A�v���N�����܂łɋ��j�������邩
			    for(int iii=1; iii <= longDay; iii++){
			    	calFrom.add(Calendar.DATE,1);
			    	
			    	if(calFrom.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){
			    		rtnVal = true;
			    		break;
			    	}
			    }
	        }else{
	        	rtnVal = true;
	        }
    	}
	    return rtnVal;
    }
    
    /** 
     * Timer 
     */
    class RedrawHandler extends Handler {
    	
        private LotoBallView view;
        private int delayTime=0;
        private int frameRate=0;
        private int sleep=0;
        private long count=0;

        /** timer Redraw */
        public RedrawHandler(LotoBallView view, int frameRate) {
            this.view = view;
            this.frameRate = frameRate;
        }

        /** timer start */
        public void start() {
            this.delayTime = 1000 / frameRate;
            count = 0;
            this.sendMessageDelayed(obtainMessage(0), delayTime);
        }
        
        /** timer stop */
        public void stop() {
            delayTime = 0;
        }

        /** timer */
        @Override
        public void handleMessage(Message msg) {
        	if( count > sleep){
        		view.invalidate();
        		count = 0;
        	}
            
            count=count+1;
            if(view.getViewEndFlg()){
            	delayTime = 0;
            }
            if (delayTime == 0){
    	        ImageButton imgBtnClear = (ImageButton)findViewById(ID_BTN_CLEAR);
    	        imgBtnClear.setVisibility(View.VISIBLE);
            	return;
            }
            sendMessageDelayed(obtainMessage(0), delayTime);
        }
        
        public void setSleep(int inSleep){
        	sleep = inSleep;
        }
    }
    
    /** 
     * START�{�^���N���b�N 
     */
    class ClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			LotoBallView ballview = (LotoBallView)findViewById(ID_VIEW_BALL);
	        
	        // ���I�ԍ����o
	        ArrayList<LotoBallSet> viewBallList = new ArrayList<LotoBallSet>();
	        for( int i=0; i < mProperty.getViewCnt(); i++){
		        // ���I�ԍ��\�z�����J�n
	        	LotoBallSet aryTmp = mSense.getNumber(mBalls);     
        		viewBallList.add(aryTmp);
        		mDebug.debug_LotoBallSet(aryTmp);
	        }
	        	        
	        // ���o���ʕ\���J�n
	        ballview.setSenceBalls(viewBallList,0);	  
	        ballview.setAnimationFlg(true);
	        ballview.start();
	        mHandler.start();
	        
	        // Start�{�^����\��
	        v.setVisibility(View.GONE);
	        
	        // Clear�{�^����\��
	        ImageButton imgB = (ImageButton)findViewById(ID_BTN_CLEAR);
	        imgB.setVisibility(View.INVISIBLE);
	        	        
		}
    };
    
    /** 
     * Clear�{�^���N���b�N 
     */
    class ClickListener2 implements OnClickListener {
		@Override
		public void onClick(View v) {

			// Ball view ������
			LotoBallView ballview = (LotoBallView)findViewById(ID_VIEW_BALL);
	        ballview.clear();
	        
	        // Clear�{�^����\��
	        v.setVisibility(View.GONE);
	        
	        // Start�{�^���\��
	        ImageButton imgB = (ImageButton)findViewById(ID_BTN_START);
	        imgB.setVisibility(View.VISIBLE);
	        
		}
    };  
    
    /** 
     * START�{�^���摜����
     */
    class TouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			ImageButton countBtn = (ImageButton) v;
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				mPushBack = mResources.getDrawable(R.drawable.startbtn2);
				countBtn.setBackgroundDrawable(mPushBack);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP)
			{
				mNormalBack = mResources.getDrawable(R.drawable.startbtn);
				countBtn.setBackgroundDrawable(mNormalBack);
			}
			return false;
		}
    	
    }

    /** 
     * CLEAR�{�^���摜���� 
     */
    class TouchListener2 implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			ImageButton countBtn = (ImageButton) v;
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				mPushBack = mResources.getDrawable(R.drawable.clearbtn2);
				countBtn.setBackgroundDrawable(mPushBack);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP)
			{
				mNormalBack = mResources.getDrawable(R.drawable.clearbtn);
				countBtn.setBackgroundDrawable(mNormalBack);
			}
			return false;
		}
    }

    /** 
     * ��ʁA�c/���ύX���ɍċN�����Ȃ��悤�ɐݒ�    
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    }
    
    /** 
     * ���j���[���쐬����    
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
    
        menu.add(0, 
                MENU_ID_HOLD, 
                0, 
                getString(R.string.menu_hold)).setIcon(android.R.drawable.ic_menu_mylocation);
        
         menu.add(0, 
                MENU_ID_SETTING, 
                1, 
                getString(R.string.menu_setting)).setIcon(android.R.drawable.ic_menu_preferences);    
                 
        return true;
    }
    
    /** 
     * ���j���[�{�^���������̏���     
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    /** 
     * ���j���[��I�����̏���     
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ID_HOLD:
            //���̉�ʂɑJ�ڂ�����
        	
            Intent intent = new Intent();
            intent.setClassName(
                    "com.apl.Loto6Sense_Lite",
                    "com.apl.Loto6Sense_Lite.LotoOptHold");
            //startActivity(intent);            
            //���N�G�X�g�R�[�h�t���C���e���g�̌Ăяo��
            startActivityForResult(intent,0);

            return true;
        case MENU_ID_SETTING:
            //���̉�ʂɑJ�ڂ�����
        	
            Intent intent2 = new Intent();
            intent2.setClassName(
                    "com.apl.Loto6Sense_Lite",
                    "com.apl.Loto6Sense_Lite.LotoSetting");
            startActivityForResult(intent2,0);

            return true;
        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    // �A�N�e�B�r�e�B�Ăяo�����ʂ̎擾
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent) {
        if (requestCode == 0 && resultCode==RESULT_OK) {
            //�߂�l�p�����[�^�̎擾
            SharedPreferences pref=getSharedPreferences("PREVIOUS_RESULT",MODE_PRIVATE);
            //String aaa = pref.getString("test","");
            //System.out.println(aaa);
        }
    }    

}











