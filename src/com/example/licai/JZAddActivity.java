package com.example.licai;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.bean.JZshouru;
import com.example.bean.JZzhichu;
import com.example.db.JZData;
import com.example.dialog.DialogLeiBie;
import com.example.service.JZMingXiAdapter;
import com.example.service.GetTime;
import com.example.bean.JZItem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JZAddActivity extends Activity implements OnClickListener {
	
	//EditText���,��ע
	private EditText jine,beizhu;
	// TextView���ʱ�䡣
    private TextView leibie, date, time;
	 // ����֧�� ���� �ı����޸�ʱ��Ҫ�Ķ��ı�����
    private Button zhichu_text, shouru_text;
    //RelativeLayout���棬ȡ��,ɾ����
    private RelativeLayout save_rl, cancel_rl, del_rl,zhichu_rl, shouru_rl;
    //
    private Spinner spinner;
    // �жϵ�ǰ�ǳ�ʼ�������Ƕ��θ���
    private boolean isUpdate = false;
    // ��ǰ�����յ�����Ϣ��ʾ��msg.what��
    public static final int leibie_msg = 1010;
    // ���ĵ����ͣ�֧�� ���� ��
    private int update_type, update_id, update_flag;
    // ��ǰѡ���������֧��������
    public static final int zhichu_flag = 2010, shouru_flag = 2020;
    // ��ǰѡ�������
    private int now_flag = zhichu_flag;
    // ���ݿ����
    JZData dataHelper;
    public static MessageHandler mh;
    
    private ArrayAdapter<String> leibieadapter; 
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addactivity_layout);
		initZhiChu();
		initUpdate();
		mh = new MessageHandler();
	}

	
	/*
     * ��������޸ĵķ�ʽ�򿪸ý���
     */
    JZzhichu zc = new JZzhichu();
    JZshouru sr = new JZshouru();
    
    private void initUpdate(){
    	Intent intent = this.getIntent();
    	if (intent.hasExtra("update")) {
            zhichu_text = (Button) this.findViewById(R.id.add_zc_bt);
            shouru_text = (Button) this.findViewById(R.id.add_sr_bt);
            del_rl.setVisibility(View.VISIBLE);
            isUpdate = true;
            update_type = intent.getIntExtra("type", 0);
            update_id = intent.getIntExtra("id", 0);
            ArrayList<?> mingXiList = JZMingXiAdapter.mingXiList;
            if (update_type == zhichu_flag) {
                for (Object zhichu : mingXiList) {
                    JZzhichu zc = (JZzhichu) zhichu;
                    if (update_id == zc.getZc_Id()) {
                        this.zc = zc;
                        getZhiChuType(zc);
                        return;
                    }
                }
            } else if (update_type == shouru_flag) {
                for (Object shouru : mingXiList) {
                    JZshouru sr = (JZshouru) shouru;
                    if (update_id == sr.getSr_Id()) {
                        this.sr = sr;
                        getShouRuType(sr);
                        return;
                    }
                }
            }
        }
    }
    
    //�����������
    private void getShouRuType(JZshouru sr) {
    	now_flag = shouru_flag;
        shouru_text.setText("�޸�����");
        leibie.setText(sr.getSr_Item());
        zhichu_text.setVisibility(View.INVISIBLE);
    
    update_flag = shouru_flag;// ����ɾ����ǰ���ݹ���
    jine.setText(sr.getSr_Count() + "");
    leibie.setText(sr.getSr_Item());
    date.setText(sr.getSr_Year() + "-" + sr.getSr_Month() + "-" + sr.getSr_Day());
    beizhu.setText(sr.getSr_Beizhu());
		
	}


    //���֧������
	private void getZhiChuType(JZzhichu zc) {
		now_flag = zhichu_flag;
        zhichu_text.setText("�޸�֧��");
        leibie.setText(zc.getZc_Item());
        shouru_text.setVisibility(View.INVISIBLE);
        
    update_flag = zhichu_flag;// ����ɾ����ǰ���ݹ���
    jine.setText(zc.getZc_Count() + "");
    date.setText(zc.getZc_Year() + "-" + zc.getZc_Month() + "-" + zc.getZc_Day());
    beizhu.setText(zc.getZc_Beizhu());
		
	}


	private void initZhiChu() {
		zhichu_text = (Button) this.findViewById(R.id.add_zc_bt);
		zhichu_text.setOnClickListener(this);
        shouru_text = (Button) this.findViewById(R.id.add_sr_bt);
        shouru_text.setOnClickListener(this);
        // �������
        jine = (EditText) findViewById(R.id.add_jine_text);
        
        leibie = (TextView) findViewById(R.id.add_leibie_text);
        leibie.setOnClickListener(new TextClick());;
        // ����
        date = (TextView) findViewById(R.id.add_time_text);
        date.setText(GetTime.getYear() + "-" + GetTime.getMonth() + "-" + GetTime.getDay());
        date.setOnClickListener(new TextClick());
        // ��ע
        beizhu = (EditText) findViewById(R.id.add_beizhu_text);
        //֧������ѡ��
        zhichu_rl = (RelativeLayout) findViewById(R.id.add_zc_rl);
        zhichu_rl.setOnClickListener(this);
        shouru_rl = (RelativeLayout) findViewById(R.id.add_sr_rl);
        shouru_rl.setOnClickListener(this);
        //���棬ȡ����ɾ��button
        save_rl = (RelativeLayout) findViewById(R.id.add_ok_rl);
        save_rl.setOnClickListener(this);
        cancel_rl = (RelativeLayout) findViewById(R.id.add_cancel_rl);
        cancel_rl.setOnClickListener(this);
        // ɾ����ǰҪ�޸ĵ����ݣ�ֻ���޸�ʱ��Ч
        del_rl = (RelativeLayout) this.findViewById(R.id.add_delete_rl);
        del_rl.setOnClickListener(this);
        del_rl.setVisibility(View.INVISIBLE);
    }
	public void onClick(View v) {
		switch (v.getId()){
		  case R.id.add_zc_bt:// ֧��tab
			  this.now_flag=zhichu_flag;
			  leibie.setText("����");
              break;
          case R.id.add_sr_bt:// ����tab
        	  leibie.setText("����");
        	  this.now_flag=shouru_flag;
              break;
		case R.id.add_ok_rl:// ���水ť
            saveToDB();
            break;
		case R.id.add_cancel_rl:// ȡ����ť
            this.finish();
            break;
        case R.id.add_delete_rl://ɾ����ť
            dataHelper = new JZData(this);
          
            switch (update_flag) {
            case zhichu_flag:
                int i1 = dataHelper.DelZhiChuInfo(update_id);
                if (i1 > 0) {
                    showMsg("ɾ���ɹ�");
                    this.finish();
                } else {
                    showMsg("ɾ��ʧ��");
                }
                break;
            case shouru_flag:
                int i2 = dataHelper.DelShouRuInfo(update_id);
                if (i2 > 0) {
                    showMsg("ɾ���ɹ�");
                    this.finish();
                } else {
                    showMsg("ɾ��ʧ��");
                }
                break;
            }
            break;
        }    
		
	}

	//Toast��Ϣ
	private void showMsg(String msg) {
	        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	/*
     * �洢֧�������������ݿ�
     */
	private void saveToDB() {
		dataHelper = new JZData(this);
		
		JZzhichu zhichu = new JZzhichu();
	    JZshouru shouru = new JZshouru();
	    // ���
        String leibies = leibie.getText().toString().trim();
        // ����
        String dateString = date.getText().toString().trim();
        String dates[] = dateString.split("-");
        // ���
        String jineString = jine.getText().toString().trim();
        // ��ע
        String beizhuString = beizhu.getText().toString().trim();
        if (jineString.equals("0.00")) {
            showMsg("����Ϊ��");
            return;
        }
        if (now_flag == zhichu_flag) {
            zhichu.setZc_Item(leibies);
            zhichu.setZc_Year(Integer.parseInt(dates[0]));
            zhichu.setZc_Month(Integer.parseInt(dates[1]));
            zhichu.setZc_Day(Integer.parseInt(dates[2]));
            zhichu.setZc_Week(GetTime.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
            zhichu.setZc_Count(Double.parseDouble(jineString));
            zhichu.setZc_Beizhu(beizhuString);
            if (!isUpdate) {
                dataHelper.SaveZhiChuInfo(zhichu);
                showMsg("����֧���洢�ɹ�");
            } else {
                dataHelper.UpdateZhiChuInfo(zhichu, zc.getZc_Id());
                showMsg("����֧���޸ĳɹ�");
            }
            this.finish();	
        } else if (now_flag == shouru_flag) {
            shouru.setSr_Item(leibies);
            shouru.setSr_Year(Integer.parseInt(dates[0]));
            shouru.setSr_Month(Integer.parseInt(dates[1]));
            shouru.setSr_Day(Integer.parseInt(dates[2]));
            shouru.setSr_Week(GetTime.getTheWeekOfYear(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2])));
            shouru.setSr_Count(Double.parseDouble(jineString));
            shouru.setSr_Beizhu(beizhuString);
            if (!isUpdate) {
                dataHelper.SaveShouRuInfo(shouru);
                showMsg("��������洢�ɹ�");
            } else {
                dataHelper.UpdateShouRuInfo(shouru, sr.getSr_Id());
                showMsg("���������޸ĳɹ�");
            }
            this.finish();		
        }
		
	}
	
	 /*
     * ����dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.add_time_text) {// �������ť��ʾ��dialog
            Calendar c = Calendar.getInstance();
            OnDateSetListener osl = new OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " ");
                }
            };
            new DatePickerDialog(this, 0, osl, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        } 
        return null;
    }

	
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    public class MessageHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case leibie_msg:
                leibie.setText((String) msg.obj);
                break;
            default:
                break;
            }
        }
    }
	
	
	/*
     * �޸�dialog
     */
    private class TextClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.add_leibie_text://�������
            	new DialogLeiBie(JZAddActivity.this, now_flag);
            	break;
            case R.id.add_time_text:// ��������
                onCreateDialog(R.id.add_time_text);
                break;
            }
        }
    }
    public boolean onKeyDown(int kCode, KeyEvent kEvent) {
        switch (kCode) {
        case KeyEvent.KEYCODE_BACK: {
                this.finish();
        }
        }
        return super.onKeyDown(kCode, kEvent);
    }



}
