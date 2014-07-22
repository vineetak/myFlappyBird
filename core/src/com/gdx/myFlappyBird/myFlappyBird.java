package com.gdx.myFlappyBird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class myFlappyBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture pipeTexture, invertedPipe, background, birdTexture,
			gameOverTexture, tapTexture, replayTexture, //fontTexture,
			gameOverBackgroundTexture, trophyTexture,newHighScoreTexture;
	// to display the score
	private BitmapFont scoreFont, highScoreFont;

	Music gameOverMusic;
	Sound tapSound, scoreSound,highScoreSound,hitSound,clickSound;
	
	int gameMode = 0; // -1 game over 0 start 1 running
	boolean played = false;
	boolean newHighScore = false;
	
	int x1, x2, x3, x4, xBird;
	int scoreLine, score;
	double yBird;
	int screenWidth, screenHeight;
	int pipeWidth, pipeHeight1, pipeHeight2, pipeHeight3, pipeHeight4;

	// minimum and maximum pipe height
	int minPipeHeight;
	int maxPipeHeight;

	// vertical distance between upper and lower parts of a pipe
	int pipeDistance;

	// speed with which the pipes are moving left
	int sceneSpeed;

	Bird bird;
	Pipe pipe1, pipe2, pipe3, pipe4;

	double velocityY;
	double backgroundX;

	private static final String HIGHEST_SCORE = "highest.score";
	private static final String PREFS_NAME = "flappy_bird";

	public myFlappyBird() {

		bird = new Bird(25, 0, 0);

		pipe1 = new Pipe();
		pipe2 = new Pipe();
		pipe3 = new Pipe();
		pipe4 = new Pipe();

		gameMode = 0; // game start

		sceneSpeed = 4;
		pipeDistance = 210;

		velocityY = 0;
		backgroundX = 0;

	}

	@Override
	public void create() {
		// create a new sprite batch to render the graphics
		batch = new SpriteBatch();

	//	gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("fall.mp3"));
		//gameOverMusic.setLooping(false);

		tapSound = Gdx.audio.newSound(Gdx.files.internal("tap.mp3"));
		scoreSound = Gdx.audio.newSound(Gdx.files.internal("score.wav"));
		highScoreSound = Gdx.audio.newSound(Gdx.files.internal("highScore.mp3"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
		
		// create a texture for the pipes
		pipeTexture = new Texture("pipe.jpg");
		invertedPipe = new Texture("invertedPipe.jpg");

		// create the bird texture
		birdTexture = new Texture("bird.png");

		// create a texture for the background image
		background = new Texture("background.jpg");

		gameOverTexture = new Texture("gameover.png");
		replayTexture = new Texture("play.png");

		tapTexture = new Texture("hand.png");

		//fontTexture = new Texture("font.png");

		gameOverBackgroundTexture = new Texture("scoreboard.jpg");
		
		trophyTexture = new Texture("trophy.png");

		newHighScoreTexture = new Texture("highScore.png");
		
		// font = new BitmapFont(Gdx.files.internal("font.fnt"), new
		// TextureRegion(fontTexture), false);
		scoreFont = new BitmapFont();
		scoreFont.setColor(0.1f, 0.1f, 0.1f, 1.0f);
		scoreFont.setScale(5, 5);

		highScoreFont = new BitmapFont();
		highScoreFont.setColor(1, 1, 1, 1);
		highScoreFont.setScale(4);

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		minPipeHeight = 100;
		maxPipeHeight = screenHeight - 250;

		/* Code for 3 pipes */
		// pipeWidth = screenWidth / 10;
		// x1 = screenWidth;
		// x2 = screenWidth + screenWidth/2 ;
		// x3 = x2 + screenWidth/2;

		pipeWidth = screenWidth / 12;
		x1 = screenWidth - screenWidth / 3;
		x2 = x1 + screenWidth / 3;
		x3 = x2 + screenWidth / 3;
		x4 = x3 + screenWidth / 3;
		scoreLine = 1;

		// generate the initial pipe heights randomly
		pipeHeight1 = minPipeHeight
				+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
		pipeHeight2 = minPipeHeight
				+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
		pipeHeight3 = minPipeHeight
				+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
		pipeHeight4 = minPipeHeight
				+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));

		xBird = 200;
		yBird = screenHeight / 2;

		bird.setPosition(xBird + 35, (int) (yBird + 35));
		return;
	}

	@Override
	public void render() {

		// draw the Scene
		drawScene();

		// whenever the screen is touched move the bird upwards
		// if the game is over this is not done
		if (Gdx.input.justTouched() && gameMode >= 0) {
			gameMode = 1;
			velocityY = 13;
			tapSound.play((float) 0.5);

		}

		// if the game is running
		if (gameMode == 1) {

			// decrease the bird velocity
			velocityY = velocityY - 1;

			// move the bird upward as per the velocity
			yBird = yBird + velocityY;

			if (yBird + 70 <= screenHeight) { // bird should not go above the
												// screen height
				bird.setHeight((int) (yBird + 35));
			} else
				// if the bird moves beyond the screen height set the velocity
				// to zero so that it comes down
				velocityY = -1;

			calculateScore();
		}

		// if the screen is not touched then the scene does not move
		if (gameMode == 0)
			return;

		// if the game is already over then make the bird fall and do not update
		// the pipes
		if (gameMode == -1) {
			tapSound.stop();

			// to avoid playing the music again and again
//			if (played == false) {
//			//	gameOverMusic.play();
//				played = true;
//			}

			if (yBird > -10) {
				yBird = yBird - 15;

			}
			else if(played == false){
				played = true;
				hitSound.play();
			}
			// if the screen is tapped again
			else if (Gdx.input.justTouched()) {
				// play the button click sound
				clickSound.play();
				restart();
			}
			return;
		}

		// if the bird falls below the screen then game over
		if (yBird <= 0 || checkForCollision()) {
			gameMode = -1;
			return;
		}

		// move the pipes in the scene towards left
		updatePipePositions();
		return;
	}

	private void calculateScore() {
		int scoreLinePosition = 0;
		if (scoreLine == 1) {
			scoreLinePosition = x1;
		} else if (scoreLine == 2) {
			scoreLinePosition = x2;
		} else if (scoreLine == 3) {
			scoreLinePosition = x3;
		} else if (scoreLine == 4) {
			scoreLinePosition = x4;
		}
		if (xBird >= scoreLinePosition) {
			tapSound.stop();

			scoreSound.play();
			score++;

			int h = getHighestScore();
			// update the highest score
			if (score > h) {
				
				if(newHighScore == false){
					scoreSound.stop();
					highScoreSound.play();
					newHighScore = true;
					// play the new high score sound
				}
					
					
				System.out.println(h + " " + score);
				setHighestScore(score);
			}
			scoreLine = scoreLine + 1;
			if (scoreLine > 4)
				scoreLine = 1;
		}
	}

	private void restart() {

		//gameOverMusic.stop();
		velocityY = 0;
		backgroundX = 0;

		// x1 = screenWidth;
		// x2 = screenWidth + screenWidth/2 ;
		// x3 = x2 + screenWidth/2;

		x1 = screenWidth - screenWidth / 3;
		x2 = x1 + screenWidth / 3;
		x3 = x2 + screenWidth / 3;
		x4 = x3 + screenWidth / 3;

		gameMode = 0; // game start

		yBird = screenHeight / 2;

		bird.setPosition(xBird + 35, (int) (yBird + 35));

		score = 0;
		scoreLine = 1;

		played = false;
		newHighScore = false;
	}

	boolean checkForCollision() {

		pipe1.setLowerPipePosition(x1, 0);
		pipe1.setUpperPipePosition(x1, pipeHeight1 + pipeDistance);
		pipe1.setLowerHeight(pipeHeight1);
		pipe1.setPipeWidth(pipeWidth);
		pipe1.setUpperHeight(screenHeight - pipeHeight1 - pipeDistance);

		pipe2.setLowerPipePosition(x2, 0);
		pipe2.setUpperPipePosition(x2, pipeHeight2 + pipeDistance);
		pipe2.setLowerHeight(pipeHeight2);
		pipe2.setPipeWidth(pipeWidth);
		pipe2.setUpperHeight(screenHeight - pipeHeight2 - pipeDistance);

		pipe3.setLowerPipePosition(x3, 0);
		pipe3.setUpperPipePosition(x3, pipeHeight3 + pipeDistance);
		pipe3.setLowerHeight(pipeHeight3);
		pipe3.setPipeWidth(pipeWidth);
		pipe3.setUpperHeight(screenHeight - pipeHeight3 - pipeDistance);

		pipe4.setLowerPipePosition(x4, 0);
		pipe4.setUpperPipePosition(x4, pipeHeight4 + pipeDistance);
		pipe4.setLowerHeight(pipeHeight4);
		pipe4.setPipeWidth(pipeWidth);
		pipe4.setUpperHeight(screenHeight - pipeHeight4 - pipeDistance);

		if (bird.collides(pipe1) || bird.collides(pipe2)
				|| bird.collides(pipe3) || bird.collides(pipe4)) {
			gameMode = -1; // game over
			hitSound.play();
			return true;
		}

		return false;
	}

	void updatePipePositions() {

		x1 = x1 - sceneSpeed;
		if (x1 < -screenWidth / 3) {
			x1 = screenWidth;
			pipeHeight1 = minPipeHeight
					+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
		}

		x2 = x2 - sceneSpeed;
		if (x2 < -screenWidth / 3) {

			pipeHeight2 = minPipeHeight
					+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
			x2 = screenWidth;
		}

		x3 = x3 - sceneSpeed;
		if (x3 < -screenWidth / 3) {
			pipeHeight3 = minPipeHeight
					+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
			x3 = screenWidth;
		}

		x4 = x4 - sceneSpeed;
		if (x4 < -screenWidth / 3) {
			pipeHeight4 = minPipeHeight
					+ (int) (Math.random() * ((maxPipeHeight - minPipeHeight) + 1));
			x4 = screenWidth;
		}
		return;
	}

	void drawScene() {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		// draw the moving background
		float temp = (float) (screenWidth - (screenWidth * backgroundX));
		batch.draw(background, 0, 0, temp, screenHeight, (float) (backgroundX),
				1, 1, 0);
		batch.draw(background, temp, 0, (float) (screenWidth * backgroundX),
				screenHeight, 0, 1, (float) backgroundX, 0);

		// draw the pipes
		batch.draw(pipeTexture, x1, 0, pipeWidth, pipeHeight1);
		batch.draw(invertedPipe, x1, pipeHeight1 + pipeDistance, pipeWidth,
				screenHeight - pipeHeight1 - pipeDistance);

		batch.draw(pipeTexture, x2, 0, pipeWidth, pipeHeight2);
		batch.draw(invertedPipe, x2, pipeHeight2 + pipeDistance, pipeWidth,
				screenHeight - pipeHeight2 - pipeDistance);

		batch.draw(pipeTexture, x3, 0, pipeWidth, pipeHeight3);
		batch.draw(invertedPipe, x3, pipeHeight3 + pipeDistance, pipeWidth,
				screenHeight - pipeHeight3 - pipeDistance);

		batch.draw(pipeTexture, x4, 0, pipeWidth, pipeHeight4);
		batch.draw(invertedPipe, x4, pipeHeight4 + pipeDistance, pipeWidth,
				screenHeight - pipeHeight4 - pipeDistance);

		// draw the bird
		batch.draw(birdTexture, xBird, (float) yBird, 70, 70);
		if (gameMode == 0) {
			batch.draw(tapTexture, screenWidth / 2 - 50,
					screenHeight / 2 - 100, 100, 100);
		}
		// draw the gameover text
		if (gameMode < 0) {

			batch.draw(gameOverBackgroundTexture, screenWidth / 8,
					screenHeight / 6, 6 * screenWidth / 8, 4 * screenHeight / 6);

			scoreFont.draw(batch, "Score",
					screenWidth / 2 - 2 * screenWidth / 8,
					(float) (3.2 * screenHeight / 6));
			scoreFont.draw(batch, "Best",
					(float) (screenWidth / 2 + 0.8 * screenWidth / 8),
					(float) (3.2 * screenHeight / 6));

			highScoreFont.draw(batch, Integer.toString(getHighestScore()),
					(float) (screenWidth / 2 + 1.1 * screenWidth / 8),
					(float) (2.5 * screenHeight / 6));
			highScoreFont.draw(batch, Integer.toString(score),
					(float) (screenWidth / 2 - 1.5 * screenWidth / 8),
					(float) (2.5 * screenHeight / 6));

			
				
			batch.draw(gameOverTexture, screenWidth / 2 - screenWidth / 4,
					(float) (3.8 * screenHeight / 6), 4 * screenWidth / 8,
					screenHeight / 6);
			batch.draw(replayTexture, screenWidth / 2 - screenWidth / 16,
					(float) (screenHeight / 5.5), screenWidth / 8,
					screenWidth / 8);
			
			if(newHighScore){
				batch.draw(newHighScoreTexture,screenWidth / 2 -  (float)(1.2 * screenWidth / 8),
						(float) (2.6 * screenHeight / 6), screenWidth/4,screenHeight/4);
			}
		}
		// display score and high score when the game is running

		if (gameMode == 1) {
			String scoreString = Integer.toString(score);
			scoreFont.draw(batch, scoreString, screenWidth / 2 - 20,
					screenHeight / 2 + 300);

			batch.draw(trophyTexture,screenWidth/100,screenHeight-screenHeight/9,screenWidth/15,screenWidth/15);
			String highScoreString = Integer.toString(getHighestScore());
			highScoreFont.draw(batch, highScoreString, screenWidth/11, screenHeight-screenHeight/30);
		}
		batch.end();

		// move the background
		if (gameMode == 1) {
			backgroundX = backgroundX + 0.002;
			if (backgroundX > 1)
				backgroundX = 0;
		}

		return;
	}

	// to store the highest score
	private Preferences preferences;

	protected Preferences getPrefs() {
		if (preferences == null) {
			preferences = Gdx.app.getPreferences(PREFS_NAME);
		}
		return preferences;
	}

	public int getHighestScore() {
		return getPrefs().getInteger(HIGHEST_SCORE, 0);
	}

	public void setHighestScore(int highScore) {
		getPrefs().putInteger(HIGHEST_SCORE, highScore);
		getPrefs().flush();
	}

}
