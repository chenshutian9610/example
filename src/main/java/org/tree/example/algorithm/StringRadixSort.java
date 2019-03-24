package org.tree.example.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class StringRadixSort {

	private static final int BUCKET_COUNT = 256;

	/**
	 * 定长字符串的排序
	 *
	 * @param arr
	 * @param stringLength 定长字符串的长度
	 */
	public static void fixedSortByList(String[] arr, int stringLength) {
		List<String>[] buckets = new ArrayList[BUCKET_COUNT];
		for (int i = 0; i < BUCKET_COUNT; i++)
			buckets[i] = new ArrayList<>();

		for (int pos = stringLength - 1; pos >= 0; pos--) {
			for (String str : arr)
				buckets[str.charAt(pos)].add(str);

			int idx = 0;
			for (List<String> list : buckets) {
				for (String str : list)
					arr[idx++] = str;
				list.clear();
			}
		}
	}

	/**
	 * 定长字符串的排序
	 *
	 * @param arr
	 * @param stringLength 定长字符串的长度
	 */
	public static void fixedSort(String[] arr, int stringLength) {
		String[] in = arr;
		String[] out = new String[arr.length];
		String[] tmp;
		for (int pos = stringLength - 1; pos >= 0; pos--) {
			int[] count = new int[BUCKET_COUNT + 1];

			// 使 count[n+1] 存放 ascii 为 n 的数量
			for (int i = 0; i < in.length; i++)
				count[in[i].charAt(pos) + 1]++;

			// 使 count[n] 存放 ascii 小于 n 的数量
			for (int i = 1; i <= BUCKET_COUNT; i++)
				count[i] += count[i - 1];

			for (int i = 0; i < in.length; i++)
				out[count[in[i].charAt(pos)]++] = in[i];

			tmp = in;
			in = out;
			out = tmp;
		}

		// 上述循环最后会将 in 和 out 替换，所以最后排序的数据在 in 中
		// stringLength 为奇数时，out 与 arr 同引用；为偶数时 arr 与 in 同引用
		if (stringLength % 2 == 1)
			System.arraycopy(in, 0, out, 0, in.length);
	}

	/**
	 * 变长字符串的基数排序
	 *
	 * @param arr             被排序的数组
	 * @param stringMaxLength 数组中最长字符串的长度
	 */
	public static void variableSort(String[] arr, int stringMaxLength) {
		List<String>[] wordsByLength = new ArrayList[stringMaxLength + 1];
		List<String>[] buckets = new ArrayList[BUCKET_COUNT];

		for (int i = 0; i < wordsByLength.length; i++)
			wordsByLength[i] = new ArrayList<>();
		for (int i = 0; i < buckets.length; i++)
			buckets[i] = new ArrayList<>();

		for (String str : arr)
			wordsByLength[str.length()].add(str);

		int idx = 0;
		for (List<String> list : wordsByLength)
			for (String str : list)
				arr[idx++] = str;

		int startIdx = arr.length;
		for (int pos = stringMaxLength - 1; pos >= 0; pos--) {
			startIdx -= wordsByLength[pos + 1].size();
			for (int n = startIdx; n < arr.length; n++)
				buckets[arr[n].charAt(pos)].add(arr[n]);
			idx = startIdx;
			for (List<String> bucket : buckets) {
				for (String str : bucket)
					arr[idx++] = str;
				bucket.clear();
			}
		}
	}

	public static void main(String[] args) {
		String[] arr = { "amazing", "apple", "aloha", "amen", "abcd", "hello", "world", "happy" };
		variableSort(arr, 7);
		System.out.println("hello");
		System.out.println(Arrays.toString(arr));
	}
}
