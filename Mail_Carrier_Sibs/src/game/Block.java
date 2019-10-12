package game;

import java.util.Arrays;

public class Block {
	
/*
 * I thought it would be good to store the properties of blocks in a boolean array. That way, when we want to add
 * another property, all we need to do is change the way the files are read and written. We won't need to mess
 * with the constructor at all.
 */
	public static final int SOLID = 0;
	public static final int MAILBOX = 1;
	public static final int NUMBER_OF_PROPERTIES = 2; //this number should equal however many property integers are 
	// listed above it, so update it every time a new property is added.
	private boolean[] properties = new boolean[NUMBER_OF_PROPERTIES];
	
	private String imageName;
	
	public Block(String imageFileName, boolean[] attributes) {
		for(int i = 0; i < attributes.length; i++) {
			properties[i] = attributes[i];
		}
		
		imageName = imageFileName;
	}
	
	public Block(String imageFileName) {
		this(imageFileName, new boolean[NUMBER_OF_PROPERTIES]);
	}
	
	public Block() {
		this("blockUno.png", new boolean[NUMBER_OF_PROPERTIES]);
	}

	public String getImageFileName() {
		return imageName;
	}
	
	public boolean[] getProperties() {
		boolean[] newProperties = new boolean[NUMBER_OF_PROPERTIES];
		for(int i = 0; i < NUMBER_OF_PROPERTIES; i ++) {
			newProperties[i] = properties[i];
		}
		return newProperties;
	}
	
	
	/*
	 * returns whether this block has the given attrubute. For example, if this block is solid - meaning that
	 * properties[0] = true - then is(SOLID) will return true.
	 */
	public boolean is(int attribute) {
		return properties[attribute];
	}
	
	public void set(int attribute, boolean value) {
		properties[attribute] = value;
	}
	
	public boolean equals(Block comp) {
		System.out.println("comparing blocks!");
		if(imageName.equals(comp.getImageFileName()) && Arrays.equals(properties, comp.getProperties())){
			return false;
		}else {
			return true;
		}
	}
	
	/*
	 * NOTE: Let's leave the block's draw() function in the Screen class because the way that the block is drawn is
	 * heavily dependent on outside factors.
	 */
}
