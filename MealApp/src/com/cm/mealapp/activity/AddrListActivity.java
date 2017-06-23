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

import com.cm.mealapp.adapter.AddrAdapter;
import com.cm.mealapp.bean.tb_addrs;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AddrListActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private AddrAdapter adapter;
	private ListView listview1;
	private TextView tvTopTitleCenter;
	private final Gson gson = new Gson();
	private List<tb_addrs> list;
	private int row = 0;
	private int type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		type = getIntent().getIntExtra("type", 1);
		findview();
		query();
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("收货地址管理");

		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setText("添加");
		btnTopTitleRight.setVisibility(View.VISIBLE);

		btnTopTitleRight.setOnClickListener(this);
		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				if (type == 1) {
					showEditDialog();
				} else {
					intent = new Intent();
					intent.putExtra("phone", list.get(row).getPhone());
					intent.putExtra("addr", list.get(row).getAddr());
					setResult(RESULT_OK, intent);
					finish();
				}

			}
		});
	}

	private void query() {
		mParamMaps.clear();
		mParamMaps.put("Action", "getaddrlist");
		mParamMaps.put("userid", user.getId());

		showProgressDialog("获取中..");
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					list = gson.fromJson(result,
							new TypeToken<List<tb_addrs>>() {
							}.getType());

				} else {
					list = new ArrayList<tb_addrs>();
				}
				adapter = new AddrAdapter(AddrListActivity.this, list);
				listview1.setAdapter(adapter);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (requestCode == 1 && resultCode == RESULT_OK) {
			query();
		}
	}

	private String[] arg;

	private void showEditDialog() {
		arg = new String[] { "删除", "修改" };

		new AlertDialog.Builder(this).setTitle("选择操作")
				.setItems(arg, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (arg[which].equals("删除")) {
							delete();
						}
						if (arg[which].equals("修改")) {
							intent = new Intent(AddrListActivity.this,
									AddrEditActivity.class);
							intent.putExtra("model", list.get(row));
							startActivityForResult(intent, 1);
						}

					}
				}).show();
	}

	private void delete() {
		mParamMaps.clear();
		mParamMaps.put("Action", "Del");
		mParamMaps.put("Table", "tb_addrs");
		mParamMaps.put("ID", list.get(row).getId());
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

	private void setdefault() {
		mParamMaps.clear();
		mParamMaps.put("Action", "setdefault");
		mParamMaps.put("userid", user.getId());
		mParamMaps.put("id", list.get(row).getId());
		showProgressDialog("处理中..");
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().length() > 0) {
					toastUtil.show("设置成功");
					query();
				} else {
					toastUtil.show("设置失败");

				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:
			intent = new Intent(AddrListActivity.this, AddrEditActivity.class);
			startActivityForResult(intent, 1);
			break;

		}

	}

}
