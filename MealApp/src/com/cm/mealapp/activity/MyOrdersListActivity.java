package com.cm.mealapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cm.mealapp.adapter.OrdersAdapter;
import com.cm.mealapp.bean.orders;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MyOrdersListActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private List<orders> list;
	private OrdersAdapter adapter;
	private ListView listview1;
	private TextView tvTopTitleCenter;
	private final Gson gson = new Gson();
	private int row;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findview();
		query();
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("订单管理(" + user.getRole() + ")");
		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);

		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				showContactDialog();
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		query();
	}

	private void query() {
		mParamMaps.clear();
		mParamMaps.put("Action", "getmyorderslist");
		mParamMaps.put("typename", user.getRole());
		mParamMaps.put("userid", user.getId());

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {

				list = new ArrayList<orders>();
				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					list = gson.fromJson(result, new TypeToken<List<orders>>() {
					}.getType());
					adapter = new OrdersAdapter(MyOrdersListActivity.this, list);
					listview1.setAdapter(adapter);
				} else {
				}
			}

		});
	}

	// 弹出上下文菜单
	private String[] arg;

	private void showContactDialog() {
		if (user.getRole().equals("用户")) {
			arg = new String[] { "取消订单", "评价" };
		} else {
			arg = new String[] { "配送" };
		}

		new AlertDialog.Builder(this).setTitle("选择操作")
				.setItems(arg, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (arg[which].equals("取消订单")) {
							delete();
						} else if (arg[which].equals("配送")) {
							changestatus("已配送");
						} else if (arg[which].equals("评价")) {
							intent = new Intent(MyOrdersListActivity.this,
									CommentActivity.class);
							intent.putExtra("dishesid", list.get(row)
									.getDishesid());
							startActivity(intent);
						}

					}
				}).show();
	}

	private void delete() {
		showProgressDialog("处理中,请稍后..");
		buildDeleteMap("orders", list.get(row).getId());
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().length() > 0) {
					toastUtil.show("取消成功");
					list.remove(row);
					adapter.notifyDataSetChanged();
				} else {
					toastUtil.show("取消失败");

				}
			}

		});
	}

	private void changestatus(String status) {
		mParamMaps.clear();
		mParamMaps.put("Action", "ChangeStatus");
		mParamMaps.put("ID", list.get(row).getId());
		mParamMaps.put("status", status);
		showProgressDialog("处理中,请稍后..");
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().equals("1")) {
					toastUtil.show("操作成功");
					query();
				} else {

				}

			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleLeft:
			finish();
			break;
		default:
			break;
		}

	}

}
