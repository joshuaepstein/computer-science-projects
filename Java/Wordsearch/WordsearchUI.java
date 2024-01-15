import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WordsearchUI {

	private JPanel rootPanel;
	private JLabel titleLabel;
	private JButton regenerateWordsearchButton;
	private JPanel wordsearchPanel;
	private JPanel wordsList;
	private List<String> words;
	private List<ClickedPosition> clickedPositions;

	protected class ClickedPosition {
		private int x;
		private int y;

		public ClickedPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() { return x; }
		public int getY() { return y; }

		public char letterInSquare() {
			JPanel square = (JPanel) wordsearchPanel.getComponent(x * 20 + y);
			JLabel label = (JLabel) square.getComponent(0);
			return label.getText().charAt(0);
		}

		public Position letterPositionIfInWord() {
			for (Map.Entry<String, List<ClickedPosition>> entry : getWordsAndPositions().entrySet()) {
				if (entry.getValue().contains(this)) {
					List<ClickedPosition> positions = entry.getValue();
					if (positions.get(0).equals(this)) {
						return Position.START;
					} else if (positions.get(positions.size() - 1).equals(this)) {
						return Position.END;
					} else {
						return Position.MIDDLE;
					}
				}
			}
			return null;
		}

		public enum Position {
			START, END, MIDDLE
		}
	}

	public Map<String, List<ClickedPosition>> getWordsAndPositions() {
		// return a map of words and their positions
		// e.g. { "HELLO" : [ (0, 0), (0, 1), (0, 2), (0, 3), (0, 4) ] }
		return null;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("WordSearch");
		frame.setContentPane(new WordsearchUI().rootPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(600, 500));
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	public WordsearchUI() {
		this.clickedPositions = new ArrayList<>();
		generateWordSearch();

		regenerateWordsearchButton.addActionListener(e -> {
			wordsearchPanel.removeAll();
			wordsList.removeAll();

			wordsList.repaint();
			wordsearchPanel.repaint();

			generateWordSearch();
		});
	}

	private void generateWordSearch() {
		wordsearchPanel.setLayout(new GridLayout(20, 20));
		wordsearchPanel.setPreferredSize(new Dimension(200, 200));
		wordsearchPanel.setMinimumSize(new Dimension(200, 200));
		wordsearchPanel.setMaximumSize(new Dimension(200, 200));
		for (int i = 0; i < 400; i++) {
			JPanel square = new JPanel();
			square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			square.setMaximumSize(new Dimension(10, 10));
			square.setSize(new Dimension(10, 10));
			wordsearchPanel.add(square);
			char randomLetter = randomLetter();
			JLabel label = new JLabel(String.valueOf(randomLetter));
			label.setFont(new Font("Arial", Font.PLAIN, 10));
			square.add(label);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);

		}
		attemptToInsertWords();
		wordsearchPanel.revalidate();
	}

	private void attemptToInsertWords() {
		String[] words = getRandomWords();

		for (String word : words) {

			boolean inserted = false;
			while (!inserted) {
				int x = (int) (Math.random() * 20);
				int y = (int) (Math.random() * 20);
				int direction = (int) (Math.random() * 8);
				inserted = switch (direction) {
					case 0, 1, 3 -> canPlace(word, x, y, 1, 0);
					case 4, 5, 6 -> canPlace(word, x, y, 1, 1);
					case 7, 8, 9 -> canPlace(word, x, y, 0, 1);
					default -> false;
				};
				if (inserted) {
					place(word, x, y, direction);
					System.out.println("Inserted " + word + " at " + x + ", " + y + " in direction " + direction);
				}
			}
		}
	}

	private boolean canPlace(String word, int x, int y, int xDirection, int yDirection) {
		// Check if word can be placed in that direction
		for (int i = 0; i < word.length(); i++) {
			if (x < 0 || x >= 20 || y < 0 || y >= 20) {
				return false;
			}
			x += xDirection;
			y += yDirection;
		}
		return true;
	}

	private void place(String word, int x, int y, int direction) {
		// Place the word in the array
		for (int i = 0; i < word.length(); i++) {
			switch (direction) {
				case 0:
					x++;
					break;
				case 1:
					x++;
					y++;
					break;
				case 2:
					y++;
					break;
				case 3:
					x--;
					y++;
					break;
				case 4:
					x--;
					break;
				case 5:
					x--;
					y--;
					break;
				case 6:
					y--;
					break;
				case 7:
					x++;
					y--;
					break;
			}

			if (x < 0 || x >= 20 || y < 0 || y >= 20) {
				return;
			}
			JPanel square = (JPanel) wordsearchPanel.getComponent(x * 20 + y);
			JLabel label = (JLabel) square.getComponent(0);
			label.setText(String.valueOf(word.charAt(i)).toLowerCase());
			System.out.println("Placed " + word.charAt(i) + " at " + x + ", " + y);
		}

		JLabel wordForList = new JLabel(word);
		wordForList.setFont(new Font("Arial", Font.PLAIN, 10));

		if (!Objects.equals(wordsList.getLayout(), new GridLayout(5, 0))) {
			wordsList.setLayout(new GridLayout(5, 0));
		}
		wordsList.add(wordForList);
		if (words == null) {
			words = new ArrayList<>();
		}
		if (!words.contains(word)) {
			words.add(word);
		}
	}

	private char randomLetter() {
		return (char) (Math.random() * 26 + 65);
	}

	private String[] getRandomWords() {
		String[] words = getListOfWordsFromFile();
		// between 2 and 4
		int randomWordsCount = (int) (Math.random() * 3 + 2);
		String[] randomWords = new String[randomWordsCount];
		for (int i = 0; i < randomWordsCount; i++) {
			int randomIndex = (int) (Math.random() * words.length);
			String word = words[randomIndex];
			if (word.contains(" ")) {
				word = word.replaceAll(" ", "");
			}
			if (word.length() > 18) {
				// if the word is too long, try again
				i--;
			} else {
				randomWords[i] = word;
			}
		}
		return randomWords;
	}

	private String[] getListOfWordsFromFile() {
		// there is a file in the project called words.txt
		// it contains a list of words, one on each line
		// return an array of all the words in the file
		File file = new File("src/words.txt");
		List<String> words = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				words.add(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return words.toArray(new String[0]);
	}
}
