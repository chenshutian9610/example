package org.tree.example.algorithm;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 */
public class Log_N {
	/* 折半查询 */
	public int binarySearch(int[] arr, int key) {
		int low = 0, high = arr.length - 1;

		while (low <= high) {
			int mid = (low + high) >> 1;

			if (arr[mid] < key)
				low = mid + 1;
			else if (arr[mid] > key)
				high = mid - 1;
			else
				return mid;
		}

		return -1;
	}

	/* 欧几里得算法 */
	public int gcd(int m, int n) {
		int temp = 0;

		if (m < n) {
			temp = m;
			m = n;
			n = temp;
		}

		int rem = m % n;

		while (rem != 0) {
			m = n;
			n = rem;
			rem = m % n;
		}
		return n;
	}

	/* 幂运算 */
	public double pow(int x, int y) {
		if (y == 0)
			return 1;

		if (y == 1)
			return x;

		if (y < 0) {
			System.out.println("请输入正整数");
			return -1;
		}

		if (y % 2 == 0)
			return pow(x * x, y >> 1);
		else
			return pow(x * x, y >> 1) * x;
		
	}
}
