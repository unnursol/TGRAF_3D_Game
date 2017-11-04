package com.ru.tgra.game;


import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.ru.tgra.models.*;
import com.ru.tgra.objects.*;
import com.ru.tgra.shapes.*;
import com.ru.tgra.utilities.RandomGenerator;

public class RaceGame extends ApplicationAdapter {

	Shader shader;

	private float angle;

	// Background graphics
	SkyBox sky;
	Ground ground;
	public static Point3D groundPosition;
	public static float groundScale;

	// Cameras
	private Camera cam;
	private Camera orthoCam;
	
	private float fov = 70.0f;

	Car playerCar;

	private Texture tex;
	
	private Random rand = new Random();

	// Menu stuff
	private Menu menu;
	private Boolean mainMenu = true;
	private Boolean gameOverMenu = false;

	// Score
	private int score = 0;

	// Objects
	private Crate crate;
	private ArrayList<Tree> trees;
	private ArrayList<Crystal> crystals;
	private ArrayList<Coin> coins;
	private ArrayList<Heart> hearts;

	// Game settings
	private float maxspeed = 1f;
	private float maxAccelration = 0.2f;
	private float acceleration = 0f;
	private float objSpeed = 0f;
	private float objStartPosition = -90f;
	private float objEndPosition = 50f;
	Music music;

	private static float[] lanes = new float[]{ -16, -8, 0, 8, 16 };
	private float zDistance = 0f;
	private float zInterval = 15f;

	private static float rightSide = 24;
	private static float leftSide = -24;


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

		playerCar = new Car(shader);
		crate = new Crate(shader, 3);

		Tree tree = new Tree(shader, leftSide, -10, 0);
//		Crystal crystal1 = new Crystal(shader, 0, -20);
//		crystals.add(crystal1);
		Crystal crystal2 = new Crystal(shader, lanes[2], -30);
		crystals.add(crystal2);

		trees.add(tree);

		Coin coin = new Coin(shader, lanes[0], -20);
		coins.add(coin);

		Heart heart = new Heart(shader, lanes[0], -5);
		hearts.add(heart);

		// Initialize cameras
		cam = new Camera();
		cam.look(new Point3D(0f, 6f, -2f), new Point3D(0,0f,3f), new Vector3D(0,1,0));

		orthoCam = new Camera();
		//orthoCam.orthographicProjection(-5, 5, -5, 5, 3.0f, 100);
		orthoCam.perspectiveProjection(100.0f, 1, 3, 100);
		orthoCam.look(new Point3D(0f, 10.0f, 0f), new Point3D(0f, 0f, 0f), new Vector3D(0,0,1));

		//TODO: try this way to create a texture image
		/*Pixmap pm = new Pixmap(128, 128, Format.RGBA8888);
		for(int i = 0; i < pm.getWidth(); i++)
		{
			for(int j = 0; j < pm.getWidth(); j++)
			{
				pm.drawPixel(i, j, rand.nextInt());
			}
		}
		tex = new Texture(pm);*/

		music = Gdx.audio.newMusic(Gdx.files.internal("audio/song1.mp3"));
		music.play();

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

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
				crystals.get(i).update(objSpeed);
				if(sameLane(crystals.get(i).getLane()) && crystals.get(i).collidingWithPlayer()) {
					crystals.remove(i);
					score += 50;
				}
				else if(crystals.get(i).isOutOfBounce()) {
					crystals.remove(i);
				}
			}

			for(int i = 0; i < coins.size(); i++) {
				coins.get(i).update(deltaTime, objSpeed);
				if(sameLane(coins.get(i).getLane()) && coins.get(i).collidingWithPlayer()) {
					coins.remove(i);
					score += 10;
				}
				else if(coins.get(i).isOutOfBounce()) {
					coins.remove(i);
				}
			}

			for(Heart heart : hearts) {
				heart.update(deltaTime, objSpeed);
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
			mainMenu = false;
		}

		// ------------ Camera god mode stuff ---------------

		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.slide(-3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.slide(3.0f * deltaTime, 0, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.slide(0, 0, -3.0f * deltaTime);
			//cam.walkForward(3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.slide(0, 0, 3.0f * deltaTime);
			//cam.walkForward(-3.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.R)) {
			cam.slide(0, 3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.F)) {
			cam.slide(0, -3.0f * deltaTime, 0);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			cam.yaw(-90.0f * deltaTime);
			//cam.rotateY(90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			cam.yaw(90.0f * deltaTime);
			//cam.rotateY(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			cam.pitch(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			cam.pitch(90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
			cam.roll(-90.0f * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
			cam.roll(90.0f * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.T)) {
			fov -= 30.0f * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.G)) {
			fov += 30.0f * deltaTime;
		}
	}

	private void spawnObjects() {
		zDistance += objSpeed;
		if(zDistance < zInterval){
			return;
		}
		zDistance = 0f;
		int numberOfSpawns = RandomGenerator.randomIntegerInRange(1,3);
		for(int i = 0; i < numberOfSpawns; i++)
		{
			int laneNr = RandomGenerator.randomIntegerInRange(0,4);
			float p = RandomGenerator.randomFloatInRange(0,1);
			if(p > 0f && p < 0.3f) {
				Coin newCoin = new Coin(shader, lanes[laneNr], objStartPosition);
				coins.add(newCoin);
			}
			else if(p > 0.3f && p < 0.4f) {
				Crystal newCrystal = new Crystal(shader, lanes[laneNr], objStartPosition);
				crystals.add(newCrystal);
			}
			else if(p > 0.4f && p < 0.8) {

			}
			else if(p > 0.8 && p < 1) {

			}
		}

		if(score > 1000) {
			Tree rightTree = new Tree(shader, rightSide, objStartPosition, 1);
			Tree leftTree = new Tree(shader, leftSide, objStartPosition, 1);

			trees.add(rightTree);
			trees.add(leftTree);
		}
		else {
			Tree rightTree = new Tree(shader, rightSide, objStartPosition, 0);
			Tree leftTree = new Tree(shader, leftSide, objStartPosition, 0);

			trees.add(rightTree);
			trees.add(leftTree);
		}
	}

	private boolean sameLane(float lane) {
		if(lane == playerCar.getLane()) {
			return true;
		}
		else {
			return false;
		}
	}

	private void display()
	{
		//do all actual drawing and rendering here
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		//Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);
/*
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
		//Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
*/
		for(int viewNum = 0; viewNum < 2; viewNum++)
		{
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight(), 0.1f, 100.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
				shader.setLightPosition(cam.eye.x,cam.eye.y,cam.eye.z,1f);
//				System.out.println("FIRST PARAMETER: " + cam.eye.x + ", " + cam.eye.y + ", " + cam.eye.z);
//				System.out.println("n (differnce(eye, center)): " + cam.n.x + ", " + cam.n.y + ", " + cam.n.z );
//				System.out.println("u (upp.cross(n)): " + cam.u.x + ", " + cam.u.y + ", " + cam.u.z );
			}
			else
			{
				int miniMapHeight = 250;
				int miniMapWidth = 250;
				Gdx.gl.glViewport((Gdx.graphics.getWidth() - miniMapWidth), Gdx.graphics.getHeight() - miniMapHeight, miniMapWidth, miniMapHeight);
				Point3D camTrace = new Point3D(cam.eye.x, cam.eye.y, cam.eye.z);
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());

				shader.setLightPosition(cam.eye.x,10f,cam.eye.z,1f);
			}

			ModelMatrix.main.loadIdentityMatrix();

			float s = (float)Math.sin((angle / 2.0) * Math.PI / 180.0);
			float c = (float)Math.cos((angle / 2.0) * Math.PI / 180.0);

			shader.setLightPosition(0.0f + c * 3.0f, 5.0f, 0.0f + s * 3.0f, 1.0f);
			//shader.setLightPosition(3.0f, 4.0f, 0.0f, 1.0f);
			//shader.setLightPosition(cam.eye.x, cam.eye.y, cam.eye.z, 1.0f);

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
			playerCar.display();

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
				//heart.display();
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
	}

	@Override
	public void render () {
		update();
		display();

	}
}