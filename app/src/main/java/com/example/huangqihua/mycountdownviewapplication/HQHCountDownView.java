package com.example.huangqihua.mycountdownviewapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huangqihua on 16/10/11.
 * 自定义倒计时的view
 */
public class HQHCountDownView extends View {

    private static final int BG_COLOR = 0x50555555;
    private static final float PROGRESSBAR_WIDTH = 15f;
    private static final int PROGRESSBAR_COLOR = 0xFF6ADBFE;
    private static final String TEXT = "跳过";
    private static final float TEXT_SIZE = 50f;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private int bgColor;
    private float proBarWidth;
    private int proBarColor;
    private String text;
    private float textSize;
    private int textColor;

    private Paint circlePaint; //绘制背景的
    private TextPaint textPaint; //绘制text
    private Paint proBarPaint; //绘制进度条

    private StaticLayout staticLayout; //处理文本换行的工具类

    private float progress = 0;

    private CountDownTimer timer;

    public interface CountDownTimerListener {
        void onStartCount();

        void onFinishCount();

    }

    private CountDownTimerListener listener;

    public void setCountDownTimerListener(CountDownTimerListener listener) {
        this.listener = listener;
    }


    public HQHCountDownView(Context context) {
        this(context, null);
    }

    public HQHCountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HQHCountDownView);
        bgColor = ta.getColor(R.styleable.HQHCountDownView_bg_color, BG_COLOR);
        proBarColor = ta.getColor(R.styleable.HQHCountDownView_progressbar_color, PROGRESSBAR_COLOR);
        proBarWidth = ta.getDimension(R.styleable.HQHCountDownView_progressbar_width, PROGRESSBAR_WIDTH);
        textColor = ta.getColor(R.styleable.HQHCountDownView_text_color, TEXT_COLOR);
        textSize = ta.getDimension(R.styleable.HQHCountDownView_text_size, TEXT_SIZE);
        text = ta.getString(R.styleable.HQHCountDownView_text_content);
        if (text == null) {
            text = TEXT;
        }

        ta.recycle();

        init();

    }

    private void init() {
        circlePaint = new Paint();
        //抗锯齿
        circlePaint.setAntiAlias(true);
        //防抖动
        circlePaint.setDither(true);
        circlePaint.setColor(bgColor);
        circlePaint.setStyle(Paint.Style.FILL);

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);

        proBarPaint = new Paint();
        proBarPaint.setAntiAlias(true);
        proBarPaint.setDither(true);
        proBarPaint.setColor(proBarColor);
        proBarPaint.setStrokeWidth(proBarWidth);
        proBarPaint.setStyle(Paint.Style.STROKE);

        int textWidth = (int) textPaint.measureText(text.substring(0, (text.length() + 1) / 2));
        staticLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            width = staticLayout.getWidth();
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = staticLayout.getHeight();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int min = Math.min(width, height);

        //画底盘
        canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);

        RectF rectF;

        //绘制边框
        if (width > height) {
            rectF = new RectF(width / 2 - min / 2 + proBarWidth / 2, 0 + proBarWidth / 2, width / 2 + min / 2 - proBarWidth / 2, height - proBarWidth / 2);
        } else {
            rectF = new RectF(proBarWidth / 2, height / 2 - min / 2 + proBarWidth / 2, width - proBarWidth / 2, height / 2 - proBarWidth / 2 + min / 2);
        }

        canvas.drawArc(rectF, -90, progress, false, proBarPaint);

        //绘制居中的文字
        canvas.translate(width / 2, height / 2 - staticLayout.getHeight() / 2);

        staticLayout.draw(canvas);

    }

    public void start() {
        if (listener != null) {
            listener.onStartCount();
        }
        timer = new CountDownTimer(3600, 36) {
            @Override
            public void onTick(long millisUntilFinished) {
                progress = ((360 - millisUntilFinished) / 3600f) * 360;
                invalidate();
            }

            @Override
            public void onFinish() {
                progress = 0;
                invalidate();
                if (listener != null) {
                    listener.onFinishCount();
                }
            }
        }.start();
    }


    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
