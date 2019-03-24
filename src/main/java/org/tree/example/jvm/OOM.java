package org.tree.example.jvm;

import sun.misc.Unsafe;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class OOM {
	private static int times = 1;

	static class Instance {
	}

	/**
	 * JVM options: -Xms1M 将堆的最小值设为 1M -Xmx1M 将堆的最大值设为 1M ps
	 * 当堆的最小值和最大值设为一样时，堆的大小是固定的
	 * <p>
	 * 在 jdk7 下的运行结果为 java.lang.OutOfMemoryError: Java heap space 在 jdk8 下的运行结果为
	 * java.lang.OutOfMemoryError: GC overhead limit exceeded
	 */
	private static void heap() {
		List<Instance> list = new LinkedList<>();
		while (true)
			list.add(new Instance());
	}

	/**
	 * JVM options: -Xss1K 将栈的大小设为 1K ps 在 HotSpot 中虚拟机栈和本地方法栈是合并在一起的
	 * <p>
	 * 运行结果为 java.lang.StackOverflowError
	 */
	private static void stack() {
		stack();
	}

	/**
	 * JVM options: -Xss1M 将栈的大小设为 1M ps Win 下的虚拟机中，Java
	 * 的线程是映射到操作系统的内核线程的，所以会导致系统假死； ps 理论上这段代码运行后会得到 OutOfMemoryError
	 * <p>
	 * 运行结果：死机
	 */
	private static void stack2() {
		while (true) {
			Thread thread = new Thread(() -> {
				while (true)
					;
			});
			thread.start();
			System.out.println("thread start!");
		}
	}

	/**
	 * JVM options: -XX:PermSize=1M 永久代大小为 1M -XX:MaxPermSize=1M 永久代最大大小为 1M ps
	 * String::intern 使用时会在常量池中检查是否含有当前字符串，如果有则返回该字符串的引用，否则将当前字符串存入常量池
	 * <p>
	 * 在 jdk7 的运行结果为 java.lang.OutOfMemoryError: PermGen space 在 jdk8 的运行结果：未知（jdk8
	 * 的方法区没有使用永久代来实现，而是本地内存）
	 * <p>
	 * 这里测试的是 jdk7 之前的方法区的运行时常量池，如果要测试整个方法区，则要不断加载 Class 对象 通常的做法是通过
	 * cglib，为某一个类不断创建子类
	 */
	private static void runtimeConstPool() {
		int i = 0;
		List<String> list = new LinkedList<>();
		while (true)
			list.add(String.valueOf(i++).intern());
	}

	/**
	 * JVM options: -XX:MaxDirectMemorySize=1M 最大直接内存为 1M ps 如果不指定
	 * MaxDirectMemorySize，则最大直接内存的值与 -Xmx 一样 ps 使用 Java nio 中的 DirectByteBuffer 的
	 * Unsafe 实例来分配直接内存
	 * <p>
	 * 在 jdk8 下的运行结果为 java.lang.OutOfMemoryError
	 */
	private static void directMemory() throws IllegalAccessException {
		Field field = Unsafe.class.getDeclaredFields()[0];
		field.setAccessible(true);
		Unsafe unsafe = (Unsafe) field.get(null);
		while (true)
			unsafe.allocateMemory(1024 * 1024);
	}

	public static void main(String[] args) throws Exception {
		stack();
	}
}
