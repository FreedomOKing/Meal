package com.cm.mealapp.activity;//添加收货地址

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cm.mealapp.bean.tb_addrs;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.BaseActivity;
import com.google.gson.Gson;

public class AddrEditActivity extends BaseActivity {
	private TextView tvTopTitleCenter;
	private Button btnTopTitleRight;
	private EditText etPerson, etPhone, etBody;
	private int id = 0;
	private final Gson gson = new Gson();
	//Gson 是 Google 提供的用来在 Java 对象和 JSON 数据之间进行映射的 Java 类库。可以将一个 JSON 字符串转成一个 Java 对象，或者反过来。
	private tb_addrs model;//传递的类

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model = (tb_addrs) getIntent().getSerializableExtra("model");//Activity之间传递类对象,接收部分
		setContentView(R.layout.activity_editaddr);
		findview();

	}

	private void findview() {
		tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
		tvTopTitleCenter.setText("收货地址");
		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setVisibility(View.VISIBLE);
		btnTopTitleRight.setOnClickListener(this);
		btnTopTitleRight.setText("完成");

		etPerson = (EditText) findViewById(R.id.etPerson);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etBody = (EditText) findViewById(R.id.etBody);

		etPerson.setText(user.getName());//用户名

		if (model != null) {
			id = model.getId();
			etPhone.setText(model.getPhone());
			etPerson.setText(model.getPerson());
			etBody.setText(model.getAddr());
		}

	}

	private void submit() {//是否有没有填写项目
		if (etPerson.getText().length() == 0) {
			toastUtil.show("请输入联系人");
			return;
		}
		if (etPhone.getText().length() == 0) {
			toastUtil.show("请输入联系电话");
			return;
		}
		if (etBody.getText().length() == 0) {
			toastUtil.show("请输入详细地址");
			return;
		}

		mParamMaps.clear();
		mParamMaps.put("Action", "editaddr");
		mParamMaps.put("id", id);
		mParamMaps.put("userid", user.getId());
		mParamMaps.put("person", etPerson.getText());
		mParamMaps.put("phone", etPhone.getText());
		mParamMaps.put("addr", etBody.getText());
		if (model != null) {
			mParamMaps.put("isdefault", model.getIsdefault());
		} else {
			mParamMaps.put("isdefault", 0);
		}
		showProgressDialog("提交中,请稍后..");
		AsyncRequestUtils.newInstance().post(mParamMaps, new AsyncListener() {
//servlet3.0异步传送
			@Override
			public void onResult(String result) {
				hideProgressDialog();//展示“正在加载，请稍后.”
				if (!TextUtils.isEmpty(result) && result.trim().equals("1")) {
					toastUtil.show("操作成功");
					setResult(RESULT_OK);
					finish();
				} else {
					toastUtil.show("提交失败");
				}
			}
		});
	}

	@Override
	public void back(View view) {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnTopTitleRight:// 完成按钮
			submit();
			break;
		default:
			break;
		}

	}

}
