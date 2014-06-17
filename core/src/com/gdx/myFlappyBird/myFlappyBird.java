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
	Texture pipeTexture,invertedPipe,background,birdTexture;

	int  gameMode; // 0 running -1 gameover
	int start = -1;
	
	int x1,x2,x3, xBird,yBird ;
	int screenWidth , screenHeight;
	int pipeWidth,pipeHeight1,pipeHeight2,pipeHeight3;

	// minimum and maximum pipe height
	int minPipeHeight ;
	int maxPipeHeight ;

	// vertical distance between upper and lower parts of a pipe
	int pipeDistance;

	// speed with which the pipes are moving left
	int sceneSpeed;

	Bird bird;
	Pipe pipe1,pipe2,pipe3;

	public myFlappyBird(){

		bird = new Bird(25,0,0);

		pipe1 = new Pipe();
		pipe2 = new Pipe();
		pipe3 = new Pipe();

		gameMode = 0; // game running

		sceneSpeed = 4;
		pipeDistance = 200;
	}

	@Override
	public void create () {
		// create a new sprite batch to render the graphics
		batch = new SpriteBatch();

		// create a texture for the pipes
		pipeTexture = new Texture("pipe.jpg");
		invertedPipe = new Texture("invertedPipe.jpg");

		// create the bird texture
		birdTexture = new Texture("bird.png");

		// create a texture for the background image
		background = new Texture("background.jpg");

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		minPipeHeight = 200;
		maxPipeHeight = screenHeight - 350;

		pipeWidth = screenWidth / 10;

		x1 = screenWidth;
		x2 = screenWidth + screenWidth/2 ;
		x3 = x2  + screenWidth/2;

		// generate the initial pipe heights randomly
		pipeHeight1 = minPipeHeight + (int)(Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
		pipeHeight2 = minPipeHeight + (int)(Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
		pipeHeight3 = minPipeHeight + (int)(Math.random() * ((maxPipeHeight - minPipeHeight) + 1));

		xBird = 200;
		yBird = screenHeight/2;

		bird.setPosition(xBird+35 , yBird +35);
	}

	@Override
	public void render () {

		// draw the Scene
		drawScene();

		if(Gdx.input.justTouched() ) {
			start = 1;
			yBird = yBird  + 75;
			bird.setHeight(yBird + 35); 
		}
		else if(start > 0){
			yBird = yBird  - 6;
			bird.setHeight(yBird + 35);
		}
		
		if(start < 0)
			return;
		// if the game is already over then make the bird fall and do not update the pipes
		if(gameMode < 0){
			if(yBird > 0){
				yBird = yBird  - 10;
			}
			return;
		}

		// game over if there is a collision
		if(checkForCollision()){
			return;
		}

		// update the pipes in the scene
		updatePipePositions();

		// whenever the screen is touched move the bird upwards
		

	}

	boolean checkForCollision(){

		pipe1.setLowerPipePosition(x1, 0);
		pipe1.setUpperPipePosition(x1,pipeHeight1+ pipeDistance);
		pipe1.setLowerHeight(pipeHeight1);
		pipe1.setPipeWidth(pipeWidth);
		pipe1.setUpperHeight(screenHeight - pipeHeight1- pipeDistance);

		pipe2.setLowerPipePosition(x2, 0);
		pipe2.setUpperPipePosition(x2,pipeHeight2+ pipeDistance);
		pipe2.setLowerHeight(pipeHeight2);
		pipe2.setPipeWidth(pipeWidth);
		pipe2.setUpperHeight(screenHeight - pipeHeight2- pipeDistance);

		pipe3.setLowerPipePosition(x3, 0);
		pipe3.setUpperPipePosition(x3,pipeHeight3 + pipeDistance);
		pipe3.setLowerHeight(pipeHeight3);
		pipe3.setPipeWidth(pipeWidth);
		pipe3.setUpperHeight(screenHeight - pipeHeight3 - pipeDistance);


		if(bird.collides(pipe1) || bird.collides(pipe2) || bird.collides(pipe3)){
			gameMode = -1; // game over
			return true;
		}

		return false;
	}
	void updatePipePositions(){

		x1 = x1 - sceneSpeed;
		if(x1 < -screenWidth/2){
			x1 = screenWidth;
			pipeHeight1 = minPipeHeight + (int)(Math.random() * ((maxPipeHeight - minPipeHeight) + 1));		
		}

		x2 = x2 - sceneSpeed;
		if(x2 < -screenWidth/2){

			pipeHeight2 = minPipeHeight + (int)(Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
			x2 = screenWidth;
		}

		x3 = x3 - sceneSpeed;
		if(x3 < -screenWidth/2){
			pipeHeight3 = minPipeHeight + (int)(Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
			x3 = screenWidth;
		}	

		return;
	}

	void drawScene(){

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		// draw the background
		batch.draw(background, 0, 0,screenWidth,screenHeight);		

		// draw the pipes
		batch.draw(pipeTexture, x1, 0,pipeWidth,pipeHeight1);
		batch.draw(invertedPipe, x1,pipeHeight1+ pipeDistance ,pipeWidth,screenHeight - pipeHeight1- pipeDistance);

		batch.draw(pipeTexture, x2, 0,pipeWidth,pipeHeight2);
		batch.draw(invertedPipe, x2,pipeHeight2+ pipeDistance ,pipeWidth,screenHeight - pipeHeight2- pipeDistance);

		batch.draw(pipeTexture, x3, 0,pipeWidth,pipeHeight3);
		batch.draw(invertedPipe, x3,pipeHeight3+ pipeDistance ,pipeWidth,screenHeight - pipeHeight3- pipeDistance);

		// draw the bird
		batch.draw(birdTexture, xBird, yBird,70,70);
		batch.end();

		return;
	}
}
