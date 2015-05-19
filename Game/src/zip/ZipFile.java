package zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFile {

	private ArrayList<ZipObject> files;
	public ZipFile(File load) throws IOException {
		files = new ArrayList<ZipObject>();
		final int BUFFER=2048;
		byte[] data=new byte[BUFFER];
		FileInputStream fis = new FileInputStream(load);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		ZipEntry entry;
		while( (entry = zis.getNextEntry()) != null) {
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int count;
			BufferedOutputStream out = new BufferedOutputStream(bout);
			while( (count = zis.read(data, 0, BUFFER)) != -1) {
				out.write(data,0,count);
			}
			out.flush();
			out.close();
			ZipObject zip = new ZipObject();
			zip.data=bout.toByteArray();
			zip.name=entry.getName();
			files.add(zip);
			
		}
		zis.close();
	}
	
	public byte[] getFile(String name) {
		for(ZipObject zip : files) {
			if(zip.name.equals(name)) 
				return zip.data;
		}
		return null;
	}
	
	public InputStream getFileAsStream(String name) {
		byte[] data = getFile(name);
		if(data==null) return null;
		return new ByteArrayInputStream(getFile(name));
	}
	
	public void save(File f) throws IOException {
		FileOutputStream out = new FileOutputStream(f);
		ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(out));
		for(ZipObject zip : files) {
			ZipEntry entry = new ZipEntry(zip.name);
			zout.putNextEntry(entry);
			zout.write(zip.data);
		}
		zout.close();
	}
	
	public void append(byte[] data, String name) {
		for(ZipObject zip : files) {
			if(zip.name.equals(name)) {
				return;
			}
		}
		ZipObject zip = new ZipObject();
		zip.data=data;
		zip.name=name;
		files.add(zip);
	}
	
	public void replace(byte[] data, String name) {
		for(int i=0;i<files.size();i++) {
			ZipObject zip=files.get(i);
			if(zip.name.equals(name)) {
				files.remove(zip);
				break;
			}
		}
		append(data,name);
	}
	
	public void delete(String name) {
		for(int i=0;i<files.size();i++) {
			ZipObject zip=files.get(i);
			if(zip.name.equals(name)) {
				files.remove(zip);
				break;
			}
		}
	}
	
	private static class ZipObject {
		byte[] data;
		String name;
	}
}
