package org.vt.ece4564.multipong;

import java.util.Random;

import android.graphics.Rect;

public class Ball {
	
	private  Rect ball_;
	int rad, dx, dy;
	boolean exists_;
	
	//constructor
	public Ball(int startX, int startY, int r){
		super();
		ball_ = new Rect();
		rad = r;
		ball_.set(startX, startY, startX+rad, startY+rad);
		exists_ = true;
		Random gen = new Random();
		dx = gen.nextInt(5) +1;
		dy = gen.nextInt(5) +1;
	}
	
	//constructor
	public Ball(int startX, int startY, int r, int start_dX, int start_dY){
		super();
		ball_ = new Rect();
		rad = r;
		ball_.set(startX, startY, startX+rad, startY+rad);
		exists_ = true;
		dx = start_dX;
		dy = start_dY;
	}
	
	boolean exists() {
		return exists_;
	}
	
	void kill() {
		exists_ = false;
	}
	void jesus() {
		exists_ = true;
	}
	
	//returns rect to be painted by canvas
	Rect getDrawable(){
		return ball_;
	}
	//moves the ball by dx and dy
	void move() {
		ball_.offset(dx,dy);
	}
	//called when there is a collision on the x-axis
	void collideX() {
		dx = -dx;
	}
	//called when there is a collision on the x-axis
	void collideY() {
		dy = -dy;
	}
	//get center_pos of the ball
	void setBall(int startX, int startY, int dyt, int dxt) {
		ball_.set(startX, startY, startX+rad, startY+rad);
		dy = dyt;
		dx = dxt;
	}
	
	int getX() {
		return ball_.centerX();
	}
	
	int getY() {
		return ball_.centerY();
	}
	
	int getCornerX() {
		return ball_.centerX()-rad;
	}
	
	int getCornerY() {
		return ball_.centerY()-rad;
	}
	
	int getRadius() {
		return rad;
	}

	int getDx() {
		return dx;
	}
	int getDy() {
		return dy;
	}
}
