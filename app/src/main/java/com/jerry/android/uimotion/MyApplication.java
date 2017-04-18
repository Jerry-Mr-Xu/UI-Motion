package com.jerry.android.uimotion;

import android.app.Application;

/**
 * 自定义Application
 * <p>
 * Created by Jerry on 2017/4/18.
 */

public class MyApplication extends Application
{
	private static MyApplication singleton;

	public static MyApplication getInstance()
	{
		return singleton;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		singleton = this;
	}
}
