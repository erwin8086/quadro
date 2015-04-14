import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;


public class Save {
	public static final int KEY_UP=0;
	public static final int KEY_LEFT=1;
	public static final int KEY_RIGHT=2;
	public static final int LEVEL=3;
	
	
	public int getKeys(int key) {
		String val = getConf(key);
		if(val!=null) {
			try {
				return Integer.valueOf(val);
			} catch(NumberFormatException e) {
				e.printStackTrace();
			}
		}
		if(key==KEY_UP) {
			return KeyEvent.VK_UP;
		} else if(key==KEY_LEFT) {
			return KeyEvent.VK_LEFT;
		} else if(key==KEY_RIGHT) {
			return KeyEvent.VK_RIGHT;
		}
		return 0;
	}
	
	public File getConfFile() {
		return new File(System.getProperty("user.home") + System.getProperty("file.separator") + "." + Main.title + ".conf");
	}
	
	@SuppressWarnings("resource")
	public String getConf(int conf) {
		File f = getConfFile();
		InputStream in=null;
		if(f.exists()) {
			try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			in = getClass().getResourceAsStream("/res/conf.txt");
		}
		if(in!=null) {
			Scanner s = new Scanner(in);
			while(s.hasNextLine()) {
				String line = s.nextLine();
				String[] split = line.split(":");
				if(split[0].equals(((Integer)conf).toString())) {
					return split[1];
				}
			}
			s.close();
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void setConf(int conf, String val) {
		File f = getConfFile();
		if(!f.exists()) {
			try {
				PrintWriter pr = new PrintWriter(f);
				pr.println(conf + ":" + val);
				pr.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				PrintWriter p = new PrintWriter(bout);
				Scanner in = new Scanner(f);
				while(in.hasNextLine()) {
					p.println(in.nextLine());
				}
				in.close();
				p.close();
				in = new Scanner(new ByteArrayInputStream(bout.toByteArray()));
				p = new PrintWriter(f);
				while(in.hasNextLine()) {
					String line = in.nextLine();
					String[] split = line.split(":");
					if(split[0].equals(String.valueOf(conf))) {
						p.println(conf + ":" + val);
					} else {
						p.println(line);
					}
				}
				in.close();
				p.close();
				
				
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
