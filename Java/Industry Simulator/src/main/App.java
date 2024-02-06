package main;
import main.factory.CoalFactory;
import main.factory.FactoryType;
import main.factory.IFactory;
import main.utils.Counter;
import main.utils.Draw;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public static List<IFactory> factories = new ArrayList<>();
	private int gameLevel = 1;
	private final JFrame frame;
	private final JPanel rootPanel;
	private final Map<FactoryType, JLabel> resourcesLabels;
	private final Map<FactoryType, JButton> createFactoriesButtons;
	private final Map<String, Long> toasts = new HashMap<>();
	private final Map<FactoryType, Boolean> lockedResource = new HashMap<>() {{
		put(FactoryType.COAL, false);
	}};

	public static Map<FactoryType, Counter> money = new HashMap<>() {{
		put(FactoryType.COAL, new Counter(100));
		put(FactoryType.COPPER, new Counter(200));
	}};
	private long ticks = 0;

	public static void main(String[] args) {
		new App();
	}

	public App() {
		factories = new ArrayList<>();
		createFactoriesButtons = new HashMap<>();
		resourcesLabels = new HashMap<>();

		frame = new JFrame("Industry Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);

		rootPanel = new JPanel();
		rootPanel.setLayout(null);
		rootPanel.setBounds(0, 0, WIDTH, HEIGHT);
		frame.add(rootPanel);

		addButtons();

		Timer timer = new Timer(100, e -> {
			ticks++;
			tickToasts();

			if (ticks % 10 == 0) {
				for (IFactory factory1 : factories) {
					if (!factory1.isOverworked()) {
						money.computeIfAbsent(factory1.getType(), (factoryType) -> new Counter(0)).add(factory1.getProductionPerTick());
					}
				}
			}

			// for (FactoryType factoryType : FactoryType.values()) {
			// 	resourcesLabels.get(factoryType).setText(factoryType.getNameAsCamel() + ": " + money.getOrDefault(factoryType, new Counter(0)).getValue());
			// }

			// If the coal money is greater than 500, level up
			if (money.get(FactoryType.COAL).getValue() >= 500 && gameLevel == 1) {
				gameLevel++;
				addToast("Level up! You are now level " + gameLevel);
				lockedResource.put(FactoryType.COPPER, false);
				// Play unlock sound which is in factory/epic_open.ogg
				try {
					String soundName = "src/main/factory/epic_open.wav";    
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
					Clip clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					clip.start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
			try {
				String soundName = "src/main/factory/epic_open.wav";    
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		timer.setRepeats(true);
		timer.start();

		frame.setLayout(null);
		frame.setVisible(true);

		for (FactoryType factoryType : FactoryType.values()) {
			JLabel label = new JLabel(factoryType.getNameAsCamel() + ": 0");
			label.setBounds(2, factoryType.ordinal() * 20, 250, 25);
			resourcesLabels.put(factoryType, label);
			rootPanel.add(label);

			Draw.drawFactory(rootPanel, factoryType);
		}
	}

	private void addButtons() {
		int buttonsY = 70;
		for (FactoryType factoryType : FactoryType.values()) {
			JButton button = new JButton("Add " + factoryType.getName() + " (" + factoryType.getCost() + ")");
			button.setBounds(5, buttonsY+=30, 250, 25);
			button.addActionListener(e -> {
				try {
					IFactory factory = factoryType.getFactoryClass().getConstructor(int.class).newInstance(1);
					if (money.getOrDefault(factoryType, new Counter(0)).getValue() < factory.getCost()) {
						addToast("Not enough money to buy " + factoryType.getName() + " factory");
						return;
					} else {
						money.get(factoryType).subtract(factory.getCost());
					}
					// factory.paint(rootPanel, 10 + ((factories.size() + 1) * 16), 250);
					// Draw.drawFactory(rootPanel, factoryType);
					
					this.factories.add(factory);
					addToast("Added " + factoryType.getName() + " factory");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			createFactoriesButtons.put(factoryType, button);
			rootPanel.add(button);
		}
	}

	private void addToast(String text) {
		toasts.put(text, this.ticks);
	}

	private void tickToasts() {
		for (String toast : toasts.keySet()) {
			if (this.ticks - toasts.get(toast) >= 5000) {
				toasts.remove(toast);
				for (Component component : rootPanel.getComponents()) {
					if (component instanceof JLabel) {
						JLabel label = (JLabel) component;
						if (label.getText().equals(toast)) {
							rootPanel.remove(label);
							break;
						}
					}
				}
			} else {
				boolean foundRenderedToast = false;
				for (Component component : rootPanel.getComponents()) {
					if (component instanceof JLabel) {
						JLabel label = (JLabel) component;
						if (label.getText().equals(toast)) {
							foundRenderedToast = true;
							break;
						}
					}
				}
				if (!foundRenderedToast) {
					JLabel label = new JLabel(toast);
					int y = HEIGHT - (5 + (toasts.size() * 5) + 20);
					label.setBounds(5, y, 250, 20);
					rootPanel.add(label);
				}
			}
		}
	}

	public void unlockResource(FactoryType factoryType) {
		lockedResource.put(factoryType, false);
	}

	public static List<IFactory> getFactoriesOfType(FactoryType type) {
		List<IFactory> factories = new ArrayList<>();
		for (IFactory factory : App.factories) {
			if (factory.getType() == type) {
				factories.add(factory);
			}
		}
		return factories;
	}
}