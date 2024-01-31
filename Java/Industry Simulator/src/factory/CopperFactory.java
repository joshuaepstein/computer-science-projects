package factory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class CopperFactory implements IFactory {

	private int level;
	private boolean overworked;

	public CopperFactory() {
		this(1);
	}

	public CopperFactory(int level) {
		this.level = level;
	}

	@Override
	public int getCost() {
		return 200;
	}

	@Override
	public int getProductionPerTick(int level) {
		return 5;
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
		return level <= 5 ? 100 : 200;
	}

	@Override
	public int getTicksUntilOverworked() {
		return 500;
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
		return new ImageIcon("src/factory/images/Factory.png").getImage();
	}

	@Override
	public void paint(JPanel panel, int x, int y) {
		if (panel == null) return;
		if (panel.getGraphics() == null) return;
		if (!panel.isValid())
			panel.validate();
		Image image = getImage();
		Image orangeImage = new ImageIcon("src/factory/images/FactoryCopper.png").getImage();
		panel.getGraphics().drawImage(orangeImage, x, y, 16, 16, new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				return false;
			}
		});
	}

	@Override
	public String getName() {
		return "Copper Factory";
	}

	@Override
	public FactoryType getType() {
		return FactoryType.COPPER;
	}
}
