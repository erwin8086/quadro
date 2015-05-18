package zip;

public class ZipClassLoader extends ClassLoader{
	private ZipFile zip;
	public ZipClassLoader(ClassLoader parent, ZipFile zip) {
		super(parent);
		this.zip=zip;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if(!name.startsWith("mod."))
			return super.loadClass(name);
		byte[] data = zip.getFile(name.substring("mod.".length()) + ".class");
		return defineClass("zip." + name.substring("mod.".length()), data, 0, data.length);
	}

}
