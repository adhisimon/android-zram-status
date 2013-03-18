package id.co.ptskp.android.zramstatus;

import java.io.BufferedReader;
import java.io.FileReader;

public class Zram {
	
	private final String ZRAMSTATFILE_DISKSIZE = "/sys/block/zram0/disksize";
	private final String ZRAMSTATFILE_COMPRESSED_DATA_SIZE = "/sys/block/zram0/compr_data_size";
	private final String ZRAMSTATFILE_ORIGINAL_DATA_SIZE = "/sys/block/zram0/orig_data_size";
	private final String ZRAMSTATFILE_MEM_USED_TOTAL = "/sys/block/zram0/mem_used_total";
	
	
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
	
	public int getDiskSize() throws Exception {
		String sizeInString = readFileAsString(ZRAMSTATFILE_DISKSIZE);
		return Integer.parseInt(sizeInString);
	}
	
	public int getCompressedDataSize() throws Exception {
		String sizeInString = readFileAsString(ZRAMSTATFILE_COMPRESSED_DATA_SIZE);
		return Integer.parseInt(sizeInString);
	}
	
	public int getOriginalDataSize() throws Exception {
		String sizeInString = readFileAsString(ZRAMSTATFILE_ORIGINAL_DATA_SIZE);
		return Integer.parseInt(sizeInString);
	}
	
	public int getMemUsedTotal() throws Exception {
		String sizeInString = readFileAsString(ZRAMSTATFILE_MEM_USED_TOTAL);
		return Integer.parseInt(sizeInString);
	}
	
	public float getCompressionRatio() throws Exception {
		return (float) getCompressedDataSize() / (float) getOriginalDataSize();
	}
	
	public float getUsedRatio() throws Exception {
		return (float) getOriginalDataSize() / (float) getDiskSize();
 	}

}
