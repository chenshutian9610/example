package org.tree.example.jvm;

import java.util.Objects;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class FinalizeTest {
	
	private static class Instance {
		
		private static Instance self;

		@Override
		public void finalize() throws Throwable {
			System.out.println("execute finalize method!");
			// 执行 finalize 方法后会将对象放到一个 finalize 队列中等待回收内存
			// 如果在被回收内存之前重新获得引用，则复活
			super.finalize();
			self = new Instance();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Instance.self = new Instance();
		Instance.self = null;
		System.gc();
		Thread.sleep(1000);
		System.out.printf("Instance.self 是否为 null : %s", Objects.isNull(Instance.self));
	}
}
