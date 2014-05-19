package org.vt.ece4564.multipong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;

public class PongView extends View {

	private final String TAG = "PongView";
	private int MAX_X, MAX_Y;
	private Paint paint_;
	private Ball ball_;
	private Paddle paddle_, opp_paddle_;
	private RefreshHandler redrawHandler_;
	public boolean initialized_;
	int paddle_dx, opp_paddle_dx, ball_dx, ball_dy;
	int PADDLE_HEIGHT, PADDLE_LENGTH, BALL_RADIUS;

	private int my_points_ = 0; // debug;
	private int op_points_ = 0;

	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (initialized_)
				update();
			invalidate(); // Mark the view as 'dirty'
		}

		public void sleep(long delay) {
			this.removeMessages(0);
			this.sendMessageDelayed(obtainMessage(0), delay);
		}
	}

	// Constructors
	public PongView(Context context) {
		super(context);
		initPongView();
	}

	public PongView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPongView();
	}

	public PongView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPongView();
	}

	public void initPongView() {
		Log.d(TAG, "initpongview called");

		initialized_ = false;
		PADDLE_HEIGHT = 10;
		PADDLE_LENGTH = 50;
		BALL_RADIUS = 10;
		paint_ = new Paint();
		redrawHandler_ = new RefreshHandler();
		redrawHandler_.sleep(33);
		
		Log.d(TAG, "Pong game found and running...");
	}

	public void onDraw(Canvas canvas) {
		// Log.d(TAG, "onDraw called");
		super.onDraw(canvas);

		if (!initialized_) {
			Log.d(TAG, "Width: " + getWidth() + " Height: " + getHeight());
			MAX_X = getWidth();
			MAX_Y = getHeight();
			paddle_ = new Paddle(MAX_X / 2, MAX_Y - 10, PADDLE_LENGTH,
					PADDLE_HEIGHT);
			opp_paddle_ = new Paddle(MAX_X / 2, 0, PADDLE_LENGTH,
					PADDLE_HEIGHT);
			ball_ = new Ball(MAX_X / 2, MAX_Y / 2, BALL_RADIUS, 5, 5);
			//ball_.kill();
			initialized_ = true;
		}

		paint_.setColor(Color.BLUE);
		canvas.drawRect(paddle_.getDrawable(), paint_);
		canvas.drawText("" + my_points_, 10, MAX_Y - 20, paint_);
		paint_.setColor(Color.argb(128,255,0,0));
		//paint_.setColor(Color.RED);
		canvas.drawRect(opp_paddle_.getDrawable(), paint_);
		canvas.drawText("" + op_points_, MAX_X - 20, 35, paint_);
		paint_.setColor(Color.WHITE);
		if (ball_.exists()) { 
		canvas.drawRect(ball_.getDrawable(), paint_);
		}
		redrawHandler_.sleep(33);
	}

	public void update() {
		//Log.d(TAG, ball_.exists()?"Exists":"Does Not Exist");
		int px = paddle_.getX();
		int py = paddle_.getY();
		int ox = opp_paddle_.getX();
		int oy = opp_paddle_.getY();
		int bx = ball_.getX();
		int by = ball_.getY();
		int br = ball_.getRadius();
		// paddle
		if (((px + paddle_dx) > 0) && ((px + paddle_dx) < MAX_X))
			paddle_.shift(paddle_dx);
		else
			paddle_.shift(0);

		if (((ox + opp_paddle_dx) > 0) && ((ox + opp_paddle_dx) < MAX_X))
			opp_paddle_.shift(opp_paddle_dx);
		else
			opp_paddle_.shift(0);

		if (ball_.exists()) {
			// ball sides
			if (((bx + ball_.getDx()) < 0) || ((bx + ball_.getDx()) > MAX_X))
				ball_.collideX(); // bounce off side walls

			// ball bottom
			if (((by + ball_.getDy()) > MAX_Y)) {
				ball_.collideY(); // TODO: change to point;
				Log.d(TAG, "SCORE: " + (++op_points_));
			}
			// ball top
			if ((by + ball_.getDy()) < 0) {
				//ball_.collideY(); // TODO: change to sendBall;
				ball_.kill();
				Log.d(TAG, "SCORE: "/* + (++my_points_) */);
			}

			// ball w/ paddle
			// if(((paddle_.getX()+PADDLE_LENGTH)>(ball_.getX()+ball_.getRadius()))&&((paddle_.getX()+paddle_dx)<MAX_X)
			// &&
			// ((paddle_.getY()+PADDLE_LENGTH)>(ball_.getY()+ball_.getRadius()))&&((paddle_.getX()+paddle_dx)<MAX_X)
			// )

			if ((((bx - br) > (px - (PADDLE_LENGTH / 2)))
					&& ((bx + br) < (px + (PADDLE_LENGTH / 2))) && (((by - br) < (py - (PADDLE_HEIGHT / 2))) && ((by + br) > (py + (PADDLE_HEIGHT / 2))))))
				ball_.collideY();
/*
			if ((((bx - br) > (ox - (PADDLE_LENGTH / 2)))
					&& ((bx + br) < (ox + (PADDLE_LENGTH / 2))) && (((by - br) < (oy - (PADDLE_HEIGHT / 2))) && ((by + br) > (oy + (PADDLE_HEIGHT / 2))))))
				ball_.collideY();
*/
			ball_.move();
		}
	}

	public void createBall() {
		ball_ = new Ball(MAX_X / 2, MAX_Y / 2, BALL_RADIUS);
		//ball_.jesus();
		Log.d(TAG, ball_.exists()?"Exists":"Does Not Exist");
		//ball_ = new Ball(MAX_X / 2, MAX_Y / 2, BALL_RADIUS);
	}
	
	public void sendBall(int x, int dx, int dy) {
		
		//ball_ = new Ball(x , BALL_RADIUS, BALL_RADIUS, dx, dy);
	}
	
	public void movePaddle(int dx) {
		paddle_dx = dx;
	}

	public void moveOppPaddle(int dx) {
		opp_paddle_dx = dx;
	}

	public void setOppPaddleX(int x_pad) {
		opp_paddle_.setPaddle(x_pad, 10);
	}

	public int getPaddleX() {
		return paddle_.getCornerX();
	}
	
	public int[] getScores() {
		int[] scores = {0,0};
		
		scores[0] = my_points_;
		scores[1] = op_points_;
		
		return scores;
		
	}
	
	public void setOppScore(int score) {
		
		my_points_ = score;
		
		
	}
	
	public void killBall() {
		if (ball_.exists()) {
			ball_.kill();
		}
	}
	
	public boolean checkBall() {
		return ball_.exists();
	}
	
	public void setBall(int x) {
	//ball_.collideY();
	//ball_.collideX();
	ball_.setBall(x,15,5,5);
	ball_.jesus();

	}
	public String getBall() {
		
		String ball = "_b_" + ball_.getCornerX() + "_b_";
		
		return ball;
	}
	
}
