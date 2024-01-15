public class SelectionSortSortMain {

	// Sort an array using Selection Sort. Selection sort works by finding the smallest item in the list and moving it to the first unsorted part of the list.
	public static void main(String[] args) {
		int[] array = { 5, 3, 6, 2, 10 };
		int[] sortedArray = selectionSort(array);
		for (int number : sortedArray) {
			System.out.print(number + " ");
		}
	}

	public static int[] selectionSort(int[] array) {
		for (int i = 0; i < array.length; i++) {
			int smallestIndex = i;
			for (int j = i; j < array.length; j++) {
				if (array[j] < array[smallestIndex]) {
					smallestIndex = j;
				}
			}
			int temp = array[i];
			array[i] = array[smallestIndex];
			array[smallestIndex] = temp;
		}
		return array;
	}

}
