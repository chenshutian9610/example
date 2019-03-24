package org.tree.example.algorithm;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 * 
 * 数组最大子序列的和
 */
public class MaxSum {
	/* O(N^2) */
	public static int getMaxSum0(int[] a) {
		int max = 0;

		for (int i = 0; i < a.length; i++) {
			int sum = 0;
			for (int j = i; j < a.length; j++) {
				sum += a[j];
				max = max > sum ? max : sum;
			}
		}

		return max;
	}

	public static int max(int x, int y, int z) {
		int max;
		max = x > y ? x : y;
		max = max > z ? max : z;
		return max;
	}

	/* O(NlogN) */
	public static int getMaxSum1(int[] a, int left, int right) {
		if (left == right)
			return a[left];

		int mid = (left + right) >> 1;
		int leftMaxSum = getMaxSum1(a, left, mid);
		int rightMaxSum = getMaxSum1(a, mid + 1, right);

		int leftBorderSum = 0, leftMaxBorderSum = 0;
		for (int i = mid; i >= left; i--) {
			leftBorderSum += a[i];
			if (leftBorderSum > leftMaxBorderSum)
				leftMaxBorderSum = leftBorderSum;
		}

		int rightBorderSum = 0, rightMaxBorderSum = 0;
		for (int i = mid + 1; i <= right; i++) {
			rightBorderSum += a[i];
			if (rightBorderSum > rightMaxBorderSum)
				rightMaxBorderSum = rightBorderSum;
		}

		return max(leftMaxSum, rightMaxSum, leftMaxBorderSum + rightMaxBorderSum);
	}

	/* O(N) */
	public static int getMaxSum2(int[] a) {
		int max = 0, temp = 0;

		for (int i = 0; i < a.length; i++) {
			temp = temp > 0 ? temp : 0;
			temp += a[i];
			max = max > temp ? max : temp;
		}

		return max;
	}

	public static void main(String args[]) {
		int[] a = { 2, -4, 5, 3, -3, 20, 3, -10, 12 };

		// 结果为 30，index 从 2 到 8
		System.out.println(MaxSum.getMaxSum0(a));
		System.out.println(MaxSum.getMaxSum1(a, 0, a.length - 1));
		System.out.println(MaxSum.getMaxSum2(a));
	}
}
