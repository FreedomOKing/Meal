package com.cm.mealapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cm.mealapp.activity.R;
import com.cm.mealapp.bean.users;

public class BusinessListAdapter extends BaseAdapter {
	private List<users> list = null;
	private final Context context;
	private LayoutInflater infater = null;

	public BusinessListAdapter(Context context, List<users> list) {
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
	public View getView(int position, View convertview, ViewGroup parent) {
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

			holder.textView3.setVisibility(View.VISIBLE);

			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.textView1.setText(list.get(position).getName());
		holder.textView2.setText("µÿ÷∑£∫" + list.get(position).getAddr());
		holder.textView3.setText("µÁª∞£∫" + list.get(position).getPhone());

		return convertview;
	}

	class ViewHolder {
		private TextView textView1;
		private TextView textView2;
		private TextView textView3;

	}

}
