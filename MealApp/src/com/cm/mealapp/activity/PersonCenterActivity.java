package com.cm.mealapp.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cm.mealapp.adapter.PersonCenterListAdapter;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;


public class PersonCenterActivity extends BaseActivity {
	private Button btnTopTitleLeft, btnTopTitleRight;
	private PersonCenterListAdapter adapter;
	private ListView listview1;
	private TextView tvTopTitleCenter;
	private final Gson gson = new Gson();
	private List<String> list;
	private int row = 0;
	private final int type = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		findview();
		init();
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("����");
		btnTopTitleLeft = (Button) findViewById(R.id.btnTopTitleLeft);
		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setOnClickListener(this);
		// btnTopTitleRight.setText("ɸѡ");
		// btnTopTitleRight.setVisibility(View.VISIBLE);
		listview1 = (ListView) findViewById(R.id.listview1);
		listview1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				intent = null;
				if (list.get(row).equals("�ջ���ַ")) {
					intent = new Intent(PersonCenterActivity.this,
							AddrListActivity.class);

				}
				if (list.get(row).equals("�ղص��̼�")) {
					intent = new Intent(PersonCenterActivity.this,
							BusinessListActivity.class);

				}
				if (list.get(row).equals("��Ӳ�Ʒ")) {
					intent = new Intent(PersonCenterActivity.this,
							EditDisheActivity.class);

				}
				if (list.get(row).equals("�ҵĲ�Ʒ")) {
					intent = new Intent(PersonCenterActivity.this,
							MyDishesListActivity.class);

				}

				if (list.get(row).equals("������Ϣ")
						|| list.get(row).equals("������Ϣ")) {
					intent = new Intent(PersonCenterActivity.this,
							RegisterActivity.class);

				}

				if (intent != null) {
					startActivity(intent);
				}

			}

		});
		listview1.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				row = position;
				return true;
			}
		});
	}

	private void init() {
		list = new ArrayList<String>();
		if (user.getRole().equals("�û�")) {
			list.add("�ջ���ַ");
			list.add("������Ϣ");
			list.add("�ղص��̼�");
		} else {
			list.add("������Ϣ");
			list.add("�ҵĲ�Ʒ");
			list.add("��Ӳ�Ʒ");
		}

		adapter = new PersonCenterListAdapter(this, list);
		listview1.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:

			break;

		}

	}

}
