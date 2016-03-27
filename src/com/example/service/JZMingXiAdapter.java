package com.example.service;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.licai.R;
import com.example.bean.JZshouru;
import com.example.bean.JZzhichu;
import com.example.db.JZData;
import com.example.licai.JZMingXiActivity;

public class JZMingXiAdapter extends BaseAdapter{
	
	//类别列表或类别子类列表的集合
	public static ArrayList<?> mingXiList;
	private Context context;
	private int flag =0;
	//数据库操作
	JZData dataHelper ;
	public JZMingXiAdapter(Context context) {
		this.context = context;
		dataHelper= new JZData(context);
	}
	
	//根据传入的参数获取相应的集合
	public double[] getList(int year,int month,int day,int flag){
		this.flag = flag;
		double countZhiChu = 0,countShouRu = 0;
		//支出list
		ArrayList<JZzhichu> zhiChuList = new ArrayList<JZzhichu>();
		String selectionzhichu = JZzhichu.ZC_YEAR+"="+year;
		zhiChuList =  dataHelper.GetZhiChuList(selectionzhichu);
		//支出汇总
		for(JZzhichu zhichu:zhiChuList){
			countZhiChu += zhichu.getZc_Count();
		}
		
		ArrayList<JZshouru> shouRuList = new ArrayList<JZshouru>();
		String selectionshouru = JZshouru.SR_YEAR+"="+year;
		shouRuList =  dataHelper.GetShouRuList(selectionshouru);
		
		//收入汇总
		for(JZshouru shouru:shouRuList){
			countShouRu += shouru.getSr_Count();
		}
		
		if(flag==JZMingXiActivity.zhichu_flag){
			mingXiList = zhiChuList;
		}else if(flag==JZMingXiActivity.shouru_flag){
				mingXiList = shouRuList;
			}
		return new double[]{countZhiChu,countShouRu};
	}

	@Override
	public int getCount() {
		return mingXiList.size();
	}
	@Override
	public Object getItem(int position) {
		return mingXiList.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		convertView = LayoutInflater.from(context).inflate(R.layout.mingxilist_layout,null);
		
		JZzhichu zhichu = new JZzhichu();
		JZshouru shouru = new JZshouru();
		
		TextView lb_text = (TextView) convertView.findViewById(R.id.listview_leibie);
		TextView jine_text = (TextView) convertView.findViewById(R.id.listview_jine);
		TextView beizhu_text = (TextView) convertView.findViewById(R.id.listview_beizhu);
		TextView time_text = (TextView) convertView.findViewById(R.id.listview_time);
		
        if(flag==JZMingXiActivity.zhichu_flag){
        	zhichu = (JZzhichu)mingXiList.get(position);
        	convertView.setTag(zhichu);
        	lb_text.setText(zhichu.getZc_Item());
        	jine_text.setText(zhichu.getZc_Count()+"");
        	beizhu_text.setText(zhichu.getZc_Beizhu());
        	time_text.setText(zhichu.getZc_Year()+"-"+zhichu.getZc_Month()+"-"+zhichu.getZc_Day());
		}else if(flag==JZMingXiActivity.shouru_flag){
		    shouru = (JZshouru)mingXiList.get(position);
		    convertView.setTag(shouru);
		    lb_text.setText(shouru.getSr_Item());
		    jine_text.setText(shouru.getSr_Count()+"");
		    beizhu_text.setText(shouru.getSr_Beizhu());
		    time_text.setText(shouru.getSr_Year()+"-"+shouru.getSr_Month()+"-"+shouru.getSr_Day());
		}
		return convertView;	
	}
	
}

