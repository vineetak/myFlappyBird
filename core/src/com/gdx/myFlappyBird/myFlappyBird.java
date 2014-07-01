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
	
	int x1,x2,x3, xBird ;
	double yBird;
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

	double velocityY;
	double backgroundX;
	
	public myFlappyBird(){

		bird = new Bird(25,0,0);

		pipe1 = new Pipe();
		pipe2 = new Pipe();
		pipe3 = new Pipe();

		gameMode = 0; // game running

		sceneSpeed = 4;
		pipeDistance = 200;
		
		velocityY = 0;
		backgroundX = 0;
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

		bird.setPosition(xBird+35 , (int) (yBird +35));
		return;
	}

	@Override
	public void render () {

		// draw the Scene
		drawScene();

		// whenever the screen is touched move the bird upwards
		if(Gdx.input.justTouched() ) {
			start = 1;
			velocityY = velocityY + 12;
		}
		else if(start > 0){  // the game starts only after the first touch
			velocityY = velocityY - 0.45;
		}
		
		// if the game is running
		if(gameMode >= 0){
			// move the bird with distance
			yBird = yBird + velocityY;
			
			if(yBird + 70 <= screenHeight){ // bird should not go above the screen height
				bird.setHeight((int) (yBird + 35));
			}
			else // if the bird moves beyond the screen height set the velocity to zero so that it comes down
				velocityY = 0;
		}
		
		
		// if the screen is not touched then the scene does not move
		if(start < 0)
			return;
		
		// if the game is already over then make the bird fall and do not update the pipes
		if(gameMode < 0){
			if(yBird > -10){
				yBird = yBird  - 10;
			}
			return;
		}

		// game over if there is a collision
		if(checkForCollision()){
			return;
		}

		// move the pipes in the scene towards left
		updatePipePositions();
		
		return;
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
//		batch.draw(background, 0, 0,screenWidth,screenHeight,0,1,1,0);		

		float temp = (float) (screenWidth - (screenWidth * backgroundX)); 
		batch.draw(background,0 ,0,temp,screenHeight,(float)(backgroundX),1,1,0);
		
		batch.draw(background,temp,0,(float) (screenWidth*backgroundX),screenHeight,0,1,(float)backgroundX,0);
//		batch.draw(background,(float)(screenWidth * backgroundX ),0,(float) ((float)screenWidth * backgroundX),(float)screenHeight,0,1,(float)backgroundX,0);
		//batch.draw(background, (float) (screenWidth*backgroundX),0,temp,screenHeight,(float)1,(float)1,(float)(1-backgroundX),(float)0);
		// draw the pipes
		batch.draw(pipeTexture, x1, 0,pipeWidth,pipeHeight1);
		batch.draw(invertedPipe, x1,pipeHeight1+ pipeDistance ,pipeWidth,screenHeight - pipeHeight1- pipeDistance);

		batch.draw(pipeTexture, x2, 0,pipeWidth,pipeHeight2);
		batch.draw(invertedPipe, x2,pipeHeight2+ pipeDistance ,pipeWidth,screenHeight - pipeHeight2- pipeDistance);

		batch.draw(pipeTexture, x3, 0,pipeWidth,pipeHeight3);
		batch.draw(invertedPipe, x3,pipeHeight3+ pipeDistance ,pipeWidth,screenHeight - pipeHeight3- pipeDistance);

		// draw the bird
		batch.draw(birdTexture, xBird, (float) yBird,70,70);
		batch.end();

		if(start > 0 && gameMode == 0){
		backgroundX = backgroundX + 0.002;
		if(backgroundX > 1)
			backgroundX = 0;
		}
		return;
	}
}
