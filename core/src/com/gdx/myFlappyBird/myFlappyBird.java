package com.gdx.myFlappyBird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class myFlappyBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture pipeTexture,invertedPipe,background,birdTexture,gameOverTexture,tapTexture,replayTexture;
	// to display the score
	private BitmapFont font;

	Music gameOverMusic;//,tapMusic;
	Sound tapSound;
	int  gameMode = 0; // -1 game over 0 start 1 running
	boolean played = false;
	
	int x1,x2,x3, xBird ;
	int scoreLine,score;
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

		gameMode = 0; // game start

		sceneSpeed = 4;
		pipeDistance = 200;
		
		velocityY = 0;
		backgroundX = 0;
		
	}

	@Override
	public void create () {
		// create a new sprite batch to render the graphics
		batch = new SpriteBatch();
		font = new BitmapFont();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        	
        font.setScale(5,5);
        
//        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("fall.mp3"));
        
        gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("fall.mp3"));
        gameOverMusic.setLooping(false);
        
        tapSound = Gdx.audio.newSound(Gdx.files.internal("tap.mp3"));
        //tapSound.setLooping(false);

        // create a texture for the pipes
		pipeTexture = new Texture("pipe.jpg");
		invertedPipe = new Texture("invertedPipe.jpg");

		// create the bird texture
		birdTexture = new Texture("bird.png");

		// create a texture for the background image
		background = new Texture("background.jpg");

		gameOverTexture = new Texture("gameover.png");
		replayTexture = new Texture("replay.png");
		
		tapTexture = new Texture("hand.png");
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		minPipeHeight = 200;
		maxPipeHeight = screenHeight - 350;

		pipeWidth = screenWidth / 10;

		x1 = screenWidth;
		x2 = screenWidth + screenWidth/2 ;
		x3 = x2  + screenWidth/2;
		scoreLine = 1;
		
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
		// if the game is over this is not done
		if(Gdx.input.justTouched() && gameMode >= 0) {
			gameMode = 1;
			velocityY = 14;
			tapSound.play();

		}
		
		// if the game is running
		if(gameMode == 1){

			// decrease the bird velocity
			velocityY = velocityY - 0.8;
			
			// move the bird upward as per the velocity
			yBird = yBird + velocityY;
			
			if(yBird + 70 <= screenHeight){ // bird should not go above the screen height
				bird.setHeight((int) (yBird + 35));
			}
			else // if the bird moves beyond the screen height set the velocity to zero so that it comes down
				velocityY = -1;
		
		}

		calculateScore();
		
		// if the screen is not touched then the scene does not move
		if(gameMode == 0)
			return;
		
		// if the game is already over then make the bird fall and do not update the pipes
		if(gameMode < 0){
			tapSound.stop();
			
			if(played == false){
				gameOverMusic.play();
				played = true;
			}
			
			if(yBird > -10){
				yBird = yBird  - 15;
				
			}
			
			// if the screen is tapped again 
			else if(Gdx.input.justTouched() ) {
				restart();
			}
			return;
		}

		// if the bird falls below the screen then game over
		if(yBird <= 0){
			gameMode = -1;
		}
		// game over if there is a collision
		if(checkForCollision()){
			return;
		}

		// move the pipes in the scene towards left
		updatePipePositions();
		
		return;
	}

	private void calculateScore() {
		int scoreLinePosition = 0;
		if(scoreLine == 1){
			scoreLinePosition = x1;
		}
		else if(scoreLine == 2){
			scoreLinePosition = x2;
		}
		else if(scoreLine == 3){
			scoreLinePosition = x3;
		}
		
		if(xBird >= scoreLinePosition){
			score++;
			scoreLine = scoreLine + 1;
			if(scoreLine > 3)
				scoreLine = 1;
		}
	}

	private void restart() {
		
		gameOverMusic.stop();
		velocityY = 0;
		backgroundX = 0;
		
		x1 = screenWidth;
		x2 = screenWidth + screenWidth/2 ;
		x3 = x2  + screenWidth/2;
		
		gameMode = 0; // game start
		
		yBird = screenHeight/2;

		bird.setPosition(xBird+35 , (int) (yBird +35));
		
		score = 0;
		scoreLine = 1;
		
		played = false;
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		// display tap 
	
		
		// draw the moving background
		float temp = (float) (screenWidth - (screenWidth * backgroundX)); 
		batch.draw(background,0 ,0,temp,screenHeight,(float)(backgroundX),1,1,0);
		batch.draw(background,temp,0,(float) (screenWidth*backgroundX),screenHeight,0,1,(float)backgroundX,0);

		// draw the pipes
		batch.draw(pipeTexture, x1, 0,pipeWidth,pipeHeight1);
		batch.draw(invertedPipe, x1,pipeHeight1+ pipeDistance ,pipeWidth,screenHeight - pipeHeight1- pipeDistance);

		batch.draw(pipeTexture, x2, 0,pipeWidth,pipeHeight2);
		batch.draw(invertedPipe, x2,pipeHeight2+ pipeDistance ,pipeWidth,screenHeight - pipeHeight2- pipeDistance);

		batch.draw(pipeTexture, x3, 0,pipeWidth,pipeHeight3);
		batch.draw(invertedPipe, x3,pipeHeight3+ pipeDistance ,pipeWidth,screenHeight - pipeHeight3- pipeDistance);

		// draw the bird
		batch.draw(birdTexture, xBird, (float) yBird,70,70);
		if(gameMode == 0  )
		{
			batch.draw(tapTexture, screenWidth/2-50, screenHeight/2-100, 100, 100);
		}
		// draw the gameover text
		if(gameMode < 0){
			batch.draw(gameOverTexture, screenWidth/2-150, screenHeight/2-50,300,150);
			batch.draw(replayTexture, screenWidth/2-75, screenHeight/2-250, 150, 150);
			
	       // gameOverSound.play();

		}
		// draw the score
		if(score > 0){
			String scoreString = Integer.toString(score);
			font.draw(batch, scoreString, screenWidth/2-20, screenHeight/2+ 300);
		}
		batch.end();

//		if(start > 0 && gameMode == 0){
		if(gameMode == 1){
		backgroundX = backgroundX + 0.002;
		if(backgroundX > 1)
			backgroundX = 0;
		}
		
		
		
		return;
	}
}
