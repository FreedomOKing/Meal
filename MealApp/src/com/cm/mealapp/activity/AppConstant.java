package com.cm.mealapp.activity;

import android.content.Context;

import com.cm.network.AbstractNetworkUrl;
import com.cm.utils.SystemUtils;

public class AppConstant extends AbstractNetworkUrl {

	private static String WebServiceName = "MealAppService";

	
	private final static String phoneIP = "192.168.1.103:8080";

	
	private final static String emulatorIP = "10.0.2.2:8080";

	private static String getRootIPHost() {
		if (SystemUtils.isEmulator()) {
			return emulatorIP;
		} else {
			return phoneIP;
		}
	}

	public static String getRootUrl(Context context) {
		return "http://" + getRootIPHost() + "/" + WebServiceName + "/";
	}

	public static String getUrl(Context context) {
		return getRootUrl(context) + "servlet/";
	}

	@Override
	public String getIPHost() {
		return getRootIPHost();
	}

	@Override
	public String getWebServiceName() {
		return WebServiceName;
	}

	@Override
	public String getServiceServletName() {
		return null;
	}

	@Override
	public String getUploadServletName() {
		return null;
	}

}
