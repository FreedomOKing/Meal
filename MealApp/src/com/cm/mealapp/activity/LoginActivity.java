package com.cm.mealapp.activity;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.cm.network.AsyncRequestUtils;
import com.cm.network.AsyncRequestUtils.AsyncListener;
import com.cm.utils.ActivityUtils;
import com.cm.utils.BaseActivity;
import com.cm.utils.BaseUtil;
import com.cm.utils.OnLineUser;
import com.cm.utils.SPUtil;
import com.cm.utils.UIUtils;

public class LoginActivity extends BaseActivity {

	private TextView tvTopTitleCenter;

	private Button btnLogin, btnRegister;
	private EditText etLoginID, etPassword;
	private CheckBox ckbSavePwd;
	private Spinner spinner1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findview();
		etLoginID.setText("1001");
		etPassword.setText("123456");
		spinner1.setSelection(0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		application.setLoginUser(null);
	}

	private void findview() {
		tvTopTitleCenter = ((TextView) findViewById(R.id.tvTopTitleCenter));
		tvTopTitleCenter.setText("登录");

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		etLoginID = (EditText) findViewById(R.id.etLoginID);
		etPassword = (EditText) findViewById(R.id.etPassword);
		ckbSavePwd = (CheckBox) findViewById(R.id.ckbSavePwd);

		etLoginID.setText(SPUtil.get(LoginActivity.this, "loginid", ""));
		etPassword.setText(SPUtil.get(LoginActivity.this, "password", ""));
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		UIUtils.bindSpinner(this, spinner1, new String[] { "用户", "商家" });
	}

	private void login() {
		if (etLoginID.getText().length() == 0) {
			toastUtil.show("请输入账号");
			return;
		}
		if (etPassword.getText().length() == 0) {
			toastUtil.show("请输入密码");
			return;
		}

		BaseUtil.HideKeyboard(LoginActivity.this);
		mParamMaps.clear();
		mParamMaps.put("Action", "login");
		mParamMaps.put("loginid", etLoginID.getText());
		mParamMaps.put("passwords", etPassword.getText());
		mParamMaps.put("typename", spinner1.getSelectedItem().toString());
		showProgressDialog("登录中,请稍后..");
		AsyncRequestUtils.newInstance().get(mParamMaps, new AsyncListener() {
			@Override
			public void onResult(String result) {
				analyzeResult(result);
			}
		});
	}

	private void analyzeResult(String result) {
		hideProgressDialog();

		if (result == null || result.trim().length() == 0) {
			toastUtil.show("登录失败");
			return;
		}

		SPUtil.set(LoginActivity.this, "loginid", etLoginID.getText()
				.toString());
		if (ckbSavePwd.isChecked()) {
			SPUtil.set(LoginActivity.this, "password", etPassword.getText()
					.toString());
		} else {
			SPUtil.set(LoginActivity.this, "password", "");
		}
		try {
			jsonArray = new JSONArray(result);
			jsonObject = jsonArray.getJSONObject(0);
			// 保存登录用户信息
			CommonApplication application = (CommonApplication) getApplicationContext();
			OnLineUser model = new OnLineUser();
			model.setId(jsonObject.getInt("id"));
			model.setLoginid(etLoginID.getText().toString());
			model.setName(jsonObject.getString("name"));
			model.setRole(spinner1.getSelectedItem().toString());
			application.setLoginUser(model);
			toastUtil.show(model.getName() + ",登录成功");
			intent = new Intent(LoginActivity.this, GroupMainActivity.class);
			startActivity(intent);
			LoginActivity.this.finish();
		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			login();
			break;
		case R.id.btnRegister:
			ActivityUtils.startActivity(RegisterActivity.class);
			break;

		default:
			break;
		}
	}

}
