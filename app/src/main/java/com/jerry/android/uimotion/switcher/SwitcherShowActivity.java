package com.jerry.android.uimotion.switcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.jerry.android.uimotion.R;
import com.jerry.android.uimotion.utils.ToastUtil;

/**
 * 展示开关的界面
 * <p>
 * Created by Jerry on 2017/4/18.
 */

public class SwitcherShowActivity extends AppCompatActivity implements SwitcherView.OnCheckedStateChangedListener
{
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_switcher);

		((SwitcherView) findViewById(R.id.sv_switcher)).setOnCheckedStateChangedListener(this);
	}

	@Override
	public void onStateChanged(boolean isLeftChecked, String checkedStr)
	{
		ToastUtil.showToast(checkedStr, Toast.LENGTH_SHORT);
	}
}
