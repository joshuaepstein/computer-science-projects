public class ArrayMain {

	// Given an array, work out the length of the longest continuous sequence of numbers that increase by 1. Print out the longest length.
	public static void main(String[] args) {
		int[] array = { 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 15, 16, 17, 18, 20, 21, 24 };
		int longestLength = longestSequence(array);
		System.out.println("The longest sequence is " + longestLength + " numbers long.");
	}

	public static int longestSequence(int[] array) {
		int longestLength = 0;
		int currentLength = 1;
		for (int i = 0; i < array.length - 1; i++) {
			if (array[i + 1] - array[i] == 1) {
				currentLength++;
			} else {
				if (currentLength > longestLength) {
					longestLength = currentLength;
				}
				currentLength = 1;
			}
		}
		if (currentLength > longestLength) {
			longestLength = currentLength;
		}
		return longestLength;
	}
}
