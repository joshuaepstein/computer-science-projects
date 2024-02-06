package main.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import main.factory.FactoryType;
import main.App;

public class Draw {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int[] PADDING = new int[] {10, 10, 10, 10};
    public static final int HEADING_HEIGHT = 100;
    public static final int SPACE_BETWEEN_FACTORIES = 50;

    public static void drawFactory(JPanel panel, FactoryType factoryType) {
		if (panel == null) {
            System.out.println("Panel is null");
            return;
        };
        Graphics g = panel.getGraphics();
		if (g == null) {
            System.out.println("Panel graphics is null");
            return;
        };
		if (!panel.isValid()){
            System.out.println("Panel is not valid");
			panel.validate();
        }
        int factoryTypeIndex = factoryType.ordinal();
        int imageWidth = factoryType.getWidth();
        int x = PADDING[0] + (factoryTypeIndex * imageWidth) + ((factoryTypeIndex + 1) * SPACE_BETWEEN_FACTORIES);
        int y = PADDING[1] + HEADING_HEIGHT;
        System.out.println("Drawing factory " + factoryType.getName() + " at " + x + ", " + y + " with width " + imageWidth);
        
        BufferedImage img = loadImage(factoryType.getImage());
        System.out.println("Loaded image " + factoryType.getImage() + " with width " + img.getWidth() + " and height " + img.getHeight());
        // img = colorImage(img, factoryType.getColor().getRed(), factoryType.getColor().getGreen(), factoryType.getColor().getBlue());
        tint(img, factoryType.getColor());
        
		panel.getGraphics().drawImage(img, x, y, imageWidth, imageWidth, new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				return false;
			}
		});
        // draw a border around the image
        g.setColor(Color.BLACK);
        g.drawRect(x, y, imageWidth, imageWidth);
        String content = String.valueOf(App.getFactoriesOfType(factoryType).size());
        int stringWith = g.getFontMetrics().stringWidth(content);
        g.drawString(content, x + (imageWidth / 2) - stringWith, y + imageWidth + 10);

        System.out.println("Drawing factory " + factoryType.getName());
    }

    public static void tint(BufferedImage image, Color color) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color pixelColor = new Color(image.getRGB(x, y), true);
                int r = (pixelColor.getRed() + color.getRed()) / 2;
                int g = (pixelColor.getGreen() + color.getGreen()) / 2;
                int b = (pixelColor.getBlue() + color.getBlue()) / 2;
                int a = pixelColor.getAlpha();
                int rgba = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgba);
            }
        }
    }

    public static BufferedImage loadImage(String url) {
        ImageIcon icon = new ImageIcon(url);
        Image image = icon.getImage();

        // Create empty BufferedImage, sized to Image
        BufferedImage buffImage = 
        new BufferedImage(
            image.getWidth(null), 
            image.getHeight(null), 
            BufferedImage.TYPE_INT_ARGB);

        // Draw Image into BufferedImage
        Graphics g = buffImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        return buffImage;
    }
}
