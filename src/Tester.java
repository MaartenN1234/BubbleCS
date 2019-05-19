import java.io.*;
import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class Tester {

	public static void main(String[] args) {
		byte [] dataOut = {3,5,7,2,6,45,7,8,3,42,6,4,5,87,34,62,46,21,4,2,4,6,3,65,54,43,23,12,41,26,27,75};
		File myFile = new File("D:\\sd.x");
		System.out.println(Arrays.toString(dataOut));
		
		
		try{
			OutputStream os = new DeflaterOutputStream((new FileOutputStream(myFile)),
                    			new Deflater(),
                    			128*1024,
                    			false);
			os.write(dataOut);
			os.close();
			
			
			byte [] dataIn = new byte[dataOut.length];
			dataOut = null;
			
			InputStream  is = new  InflaterInputStream(new FileInputStream(myFile), 
															new Inflater(), 
															512*1024);
			is.read(dataIn);
			is.close();
			
			System.out.println(Arrays.toString(dataIn));
			

			 is =new FileInputStream(myFile);
			is.read(dataIn);
			is.close();
			System.out.println(Arrays.toString(dataIn));
			
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}
	
	

	/*
	 * try{
			ZipOutputStream os = new ZipOutputStream(new FileOutputStream(myFile));
			ZipEntry ze = new ZipEntry("D");
			os.putNextEntry(ze);
			os.write(dataOut);
			os.close();
			
			
			byte [] dataIn = new byte[dataOut.length];
			dataOut = null;
			
			ZipInputStream  is = new ZipInputStream(new FileInputStream(myFile));
			is.getNextEntry();
			is.read(dataIn);
			is.close();
			
			System.out.println(Arrays.toString(dataIn));
		} catch(Exception e){
			throw new RuntimeException(e);
		}
		*/


}
