package org.tree.example.stream.collector;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 * <p>
 * Collector 的三个泛型参数分别表示元素类型，累加器类型，返回类型
 */
public class ToPrimeCollector implements Collector<Integer, List<Integer>, List<Integer>> {

    // 通过平方根来素数，普通
    public static boolean isPrime(int i) {
        int sqrt = (int) Math.sqrt(i);
        return IntStream.rangeClosed(2, sqrt).boxed().noneMatch(n -> i % n == 0);
    }

    // 通过小于平方根的素数来求取素数，优化的重点
    public static boolean isPrime(List<Integer> primes, int n) {
        Integer sqrt = (int) Math.sqrt(n);

        // 正确写法
        // 当检出 1_000_000 以内的素数时，平均运行时间为 0.43 秒（原生为 1.2 秒）
        // 当检出 10_000_000 以内的素数时，平均运行时间为 6.7 秒（原生为 20 秒）
        List<Integer> temp = primes;
        for (int i = 0; i < primes.size(); i++) {
            if (primes.get(i).compareTo(sqrt) > 0) {
                temp = primes.subList(0, i + 1);
                break;
            }
        }
        return temp.stream().noneMatch(prime -> n % prime == 0);

        // 错误写法，当检出 1_000_000 以内的素数时，平均运行时间为 17 秒
        // 原因：filter 会检查 1_000_000 次，而不是在第一次检查为 false 就 break
        // return primes.stream().filter(i -> i <= sqrt).noneMatch(prime -> n % prime == 0);
    }

    @Override // 累加器初始化
    public Supplier<List<Integer>> supplier() {
        return ArrayList::new;
    }

    @Override // 累加规则
    public BiConsumer<List<Integer>, Integer> accumulator() {
        return (list, i) -> {
            if (isPrime(list, i))
                list.add(i);
        };
    }

    @Override // 并行时使用，但在这里的求取的素数是有序的，所以不允许并行
    public BinaryOperator<List<Integer>> combiner() {
        return (list1, list2) -> {
            throw new UnsupportedOperationException();
        };
    }

    @Override // 将累加器转化为返回类型
    public Function<List<Integer>, List<Integer>> finisher() {
        return Function.identity();
    }

    @Override // 标记：用来说明该收集器是否恒等，无序，可并行的
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}
