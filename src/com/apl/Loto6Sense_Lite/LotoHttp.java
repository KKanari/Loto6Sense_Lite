package com.apl.Loto6Sense_Lite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class LotoHttp {

    //HTTP�ʐM �� ������
    public static String http2str(Context context,
        String path) throws Exception {  
        byte[] w=http2data(context,path);
        return new String(w);
    }    
    
    //HTTP�ʐM
    public static byte[] http2data(Context context,String path) throws Exception {
    	
        int size;
        byte[] w=new byte[1024]; 
        HttpURLConnection c=null;
        InputStream in=null;
        ByteArrayOutputStream out=null;
        
        try {
            //HTTP�ڑ��̃I�[�v��
            URL url=new URL(path);
            c=(HttpURLConnection)url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            in=c.getInputStream();
            
            //�o�C�g�z��̓ǂݍ���
            out=new ByteArrayOutputStream();
            while (true) {
                size=in.read(w);
                if (size<=0) break;
                out.write(w,0,size);
            }
            out.close();

            //HTTP�ڑ��̃N���[�Y
            in.close();
            c.disconnect();
            
            return out.toByteArray();
            
        } catch (Exception e) {
            try {
                if (c!=null){
                	c.disconnect();
                }
                if (in!=null){
                	in.close();
                }
                if (out!=null){
                	out.close();
                }
            } catch (Exception e2) {
            }
            throw e;
        }
    }     
}
