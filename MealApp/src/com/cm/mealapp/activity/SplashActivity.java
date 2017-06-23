package com.cm.mealapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cm.mealapp.activity.R;
import com.cm.utils.BaseActivity;

public class SplashActivity extends BaseActivity {
	private final long m_dwSplashTime = 500;
	private boolean m_bPaused = false;
	private boolean m_bSplashActive = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					long ms = 0;
					while (m_bSplashActive && ms < m_dwSplashTime) {
						Thread.sleep(1000);
						if (!m_bPaused)
							ms += 1000;
					}
					startActivity(new Intent(SplashActivity.this,
							LoginActivity.class));
				} catch (Exception ex) {

				} finally {
					finish();
				}
			}
		});
		thread.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		m_bPaused = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		m_bPaused = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			m_bSplashActive = false;
			break;
		case KeyEvent.KEYCODE_BACK:
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		default:
			break;
		}
		return true;
	}

}
