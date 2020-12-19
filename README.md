# CSC413-Tank-War-Game

## Student Name: Xiaoqian Yang

## Student ID: 920294060

## Student Email: xyang8@mail.sfsu.edu


A Classic tank game implemented with Java Swing.

The game supports one player tank and several types of Aitank.

All tank has 7 healthPoint initially, hitting by a shell will decrease it by 1.

**Dumb AI Tank:** Will always moves in a circle, shoot at random time.

**Turret AI Tank:** Will always faces the player tank, shoot at random time.

**Cushion AI Tank:** Will always faces the player tank, and keep the distance in a range, shoot waits 300 frames.

**For player tank:** 

		w: move forward,
                
		S: move backward,
                
		A: turn left,
                
		D: turn right,
                
		Space: shoot,
		
		Esc: Return to game menu.

Extra features implemented:

1. **Power up:** Tank can pick up "Power up" and increase its healthPoint by 5, increase its speed to speed x 2.
2. **Better collision detection:** Confine pair combinations to unique ones instead of using brute-force approach.		
3. **Animations:** Add animations when shell hits a target
4. **Game UI:**  Tanks' HP and player tank's speed are shown on the bottom of the screen.
5. **Sound Effects:**  Sound Effects are added to the conditions below: <br>
            - player tank shoots<br>
	    - pick up powerup<br>
	    - tank explosion<br>
	    - wall explosion<br>
	    - player wins<br>
	    - player loses<br>
