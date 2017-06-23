package com.cm.mealapp.activity;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cm.mealapp.adapter.CommentAdapter;
import com.cm.mealapp.bean.comments;
import com.cm.mealapp.bean.dishes;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.AsyncImageLoader;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class DishesDetailActivity extends BaseActivity {
	private int id = 0;
	private ImageView imageView1;

	private TextView tvIntro;

	private Button btnTopTitleRight, btnTopTitleLeft;
	private ListView listview1;
	private CommentAdapter adapter;
	private Button btnAddCar;
	private dishes model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dishedetail);
		model = (dishes) getIntent().getSerializableExtra("model");
		id = model.getId();
		findview();
	}

	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("����");
		imageView1 = (ImageView) findViewById(R.id.imageView1);

		tvIntro = (TextView) findViewById(R.id.tvIntro);

		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setText("����");
		btnTopTitleRight.setVisibility(View.VISIBLE);
		btnTopTitleRight.setOnClickListener(this);

		btnAddCar = (Button) findViewById(R.id.btnAddCar);
		btnAddCar.setOnClickListener(this);
		listview1 = (ListView) findViewById(R.id.listview1);

		AsyncImageLoader.getInstance().loadBitmap(model.getImg_url(),
				imageView1);
		String intro = "����:" + model.getTitle() + "\n";
		intro += "�̼�:" + model.getBusinessname() + "\n";
		intro += "����:��" + model.getPrice() + "\n";
		intro += "����:" + model.getAmount() + "\n";
		intro += "���:" + model.getIntro() + "\n";
		intro += "����:";
		tvIntro.setText(intro);

		getComment();

		if (user.getRole().equals("�̼�")) {
			btnTopTitleRight.setVisibility(View.GONE);
			btnAddCar.setVisibility(View.GONE);
		}
	}

	private void submit() {
		mParamMaps.clear();
		mParamMaps.put("Action", "collect");
		mParamMaps.put("proid", model.getBusinessid());
		mParamMaps.put("userid", user.getId());
		dialog = ProgressDialog.show(this, "��ʾ", "������,���Ժ�..");
		AsyncRequestUtils.newInstance().post(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				if (result.trim().equals("1")) {
					toastUtil.show("�ղ��̼ҳɹ�");
				} else if (result.trim().equals("-1")) {
					toastUtil.show("���̼��Ѿ��ղع�,�����ظ��ղ�");
				} else {
					toastUtil.show("�ղ�ʧ��");
				}

			}

		});
	}

	private void getComment() {
		mParamMaps.clear();
		mParamMaps.put("Action", "getComment");
		mParamMaps.put("dishesid", id);

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();
				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					List<comments> list = new Gson().fromJson(result,
							new TypeToken<List<comments>>() {
							}.getType());
					adapter = new CommentAdapter(DishesDetailActivity.this,
							list);
					listview1.setAdapter(adapter);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopTitleRight:
			intent = new Intent(DishesDetailActivity.this,
					CreateOrderActivity.class);
			intent.putExtra("id", id);
			startActivityForResult(intent, 1);
			break;
		case R.id.btnAddCar:
			submit();
			break;
		default:
			break;
		}

	}

}
