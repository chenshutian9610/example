package org.tree.example.stream;

import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.tree.commons.utils.PerformanceUtils;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class ParallelStreamToGetSumTest {

	private long num = 10_000_000;

	/****************************** sequential *******************************/

	@Test // 正确的顺序计算
	public void sequential() {
		PerformanceUtils.test(() -> LongStream.rangeClosed(1, num).sum()); // 19 milliseconds
	}

	@Test // 错误的顺序计算，没有考虑拆箱的开销
	public void errorSequential() {
		PerformanceUtils.test(() -> Stream.iterate(1L, i -> i + 1L).limit(num).reduce(0L, Long::sum)); // 256
																										// milliseconds
	}

	/****************************** parallel *******************************/

	@Test // 正确的并行计算
	public void parallel() {
		PerformanceUtils.test(() -> LongStream.rangeClosed(1, num).parallel().sum()); // 4 milliseconds
	}

	@Test // 错误的并行计算，没有考虑开箱的开销与违反了无序性原则（iterate 没有范围，是顺序产生的）
	public void errorParallel() {
		PerformanceUtils.test(() -> Stream.iterate(1L, i -> i + 1L).limit(num).parallel().reduce(0L, Long::sum)); // 1680
																													// milliseconds
	}

	@Test // 错误的并行计算，使用了共享变量，导致了线程竞争，并且共享变量的累加不是原子性的，所以结果也是错误的
	public void errorParallel2() {
		PerformanceUtils.test(() -> {
			Accumulator accumulator = new Accumulator();
			Stream.iterate(1L, i -> i + 1L).limit(num).parallel().forEach(accumulator::add);
			return accumulator.total;
		}); // 1125 milliseconds but error result
	}

	/****************************** inner class *******************************/

	private static class Accumulator {
		private long total = 0;

		private void add(long value) {
			total += value;
		}
	}
}
