package org.tree.example.stream.collector;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;
import org.tree.commons.utils.PerformanceUtils;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class ToPrimeCollectorTest {

	int max = 1_000_000;

	@Test
	public void test1() {
		PerformanceUtils.test(() -> IntStream.rangeClosed(2, max).boxed().filter(ToPrimeCollector::isPrime)
				.collect(Collectors.toList()));
	}

	@Test
	public void test2() {
		PerformanceUtils.test(() -> IntStream.rangeClosed(2, max).boxed().collect(new ToPrimeCollector()));
	}

	@Test
	public void test3() {
		// 偏慢
		PerformanceUtils
				.test(() -> IntStream.rangeClosed(2, max).boxed().collect(ArrayList<Integer>::new, (list, i) -> {
					if (ToPrimeCollector.isPrime(list, i))
						list.add(i);
				}, (list1, list2) -> {
					throw new UnsupportedOperationException();
				}));
	}

}
