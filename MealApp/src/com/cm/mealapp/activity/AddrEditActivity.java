package com.cm.mealapp.activity;//����ջ���ַ

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
	//Gson �� Google �ṩ�������� Java ����� JSON ����֮�����ӳ��� Java ��⡣���Խ�һ�� JSON �ַ���ת��һ�� Java ���󣬻��߷�������
	private tb_addrs model;//���ݵ���

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		model = (tb_addrs) getIntent().getSerializableExtra("model");//Activity֮�䴫�������,���ղ���
		setContentView(R.layout.activity_editaddr);
		findview();

	}

	private void findview() {
		tvTopTitleCenter = (TextView) findViewById(R.id.tvTopTitleCenter);
		tvTopTitleCenter.setText("�ջ���ַ");
		btnTopTitleRight = (Button) findViewById(R.id.btnTopTitleRight);
		btnTopTitleRight.setVisibility(View.VISIBLE);
		btnTopTitleRight.setOnClickListener(this);
		btnTopTitleRight.setText("���");

		etPerson = (EditText) findViewById(R.id.etPerson);
		etPhone = (EditText) findViewById(R.id.etPhone);
		etBody = (EditText) findViewById(R.id.etBody);

		etPerson.setText(user.getName());//�û���

		if (model != null) {
			id = model.getId();
			etPhone.setText(model.getPhone());
			etPerson.setText(model.getPerson());
			etBody.setText(model.getAddr());
		}

	}

	private void submit() {//�Ƿ���û����д��Ŀ
		if (etPerson.getText().length() == 0) {
			toastUtil.show("��������ϵ��");
			return;
		}
		if (etPhone.getText().length() == 0) {
			toastUtil.show("��������ϵ�绰");
			return;
		}
		if (etBody.getText().length() == 0) {
			toastUtil.show("��������ϸ��ַ");
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
		showProgressDialog("�ύ��,���Ժ�..");
		AsyncRequestUtils.newInstance().post(mParamMaps, new AsyncListener() {
//servlet3.0�첽����
			@Override
			public void onResult(String result) {
				hideProgressDialog();//չʾ�����ڼ��أ����Ժ�.��
				if (!TextUtils.isEmpty(result) && result.trim().equals("1")) {
					toastUtil.show("�����ɹ�");
					setResult(RESULT_OK);
					finish();
				} else {
					toastUtil.show("�ύʧ��");
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

		case R.id.btnTopTitleRight:// ��ɰ�ť
			submit();
			break;
		default:
			break;
		}

	}

}
