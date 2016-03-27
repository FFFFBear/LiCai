package com.example.service;

import java.util.ArrayList;

import com.example.bean.JZItem;
import com.example.dialog.DialogLeiBie;
import com.example.licai.JZAddActivity;
import com.example.licai.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class JZLeibieAdapter extends BaseAdapter{
	//����б����������б�ļ���
		private ArrayList<String> leibiearr;
		private Context context;
		private int flag = 0;

		public JZLeibieAdapter(Context context, int flag,String subitem) {
			this.flag = flag;
			this.context = context;
			//���ݴ���Ĳ�����ȡ��Ӧ�ļ���
			if(flag==JZAddActivity.zhichu_flag){
				getLeibieList(JZItem.leibie_s);
			}else{
				getLeibieList(JZItem.shoru_s);
			}
		}

		/*
		 * ��ȡ�����б���
		 * */
		public void getLeibieList(String s[]) {
			leibiearr = new ArrayList<String>();
			for (String str : s) {
				leibiearr.add(str);
			}
		}

		

		@Override
		public int getCount() {
			return leibiearr.size();
		}

		@Override
		public Object getItem(int position) {
			return leibiearr.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private RelativeLayout addsub=null;
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (flag!=DialogLeiBie.flagsubleibie) {//����ʶΪ�б�ʱ��������Ľ���
				convertView = LayoutInflater.from(context).inflate(R.layout.jz_leibie_item, null);
				String lb = leibiearr.get(position);
				TextView lb_text = (TextView) convertView.findViewById(R.id.leibie_item_text);
				addsub = (RelativeLayout) convertView.findViewById(R.id.jz_item_addsub_rl);
				convertView.setTag(lb);
				//��ǰѡ�б�ʶ
				addsub.setTag(lb);
				lb_text.setText(lb);
			} 
			return convertView;
		}
	}
