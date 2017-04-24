package com.jerry.android.uimotion.textview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jerry.android.uimotion.R;

import java.util.Locale;

/**
 * 自定义TextView展示页面
 * <p>
 * Created by Jerry on 2017/4/18.
 */

public class ShowCustomTextViewActivity extends AppCompatActivity
{
	private ColorTrackTextView tvColorTrack;
	private SeekBar sbChangedProgress;
	private TextView tvChangedProgress;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_text_view);

		tvColorTrack = (ColorTrackTextView) findViewById(R.id.tv_color_track);
		sbChangedProgress = (SeekBar) findViewById(R.id.sb_changed_progress);
		tvChangedProgress = (TextView) findViewById(R.id.tv_changed_progress);
		sbChangedProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				tvColorTrack.setChangeProgress(progress / 100f);
				tvChangedProgress.setText(String.format(Locale.CHINA, "%d%%", progress));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}
		});
	}
}
