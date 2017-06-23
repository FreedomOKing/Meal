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
		tvTopTitleCenter.setText("��������(" + user.getRole() + ")");
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

	// ���������Ĳ˵�
	private String[] arg;

	private void showContactDialog() {
		if (user.getRole().equals("�û�")) {
			arg = new String[] { "ȡ������", "����" };
		} else {
			arg = new String[] { "����" };
		}

		new AlertDialog.Builder(this).setTitle("ѡ�����")
				.setItems(arg, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (arg[which].equals("ȡ������")) {
							delete();
						} else if (arg[which].equals("����")) {
							changestatus("������");
						} else if (arg[which].equals("����")) {
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
		showProgressDialog("������,���Ժ�..");
		buildDeleteMap("orders", list.get(row).getId());
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().length() > 0) {
					toastUtil.show("ȡ���ɹ�");
					list.remove(row);
					adapter.notifyDataSetChanged();
				} else {
					toastUtil.show("ȡ��ʧ��");

				}
			}

		});
	}

	private void changestatus(String status) {
		mParamMaps.clear();
		mParamMaps.put("Action", "ChangeStatus");
		mParamMaps.put("ID", list.get(row).getId());
		mParamMaps.put("status", status);
		showProgressDialog("������,���Ժ�..");
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (result != null && result.trim().equals("1")) {
					toastUtil.show("�����ɹ�");
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
