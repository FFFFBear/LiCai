package com.example.licai;

import com.example.bean.JZshouru;
import com.example.bean.JZzhichu;
import com.example.service.GetTime;
import com.example.service.JZMingXiAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class JZMingXiActivity extends Activity implements OnClickListener,OnItemClickListener {
	//当前年月
	private int year,month;
	//当前类别
	private int flag=0;
	//类别  支出和收入
	public static final int shouru_flag=4010;
	public static final int zhichu_flag=4020;
	public MessageHandler mh;
	private Button zc_bt, sr_bt;
	private LinearLayout list_ll;
	//list列表
	private ListView listView;
	//适配器
	private JZMingXiAdapter adapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mingxilist);
		mh = new MessageHandler();
		adapter = new JZMingXiAdapter(this);
		init();
	}

	private void init(){
		list_ll = (LinearLayout)this.findViewById(R.id.listview_list_ll);
		flag = zhichu_flag;
		zc_bt = (Button) findViewById(R.id.listview_zc_bt);
		zc_bt.setOnClickListener(this);
		sr_bt = (Button) findViewById(R.id.listview_sr_bt);
		sr_bt.setOnClickListener(this);
		year = GetTime.getYear();
		month = GetTime.getMonth();
		list();
		//ListView
	    listView  = (ListView) findViewById(R.id.listview_list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
	}
	/*
	 * 获取List
	 * */
	public void list(){
		double count[] = adapter.getList(year, month, 0, flag);
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void onResume() {
		list();
		super.onResume();
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
		case R.id.listview_zc_bt:
			flag = zhichu_flag;
			changeShow(list_ll);
			break;
		case R.id.listview_sr_bt:
			flag  =shouru_flag;
			changeShow(list_ll);
			break;
		}
	}
	
	
	/*
	 * 切换
	 * */
    public void changeShow(final View v){
    	new Thread(){
    		public void run(){
    			try {
					sleep(400);
					mh.post(new Runnable(){
        				public void run(){
        					listThread();
        				 }
    				});
    			} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}.start();
    }
    
    
    public void listThread(){
    	new Thread(){
    		public void run(){
    			mh.post(new Runnable(){
    				public void run(){
    					list();
    				 }
				});
    		}
    	}.start();
    }

	public class MessageHandler extends Handler {
		public void handleMessage(Message msg) {
		}
	}
	public boolean onKeyDown(int kCode, KeyEvent kEvent) {
		switch (kCode) {
		case KeyEvent.KEYCODE_BACK: {
			this.finish();
			return false;
		 }
		}
		return super.onKeyDown(kCode, kEvent);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this,JZAddActivity.class);
		if(flag == zhichu_flag){
			JZzhichu zc = (JZzhichu)view.getTag();
			intent.putExtra("update", true);
			intent.putExtra("type", JZAddActivity.zhichu_flag);
			intent.putExtra("id", zc.getZc_Id());
			startActivity(intent);
		}else if(flag == shouru_flag){
			JZshouru sr = (JZshouru)view.getTag();
			intent.putExtra("update", true);
			intent.putExtra("type", JZAddActivity.shouru_flag);
			intent.putExtra("id", sr.getSr_Id());
			startActivity(intent);
		}
	}	

}
