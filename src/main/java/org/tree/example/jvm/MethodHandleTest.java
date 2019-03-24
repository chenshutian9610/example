package org.tree.example.jvm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.tree.example.materia.Person;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class MethodHandleTest {
	private static MethodHandle getMethod(Object receiver) throws Throwable {
		return MethodHandles.lookup()
				.findVirtual(receiver.getClass(), "greet", MethodType.methodType(String.class))
				.bindTo(receiver);
	}

	public static void main(String[] args) throws Throwable {
		System.out.println(getMethod(new Person()).invoke());
	}
}