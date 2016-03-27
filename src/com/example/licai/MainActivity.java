package com.example.licai;


import com.example.service.GetTime;
import com.example.service.JZMingXiAdapter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	//TextView�꣬��
	private TextView toptime,zcjine_text,yejine_text,srjine_text;
	private Button additem_bt,mingxi_bt;
	//��ǰ����
	private int year,month;
	//��ǰ���
	private int flag=0;
	//������
	private JZMingXiAdapter adapter;
    private MessageHandler mh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new JZMingXiAdapter(this);
		mh = new MessageHandler();
		init();
	}
	

/*
 * ���������ڽ����ʾ
 */
    private void init() {
    	//Money
    	zcjine_text = (TextView) findViewById(R.id.jz_zhichujine_text);
    	srjine_text = (TextView) findViewById(R.id.jz_shourujine_text);
    	yejine_text = (TextView) findViewById(R.id.jz_yuejine_text);
    	//ʱ��
        toptime = (TextView) findViewById(R.id.jz_year_text);
    	//�㿪Add����
    	additem_bt = (Button) findViewById(R.id.button_add);
    	additem_bt.setOnClickListener(this);
    	mingxi_bt = (Button) findViewById(R.id.button_mingxi);
    	mingxi_bt.setOnClickListener(this);
    	 
    	new Thread() {
            public void run() {
                try {
                	Message msg = Message.obtain();
                    msg.what=1;
                    mh.sendMessage(msg);
                    sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    @Override
    protected void onResume(){
    	getHuiZong();
        super.onResume();
    }
    
    public void onClick(View v) {
    	switch (v.getId()){
    	case R.id.button_add:
    		changeActivity (JZAddActivity.class);
    		break;
    	case R.id.button_mingxi:
    		changeActivity (JZMingXiActivity.class);
    		break;
    	}
    }
    public void getHuiZong(){
    	
    	double count[] = adapter.getList(year, month, 0, flag);
		zcjine_text.setText(count[0]+"");
		srjine_text.setText(count[1]+"");
		yejine_text.setText((count[1]-count[0])+"");
		adapter.notifyDataSetChanged();
		toptime.setText(GetTime.getYear() + "��" + GetTime.getMonth() + "��" + GetTime.getDay() + "��");
        year = GetTime.getYear();
    	month = GetTime.getMonth();
    }
   
    //handler���½�����
    Handler handler = new Handler();
    class MessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
            	getHuiZong();
                break;
            }
            super.handleMessage(msg);
        }
        
    }
    /*
     * �л�����
     */
    public void changeActivity(final Class<?> c) {
    	new Thread() {
            public void run() {
                try {
                    Intent intent = new Intent(MainActivity.this, c);
                    MainActivity.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

