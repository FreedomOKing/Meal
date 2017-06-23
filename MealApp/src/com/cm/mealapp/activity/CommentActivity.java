package com.cm.mealapp.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cm.utils.BaseActivity;
import com.cm.utils.BaseUtil;

public class CommentActivity extends BaseActivity {

	private Button btnLogin;
	private EditText etBody;

	private loadAsyncTask loginAsyncTask;
	private int dishesid = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		dishesid = getIntent().getIntExtra("dishesid", 0);
		findview();
		setListener();

	}

	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("����");
		etBody = (EditText) findViewById(R.id.etBody);
		btnLogin = (Button) findViewById(R.id.btnLogin);

	}

	private void setListener() {
		btnLogin.setOnClickListener(new btnRegisterOnClickListener());
	}

	@SuppressWarnings("unchecked")
	private class btnRegisterOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (etBody.getText().length() == 0) {
				toastUtil.show("����������");
				return;
			}

			BaseUtil.HideKeyboard(CommentActivity.this);
			loginAsyncTask = new loadAsyncTask();
			loginAsyncTask.execute("");

		}
	};

	@SuppressWarnings("deprecation")
	private class loadAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(CommentActivity.this);
			dialog.setTitle("��ʾ");
			dialog.setMessage("������,���Ժ�..");
			dialog.setCancelable(true);
			dialog.setButton("ȡ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (loginAsyncTask != null) {
						loginAsyncTask.cancel(true);
						loginAsyncTask = null;
						toastUtil.show("������ȡ��");
					}
				}
			});
			dialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String urlString = AppConstant.getUrl(getApplicationContext()) + "ServletService";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Action", "createcomment");
			map.put("dishesid", dishesid);
			map.put("body", etBody.getText());
			map.put("userid", user.getId());
			map.put("username", user.getName());
			String result = httpHelper.HttpPost(urlString, map);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loginAsyncTask = null;
			dialog.dismiss();
			if (result != null && result.trim().equals("1")) {
				toastUtil.show("���۳ɹ�");
				setResult(1);
				finish();
			} else {
				toastUtil.show("����ʧ��");
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			finish();
			break;

		default:
			break;
		}
	}

}
