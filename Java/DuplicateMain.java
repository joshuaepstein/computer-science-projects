import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DuplicateMain {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Please enter an array of integers: ");
		String[] input = scanner.nextLine().split(" ");
		int[] array = new int[input.length];
		for (int i = 0; i < input.length; i++) {
			array[i] = Integer.parseInt(input[i]);
		}

		int[] result = removeDuplicates(array);
		System.out.print("The array without duplicates is: ");
		for (int j : result) {
			System.out.print(j + " ");
		}
	}

	public static int[] removeDuplicates(int[] initialArray) {
		List<Integer> list = new ArrayList<>();
		for (int j : initialArray) {
			if (!list.contains(j)) {
				list.add(j);
			}
		}
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}
}