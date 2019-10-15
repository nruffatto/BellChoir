package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Testing {

	public static void main(String[] args) {
		Map map1 = openFile("mapTest1.txt");
		Map map2 = openFile("mapTest2.txt");
		System.out.println(map1.equals(map2));
	}	
	
	private static Map openFile(String fileName) {
		Map map = new Map(1, 1);
		System.out.println("opening...");
		File f1 = new File(fileName);
		Scanner s1;
		int width;
		int height;
		int xCoord;
		int yCoord;
		String imageFileName;
		int numBlockPropertiesOfFile;
		boolean[] blockProperties = new boolean[Block.NUMBER_OF_PROPERTIES];
		try {
			s1 = new Scanner(f1);
			width = s1.nextInt();
			height = s1.nextInt();
			numBlockPropertiesOfFile = s1.nextInt();
			map.resize(width, height);
			while(s1.hasNextLine() && s1.hasNext()) {
				xCoord = s1.nextInt();
				yCoord = s1.nextInt();
				imageFileName = s1.next();
				for(int i = 0; i < numBlockPropertiesOfFile; i ++) {
					blockProperties[i] = s1.nextBoolean();
				}
				map.insertBlock(xCoord, yCoord, new Block(imageFileName, blockProperties));
			}
			System.out.println("done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}

}
