package factory;

import javax.swing.*;
import java.awt.*;

public interface IFactory {

	int getCost();

	default int getProductionPerTick() {
		return getProductionPerTick(1);
	}

	int getProductionPerTick(int level);

	int getLevel();

	IFactory setLevel(int level);

	int getUpgradeCost(int level);

	int getTicksUntilOverworked();

	boolean isOverworked();

	IFactory setOverworked(boolean overworked);

	FactoryType getType();

	void upgrade();

	void paint(JPanel panel, int x, int y);

	String getName();

	Image getImage();

}
