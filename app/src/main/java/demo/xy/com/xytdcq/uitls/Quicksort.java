package demo.xy.com.xytdcq.uitls;

public class Quicksort {
	// 划分数组
	int partion(int array[], int p, int r) {
		int x = array[r];
		int i = p - 1;// 注意这点，把i设成负值，然后作为移动的标志
		int j;
		for (j = p; j < r; j++) {
			if (array[j] <= x) {
				i++;
				int temp = array[j];
				array[j] = array[i];
				array[i] = temp;
			}
		}
		int temp = array[j];
		array[j] = array[i + 1];
		array[i + 1] = temp;
		return i + 1;// 返回的应该是交换后的哨兵的位置
	}

	// 递归解决每个划分后的小数组
	public void quickSort(int[] array, int p, int r) {
		if (p < r) {
			int q = partion(array, p, r);
			quickSort(array, p, q - 1);
			quickSort(array, q + 1, r);
		}
	}

}
