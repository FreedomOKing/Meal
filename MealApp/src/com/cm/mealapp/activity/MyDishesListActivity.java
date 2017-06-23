package com.cm.mealapp.activity;

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

import com.cm.mealapp.adapter.DishesAdapter;
import com.cm.mealapp.bean.dishes;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MyDishesListActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private List<dishes> list;
	private DishesAdapter adapter;
	private ListView listview1;
	private TextView tvTopTitleCenter;
	private final String keyword = "";
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
		tvTopTitleCenter.setText("我的菜品");

		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				showMenuDialog();

			}
		});
	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		mParamMaps.clear();
		mParamMaps.put("Action", "getdisheslist");
		mParamMaps.put("businessid", user.getId());

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					list = gson.fromJson(result, new TypeToken<List<dishes>>() {
					}.getType());
					adapter = new DishesAdapter(MyDishesListActivity.this, list);
					listview1.setAdapter(adapter);
				} else {
					toastUtil.show("没有数据");
				}
			}

		});
	}

	private void showMenuDialog() {
		final String[] arg = new String[] { "修改", "删除" };
		new AlertDialog.Builder(this).setTitle("选择操作")
				.setItems(arg, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (arg[which].equals("删除")) {
							delete();
						}
						if (arg[which].equals("修改")) {
							intent = new Intent(MyDishesListActivity.this,
									EditDisheActivity.class);
							intent.putExtra("model", list.get(row));
							startActivityForResult(intent, 1);
						}
					}
				}).show();
	}

	private void delete() {
		buildDeleteMap("dishes", list.get(row).getId());
		showProgressDialog("处理中..");
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().length() > 0) {
					toastUtil.show("删除成功");
					list.remove(row);
					adapter.notifyDataSetChanged();
				} else {
					toastUtil.show("删除失败");
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			query();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:

			break;
		case R.id.btnTopTitleLeft:
			break;

		default:
			break;
		}

	}

}
