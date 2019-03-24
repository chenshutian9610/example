package org.tree.example.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class Sort<T extends Comparable<? super T>> {
	private Comparator<T> comparator = (x, y) -> {
		return x.compareTo(y);
	};

	private final void swapReferences(T[] arr, int i, int j) {
		T tmp;
		tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * 插入排序
	 */
	public void insertionSort(T[] arr) {
		int n; // 最后空出的位置的索引
		T tmp; // 存放基准值
		for (int i = 1; i < arr.length; i++) {
			tmp = arr[i];
			for (n = i; n > 0 && comparator.compare(tmp, arr[n - 1]) < 0; n--)
				arr[n] = arr[n - 1];
			arr[n] = tmp;
		}
	}

	/**
	 * 希尔排序 使用插入排序对子数组进行排序（也可以是别的排序算法） 假设增量序列为 h，步长从 h[arr.length / 2]
	 * 开始不断递减为原来的一半（h[n] = h[n + 1] / 2;）
	 */
	public void shellSort(T[] arr) {
		int n; // 最后空出的位置的索引
		T tmp; // 存放基准值
		for (int gap = arr.length / 2; gap > 0; gap /= 2)
			for (int i = gap; i < arr.length; i++) {
				tmp = arr[i];
				for (n = i; n >= gap && comparator.compare(tmp, arr[n - gap]) < 0; n -= gap)
					arr[n] = arr[n - gap];
				arr[n] = tmp;
			}
	}

	/**
	 * 堆排序
	 */
	public void heapSort(T[] arr) {
		// build heap
		for (int i = arr.length / 2 - 1; i >= 0; i--)
			percDown(arr, i, arr.length);
		// delete root
		for (int i = arr.length - 1; i > 0; i--) {
			swapReferences(arr, 0, i);
			percDown(arr, 0, i);
		}
	}

	private void percDown(T[] arr, int i, int n) {
		T tmp = arr[i];
		int child;
		for (; leftChild(i) < n; i = child) {
			child = leftChild(i);
			if (child != n - 1 && comparator.compare(arr[child], arr[child + 1]) < 0)
				child++;
			if (comparator.compare(tmp, arr[child]) < 0)
				arr[i] = arr[child];
			else
				break;
		}
		arr[i] = tmp;
	}

	private int leftChild(int i) {
		return 2 * i + 1;
	}

	/**
	 * 归并排序
	 */
	public void mergeSort(T[] arr) {
		// 这里创建一个临时数组是为了避免在递归中创建数组，可以节省很多内存
		mergeSort(arr, (T[]) new Comparable[arr.length], 0, arr.length - 1);
	}

	private void mergeSort(T[] arr, T[] tmpArr, int left, int right) {
		if (left < right) {
			int center = (left + right) >> 1;
			mergeSort(arr, tmpArr, left, center);
			mergeSort(arr, tmpArr, center + 1, right);
			merge(arr, tmpArr, left, center, center + 1, right);
		}
	}

	private void merge(T[] arr, T[] tmpArr, int leftPos, int leftEnd, int rightPos, int rightEnd) {
		int tmpPos = leftPos;
		int leftStart = leftPos;
		while (leftPos <= leftEnd && rightPos <= rightEnd)
			tmpArr[tmpPos++] = comparator.compare(arr[leftPos], arr[rightPos]) <= 0 ? arr[leftPos++] : arr[rightPos++];
		while (leftPos <= leftEnd)
			tmpArr[tmpPos++] = arr[leftPos++];
		while (rightPos <= rightEnd)
			tmpArr[tmpPos++] = arr[rightPos++];
		System.arraycopy(tmpArr, leftStart, arr, leftStart, rightEnd - leftStart + 1);
	}

	/**
	 * 简单的快速排序
	 */
	public void quickSort(List<T> list) {
		if (list.size() > 1) {
			List<T> smaller = new ArrayList<>();
			List<T> same = new ArrayList<>();
			List<T> bigger = new ArrayList<>();

			T middleValue = list.get(list.size() / 2);
			for (T element : list)
				if (comparator.compare(element, middleValue) < 0)
					smaller.add(element);
				else if (comparator.compare(element, middleValue) > 0)
					bigger.add(element);
				else
					same.add(element);

			quickSort(smaller);
			quickSort(bigger);

			list.clear();
			list.addAll(smaller);
			list.addAll(same);
			list.addAll(bigger);
		}
	}

	/**
	 * 快速排序
	 */
	public void quickSort(T[] arr) {
		quickSort(arr, 0, arr.length - 1);
	}

	private void quickSort(T[] arr, int left, int right) {
		if (left < right) {
			T pivot = median(arr, left, right);
			int i = left, j = right - 1;
			while (left != right - 1) { // 这里如果是 while(true) 的话，降序排序的时候会报错
				while (comparator.compare(arr[++i], pivot) < 0)
					;
				while (comparator.compare(arr[--j], pivot) > 0)
					;
				if (i < j)
					swapReferences(arr, i, j);
				else
					break;
			}
			// 这里不加这个判定条件的话结果会出错
			// 假设不加，在最后一次排序的时候 i = right，那么 i 就是最大或最小值，再和 i -1 交换引用，结果就错了
			if (i != right)
				swapReferences(arr, i, right - 1); // 将中值放到 i 位置

			quickSort(arr, left, i - 1);
			quickSort(arr, i + 1, right);
		}
	}

	private T median(T[] arr, int left, int right) {
		int center = (left + right) / 2;
		if (comparator.compare(arr[center], arr[left]) < 0)
			swapReferences(arr, left, center);
		if (comparator.compare(arr[right], arr[left]) < 0)
			swapReferences(arr, left, right);
		if (comparator.compare(arr[right], arr[center]) < 0)
			swapReferences(arr, center, right);
		// 将中值放在 right-1 的位置
		swapReferences(arr, center, right - 1);
		return arr[right - 1];
	}

	public static void main(String[] args) {
		Integer[] arr = { 1, 4, 3, 5, 2, 8, 9, 6, 7 };
		new Sort<Integer>().quickSort(arr);
		System.out.println(Arrays.toString(arr));
		List<Integer> list = new LinkedList<>();
	}
}
