package com.gdx.myFlappyBird;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Pipe {

	Rectangle upper,lower;

	public Pipe(){
		upper = new Rectangle();
		lower = new Rectangle();		
	}
	
	void setLowerHeight(int lowerHeight ){
		
		lower.height = lowerHeight; 
	}
	
	void setUpperHeight(int upperHeight){
		upper.height = upperHeight;
	}
	
	void setPipeWidth(int pipeWidth){
		lower.width = pipeWidth;
		upper.width = pipeWidth;
	}
	
	void setLowerPipePosition(int lowerX, int lowerY){
		lower.x = lowerX;
		lower.y = lowerY;

		
	}
	
	void setUpperPipePosition(int x, int y){
		upper.x = x;
		upper.y = y;
	}
	
	boolean overlaps(Circle c){
		
		return (Intersector.overlaps(c, upper) || Intersector.overlaps(c, lower));
	}
}
