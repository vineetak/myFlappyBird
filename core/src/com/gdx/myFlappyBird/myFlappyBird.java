package com.gdx.myFlappyBird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class myFlappyBird extends ApplicationAdapter {
	
	SpriteBatch batch;
	Texture pipe,invertedPipe,background,birdTexture;
	
	int  mode; // 0 running -1 over
	int x,x1,x2, xBird,yBird ;
	int screenWidth , screenHeight;
	int midX;
	int pipeWidth,pipeHeight1,pipeHeight2,pipeHeight3;

	// minimum and maximum height
	int min ;
	int max ;
	
	int pipeDistance;
	int sceneSpeed;
	
	// libgdx representations of the bird and pipes to detect collisions
	
	Bird bird;

	Rectangle pipe1Upper,pipe1Lower;
	Rectangle pipe2Upper,pipe2Lower;
	Rectangle pipe3Upper,pipe3Lower;

	ShapeRenderer shapeRenderer;
	
	public myFlappyBird(){
		
		sceneSpeed = 3;
		pipeDistance = 200;
		
		bird = new Bird(25,0,0);
		
		pipe1Upper = new Rectangle();
		pipe1Lower = new Rectangle();
		
		pipe2Upper = new Rectangle();
		pipe2Lower = new Rectangle();
		
		pipe3Upper = new Rectangle();
		pipe3Lower = new Rectangle();
		
		mode = 0;
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		// create a texture for the pipe image
		pipe = new Texture("pipe.jpg");
		invertedPipe = new Texture("invertedPipe.jpg");
				
		birdTexture = new Texture("bird.png");
		// create a texture for the background image
		background = new Texture("background.jpg");
				
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		

		min = 200;
		max = screenHeight - 350;
		
		midX = screenWidth /2;
		
		pipeWidth = screenWidth / 10;
		
//		x = screenWidth;
//		x1 = screenWidth + screenWidth/3 ;
//		x2 = x1  + screenWidth/3;
		
		x = screenWidth;
		x1 = screenWidth + screenWidth/2 ;
		x2 = x1  + screenWidth/2;
		
		pipeHeight1 = min + (int)(Math.random() * ((max - min) + 1));
		pipeHeight2 = min + (int)(Math.random() * ((max - min) + 1));
		pipeHeight3 = min + (int)(Math.random() * ((max - min) + 1));
						
		xBird = 200;
		yBird = Gdx.graphics.getHeight()/2;
		
		bird.setPosition(xBird + 35, yBird +35);
	}

	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);				

		batch.begin();

		// draw the background
		batch.draw(background, 0, 0,screenWidth,screenHeight);		
		
		// draw the pipes
		batch.draw(pipe, x, 0,pipeWidth,pipeHeight1);
		batch.draw(invertedPipe, x,pipeHeight1+ pipeDistance ,pipeWidth,screenHeight - pipeHeight1- pipeDistance);
		
		batch.draw(pipe, x1, 0,pipeWidth,pipeHeight2);
		batch.draw(invertedPipe, x1,pipeHeight2+ pipeDistance ,pipeWidth,screenHeight - pipeHeight2- pipeDistance);
		
		batch.draw(pipe, x2, 0,pipeWidth,pipeHeight3);
		batch.draw(invertedPipe, x2,pipeHeight3+ pipeDistance ,pipeWidth,screenHeight - pipeHeight3- pipeDistance);
		
		// draw the bird
		batch.draw(birdTexture, xBird, yBird,70,70);
		
		batch.end();
	
		// if the game is over then make the bird fall
		if(mode < 0){
			if(yBird > 0)
				yBird = yBird  - 10;
				bird.setHeight(yBird);
				
			return;
			
		}

		// set the dimensions of the pipes to detect collision
		pipe1Lower.set(x,0,pipeWidth,pipeHeight1);
		pipe1Upper.set(x,pipeHeight1+ pipeDistance,pipeWidth,screenHeight - pipeHeight1- pipeDistance);
		
		pipe2Lower.set(x1,0,pipeWidth,pipeHeight2);
		pipe2Upper.set(x1,pipeHeight2+ pipeDistance,pipeWidth,screenHeight - pipeHeight2- pipeDistance);
		
		pipe3Lower.set(x2,0,pipeWidth,pipeHeight3);
		pipe3Upper.set(x2,pipeHeight3+ pipeDistance,pipeWidth,screenHeight - pipeHeight3- pipeDistance);

		// check if the bird collides with any of the pipes
		if (bird.collides(pipe1Upper) || bird.collides(pipe1Lower) ||
				bird.collides(pipe2Upper) || bird.collides(pipe2Lower) ||
				bird.collides(pipe3Upper) || bird.collides(pipe3Lower)){
		
			mode = -1;
			return;
		}
		
		// if the bird did not collide update the screen 
		x = x - sceneSpeed;
		if(x < -screenWidth/2){
			x = screenWidth;
			pipeHeight1 = min + (int)(Math.random() * ((max - min) + 1));		
		}

		x1 = x1 - sceneSpeed;
		if(x1 < -screenWidth/2){
			
			pipeHeight2 = min + (int)(Math.random() * ((max - min) + 1));
			x1 = screenWidth;
		}

		x2 = x2 - sceneSpeed;
		if(x2 < -screenWidth/2){
			pipeHeight3 = min + (int)(Math.random() * ((max - min) + 1));
			x2 = screenWidth;
		}	
		
		// process the touch input 
		// whenever the screen is touch move the bird up
		 if(Gdx.input.isTouched()) {
			 yBird = yBird  + 10;
			 bird.setHeight(yBird);
		 }
		 else{
			 yBird = yBird  - 4;
			 bird.setHeight(yBird);
		 }
		
	}
	
}
