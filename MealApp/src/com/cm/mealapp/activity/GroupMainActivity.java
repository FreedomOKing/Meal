package com.cm.mealapp.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.cm.utils.OnLineUser;
import com.cm.utils.ToastUtil;

@SuppressWarnings("deprecation")
public class GroupMainActivity extends ActivityGroup {
	private FrameLayout containerBody = null;
	private Button btnModule1, btnModule2, btnModule3;

	private ToastUtil toastUtil;
	private RelativeLayout rlRoot;
	private OnLineUser user;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_group);
		user = ((CommonApplication) getApplicationContext()).getOnlineUser();
		toastUtil = new ToastUtil(GroupMainActivity.this);
		containerBody = (FrameLayout) findViewById(R.id.containerBody);
		btnModule1 = (Button) findViewById(R.id.btnModule1);
		btnModule2 = (Button) findViewById(R.id.btnModule2);
		btnModule3 = (Button) findViewById(R.id.btnModule3);
		btnModule1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				changeBottomiButton(0);
				containerBody.removeAllViews();
				containerBody.addView(getLocalActivityManager().startActivity(
						"Module1",
						new Intent(GroupMainActivity.this,
								DishesListActivity.class)).getDecorView());

			}
		});
		btnModule2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeBottomiButton(1);
				containerBody.removeAllViews();
				Intent intent = new Intent(GroupMainActivity.this,
						MyOrdersListActivity.class);
				containerBody.addView(getLocalActivityManager().startActivity(
						"Module2", intent).getDecorView());
			}
		});
		btnModule3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeBottomiButton(2);
				containerBody.removeAllViews();
				Intent intent = new Intent(GroupMainActivity.this,
						PersonCenterActivity.class);
				containerBody.addView(getLocalActivityManager().startActivity(
						"Module3", intent).getDecorView());
			}
		});

		btnModule1.performClick();

	}

	protected void changeBottomiButton(int index) {
		switch (index) {
		case 0:
			btnModule1.setBackgroundColor(getResources().getColor(R.color.sel));
			btnModule2.setBackgroundColor(getResources().getColor(
					R.color.no_sel));
			btnModule3.setBackgroundColor(getResources().getColor(
					R.color.no_sel));

			break;
		case 1:
			btnModule1.setBackgroundColor(getResources().getColor(
					R.color.no_sel));
			btnModule2.setBackgroundColor(getResources().getColor(R.color.sel));
			btnModule3.setBackgroundColor(getResources().getColor(
					R.color.no_sel));

			break;
		case 2:
			btnModule1.setBackgroundColor(getResources().getColor(
					R.color.no_sel));
			btnModule2.setBackgroundColor(getResources().getColor(
					R.color.no_sel));
			btnModule3.setBackgroundColor(getResources().getColor(R.color.sel));

			break;
		case 3:
			btnModule1.setBackgroundColor(getResources().getColor(
					R.color.no_sel));
			btnModule2.setBackgroundColor(getResources().getColor(
					R.color.no_sel));
			btnModule3.setBackgroundColor(getResources().getColor(
					R.color.no_sel));

			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			this.getLocalActivityManager().getCurrentActivity()
					.openOptionsMenu();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

}
