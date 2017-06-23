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
import android.widget.EditText;
import android.widget.TextView;

import com.cm.mealapp.adapter.DishesAdapter;
import com.cm.mealapp.bean.dishes;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.BaseActivity;
import com.cm.view.PullToRefreshListView;
import com.cm.view.PullToRefreshListView.Mode;
import com.cm.view.PullToRefreshListView.OnLoadMoreListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class DishesListActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private final List<dishes> list = new ArrayList<dishes>();
	private DishesAdapter adapter;
	private PullToRefreshListView listview1;
	private TextView tvTopTitleCenter;
	private String keyword = "";
	private int businessid;
	private String businessname;
	private final Gson gson = new Gson();
	private int index = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		businessid = getIntent().getIntExtra("businessid", 0);
		businessname = getIntent().getStringExtra("businessname");
		findview();
		query();
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("首页");
		if (!TextUtils.isEmpty(businessname)) {
			tvTopTitleCenter.setText(businessname);
		}
		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
		btnTopTitleLeft.setVisibility(View.VISIBLE);
		btnTopTitleLeft.setOnClickListener(this);
		btnTopTitleLeft.setText("搜索");

		listview1 = (PullToRefreshListView) findViewById(R.id.listview1);
		listview1.setMode(Mode.PULL_FROM_END);
		if (!TextUtils.isEmpty(businessname)) {
			listview1.setMode(Mode.DISABLED);
		}
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(DishesListActivity.this,
						DishesDetailActivity.class);
				intent.putExtra("model", list.get(position - 1));
				startActivity(intent);

			}
		});

		listview1.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				index += 10;
				query();
			}
		});
	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		mParamMaps.clear();
		mParamMaps.put("Action", "getdisheslist");
		mParamMaps.put("index", index);
		mParamMaps.put("keyword", keyword);
		if (businessid > 0) {
			mParamMaps.put("businessid", businessid);
		}

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				listview1.setIsLoading(false);

				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					List<dishes> list1 = gson.fromJson(result,
							new TypeToken<List<dishes>>() {
							}.getType());
					list.addAll(list1);
					adapter = new DishesAdapter(DishesListActivity.this, list);
					listview1.setAdapter(adapter);
				} else {
					listview1.setHasMore(false);
				}
			}

		});
	}

	private void dialog() {
		final EditText eText = new EditText(this);
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("搜索")
				.setIcon(android.R.drawable.ic_dialog_info).setView(eText)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						index = 0;
						listview1.setHasMore(true);
						list.clear();
						keyword = eText.getText().toString();
						query();
					}

				}).setNegativeButton("取消", null).create();
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:

			break;
		case R.id.btnTopTitleLeft:
			dialog();
			break;

		default:
			break;
		}

	}

}
