package com.example.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class luckPan extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder mHolder;
	private Canvas mCanvas;

	private Thread t;// ���ڻ��Ƶ����߳�
	private boolean isRunning; // �̵߳Ŀ��ƿ���
	// ת����Ϣ
	private String[] mStrs = new String[] { "�������", "IPAD", "��ϲ����", "IPHONE",
			"��Ů��", "��ϲ����" };// ת������
	private int[] mImgs = new int[] { R.drawable.danfan, R.drawable.ipad,
			R.drawable.f040, R.drawable.iphone, R.drawable.meizi,
			R.drawable.f040 };// ת��ͼƬ
	private Bitmap[] mImgsBitmap;
	private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
			0xFFF17E01, 0xFFFFC300, 0xFFF17E01 };// �̿���ɫ
	private int mItemCount = 6;// �̿���Ŀ
	// �̿鷶Χ
	private RectF mRange = new RectF();
	// �̿�ֱ��
	private int mRadius;
	// �����̿�Ļ���
	private Paint mArcPaint;
	// �����ı��Ļ���
	private Paint mTextPaint;
	// �̿�Ĺ����ٶ�
	private double mSpeed = 0;
	private volatile float mStartAngele = 0;
	// �Ƿ�����ֹͣ��ť
	private boolean isShouldEnd;
	// ת�̵�����λ��
	private int mCenter;
	private int mPadding;// paddingȡ�ĸ�padding����Сֵ��ֱ����paddingleftΪ׼
	// ����ͼ
	private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.bg2);
	// ���ֵĴ�С
	private float mTextSize = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());// ת��Ϊ��׼�ߴ�

	public luckPan(Context context) {
		this(context, null);
	}

	public luckPan(Context context, AttributeSet attrs) {
		super(context, attrs);

		mHolder = getHolder();
		mHolder.addCallback(this); // ��ӻص��ṹ

		setFocusable(true);// �ɻ�ý���
		setFocusableInTouchMode(true);// ���Ե��
		setKeepScreenOn(true);// ���ó���

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
		mPadding = getPaddingLeft();
		// ֱ��
		mRadius = width - mPadding * 2;
		// ���ĵ�
		mCenter = width / 2;
		setMeasuredDimension(width, width);// ����ʵ���Զ�������Ĵ�С
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		// ��ʼ�������̿黭��
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);// ����ݷ���
		mArcPaint.setDither(true);// ������
		// ��ʼ���ı��Ļ���
		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffffff);// ������ɫ��ɫ
		mTextPaint.setTextSize(mTextSize);// ���ô�С
		// Բ���Ļ��Ʒ�Χ
		mRange = new RectF(getPaddingLeft(), getPaddingLeft(), getPaddingLeft()
				+ mRadius, getPaddingLeft() + mRadius);
		// ��ʼ��ͼƬ
		mImgsBitmap = new Bitmap[mItemCount];
		for (int i = 0; i < mItemCount; i++) {
			mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),
					mImgs[i]);
		}

		isRunning = true;
		t = new Thread(this);
		t.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		isRunning = false;

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRunning) {
			long start = System.currentTimeMillis();
			draw();// ���л�����Ҫ��
			long end = System.currentTimeMillis();
			if (end - start < 50) {
				try {
					Thread.sleep(50 - (end - start));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void draw() {
		// ��ȡcanvas
		try {
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null) {
				// ���Ʊ���
				drawBg();
				// �����̿�
				float tmpAngle = mStartAngele;
				float sweepAngle = (float) (360 / mItemCount);
				for (int i = 0; i < mItemCount; i++) {
					mArcPaint.setColor(mColors[i]);
					mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true,
							mArcPaint);// ���ƻ��Σ����Σ�
					/**
					 * ����˵���� ��һ������������ʵ�� �ڶ������������ε���ʼ�Ƕ� ���������������ε���ֹ�Ƕ�
					 * ���ĸ��������Ƿ�������ĵ�
					 * ����Ϊ�棬��ʼ������ֹ�㶼��ֱ��������ģ��Ӷ��γɷ��ͼ�Σ����Ϊ�٣�����ʼ��ֱ��������ֹ�㣬�Ӷ��γɷ��ͼ��
					 */
					// �����ı�
					drawText(tmpAngle, sweepAngle, mStrs[i]);
					// ����ͼ��
					drawIconn(tmpAngle, mImgsBitmap[i]);
					tmpAngle += sweepAngle;

				}
				mStartAngele += mSpeed;
				// ��������ֹͣ
				if (isShouldEnd) {
					mSpeed -= 1;
				}
				if (mSpeed <= 0) {
					mSpeed = 0;
					isShouldEnd = false;
				}

			}
		} catch (Exception e) {
			/*
			 * // TODO Auto-generated catch block e.printStackTrace();
			 */
		} finally {
			// canvas �ͷ�
			if (mCanvas != null) {
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}

	}

	// ����ת��
	/* public void LuckyStart() */
	public void LuckyStart(int index) {
		/*
		 * mSpeed = 50; isShouldEnd = false;
		 */
		int angle = 360 / mItemCount;// ÿһ��ĽǶ�
		// ���㵱ǰindex���н���Χ
		// 1:150-210 ipad
		// 0:210-270 ����
		float from = 270 - (index + 1) * angle;
		float end = from + angle;
		// ����ͣ������Ҫ��ת�ľ���
		float targetFrom = 4 * 360 + from;
		float targetEnd = 4 * 360 + end;
		// v1-0 ÿ�μ�һ�ģ���targetFrom
		float v1 = (float) ((-1 + Math.sqrt(1 + 8 * targetFrom)) / 2);
		float v2 = (float) ((-1 + Math.sqrt(1 + 8 * targetEnd)) / 2);

		mSpeed = v1 + Math.random() * (v2 - v1);
		isShouldEnd = false;
	}

	public void LuckyEnd() {
		isShouldEnd = true;
		mStartAngele = 0;
	}

	// ת���Ƿ�����ת
	public boolean isStart() {
		return mSpeed != 0;
	}

	public boolean isShouldEnd() {
		return isShouldEnd;
	}

	// ����ͼƬ
	private void drawIconn(float tmpAngle, Bitmap bitmap) {
		// TODO Auto-generated method stub
		// ����ͼƬ���Ϊֱ����1/2
		int imgWidth = mRadius / 8;
		float angle = (float) ((tmpAngle + 360 / mItemCount / 2) * Math.PI / 180);// ����
		int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
		int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));// ͼƬ���ĵ�λ��
		// ȷ��ͼƬ
		Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
				/ 2, y + imgWidth / 2);
		mCanvas.drawBitmap(bitmap, null, rect, null);

	}

	// ����ÿ���̿���ı�
	private void drawText(float tmpAngle, float sweepAngle, String string) {
		Path path = new Path();
		path.addArc(mRange, tmpAngle, sweepAngle); // Բ��·��
		float textWidth = mTextPaint.measureText(string);
		// ����ˮƽƫ���������־���
		int hOffset = (int) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);
		// ��ֱƫ����
		int vOffset = mRadius / 2 / 6;
		mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
	}

	private void drawBg() {
		// ���Ʊ���
		mCanvas.drawColor(0xffffffff);
		mCanvas.drawBitmap(mBgBitmap, null, new RectF(mPadding / 2,
				mPadding / 2, getMeasuredWidth() - mPadding / 2,
				getMeasuredHeight() - mPadding / 2), null);

	}

}
