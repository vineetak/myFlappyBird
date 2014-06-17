package com.gdx.myFlappyBird;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Bird {

	Circle shape;

	Bird(int radius,int x , int y){
		
		shape = new Circle();
		shape.radius = radius;
		shape.x = x;
		shape.y = y;
	}
	
	boolean collides(Pipe p){
		
		return (Intersector.overlaps(shape, p.upper) || Intersector.overlaps(shape, p.lower));
		//return p.overlaps(shape);
		
	}
	boolean collides(Rectangle r){
		
		return Intersector.overlaps(shape, r); 
	}
	
	void setHeight(int height){
		
		shape.y = height;
	}
	
	void setPosition(int x,int y){
		
		shape.x = x;
		shape.y = y;
	}
}
