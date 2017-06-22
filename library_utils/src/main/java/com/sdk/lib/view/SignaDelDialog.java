package com.sdk.lib.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sdk.lib.util.SystemUtil;
import com.sdk.lib.util.Util;

public class SignaDelDialog extends Activity  {

	private TextView dialogTitle, dialogMessage;
	private Button dialogCancle, dialogOk;
	private String pkg;

	public static void actionSignaDelDialog(Context mContext, String pkg) {
		Intent iIntent = new Intent(mContext, SignaDelDialog.class);
		iIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		iIntent.putExtra("pkg", pkg);
		mContext.startActivity(iIntent);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		pkg = getIntent().getStringExtra("pkg");
		setContentView(Util.R_layout(this, "layout_util_dialog_signa"));

		dialogTitle = (TextView) findViewById(Util.R_id(this, "alertTitle"));
		dialogTitle.setText("安装包签名冲突");
		dialogTitle.setVisibility(View.VISIBLE);

		dialogMessage = (TextView) findViewById(Util.R_id(this, "message"));
		dialogMessage.setText("建议卸载原安装包，是否卸载？");
		dialogMessage.setVisibility(View.VISIBLE);

		dialogCancle = (Button) findViewById(Util.R_id(this, "button3"));
		dialogCancle.setText("取消");
		dialogCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		dialogOk = (Button) findViewById(Util.R_id(this, "button1"));
		dialogOk.setText("卸载");
		dialogOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SystemUtil.uninstallApp(SignaDelDialog.this, pkg);
				finish();
			}
		});
	}
}
