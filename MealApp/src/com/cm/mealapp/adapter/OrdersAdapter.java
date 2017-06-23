package com.cm.mealapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cm.mealapp.activity.R;
import com.cm.mealapp.bean.orders;
import com.cm.utils.AsyncImageLoader;

public class OrdersAdapter extends BaseAdapter {
	private List<orders> list = null;
	private final Context context;
	private LayoutInflater infater = null;

	public OrdersAdapter(Context context, List<orders> list) {
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
			convertview = infater.inflate(R.layout.listview_item_dishes, null);
			holder.imageView1 = (ImageView) convertview
					.findViewById(R.id.imageView1);
			holder.tvTitle = (TextView) convertview.findViewById(R.id.tvTitle);
			holder.tvPrice = (TextView) convertview.findViewById(R.id.tvPrice);
			holder.tvIntro = (TextView) convertview.findViewById(R.id.tvIntro);

			convertview.setTag(holder);
		} else {
			holder = (ViewHolder) convertview.getTag();
		}
		holder.tvTitle.setText(list.get(position).getTitle());
		holder.tvPrice.setText("数量:" + list.get(position).getAmount() + " 单价:"
				+ list.get(position).getPrice() + "\n订单状态:"
				+ list.get(position).getStatus());
		String intro = "联系电话:" + list.get(position).getPhone() + "\n下单时间"
				+ list.get(position).getCreatetime() + "\n送餐地址:"
				+ list.get(position).getAddr();
		holder.tvIntro.setText(intro);

		AsyncImageLoader.getInstance().loadBitmap(
				list.get(position).getImg_url().trim(), holder.imageView1);
		return convertview;
	}

	class ViewHolder {
		private ImageView imageView1;
		private TextView tvTitle;
		private TextView tvPrice;
		private TextView tvIntro;

	}

}
