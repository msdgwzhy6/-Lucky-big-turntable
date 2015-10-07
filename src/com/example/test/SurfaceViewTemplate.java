package com.example.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
/**
 * 
 * @author ELVIS
 *surfaceView ���ñ�дģʽ
 */
public class SurfaceViewTemplate extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;

	private Thread t;// ���ڻ��Ƶ����߳�
	private boolean isRunning; // �̵߳Ŀ��ƿ���

	public SurfaceViewTemplate(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public SurfaceViewTemplate(Context context, AttributeSet attrs) {
		super(context, attrs);

		mHolder = getHolder();
		mHolder.addCallback(this); // ��ӻص��ṹ������

		setFocusable(true);// �ɻ�ý���
		setFocusableInTouchMode(true);
		setKeepScreenOn(true);// ���ó���

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isRunning = true;
		t = new Thread(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isRunning = false;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			draw();//���л���
		}
	}

	private void draw() {
		//��ȡcanvas
		try {
			mCanvas = mHolder.lockCanvas();
			if(mCanvas!=null){
				//draw 
			}
		} catch (Exception e) {
			/*// TODO Auto-generated catch block
			e.printStackTrace();*/
		}
		finally{
			//canvas �ͷ�
			if(mCanvas!=null){
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}
		
	}
	
}

