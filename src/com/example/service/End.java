package com.example.service;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

public class End {
	  //�������� ������ ��ͼ�������ģ�handler,��ʼ�������ӳ�ʱ��
	    public static void dialogEnd(final Dialog d,final View v,final Context context,final Handler handler,final int eanimId,final int time) {
	        new Thread() {
	            public void run() {
	                try {
	                    handler.post(new Runnable(){
	                    	public void run(){
	                    		v.startAnimation(AnimationUtils.loadAnimation(context, eanimId));
	                    	}
	                    });
	                    sleep(time);
	                    handler.post(new Runnable(){
	                    	public void run(){
	                    		d.cancel();
	                    		d.dismiss();
	                    	}
	                    });
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }.start();
	    }
}