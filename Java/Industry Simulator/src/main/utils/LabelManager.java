package main.utils;

import java.util.Map;

/**
 * The LabelManager will be a class which will store all of the labels,
 * create an enum with the type of the Label (e.g. TITLE, TEXT, BUTTON, etc.)
 * and a method to create a label with a specific type.
 * 
 * Each label should be stored to a map with the type as the key and the label as the value.
 */
public class LabelManager {

    private Map<LabelType, Label> labels;
    
    public class Label {
        private LabelType type;
        private String text;
        private int x;
        private int y;
        private int width;
        private int height;
        private boolean visible;
        
        public Label(LabelType type, String text, int x, int y, int width, int height, boolean visible) {
            this.type = type;
            this.text = text;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.visible = visible;
        }
        
        public LabelType getType() {
            return type;
        }
        
        public String getText() {
            return text;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        public int getWidth() {
            return width;
        }
        
        public int getHeight() {
            return height;
        }
        
        public boolean isVisible() {
            return visible;
        }
    }

    public enum LabelType {
        TITLE, TEXT, BUTTON
    }
}
