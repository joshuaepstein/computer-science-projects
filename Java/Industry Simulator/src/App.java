import factory.CoalFactory;
import factory.FactoryType;
import factory.IFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;

	public List<IFactory> factories;
	private int gameLevel = 1;
	private final JFrame frame;
	private final JPanel rootPanel;
	private final Map<FactoryType, JLabel> resourcesLabels;
	private final Map<FactoryType, JButton> createFactoriesButtons;
	private final Map<String, Long> toasts = new HashMap<>();
	private final Map<FactoryType, Boolean> lockedResource = new HashMap<>() {{
		put(FactoryType.COAL, false);
	}};

	private Map<FactoryType, Counter> money = new HashMap<>() {{
		put(FactoryType.COAL, new Counter(100));
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

		for (FactoryType factoryType : FactoryType.values()) {
			JLabel label = new JLabel(factoryType.getNameAsCamel() + ": 0");
			label.setBounds(2, factoryType.ordinal() * 20, 250, 25);
			resourcesLabels.put(factoryType, label);
			rootPanel.add(label);
		}

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

			for (FactoryType factoryType : FactoryType.values()) {
				resourcesLabels.get(factoryType).setText(factoryType.getNameAsCamel() + ": " + money.getOrDefault(factoryType, new Counter(0)).getValue());
			}
		});
		timer.setRepeats(true);
		timer.start();

		frame.setLayout(null);
		frame.setVisible(true);
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
					factory.paint(rootPanel, 10 + ((factories.size() + 1) * 16), 250);
					
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
}