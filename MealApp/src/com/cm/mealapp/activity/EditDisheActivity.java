package com.cm.mealapp.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cm.component.ChooseImageDialog;
import com.cm.component.ChooseImageDialog.OnResultListener;
import com.cm.mealapp.bean.dishes;
import com.cm.utils.AsyncImageLoader;
import com.cm.utils.BaseActivity;
import com.cm.utils.BaseUtil;
import com.cm.utils.UIUtils;
import com.google.gson.Gson;

public class EditDisheActivity extends BaseActivity {
	private String filepath = "";
	private Button btnOK;

	private EditText etTitle, etPrice, etAmount, etBody;

	private int id = 0;
	private final Gson gson = new Gson();
	private ImageView ivChooseImage;
	private LoadAsyncTask loginAsyncTask;
	private dishes model;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editdishe);
		model = (dishes) getIntent().getSerializableExtra("model");
		if (model != null) {
			id = model.getId();
		}
		findview();

	}

	private void findview() {
		((TextView) findViewById(R.id.tvTopTitleCenter)).setText("菜品信息");
		btnOK = (Button) findViewById(R.id.btnOK);
		etTitle = (EditText) findViewById(R.id.etTitle);
		etPrice = (EditText) findViewById(R.id.etPrice);
		etAmount = (EditText) findViewById(R.id.etAmount);
		etBody = (EditText) findViewById(R.id.etBody);
		ivChooseImage = (ImageView) findViewById(R.id.ivChooseImage);
		ivChooseImage.setOnClickListener(this);
		btnOK.setOnClickListener(new btnRegisterOnClickListener());

		if (model != null) {
			etAmount.setText(model.getAmount() + "");
			etBody.setText(model.getIntro());
			etPrice.setText(model.getPrice() + "");
			etTitle.setText(model.getTitle());
			if (!TextUtils.isEmpty(model.getImg_url())) {
				AsyncImageLoader.getInstance().loadBitmap(model.getImg_url(),
						ivChooseImage);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private class btnRegisterOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (etTitle.getText().length() == 0) {
				toastUtil.show("请输入标题");
				return;
			}

			if (etPrice.getText().length() == 0) {
				toastUtil.show("请输入价格");
				return;
			}
			if (etAmount.getText().length() == 0) {
				toastUtil.show("请输入数量");
				return;
			}

			if (filepath.length() == 0) {
				toastUtil.show("请选择图片");
				return;
			}

			BaseUtil.HideKeyboard(EditDisheActivity.this);
			loginAsyncTask = new LoadAsyncTask();
			loginAsyncTask.execute("");

		}
	};

	@SuppressWarnings("deprecation")
	private class LoadAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(EditDisheActivity.this);
			dialog.setTitle("提示");
			dialog.setMessage("处理中,请稍后..");
			dialog.setCancelable(true);
			dialog.setButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (loginAsyncTask != null) {
						loginAsyncTask.cancel(true);
						loginAsyncTask = null;
						toastUtil.show("操作被取消");
					}
				}
			});
			dialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			if (filepath.length() > 0) {
				filepath = httpHelper.uploadFile(
						AppConstant.getUrl(getApplicationContext())
								+ "UploadServlet", filepath);
			}
			String urlString = AppConstant.getUrl(getApplicationContext())
					+ "ServletService";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("Action", "edit");
			map.put("id", id);
			map.put("title", etTitle.getText());
			map.put("businessid", user.getId());
			map.put("price", etPrice.getText());
			map.put("amount", etAmount.getText());
			map.put("intro", etBody.getText());
			map.put("img_url", filepath);
			String result = httpHelper.HttpPost(urlString, map);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			loginAsyncTask = null;
			dialog.dismiss();
			if (!TextUtils.isEmpty(result)) {
				if (result.trim().equals("1")) {
					toastUtil.show("提交成功");
					setResult(RESULT_OK);
					finish();
				} else {
					toastUtil.show("提交失败");
				}
			} else {
				toastUtil.show("操作失败");
			}
		}
	}

	private class ChooseResultListener implements OnResultListener {
		@Override
		public void onResult(int mode, String path) {
			filepath = path;
			UIUtils.loadImageAvoidOOM(ivChooseImage, path);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			finish();
			break;

		case R.id.ivChooseImage:
			ChooseImageDialog.getInstance().show(EditDisheActivity.this,
					new ChooseResultListener());
			break;
		}
	}

}
