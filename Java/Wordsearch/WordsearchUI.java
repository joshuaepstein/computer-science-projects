import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class WordsearchUI {

	private final JPanel rootPanel = new JPanel();
	private final JLabel titleLabel = new JLabel("WordSearch");
	private final JButton regenerateWordsearchButton = new JButton("Regenerate Wordsearch");
	private final JPanel wordsearchPanel = new JPanel();
	private final JPanel wordsList = new JPanel();
	private List<String> words = new ArrayList<>();
	private List<ClickedPosition> clickedPositions = new ArrayList<>();
	private final List<ClickedPosition> clickablePositions = new ArrayList<>();

	protected class ClickedPosition {
		private final int x;
		private final int y;

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

		public boolean isInside(int x, int y, int width, int height) {
			return this.x >= x && this.x <= x + width && this.y >= y && this.y <= y + height;
		}

		public enum Position {
			START, END, MIDDLE
		}
	}

	public Map<String, List<ClickedPosition>> getWordsAndPositions() {
		// return a map of words and their positions
		// e.g. { "HELLO" : [ (0, 0), (0, 1), (0, 2), (0, 3), (0, 4) ] }
		Map<String, List<ClickedPosition>> wordsAndPositions = new HashMap<>();
		for (String word : words) {
			// get the position of the square for the first and last letter of the word
			int firstLetterX = -1;
			int firstLetterY = -1;
			int lastLetterX = -1;
			int lastLetterY = -1;

			String firstLetter = word.substring(0, 1);
			String lastLetter = word.substring(word.length() - 1);
			for (int i = 0; i < 400; i++) {
				JPanel square = (JPanel) wordsearchPanel.getComponent(i);
				JLabel label = (JLabel) square.getComponent(0);
				if (label.getText().equals(firstLetter)) {
					firstLetterX = i / 20;
					firstLetterY = i % 20;
				}
				if (label.getText().equals(lastLetter)) {
					lastLetterX = i / 20;
					lastLetterY = i % 20;
				}
			}

			int finalFirstLetterX = firstLetterX;
			int finalFirstLetterY = firstLetterY;
			int finalLastLetterX = lastLetterX;
			int finalLastLetterY = lastLetterY;
			wordsAndPositions.put(word, new ArrayList<>() {{
				add(new ClickedPosition(finalFirstLetterX, finalFirstLetterY));
				add(new ClickedPosition(finalLastLetterX, finalLastLetterY));
			}});
		}
		return null;
	}

	public static void main(String[] args) {
		new WordsearchUI();
	}

	public WordsearchUI() {
		JFrame frame = new JFrame("WordSearch");
		frame.setContentPane(rootPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(700, 600));
		frame.setResizable(false);

		rootPanel.setBounds(0, 0, 700, 600);
		rootPanel.setLayout(null);

		titleLabel.setBounds(0, 0, 700, 40);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setVerticalAlignment(JLabel.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		rootPanel.add(titleLabel);

		regenerateWordsearchButton.setBounds((700 / 2) - (200 / 2), 40, 200, 30);
		regenerateWordsearchButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		regenerateWordsearchButton.setFont(new Font("Arial", Font.PLAIN, 15));
		regenerateWordsearchButton.setBackground(Color.WHITE);
		regenerateWordsearchButton.setForeground(Color.BLACK);
		rootPanel.add(regenerateWordsearchButton);

		wordsearchPanel.setBounds((700 / 2) - (200), 80, 400, 400);
		wordsearchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rootPanel.add(wordsearchPanel);

		wordsList.setBounds((700 / 2) - (200), 480, 400, 100);
		wordsList.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rootPanel.add(wordsList);

		frame.pack();
		frame.setVisible(true);
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
		wordsearchPanel.setLayout(null);
		wordsearchPanel.setPreferredSize(new Dimension(200, 200));
		wordsearchPanel.setMinimumSize(new Dimension(200, 200));
		wordsearchPanel.setMaximumSize(new Dimension(200, 200));
		for (int i = 0; i < 400; i++) {
			JPanel square = new JPanel();
			square.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			square.setMaximumSize(new Dimension(10, 10));
			square.setSize(new Dimension(10, 10));
			wordsearchPanel.add(square);
			square.setBounds((i / 20) * 20, (i % 20) * 20, 20, 20);
			char randomLetter = randomLetter();
			JLabel label = new JLabel(String.valueOf(randomLetter));
			label.setFont(new Font("Arial", Font.PLAIN, 10));
			square.add(label);
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);

			this.clickablePositions.add(new ClickedPosition(i / 20, i % 20));
			label.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// add to clicked positions

				}

				@Override
				public void mousePressed(MouseEvent e) {

				}

				@Override
				public void mouseReleased(MouseEvent e) {

				}

				@Override
				public void mouseEntered(MouseEvent e) {

				}

				@Override
				public void mouseExited(MouseEvent e) {

				}
			});
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
				//TODO
				// undo adding the word
				System.out.println("Word letter could not be placed (Word: " + word + ", Letter: " + word.charAt(i) + ", X: " + x + ", Y: " + y + ", Direction: " + direction + ")");
				for (char letter : word.toCharArray()) {
					if (word.indexOf(letter) < i) {
						// This should undo the previous letters.
						JPanel square = (JPanel) wordsearchPanel.getComponent(x * 20 + y);
						JLabel label = (JLabel) square.getComponent(0);
						label.setText(String.valueOf(randomLetter()).toLowerCase());
						System.out.println("Removed " + letter + " at " + x + ", " + y);
					}
				}
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
