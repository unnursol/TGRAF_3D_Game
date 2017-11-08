package com.ru.tgra.game;


import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.ru.tgra.models.*;
import com.ru.tgra.objects.*;
import com.ru.tgra.shapes.*;
import com.ru.tgra.shapes.g3djmodel.G3DJModelLoader;
import com.ru.tgra.shapes.g3djmodel.MeshModel;
import com.ru.tgra.utilities.RandomGenerator;

public class RaceGame extends ApplicationAdapter {

	Shader shader;

	// Background graphics
	SkyBox sky;
	Ground ground;
	public static Point3D groundPosition;
	public static float groundScale;

	// Cameras
	private Camera cam;
	private Camera orthoCam;
	private Camera lifeCam;
	private float fov = 70.0f;

	// Lights
	private float angle;

	// Menu stuff
	private Menu menu;
	private Boolean mainMenu = true;
	private Boolean gameOverMenu = false;

	// Score
	private int score = 0;

	// Objects
	Car playerCar;
	private ArrayList<Tree> trees;
	private ArrayList<Crystal> crystals;
	private ArrayList<Coin> coins;
	private ArrayList<Heart> hearts;
	private ArrayList<CarObsticle> cars;
	private ArrayList<Cone> cones;

	// Game settings
	private float maxspeed = 0.8f;
	private float maxAccelration = 0.2f;
	private float acceleration = 0f;
	private float objSpeed = 0f;
	private float objStartPosition = -90f;
	private float objEndPosition = 90f;
	private int coinLane;
	private int coinLaneCount;

	private boolean crashed = false;
	private float crashTime = maxspeed;
	private float crashTimer = 0;
	private float crashBlick = 0;

	Music music;
	Sound coinSound;
	Sound carHornSound;
	Sound gameOver;

	private static float[] lanes = new float[]{ -16, -8, 0, 8, 16 };
	private float zDistance = 0f;
	private float zInterval = 15f;

	private static float rightSide = 24;
	private static float leftSide = -24;

	private int maxLife = 3;
	private int life = maxLife;
	private MeshModel lifeHeartModel;


	@Override
	public void create () {

		// Fullscreen
		DisplayMode disp = Gdx.graphics.getDesktopDisplayMode();
		Gdx.graphics.setDisplayMode(disp.width, disp.height, true);

		shader = new Shader();

		BoxGraphic.create();
		SphereGraphic.create();

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		sky = new SkyBox();
		groundPosition = new Point3D(0,-20,0);
		groundScale = 20f;
		ground = new Ground(groundPosition, groundScale, shader);

		// Initialize arrays of objects
		trees = new ArrayList<Tree>();
		crystals = new ArrayList<Crystal>();
		coins = new ArrayList<Coin>();
		hearts = new ArrayList<Heart>();
		cars = new ArrayList<CarObsticle>();
		cones = new ArrayList<Cone>();

		playerCar = new Car(shader);


		// Initialize cameras
		cam = new Camera();
		cam.look(new Point3D(0f, 6f, -2f), new Point3D(0,0f,3f), new Vector3D(0,1,0));

		orthoCam = new Camera();
		orthoCam.perspectiveProjection(100.0f, 1, 3, 100);
		orthoCam.look(new Point3D(0f, 15.0f, 0f), new Point3D(0f, 0f, 0f), new Vector3D(0,0,1));

		lifeCam = new Camera();
		lifeCam.orthographicProjection(-83.3f,83.3f,-25.0f,25.0f,1.0f, 100.0f);

		music = Gdx.audio.newMusic(Gdx.files.internal("audio/song1.mp3"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("audio/coinSound.mp3"));
		carHornSound = Gdx.audio.newSound(Gdx.files.internal("audio/carHornSound.mp3"));
		gameOver = Gdx.audio.newSound(Gdx.files.internal("audio/gameOver.mp3"));


		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		lifeHeartModel = G3DJModelLoader.loadG3DJFromFile("heart.g3dj");
		menu = new Menu();
	}

	private void crash(){
		objSpeed = 0f;
		acceleration = 0f;
	}

	private void updateSpeed(float deltaTime){
		if(acceleration < maxAccelration){
			acceleration += deltaTime * 0.1f;
			if(acceleration > maxAccelration){
				acceleration = maxAccelration;
			}
		}
		if(objSpeed < maxspeed){
			objSpeed += acceleration * deltaTime;
			if(objSpeed > maxspeed){
				objSpeed = maxspeed;
			}
		}
	}

	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		// While playing the game
		if(!mainMenu && !gameOverMenu) {
			updateSpeed(deltaTime);
			spawnObjects();
			playerCar.update(deltaTime);
			ground.update(objSpeed);

			for(int i = 0; i < trees.size(); i++) {
				trees.get(i).update(objSpeed);
				if(trees.get(i).isOutOfBounce()) {
					trees.remove(i);
				}
			}

			for(int i = 0; i < crystals.size(); i++) {
				crystals.get(i).update(deltaTime, objSpeed);
				if(crystals.get(i).collidingWithPlayer(playerCar)) {
					crystals.remove(i);
					score += 50;
				}
				else if(crystals.get(i).isOutOfBounce()) {
					crystals.remove(i);
				}
			}

			for(int i = 0; i < coins.size(); i++) {
				coins.get(i).update(deltaTime, objSpeed);
				if(coins.get(i).collidingWithPlayer(playerCar)) {
					coinSound.play(1f);
					coins.remove(i);
					score += 10;
				}
				else if(coins.get(i).isOutOfBounce()) {
					coins.remove(i);
				}
			}


			for(int i = 0; i < hearts.size(); i++) {
				hearts.get(i).update(deltaTime, objSpeed);
				if(hearts.get(i).collidingWithPlayer(playerCar)) {
					hearts.remove(i);
					if(life < maxLife)
						life += 1;
				}
				else if(hearts.get(i).isOutOfBounce()) {
					hearts.remove(i);
				}
			}

			for(int i = 0; i < cones.size(); i++) {
				cones.get(i).update(objSpeed);
				if(!crashed && cones.get(i).collidingWithPlayer(playerCar)) {
					if(objSpeed > maxspeed/2){
						objSpeed = maxspeed/2;
					}
				}
				else if(cones.get(i).isOutOfBounce()) {
					cones.remove(i);
				}
			}

			if(!crashed)
			{
				for(int i = 0; i < cars.size(); i++) {
					cars.get(i).update(objSpeed);
					if(cars.get(i).collidingWithPlayer(playerCar)) {
						carHornSound.play(1f);
						acceleration = 0;
						objSpeed = 0;
						//cars.remove(i);
						crashed = true;
						life --;
					}
					else if(cars.get(i).isOutOfBounce()) {
						cars.remove(i);
					}
					else
						collidingWithOtherObject(cars.get(i));
				}
			}
			else if(crashed)
			{
				crashBlick += 0.2f;
				for(int i = 0; i < cars.size(); i++) {
					cars.get(i).oppositeUpdate();
					if(cars.get(i).isOppositeOutOfBounce()) {
						cars.remove(i);
					}
				}
				crashTimer += deltaTime*acceleration;
				//System.out.println(crashTimer);
				if (crashTimer >= crashTime)
				{
					crashTimer = 0;
					crashed = false;
					crashBlick = 0;
				}
			}
			// Game over
			if(life <= 0) {
				gameOver();
			}
		}

		// Quit the game
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
		{
			Gdx.graphics.setDisplayMode(500, 500, false);
			Gdx.app.exit();
		}

		// Start the game
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && mainMenu)
		{
			music.play();
			music.setLooping(true);
			mainMenu = false;
		}

		// Restart the game
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && gameOverMenu)
		{
			score = 0;
			music.play();
			music.setLooping(true);
			life = maxLife;
			gameOverMenu = false;
		}

	}

	private void collidingWithOtherObject(CarObsticle theCar) {
		for(CarObsticle car : cars)
		{
			if((car != theCar) && (car.getLane() == theCar.getLane()) && theCar.collidingWithObj(car)) {
				if (car.getSpeed() > theCar.getSpeed())
					car.setSpeed(theCar.getSpeed());
				else
					theCar.setSpeed(car.getSpeed());
			}
		}

		for(Crystal crystal : crystals)
		{
			if((crystal.getLane() == theCar.getLane()) && theCar.collidingWithObj(crystal)) {
				// Beygja á næstu akrein maybe
			}

		}
	}


	private void display()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		for(int viewNum = 0; viewNum < 2; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight(), 0.1f, 100.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
				shader.setLightPosition(cam.eye.x,cam.eye.y,cam.eye.z,1f);
			}
			else
			{
				int miniMapHeight = 250;
				int miniMapWidth = 250;
				Gdx.gl.glViewport((Gdx.graphics.getWidth() - miniMapWidth), Gdx.graphics.getHeight() - miniMapHeight, miniMapWidth, miniMapHeight);
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());

				shader.setLightPosition(cam.eye.x,10f,cam.eye.z,1f);
			}

			ModelMatrix.main.loadIdentityMatrix();

			float s = (float)Math.sin((angle / 2.0) * Math.PI / 180.0);
			float c = (float)Math.cos((angle / 2.0) * Math.PI / 180.0);

			shader.setLightPosition(0.0f + c * 3.0f, 5.0f, 0.0f + s * 3.0f, 1.0f);

			float s2 = Math.abs((float)Math.sin((angle / 1.312) * Math.PI / 180.0));
			float c2 = Math.abs((float)Math.cos((angle / 1.312) * Math.PI / 180.0));

			shader.setSpotDirection(s2, -0.3f, c2, 0.0f);
			//shader.setSpotDirection(-cam.n.x, -cam.n.y, -cam.n.z, 0.0f);
			shader.setSpotExponent(0.0f);
			shader.setConstantAttenuation(1.0f);
			shader.setLinearAttenuation(0.00f);
			shader.setQuadraticAttenuation(0.00f);

			//shader.setLightColor(s2, 0.4f, c2, 1.0f);
			shader.setLightColor(1.0f, 1.0f, 1.0f, 1.0f);

			shader.setGlobalAmbient(0.3f, 0.3f, 0.3f, 1);

			shader.setMaterialDiffuse(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialSpecular(1.0f, 1.0f, 1.0f, 1.0f);
			shader.setMaterialEmission(0, 0, 0, 1);
			shader.setShininess(50.0f);

			shader.setModelMatrix(ModelMatrix.main.getMatrix());

			// Draw the playerCar
			if(!crashed) {
				playerCar.display();
			}
			else if(crashed && crashBlick % 2 >= 1) {
				playerCar.display();
			}

			// Draw the ground
			ground.display();

			// Draw objects
			for(Tree tree : trees) {
				tree.display();
			}

			for(Crystal crystal : crystals) {
				crystal.display();
			}

			for(Coin coin : coins) {
				coin.display();
			}

			for(Heart heart : hearts) {
				heart.display();
			}

			for(CarObsticle car : cars) {
				car.display();
			}

			for(Cone cone : cones) {
				cone.display();
			}

			if( viewNum == 0)
			{
				sky.display(shader);

				// Display score and menus
				if(mainMenu) {
					menu.displayMainMenu(shader);
				}
				else if(gameOverMenu) {
					menu.displayGameOver(shader, score);
				}
				else {
					menu.displayScore(shader, score);
				}
			}
		}

		displayLife();
	}

	@Override
	public void render () {
		update();
		display();

	}

	// --------------------------------------------------
	//				Helper functions
	// --------------------------------------------------

	private void spawnObjects() {
		zDistance += objSpeed;
		if(zDistance < zInterval) {
			return;
		}
		zDistance = 0f;

		// Spawn coin

		if(coinLaneCount <= 0)
		{
			int temp = RandomGenerator.randomIntegerInRange(0, 1);
			if((temp == 1 && coinLane != 4) || (temp == 0 && coinLane == 0))
				coinLane++;
			else
				coinLane--;
			coinLaneCount = RandomGenerator.randomIntegerInRange(1, 8);
		}
		Coin newCoin = new Coin(shader, lanes[coinLane], objStartPosition);
		coins.add(newCoin);
		coinLaneCount--;


		// Spawn other objects
		int numberOfSpawns = RandomGenerator.randomIntegerInRange(0,3);
		int[] positions = new int[]{-1, -1, -1};
		for(int i = 0; i < numberOfSpawns; i++)
		{
			while(true)
			{
				int laneNr = RandomGenerator.randomIntegerInRange(0,4);
				if(!doublePosition(positions, laneNr) && laneNr != coinLane) {
					positions[i] = laneNr;
					float p = RandomGenerator.randomFloatInRange(0,1);
					if(p > 0f && p < 0.3f) {

					}
					else if(p >= 0.3f && p < 0.35f) {
						Crystal newCrystal = new Crystal(shader, lanes[laneNr], objStartPosition);
						crystals.add(newCrystal);
					}
					else if(p >= 0.35f && p < 0.7) {
						Cone newCone = new Cone(shader, lanes[laneNr], objStartPosition);
						cones.add(newCone);
					}
					else if(p >= 0.7 && p < 0.995) {
						// Random number and random speed
						float speed = RandomGenerator.randomFloatInRange(0.3f,0.8f);
						int color = RandomGenerator.randomIntegerInRange(0,3);
						CarObsticle newCar = new CarObsticle(shader, lanes[laneNr], objStartPosition,speed, color);
						cars.add(newCar);
					}
					else if(p >= 0.995 && p < 1) {
						Heart newHeart = new Heart(shader, lanes[laneNr], objStartPosition);
						hearts.add(newHeart);
					}
					break;
				}
			}
		}
		int treeType;
		if(score > 2000) {
			treeType = 2;
		}
		else if(score > 1000) {
			treeType = 1;
			Tree rightTree = new Tree(shader, rightSide + 8, objStartPosition - 8, treeType);
			Tree leftTree = new Tree(shader, leftSide - 8, objStartPosition - 8, treeType);

			trees.add(rightTree);
			trees.add(leftTree);
		}
		else {
			treeType = 0;
		}
		Tree rightTree = new Tree(shader, rightSide, objStartPosition, treeType);
		Tree leftTree = new Tree(shader, leftSide, objStartPosition, treeType);

		trees.add(rightTree);
		trees.add(leftTree);
	}

	private boolean doublePosition(int[] positions, int pos){
		for(int position : positions) {
			if(position == pos) {
				return true;
			}
		}
		return false;
	}

	private boolean sameLane(float lane, float playerLane) {
		if(lane == playerLane) {
			return true;
		}
		else {
			return false;
		}
	}

	public void displayLife() {

		int lifeHeight = 150;
		int lifeWidth = 500;

		Gdx.gl.glViewport((Gdx.graphics.getWidth()/2)-lifeWidth/2, Gdx.graphics.getHeight() - lifeHeight, lifeWidth, lifeHeight);

		lifeCam.look(new Point3D(0,40,0), new Point3D(0,1,0), new Vector3D(0,0,-1));
		shader.setViewMatrix(lifeCam.getViewMatrix());
		shader.setProjectionMatrix(lifeCam.getProjectionMatrix());

		shader.setLightPosition(10,40f,10,1f);

		float x = -25f;
		int z = -8;
		float heartWidth = 25f;

		// Drawing life hearts
		for(int i = 0; i < life; i++) {
			ModelMatrix.main.loadIdentityMatrix();
			ModelMatrix.main.pushMatrix();
			ModelMatrix.main.addTranslation(x,1f,z);
			ModelMatrix.main.addRotationZ(90);
			ModelMatrix.main.addRotationX(-90);
			ModelMatrix.main.addScale(5f, 5f,5f);
			shader.setModelMatrix(ModelMatrix.main.getMatrix());
			lifeHeartModel.draw(shader);
			ModelMatrix.main.popMatrix();

			x += heartWidth;

		}
	}

	public void gameOver() {
		gameOverMenu = true;
		gameOver.play(1f);
		music.dispose();
	}

}