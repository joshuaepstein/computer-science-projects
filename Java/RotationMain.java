import java.util.Scanner;

public class RotationMain {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter the number of rotations: ");
		int rotation = scanner.nextInt();

		System.out.print("Enter the number of elements: ");
		int elements = scanner.nextInt();

		int[] array = new int[elements];
		for (int i = 0; i < elements; i++) {
			array[i] = i + 1;
		}
		int[] newArray = rotate(array, rotation);
		for (int number : newArray){
			System.out.print(number + " ");
		}
	}

	public static int[] rotate(int[] array, int rotations) {
		int[] newArray = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			newArray[(i + rotations) % array.length] = array[i];
		}
		return newArray;
	}
}