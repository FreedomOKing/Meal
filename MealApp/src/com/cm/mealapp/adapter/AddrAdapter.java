package com.cm.mealapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cm.mealapp.activity.R;
import com.cm.mealapp.bean.tb_addrs;

public class AddrAdapter extends BaseAdapter {
	private List<tb_addrs> list = null;
	private final Context context;
	private LayoutInflater infater = null;

	public AddrAdapter(Context context, List<tb_addrs> list) {
		this.infater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertview, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertview == null) {
			holder = new ViewHolder();
			convertview = infater.inflate(R.layout.listview_item_common, null);

			holder.textView1 = (TextView) convertview
					.findViewById(R.id.textView1);
			holder.textView2 = (TextView) convertview
					.findViewById(R.id.textView2);
			holder.textView3 = (TextView) convertview
					.findViewById(R.id.textView3);
			holder.textView1.setTextSize(18);
			holder.textView2.setTextSize(16);
			holder.textView3.setTextSize(16);
			holder.textView3.setVisibility(View.GONE);
			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}

		holder.textView1.setText("收货人:" + list.get(position).getPerson());
		holder.textView2.setText("电话:" + list.get(position).getPhone()
				+ "\n地址:" + list.get(position).getAddr());

		return convertview;
	}

	class ViewHolder {

		private TextView textView1;
		private TextView textView2;
		private TextView textView3;

	}

}
