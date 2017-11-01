package com.ru.tgra;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.ru.tgra.materials.Shader;
import com.ru.tgra.models.ModelMatrix;
import com.ru.tgra.models.Point3D;
import com.ru.tgra.models.Vector3D;
import com.ru.tgra.objects.Camera;
import com.ru.tgra.objects.Token;
import com.ru.tgra.shapes.*;

import java.util.ArrayList;
import java.util.Random;

public class Game extends ApplicationAdapter {

	// Game variables
	private int mazeSize;
	private float cellSize;


	// Player variables
	private final int GOD_MODE = 0;
	private final int FIRST_PERSON = 1;
	private int playerViewMode;
	private float playerDirection;
	private int score;
	float lookSouth;
	float lookEast;

	// Camera variables
	private Camera cam;
	private Camera orthoCam;
	private Camera scoreCam;
	private float orthoZoom = 20f;
	float angle;

	// Token variables
	private ArrayList<Token> tokens;
	ArrayList<Point3D> tokenPositions;
	private int tokenNumber;
	private Random rand;

	// Shader
	private Shader shader;

	private float fov = 50.0f;

	private float movementSpeed = 4f; // used with deltatime, WASD keys
	private float mouseSpeed = 10f;
	private float playerSize = 1f; // Radius of player circle, for collision and display in 2D

	@Override
	public void create () {

		shader = new Shader();

		//COLOR IS SET HERE
		shader.setMaterialDiffuse(0.7f, 0.2f, 0, 1);

		BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
		SincGraphic.create(shader.getVertexPointer());
		CoordFrameGraphic.create(shader.getVertexPointer());

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		shader.setModelMatrix(ModelMatrix.main.getMatrix());

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);


		// Birkir and his amazing maze
		mazeSize = 4;
		cellSize = 6f;


		tokenNumber = (mazeSize*mazeSize) / 2;

		// ----------------------------------
		// 		Camera init & settings
		// ----------------------------------
		lookSouth = ((cellSize/2f)*6.0f);
		lookEast = (cellSize/2f);

		// --- Player camera ---
		cam = new Camera();
		cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight(), 0.4f, 100.0f);
		cam.look(new Point3D((cellSize/2), 2.5f, (cellSize/2)), new Point3D(6,2.5f,lookEast), new Vector3D(0,1,0));

		// --- Mini map camera ---
		orthoCam = new Camera();
		orthoCam.orthographicProjection(-orthoZoom,orthoZoom,-orthoZoom,orthoZoom,1.0f, 100.0f);
		// --- Score camera ---
		scoreCam = new Camera();
		scoreCam.orthographicProjection(-83.3f,83.3f,-25.0f,25.0f,1.0f, 100.0f);

		// ----------------------------------
		// 		  Game play settings
		// ----------------------------------
		playerViewMode = FIRST_PERSON;
		playerDirection = 0f;
		score = 0;

		// ----------------------------------
		// 		  Token settings
		// ----------------------------------
		rand = new Random();
		tokenPositions = new ArrayList<Point3D>();
		tokens = new ArrayList<Token>();
		initializeTokens();
	}


	private void update()
	{
		float deltaTime = Gdx.graphics.getDeltaTime();

		angle += 180.0f * deltaTime;

		Gdx.input.setCursorCatched(true);

			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				cam.roll(110.f * deltaTime);
				playerDirection -= 110f * deltaTime;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				cam.roll(-110.f * deltaTime);
				playerDirection += 110f * deltaTime;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
				cam.pitch(90.f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				cam.pitch(-90.f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.A)) {
				cam.slide(-movementSpeed * deltaTime, 0, 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.D)) {
				cam.slide(movementSpeed * deltaTime, 0, 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.W)) {
				cam.slide(0, 0, -movementSpeed * deltaTime);

			}
			if(Gdx.input.isKeyPressed(Input.Keys.S)) {
				cam.slide(0, 0, movementSpeed * deltaTime);
			}
			if(playerViewMode == GOD_MODE)
			{
				if(Gdx.input.isKeyPressed(Input.Keys.R)) {
					cam.slide(0, -movementSpeed * deltaTime, 0);
				}
				if(Gdx.input.isKeyPressed(Input.Keys.F)) {
					cam.slide(0, movementSpeed * deltaTime, 0);
				}
				if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
					cam.roll(-90.f * deltaTime);
				}
				if(Gdx.input.isKeyPressed(Input.Keys.E)) {
					cam.roll(90.f * deltaTime);
				}
				if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
					cam.pitch(-90.f * deltaTime);
				}
				if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
					cam.pitch(90.f * deltaTime);
				}
			}


		if(Gdx.input.isKeyJustPressed(Input.Keys.V)) {
			if(playerViewMode == GOD_MODE) {
				playerViewMode = FIRST_PERSON;
				cam.perspectiveProjection(fov, 1.0f, 0.4f, 100.0f);
				cam.look(new Point3D((cellSize/2), 3f, (cellSize/2)), new Point3D(6,3,(cellSize/2)), new Vector3D(0,1,0));
			}
			else {
				playerViewMode = GOD_MODE;
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			float acceleration = 1.1f;
			if(movementSpeed <= 8f)
				movementSpeed *= acceleration;
		}
		else
		{
			movementSpeed = 4f;
		}

		// --- Token updates ---
		Token removedToken = null;

		for(Token token : tokens) {
			token.bounce(deltaTime);
			if(cam.gotToken(token)) {
				removedToken = token;
				score++;
			}
		}

		if(removedToken != null)
			tokens.remove(removedToken);


		// --- Mouse movement ---

		cam.roll(-Gdx.input.getDeltaX() * deltaTime * mouseSpeed);
		cam.pitch(-Gdx.input.getDeltaY() * deltaTime * mouseSpeed);


	}

	private void display()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		for(int viewNum = 0; viewNum < 2; viewNum++)
		{
			// --- The player view ---
			if(viewNum == 0)
			{
				Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				cam.perspectiveProjection(fov, (float)Gdx.graphics.getWidth()/(float)Gdx.graphics.getHeight(), 0.1f, 100.0f);
				shader.setViewMatrix(cam.getViewMatrix());
				shader.setProjectionMatrix(cam.getProjectionMatrix());
				shader.setLightPosition(cam.eye.x,cam.eye.y,cam.eye.z,1f);
			}
			// -- The minimap view --
			else
			{
				int miniMapHeight = 250;
				int miniMapWidth = 250;
				Gdx.gl.glViewport((Gdx.graphics.getWidth() - miniMapWidth), Gdx.graphics.getHeight() - miniMapHeight, miniMapWidth, miniMapHeight);
				Point3D camTrace = new Point3D(cam.eye.x, cam.eye.y, cam.eye.z);
				if(orthoZoom * 2 > mazeSize * cellSize){
					camTrace.x = mazeSize * cellSize/2;
					camTrace.z = mazeSize * cellSize/2;
				} else{
					if(camTrace.x < orthoZoom){
						camTrace.x = orthoZoom - cellSize/4;
					} else if(camTrace.x > (mazeSize * cellSize) - orthoZoom){
						camTrace.x = (mazeSize * cellSize) - orthoZoom + cellSize/4;
					}
					if(camTrace.z < orthoZoom){
						camTrace.z = orthoZoom - cellSize/4;
					} else if((camTrace.z > (mazeSize * cellSize) - orthoZoom)) {
						camTrace.z = (mazeSize * cellSize) - orthoZoom + cellSize/4;
					}
				}
				orthoCam.look(new Point3D(camTrace.x, 10.0f, camTrace.z), camTrace, new Vector3D(0,0,-1));
				shader.setViewMatrix(orthoCam.getViewMatrix());
				shader.setProjectionMatrix(orthoCam.getProjectionMatrix());

				shader.setLightPosition(cam.eye.x,10f,cam.eye.z,1f);

			}

			// ----------------------------------
			// 		 Lighting stuff
			// ----------------------------------
			shader.setLightDiffuse(1f,1f,1f,1f);

			for(Token token : tokens) {
				token.display();
			}



			// --- Our position in the mini map ---
			if(viewNum == 1)
			{
				shader.setMaterialDiffuse(0.6f,0.0f,0.6f, 1.0f);

				ModelMatrix.main.loadIdentityMatrix();
				ModelMatrix.main.pushMatrix();
				ModelMatrix.main.addScale(playerSize, playerSize, playerSize);
				ModelMatrix.main.addTranslationBaseCoords(cam.eye.x, cam.eye.y,cam.eye.z);
				ModelMatrix.main.addRotationY(playerDirection);
				shader.setModelMatrix(ModelMatrix.main.getMatrix());
				SphereGraphic.drawSolidSphere();

			}
		}
	}

	@Override
	public void render () {
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();
	}

	private void initializeTokens() {
		tokens.clear();
		tokenPositions.clear();

		// Initialize game tokens
		for(int i = 0; i < tokenNumber; i++) {
			float x;
			float y;
			while(true) {
				x = ((rand.nextInt(mazeSize) * cellSize) + (cellSize / 2));
				y = ((rand.nextInt(mazeSize) * cellSize) + (cellSize / 2));

				if(!doublePosition(x, y) && !(x == (cellSize / 2) && y == (cellSize / 2))) {
					tokenPositions.add(new Point3D(x, y, 0));
					break;
				}
			}

			tokens.add(new Token(x, y, ModelMatrix.main, shader));
		}
	}

	private boolean doublePosition(float x, float y){
		for(Point3D position : tokenPositions) {
			if(position.x == x && position.y == y) {
				return true;
			}
		}
		return false;
	}
}