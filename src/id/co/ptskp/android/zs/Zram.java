package id.co.ptskp.android.zs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import android.os.Build;

public class Zram {
	
	private final String ZRAMSTATFILE_DISKSIZE = "/sys/block/zram%d/disksize";
	private final String ZRAMSTATFILE_COMPRESSED_DATA_SIZE = "/sys/block/zram%d/compr_data_size";
	private final String ZRAMSTATFILE_ORIGINAL_DATA_SIZE = "/sys/block/zram%d/orig_data_size";
	private final String ZRAMSTATFILE_MEM_USED_TOTAL = "/sys/block/zram%d/mem_used_total";
	
	private int _diskSize = 0;
	private int _compressedDataSize = 0;
	private int _originalDataSize = 0;
	private int _memUsedTotal = 0;
	private int _numberOfFiles = 0;
	private boolean _valuesLoaded = false;
	
	/**
	 * Zram constructor 
	 */
	public Zram() {
		super();
		clearCache();
	}

	/**
	 * Read a file an return content of that file as a string
	 * 
	 * Copied from http://stackoverflow.com/a/4867192
	 * 
	 * @param filePath path of file
	 * @return file content as a string 
	 * @throws java.io.IOException
	 */
	private static String readFileAsString(String filePath) throws java.io.IOException {
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    String line, results = "";
	    while( ( line = reader.readLine() ) != null)
	    {
	        results += line;
	    }
	    reader.close();
	    return results;
	}
	
	public void clearCache() {
		_compressedDataSize = 0;
		_diskSize = 0;
		_memUsedTotal = 0;
		_originalDataSize = 0;
		_numberOfFiles = 0;
		_valuesLoaded = false;
	}

	/**
	 * Loads values in the cache
	 * @throws Exception
	 */
	private void _loadValues() throws Exception {
		_compressedDataSize = 0;
		_diskSize = 0;
		_memUsedTotal = 0;
		_originalDataSize = 0;
		_numberOfFiles = 0;
		while(new File(String.format(ZRAMSTATFILE_DISKSIZE, _numberOfFiles)).exists()) {
			_compressedDataSize += Integer.parseInt(readFileAsString(String.format(ZRAMSTATFILE_COMPRESSED_DATA_SIZE, _numberOfFiles)));
			_diskSize += Integer.parseInt(readFileAsString(String.format(ZRAMSTATFILE_DISKSIZE, _numberOfFiles)));
			_memUsedTotal += Integer.parseInt(readFileAsString(String.format(ZRAMSTATFILE_MEM_USED_TOTAL, _numberOfFiles)));
			_originalDataSize += Integer.parseInt(readFileAsString(String.format(ZRAMSTATFILE_ORIGINAL_DATA_SIZE, _numberOfFiles)));
			_numberOfFiles++;
		}
		_valuesLoaded = true;
	}

	
	/**
	 * Get ZRAM disk size
	 * @return ZRAM disk size
	 * @throws Exception
	 */
	public int getDiskSize() throws Exception {
		if (!_valuesLoaded) {
			_loadValues();
		}
		return _diskSize;
	}
	
	public int getCompressedDataSize() throws Exception {
		if (!_valuesLoaded) {
			_loadValues();
		}
		return _compressedDataSize;
	}
	
	public int getOriginalDataSize() throws Exception {
		if (!_valuesLoaded) {
			_loadValues();
		}
		return _originalDataSize;
	}
		
	public int getMemUsedTotal() throws Exception {
		if (!_valuesLoaded) {
			_loadValues();
		}
		return _memUsedTotal;
	}

	public int getNumberOfFiles() throws Exception {
		if (!_valuesLoaded) {
			_loadValues();
		}
		return _numberOfFiles;
	}

	public float getCompressionRatio() throws Exception {
		return (float) getCompressedDataSize() / (float) getOriginalDataSize();
	}
	
	public float getUsedRatio() throws Exception {
		return (float) getOriginalDataSize() / (float) getDiskSize();
 	}
	
	public String getKernelVersionFromSystemProperty() {
		return System.getProperty("os.version");
	}
	
	public String getKernelVersion() {
		return getKernelVersionFromSystemProperty();
	}
	
	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return model;
		} else {
			return manufacturer + " " + model;
		}
	}
}
