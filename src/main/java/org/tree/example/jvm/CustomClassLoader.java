package org.tree.example.jvm;

import java.io.IOException;
import java.io.InputStream;

import org.tree.example.materia.Person;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 * ps:自定义的类加载器一般不会重写 loadClass 方法，一般重写的是 findClass 方法
 */
public class CustomClassLoader extends ClassLoader {
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			String filename = name.substring(name.lastIndexOf(".") + 1) + ".class";
			InputStream in = getClass().getResourceAsStream(filename);
			if (in == null)
				return super.loadClass(name);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			return defineClass(name, bytes, 0, bytes.length);
		} catch (IOException e) {
			throw new RuntimeException("Can not find the Class file !");
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.printf("自定义的类加载器加载的类是否和系统加载器的类相等：%s",
				new CustomClassLoader().loadClass("org.tree.example.materia.Person").getClass().equals(Person.class));
	}
}
