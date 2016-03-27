package com.example.dialog;



import com.example.licai.JZAddActivity;
import com.example.licai.R;
import com.example.service.End;
import com.example.service.JZLeibieAdapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DialogLeiBie extends Dialog implements OnClickListener {
	
	//类别列表和类别子类列表
	private ListView lbList;
	//类别列表适配器
	private JZLeibieAdapter adapter;
	private Context context;
	private View lbView;
	private int now_flag=0;
	//类别和类别子类标识
	public static final int flagsubleibie=3020;
	public DialogLeiBie(Context context,int now_flag) {
		super(context, R.style.leibiedialog);
		this.context = context;
		this.now_flag = now_flag;
		lbView = View.inflate(context, R.layout.dialog_leibie, null);
		this.setContentView(lbView);
		//类别适配器
		adapter = new JZLeibieAdapter(context,now_flag,null);
		//类别列表
		lbList = (ListView) lbView.findViewById(R.id.leibie_dialog_list);
		lbList.setAdapter(adapter);
		lbList.setOnItemClickListener(new clickItem());
		this.show();
	}

	@Override
	public void onClick(View v) {
	}
	
	String flagShow;//判断当前该列表是否已显示
	Handler handler = new Handler();
	private class clickItem implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(now_flag==JZAddActivity.zhichu_flag){
				getTextandSend(view);
			}else if(now_flag==JZAddActivity.shouru_flag){
				getTextandSend(view);
			}
		}
	}
	
	
	
	public void getTextandSend(View view){
		String string = (String)view.getTag();
		End.dialogEnd(this, lbView, context, handler, R.anim.push_up_out, 30);
		Message msg  =Message.obtain();
		msg.what=JZAddActivity.leibie_msg;
		msg.obj = string;//发送当前选择给JZAddActivity，让其界面作出相应改变
		JZAddActivity.mh.sendMessage(msg);
	}
	
	public boolean onKeyDown(int kCode, KeyEvent kEvent) {
		switch (kCode) {
		case KeyEvent.KEYCODE_BACK: {
			if (lbView.isShown()) {
				return false;
			} else {
				this.cancel();
			}
		}
		}
		return super.onKeyDown(kCode, kEvent);
	}
	
}
