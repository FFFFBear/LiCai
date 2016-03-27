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
	
	//EditText金额,备注
	private EditText jine,beizhu;
	// TextView类别，时间。
    private TextView leibie, date, time;
	 // 顶部支出 收入 文本，修改时需要改动文本内容
    private Button zhichu_text, shouru_text;
    //RelativeLayout保存，取消,删除。
    private RelativeLayout save_rl, cancel_rl, del_rl,zhichu_rl, shouru_rl;
    //
    private Spinner spinner;
    // 判断当前是初始创建还是二次更新
    private boolean isUpdate = false;
    // 当前界面收到的消息表示（msg.what）
    public static final int leibie_msg = 1010;
    // 更改的类型（支处 收入 ）
    private int update_type, update_id, update_flag;
    // 当前选择的添加类别支出，收入
    public static final int zhichu_flag = 2010, shouru_flag = 2020;
    // 当前选择的类型
    private int now_flag = zhichu_flag;
    // 数据库操作
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
     * 如果是以修改的方式打开该界面
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
    
    //获得收入类型
    private void getShouRuType(JZshouru sr) {
    	now_flag = shouru_flag;
        shouru_text.setText("修改收入");
        leibie.setText(sr.getSr_Item());
        zhichu_text.setVisibility(View.INVISIBLE);
    
    update_flag = shouru_flag;// 用于删除当前数据功能
    jine.setText(sr.getSr_Count() + "");
    leibie.setText(sr.getSr_Item());
    date.setText(sr.getSr_Year() + "-" + sr.getSr_Month() + "-" + sr.getSr_Day());
    beizhu.setText(sr.getSr_Beizhu());
		
	}


    //获得支出类型
	private void getZhiChuType(JZzhichu zc) {
		now_flag = zhichu_flag;
        zhichu_text.setText("修改支出");
        leibie.setText(zc.getZc_Item());
        shouru_text.setVisibility(View.INVISIBLE);
        
    update_flag = zhichu_flag;// 用于删除当前数据功能
    jine.setText(zc.getZc_Count() + "");
    date.setText(zc.getZc_Year() + "-" + zc.getZc_Month() + "-" + zc.getZc_Day());
    beizhu.setText(zc.getZc_Beizhu());
		
	}


	private void initZhiChu() {
		zhichu_text = (Button) this.findViewById(R.id.add_zc_bt);
		zhichu_text.setOnClickListener(this);
        shouru_text = (Button) this.findViewById(R.id.add_sr_bt);
        shouru_text.setOnClickListener(this);
        // 金额数量
        jine = (EditText) findViewById(R.id.add_jine_text);
        
        leibie = (TextView) findViewById(R.id.add_leibie_text);
        leibie.setOnClickListener(new TextClick());;
        // 日期
        date = (TextView) findViewById(R.id.add_time_text);
        date.setText(GetTime.getYear() + "-" + GetTime.getMonth() + "-" + GetTime.getDay());
        date.setOnClickListener(new TextClick());
        // 备注
        beizhu = (EditText) findViewById(R.id.add_beizhu_text);
        //支出收入选择
        zhichu_rl = (RelativeLayout) findViewById(R.id.add_zc_rl);
        zhichu_rl.setOnClickListener(this);
        shouru_rl = (RelativeLayout) findViewById(R.id.add_sr_rl);
        shouru_rl.setOnClickListener(this);
        //保存，取消，删除button
        save_rl = (RelativeLayout) findViewById(R.id.add_ok_rl);
        save_rl.setOnClickListener(this);
        cancel_rl = (RelativeLayout) findViewById(R.id.add_cancel_rl);
        cancel_rl.setOnClickListener(this);
        // 删除当前要修改的数据，只在修改时有效
        del_rl = (RelativeLayout) this.findViewById(R.id.add_delete_rl);
        del_rl.setOnClickListener(this);
        del_rl.setVisibility(View.INVISIBLE);
    }
	public void onClick(View v) {
		switch (v.getId()){
		  case R.id.add_zc_bt:// 支出tab
			  this.now_flag=zhichu_flag;
			  leibie.setText("餐饮");
              break;
          case R.id.add_sr_bt:// 收入tab
        	  leibie.setText("工资");
        	  this.now_flag=shouru_flag;
              break;
		case R.id.add_ok_rl:// 保存按钮
            saveToDB();
            break;
		case R.id.add_cancel_rl:// 取消按钮
            this.finish();
            break;
        case R.id.add_delete_rl://删除按钮
            dataHelper = new JZData(this);
          
            switch (update_flag) {
            case zhichu_flag:
                int i1 = dataHelper.DelZhiChuInfo(update_id);
                if (i1 > 0) {
                    showMsg("删除成功");
                    this.finish();
                } else {
                    showMsg("删除失败");
                }
                break;
            case shouru_flag:
                int i2 = dataHelper.DelShouRuInfo(update_id);
                if (i2 > 0) {
                    showMsg("删除成功");
                    this.finish();
                } else {
                    showMsg("删除失败");
                }
                break;
            }
            break;
        }    
		
	}

	//Toast信息
	private void showMsg(String msg) {
	        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	/*
     * 存储支出收入借贷到数据库
     */
	private void saveToDB() {
		dataHelper = new JZData(this);
		
		JZzhichu zhichu = new JZzhichu();
	    JZshouru shouru = new JZshouru();
	    // 类别
        String leibies = leibie.getText().toString().trim();
        // 日期
        String dateString = date.getText().toString().trim();
        String dates[] = dateString.split("-");
        // 金额
        String jineString = jine.getText().toString().trim();
        // 备注
        String beizhuString = beizhu.getText().toString().trim();
        if (jineString.equals("0.00")) {
            showMsg("金额不能为零");
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
                showMsg("该条支出存储成功");
            } else {
                dataHelper.UpdateZhiChuInfo(zhichu, zc.getZc_Id());
                showMsg("该条支出修改成功");
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
                showMsg("该条收入存储成功");
            } else {
                dataHelper.UpdateShouRuInfo(shouru, sr.getSr_Id());
                showMsg("该条收入修改成功");
            }
            this.finish();		
        }
		
	}
	
	 /*
     * 日期dialog
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == R.id.add_time_text) {// 当点击按钮显示该dialog
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
     * 修改dialog
     */
    private class TextClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.add_leibie_text://更改类别
            	new DialogLeiBie(JZAddActivity.this, now_flag);
            	break;
            case R.id.add_time_text:// 更改日期
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
