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
	Texture pipe,invertedPipe,background,bird;
	OrthographicCamera camera;
	
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
	int velocity;
	
	// libgdx representations of the bird and pipes to detect collisions
	Circle birdShape;
	
	Rectangle pipe1Upper,pipe1Lower;
	Rectangle pipe2Upper,pipe2Lower;
	Rectangle pipe3Upper,pipe3Lower;

	ShapeRenderer shapeRenderer;
	
	public myFlappyBird(){
		
		velocity = 0;
		sceneSpeed = 3;
		pipeDistance = 200;
		
		birdShape = new Circle();
		birdShape.radius = 25;
		
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
		
		
		bird = new Texture("bird.png");
		// create a texture for the background image
		background = new Texture("background.jpg");
				
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
		
		batch.setProjectionMatrix(camera.combined);
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
		
		birdShape.x = 200+35;
		birdShape.y = yBird+35;
	//	shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);				

	
		batch.begin();
		camera.update();
		// draw the background
		batch.draw(background, 0, 0,screenWidth,screenHeight);
		
		pipe1Lower.set(x,0,pipeWidth,pipeHeight1);
		pipe1Upper.set(x,pipeHeight1+ pipeDistance,pipeWidth,screenHeight - pipeHeight1- pipeDistance);
		
		pipe2Lower.set(x1,0,pipeWidth,pipeHeight2);
		pipe2Upper.set(x1,pipeHeight2+ pipeDistance,pipeWidth,screenHeight - pipeHeight2- pipeDistance);
		
		pipe3Lower.set(x2,0,pipeWidth,pipeHeight3);
		pipe3Upper.set(x2,pipeHeight3+ pipeDistance,pipeWidth,screenHeight - pipeHeight3- pipeDistance);

		
		// draw the pipes
		batch.draw(pipe, x, 0,pipeWidth,pipeHeight1);
		batch.draw(invertedPipe, x,pipeHeight1+ pipeDistance ,pipeWidth,screenHeight - pipeHeight1- pipeDistance);
		
		batch.draw(pipe, x1, 0,pipeWidth,pipeHeight2);
		batch.draw(invertedPipe, x1,pipeHeight2+ pipeDistance ,pipeWidth,screenHeight - pipeHeight2- pipeDistance);
		
		batch.draw(pipe, x2, 0,pipeWidth,pipeHeight3);
		batch.draw(invertedPipe, x2,pipeHeight3+ pipeDistance ,pipeWidth,screenHeight - pipeHeight3- pipeDistance);
		
		// draw the bird
		batch.draw(bird, xBird, yBird,70,70);
		
		batch.end();
	
		if(mode < 0){
			if(yBird > 0)
				yBird = yBird  - 10;
			return;
			
		}
		// check if the bird collides with any of the pipes
		if(Intersector.overlaps(birdShape, pipe1Upper) || Intersector.overlaps(birdShape, pipe1Lower)
				|| Intersector.overlaps(birdShape, pipe2Upper) || Intersector.overlaps(birdShape, pipe2Lower)
				|| Intersector.overlaps(birdShape, pipe3Upper) || Intersector.overlaps(birdShape, pipe3Lower)
				){
			
			mode = -1;
		
		}
		
		
		x = x - sceneSpeed;
		if(x < -screenWidth/2){
		
//		if(x < (-1 * pipeWidth)){
				
			x = Gdx.graphics.getWidth();
			pipeHeight1 = min + (int)(Math.random() * ((max - min) + 1));
		
			
		}

		x1 = x1 - sceneSpeed;
//		if(x1 < (-1 * pipeWidth)){
		if(x1 < -screenWidth/2){
			
			pipeHeight2 = min + (int)(Math.random() * ((max - min) + 1));
			x1 = Gdx.graphics.getWidth();
		}
		x2 = x2 - sceneSpeed;
//		if(x2 < (-1 * pipeWidth)){
		if(x2 < -screenWidth/2){
			pipeHeight3 = min + (int)(Math.random() * ((max - min) + 1));
			x2 = Gdx.graphics.getWidth();
		}	
		
		 if(Gdx.input.isTouched()) {
			 yBird = yBird  + 10;
			 birdShape.y = yBird+35;
		 }
		 else{
			 yBird = yBird  - 4;
			 birdShape.y = yBird+35;
		 }
		
		
	}
	
}
