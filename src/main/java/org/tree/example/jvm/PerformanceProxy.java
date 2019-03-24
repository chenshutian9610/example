package org.tree.example.jvm;

import java.lang.reflect.Proxy;

import org.tree.example.materia.IPerson;
import org.tree.example.materia.Person;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class PerformanceProxy<T> {
	
	@SuppressWarnings("unchecked")
	public static <I, T extends I> I proxy(T obj) {
		return (I) Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
				(proxy, method, args) -> {
					long start = System.nanoTime();
					Object result = method.invoke(obj, args);
					System.out.printf("consume %s nanoseconds%n", System.nanoTime() - start);
					return result;
				});
	}

	public static void main(String[] args) {
		// 会在项目中保存生成的 Proxy （com.sun.proxy.$Proxy0.class）
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
		IPerson person = proxy(new Person());
		System.out.println(person.greet());
	}
}
