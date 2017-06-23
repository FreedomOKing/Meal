package com.cm.mealapp.activity;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cm.mealapp.bean.users;
import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.BaseActivity;
import com.cm.utils.BaseUtil;
import com.cm.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RegisterActivity extends BaseActivity {

	private TextView tvTopTitleCenter;

	private Button btnLogin, btnRegister;
	private EditText etLoginID, etPassword, etPasswordOK, etName;
	private EditText etPhone, etAddr;
	private final Gson gson = new Gson();
	private int id = 0;
	private Spinner spinner1, spinner2;
	private LinearLayout llEnable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findview();
		if (user != null) {
			id = user.getId();
			if (user.getRole().equals("用户")) {
				tvTopTitleCenter.setText("个人信息");
			} else {
				tvTopTitleCenter.setText("店铺信息");
				llEnable.setVisibility(View.VISIBLE);
			}

			etLoginID.setText(user.getLoginid());
			etName.setText(user.getName());
			btnRegister.setText("修改");
			btnLogin.setVisibility(View.GONE);
			spinner1.setEnabled(false);
			query();
		}

	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("注册");
		llEnable = (LinearLayout) findViewById(R.id.llEnable);
		llEnable.setVisibility(View.GONE);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		etLoginID = (EditText) findViewById(R.id.etLoginID);
		etPassword = (EditText) findViewById(R.id.etPassword);

		etPasswordOK = (EditText) findViewById(R.id.etPasswordOK);
		etName = (EditText) findViewById(R.id.etName);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etAddr = (EditText) findViewById(R.id.etAddr);

		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);

		spinner1 = (Spinner) findViewById(R.id.spinner1);
		UIUtils.bindSpinner(this, spinner1, new String[] { "用户", "商家" });

		spinner2 = (Spinner) findViewById(R.id.spinner2);
		UIUtils.bindSpinner(this, spinner2, new String[] { "营业", "非营业" });

	}

	private void query() {
		showProgressDialog("获取中,请稍后..");
		buildGetOneRowMap("users", user.getId());

		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (!TextUtils.isEmpty(result) && result.trim().length() > 0) {
					List<users> list = gson.fromJson(result,
							new TypeToken<List<users>>() {
							}.getType());
					if (list != null && list.size() > 0) {
						etLoginID.setText(list.get(0).getLoginid());
						etPassword.setText(list.get(0).getPasswords());
						etPasswordOK.setText(list.get(0).getPasswords());
						etName.setText(list.get(0).getName());
						etPhone.setText(list.get(0).getPhone());
						etAddr.setText(list.get(0).getAddr());
						if (list.get(0).getTypename().equals("商家")) {
							spinner1.setSelection(1);
						}

						if (list.get(0).getIsenable() == 0) {
							spinner2.setSelection(1);
						}
					}
				}
			}

		});
	}

	private void register() {
		if (etLoginID.getText().length() == 0) {
			toastUtil.show("请输入账号");
			return;
		}

		if (etName.getText().length() == 0) {
			toastUtil.show("请输入姓名");
			return;
		}
		if (etPassword.getText().length() == 0) {
			toastUtil.show("请输入密码");
			return;
		}

		if (etPasswordOK.getText().length() == 0) {
			toastUtil.show("请再次输入密码");
			return;
		}
		if (!etPassword.getText().toString()
				.equals(etPasswordOK.getText().toString())) {
			toastUtil.show("两次输入的密码不一致");
			return;
		}

		BaseUtil.HideKeyboard(this);

		mParamMaps.clear();
		mParamMaps.put("Action", "register");
		mParamMaps.put("id", id);
		mParamMaps.put("loginid", etLoginID.getText());
		mParamMaps.put("password", etPassword.getText());
		mParamMaps.put("name", etName.getText());
		mParamMaps.put("phone", etPhone.getText());
		mParamMaps.put("addr", etAddr.getText());

		mParamMaps.put("typename", spinner1.getSelectedItem().toString());
		mParamMaps.put("isenable", spinner2.getSelectedItemPosition() == 0 ? 1
				: 0);
		showProgressDialog("处理中,请稍后..");
		AsyncRequestUtils.newInstance().post(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				hideProgressDialog();

				if (result != null && result.trim().equals("1")) {
					if (id == 0) {
						toastUtil.show("注册成功");
					} else {
						toastUtil.show("修改成功");
					}
					finish();
				} else {
					toastUtil.show("注册失败");
				}

			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRegister:
			register();
			break;
		case R.id.btnLogin:
			finish();
			break;

		default:
			break;
		}
	}

}
