package main.factory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class CoalFactory implements IFactory {

	private int level;
	private boolean overworked;

	public CoalFactory() {
		this(1);
	}

	public CoalFactory(int level) {
		this.level = level;
	}

	@Override
	public int getCost() {
		if (level < 5) return 50;
		return 100;
	}

	@Override
	public int getProductionPerTick(int level) {
		return 100;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public IFactory setLevel(int level) {
		this.level = level;
		return this;
	}

	@Override
	public int getUpgradeCost(int level) {
		return level <= 5 ? 50 : 100;
	}

	@Override
	public int getTicksUntilOverworked() {
		return 0;
	}

	@Override
	public boolean isOverworked() {
		return this.overworked;
	}

	@Override
	public IFactory setOverworked(boolean overworked) {
		this.overworked = overworked;
		return this;
	}

	@Override
	public void upgrade() {

	}

	@Override
	public Image getImage() {
		return new ImageIcon("src/main/factory/images/Factory.png").getImage();
	}

	@Override
	public void paint(JPanel panel, int x, int y) {
		if (panel == null) return;
		if (panel.getGraphics() == null) return;
		if (!panel.isValid())
			panel.validate();
			
		panel.getGraphics().drawImage(getImage(), x, y, 16, 16, new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				return false;
			}
		});
	}

	@Override
	public String getName() {
		return "Coal Factory";
	}

	@Override
	public FactoryType getType() {
		return FactoryType.COAL;
	}

	@Override
	public Color getColor() {
		return Color.BLACK;
	}
}
