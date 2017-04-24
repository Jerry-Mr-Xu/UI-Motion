package com.jerry.android.uimotion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jerry.android.uimotion.switcher.ShowCustomSwitcherActivity;
import com.jerry.android.uimotion.textview.ShowCustomTextViewActivity;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.btn_show_switcher:
				startActivity(new Intent(MainActivity.this, ShowCustomSwitcherActivity.class));
				break;

			case R.id.btn_show_text_view:
				startActivity(new Intent(MainActivity.this, ShowCustomTextViewActivity.class));
				break;

			default:
				break;
		}
	}
}
