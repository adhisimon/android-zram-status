package id.co.ptskp.android.zramstatus;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class About extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        
        TextView appVersion = (TextView) findViewById(R.id.app_version);
        
        PackageManager manager = this.getPackageManager();
        try {
        	PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
        	int versionCode = info.versionCode;
        	String versionName = info.versionName;
        	
        	appVersion.setText(
        			getString(R.string.text_version)
        			+ " " + versionName 
        			+ "-" + Integer.toString(versionCode)
        		);
        } catch (NameNotFoundException e) {
        	
        }
        
        TextView appLicense = (TextView) findViewById(R.id.app_license);
        appLicense.setText(Html.fromHtml(getString(R.string.app_license)));
        
	}
}
