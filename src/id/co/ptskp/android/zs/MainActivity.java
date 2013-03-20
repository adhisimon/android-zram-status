package id.co.ptskp.android.zs;

import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Zram zram;
	private Timer recalculateTimer;
	private final int recalculateTimerInterval = 5 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        zram = new Zram();
        
        updateDeviceInfo();
        recalculateTimerSchedule();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.action_reload:
    		recalculate();
    		return true;
    	case R.id.action_about:
    		Intent aboutIntent = new Intent(this, About.class);
    		startActivity(aboutIntent);
    		return true;
    	case R.id.action_share:
    		Intent shareIntent = new Intent();
    		shareIntent.setAction(Intent.ACTION_SEND);
    		shareIntent.putExtra(
    				Intent.EXTRA_TEXT, 
    				"Check out \"" 
    				+ getString(R.string.app_name) 
    				+ "\" - https://play.google.com/store/apps/details?id=id.co.ptskp.android.zs"
    			);
    		shareIntent.setType("text/plain");
    		startActivity(shareIntent);
    		return true;
    	case R.id.action_exit:
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    
    private void recalculateTimerSchedule() {
    	recalculateTimer = new Timer();
    	recalculateTimer.schedule(
    			new TimerTask() {
					
					@Override
					public void run() {
						recalculateTimerMethod();
					}
				}, 
    			0, recalculateTimerInterval);
    }
    
    private void recalculateTimerMethod() {
    	runOnUiThread(RecalculateTimerTick);
    }
    
    private Runnable RecalculateTimerTick = new Runnable() {
		
		@Override
		public void run() {
			recalculate();
		}
	};
    
    private void recalculate() {
    	zram.clearCache();
    	
    	try {
    		NumberFormat nf = NumberFormat.getNumberInstance();
    		
    		TextView tvZramDiskSize = (TextView) findViewById(R.id.zram_disk_size);
    		tvZramDiskSize.setText(
        			getResources().getString(R.string.zram_disk_size) 
        			+ " "
        			+ nf.format(zram.getDiskSize())
        			+ " bytes"
    			);
    		
    		TextView tvZramCompressedDataSize = (TextView) findViewById(R.id.zram_compressed_data_size);
    		tvZramCompressedDataSize.setText(
        			getResources().getString(R.string.zram_compressed_data_size) 
        			+ " "
        			+ nf.format(zram.getCompressedDataSize())
        			+ " bytes"
    			);
    		
    		TextView tvZramOriginalDataSize = (TextView) findViewById(R.id.zram_original_data_size);
    		tvZramOriginalDataSize.setText(
        			getResources().getString(R.string.zram_original_data_size) 
        			+ " "
        			+ nf.format(zram.getOriginalDataSize())
        			+ " bytes"
    			);
    		
    		TextView tvZramMemUsedTotal = (TextView) findViewById(R.id.zram_mem_used_total);
    		tvZramMemUsedTotal.setText(
        			getResources().getString(R.string.zram_mem_used_total) 
        			+ " "
        			+ nf.format(zram.getMemUsedTotal())
        			+ " bytes"
    			);
    		
    		final int compressionRatio = Math.round(zram.getCompressionRatio() * 100);
    		ProgressBar pbZramCompressionRatio = (ProgressBar) findViewById(R.id.zram_compression_ratio_bar);
    		pbZramCompressionRatio.setProgress(compressionRatio);
    		
    		TextView tvZramCompressionRatio = (TextView) findViewById(R.id.zram_compression_ratio);
    		tvZramCompressionRatio.setText(
        			getResources().getString(R.string.zram_compression_ratio) 
        			+ " "
        			+ Integer.toString(compressionRatio)
        			+ "%"
    			);
    		
    		final int usedRatio = Math.round(zram.getUsedRatio() * 100);
    		ProgressBar pbZramUsedRatio = (ProgressBar) findViewById(R.id.zram_used_ratio_bar);
    		pbZramUsedRatio.setProgress(usedRatio);
    		
    		TextView tvZramUsedRatio = (TextView) findViewById(R.id.zram_used_ratio);
    		tvZramUsedRatio.setText(
        			getResources().getString(R.string.zram_used_ratio) 
        			+ " "
        			+ Integer.toString(usedRatio)
        			+ "%"
    			);
    		
    		MemoryInfo mi = new MemoryInfo();
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            
            //long memoryTotal = mi.totalMem;
            long memoryAvail = mi.availMem;
            //long memoryUsed = memoryTotal - memoryAvail;

            TextView tvRamAvailable = (TextView) findViewById(R.id.ram_available);
            tvRamAvailable.setText(
            		getString(R.string.ram_available)
            		+ " " + nf.format(memoryAvail)
            		+ " bytes"
            	);
            
    	} catch (Exception e) {
    		Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    		finish();
    	}
    	
    }
    
    private void updateDeviceInfo() {
    	TextView tvDeviceInfo = (TextView) findViewById(R.id.device_info);
        tvDeviceInfo.setText(
        		zram.getDeviceName()
        		+ " - " + Build.DISPLAY
        	);
         
        TextView tvKernelVersion = (TextView) findViewById(R.id.kernel_version);
        tvKernelVersion.setText(
        		getString(R.string.text_kernel)
        		+ " " + zram.getKernelVersion()
        	);
    }
    
}
