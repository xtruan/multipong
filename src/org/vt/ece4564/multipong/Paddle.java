package org.vt.ece4564.multipong;

import android.graphics.Rect;
import android.util.Log;

public class Paddle {
	private Rect paddle_;
	private final String TAG = "Paddle";
	private int width_ = 0;
	private int height_ = 0;
	
	public Paddle(int startX, int startY, int width, int height) {
		super();
		width_ = width;
		height_ = height;
		paddle_ = new Rect();
		paddle_.set(startX, startY, startX+width, startY+height);
	}
	//returns rect to be painted by canvas
	Rect getDrawable(){
		return paddle_;
	}
	//moves the paddle by dx
	void shift(int dx ) {
		paddle_.offset(dx,0);
		//Log.d(TAG,"Paddle x = "+paddle_.centerX());
	}
	
	int getX() {
		return paddle_.centerX();
	}
	
	int getY() {
		return paddle_.centerY();
	}
	
	int getCornerX() {
		return paddle_.centerX()-width_;
	}
	int getCornerY(){
		return paddle_.centerX()-height_;
	}
	
	void setPaddle(int startX, int startY) {
		paddle_.set(startX, startY, startX+width_, startY+height_);
	}
}

