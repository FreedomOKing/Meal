package com.cm.mealapp.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cm.mealapp.adapter.BusinessListAdapter;
import com.cm.mealapp.bean.users;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.ActivityUtils;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class BusinessListActivity extends BaseActivity {
	private TextView tvTopTitleCenter, tvTopTitleLeft;
	private Button btnTopTitleRight;

	private List<users> list;
	private BusinessListAdapter adapter;
	private ListView listview1;
	private String keyword;
	private final Gson gson = new Gson();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findview();
		query();
	}

	private void findview() {
		tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
		tvTopTitleCenter.setText("收藏的商家");

		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				intent = new Intent(BusinessListActivity.this,
						DishesListActivity.class);
				intent.putExtra("businessid", list.get(position).getId());
				intent.putExtra("businessname", list.get(position).getName());
				startActivity(intent);

			}

		});
	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		mParamMaps.clear();
		mParamMaps.put("Action", "getuserslist");
		mParamMaps.put("iscollect", "1");
		mParamMaps.put("userid", user.getId());
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					list = gson.fromJson(result, new TypeToken<List<users>>() {
					}.getType());
					adapter = new BusinessListAdapter(
							BusinessListActivity.this, list);
					listview1.setAdapter(adapter);
				} else {
					toastUtil.show("没有数据");
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:
			break;
		case R.id.tvTopTitleLeft:
			ActivityUtils.startActivity(PersonCenterActivity.class);
			break;

		default:
			break;
		}

	}
}
