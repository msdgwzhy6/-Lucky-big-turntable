package com.example.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	// ���ڿ���surfacView
	private SurfaceHolder mHolder;
	// ����һ������
	private Paint paint;
	// �ı�������
	private int textX = 10, textY = 10;
	// ����һ���߳�
	private Thread th;
	// �߳������ı�־λ
	private boolean flag;
	// ����һ������
	private Canvas canvas;
	// ������Ļ�Ŀ��
	private int screenW, screenH;

	public MySurfaceView(Context context) {
		super(context);
		// ʵ����mHolder
		mHolder = this.getHolder();
		// ΪsurfaceView��Ӽ�����
		mHolder.addCallback(this);
		// ʵ��������
		paint = new Paint();
		// ʵ����������ɫΪ��ɫ
		paint.setColor(Color.WHITE);
		// ���ý���
		setFocusable(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		flag = true;
		// ʵ�����߳�
		th = new Thread(this);
		// �����߳�
		th.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = true;

	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/* ��Ϸ�����߼� */
	private void logic() {
		// ������Ϸ�߼�����

	}

	private void myDraw() {
		try {
			canvas = mHolder.lockCanvas();
			if (canvas != null) {
				// ������û��ƾ��η�ʽˢ��
				// ���ƾ���
				canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
				canvas.drawText("Hello World", textX, textY, paint);
			}
		} catch (Exception e) {

		} finally {
			if (canvas != null)
				mHolder.unlockCanvasAndPost(canvas);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		textX = (int) event.getX();
		textY = (int) event.getY();
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}
	

}
