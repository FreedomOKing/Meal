package com.cm.mealapp.activity;

import com.cm.network.NetworkConfig;
import com.cm.utils.BaseApplication;

public class CommonApplication extends BaseApplication {

	private boolean isRefreshComment;

	@Override
	public void onCreate() {
		super.onCreate();
		NetworkConfig.setConfig(new AppConstant());
	}

	public boolean isRefreshComment() {
		return isRefreshComment;
	}

	public void setRefreshComment(boolean isRefreshComment) {
		this.isRefreshComment = isRefreshComment;
	}

}
