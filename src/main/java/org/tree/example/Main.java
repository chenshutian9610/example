package org.tree.example;

import org.junit.jupiter.api.Test;
import org.tree.commons.utils.PerformanceUtils;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class Main {
	@Test
	public void test() {
		PerformanceUtils.test(() -> {
			return "hello world";
		});
	}

	@Test
	public void test2() {
		System.out.println("nihao");
	}
}
