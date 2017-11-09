# 3D Game - Svenskt Roligt Roadtrip - Assignment 5

**Computer Graphics - Fall 2017**.

## Build and run

From the root directory execute: `gradle run`

... or:

1. Import the gradle project into your IDE.
1. Set the `Working directory` in your build/run configurations to the `core/assets/` folder.
1. Build & Run

## Controls

- `AD` or `← →` Moves the car between lanes.
- `Esc` to Quit
- `ENTER` to restart at the beginning and when you've lost, game over.

### God mode

- Press `G` to turn on godmode.
- Use the controls `WASD RFYH` and `← → ↑ ↓` to move the camera around.

## Gameplay
Your goal is to drive around the amazing planet while collecting points and crystals while avoid hitting the other cars driving.

- Coins award 10 points
- Crystals award 50 points
- Hitting another car makes you loose 1 life (heart)

## Cool features

### Models & Orbitual rotation
We learned some basic modeling techniques in blender and were able to put together the **Globe** with multiple diffuse material and stripes to mark between the road-lanes. And we made the **heart** <3 ourselves as well.
Other models we found online, mostly at free3d.com, imported into blender, color edited and exported using the g3dj exporter.

- Car model
- 3 types of trees
- Coins
- Traffic cones
- Crystals

Simple when we finally figured out a proper way to use the ModelMatrix properly, but we are quite proud of all the orbitual movement happening in the scene.

### Day and Night

The game transitions slowly between day and night, giving you the swedish lagom feelz.

### Spawning

- Everything spawns at a regular interval.
- Cars move at different speeds so we've made sure they don't spawn inside of eachother or anything else.
- Cars don't drive into eachother. Cars that reach the tail of another car will drive at his speed.
- All cars stop when there is a cone in front of them. This makes it dangerous to be driving behind a car as it might stop at any moment.
- Coins spawn in an chain.
- Crystals spawns rarely
- Hearts (extra life) spawns extremely rarely

### Trees spawning

For every 500 points you collect you drive into a forest and you stay there for the next 500 points.
There are 3 different forest models.
When you enter a new forest interval the maxspeed is increased making the game infinitely harder. At later stages you will see great benefit in crashing into cones.

### Switching lanes

The player-car takes a gradual rotation movement dependent on how far his destination is when switching lanes. Gives a smooth drifting look.

### Collision

It is safe to collide into cones, it slows you down but there is no other penalty.

Colliding into cars makes you loose a life, it respawns you at the lane you were on or moving from if you collided sideways. When respawning the car blinks for a few seconds, during that time it is invincible and can't collide with other cars.

## Authors

- [Birkir Brynjarsson](https://github.com/birkirbrynjarsson/)
- [Unnur Sól Ingimarsdóttir](https://github.com/unnursol/)
