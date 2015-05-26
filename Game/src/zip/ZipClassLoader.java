package zip;

/**
 * ClassLoader for ZipFile
 * used by Mods
 * @author erwin
 *
 */
public class ZipClassLoader extends ClassLoader{
	private ZipFile zip;
	public ZipClassLoader(ClassLoader parent, ZipFile zip) {
		super(parent);
		this.zip=zip;
	}

	/**
	 * loadClass from zip if name starts with "mod."
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if(!name.startsWith("mod."))
			return super.loadClass(name);
		byte[] data = zip.getFile(name.substring("mod.".length()) + ".class");
		return defineClass(name, data, 0, data.length);
	}

}
