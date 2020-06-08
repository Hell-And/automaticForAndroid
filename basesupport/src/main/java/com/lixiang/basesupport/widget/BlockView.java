package com.lixiang.basesupport.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.ColorInt;

import com.lixiang.basesupport.R;


/**
 * Created by xiang
 * on 2018/11/14 15:51
 */
public class BlockView extends View {
    private Paint mpaint;
    private RectF rectF;
    private float radius;
    private Paint mtitlePaint;
    private Rect mtitleBound;
    private int textColor;
    private int solid_color; //填充色 有边框时
    private int dpsize;
    private Context context;
    private String text = "";
    private int bgcolor;
    private int stroke_width;//边框宽度
    private boolean isSolid;
    private boolean enable;

    public BlockView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BlockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initview(attrs);
        init();
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initview(attrs);
        init();
    }

    private void initview(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BlockView);
        dpsize = a.getDimensionPixelSize(R.styleable.BlockView_block_textSize, dip2px(getContext(), 14));
        bgcolor = a.getColor(R.styleable.BlockView_block_backgroundcolor, bgcolor);
        radius = a.getDimensionPixelSize(R.styleable.BlockView_block_radius, 0);
        stroke_width = a.getDimensionPixelSize(R.styleable.BlockView_block_stroke_width, 0);
        isSolid = a.getBoolean(R.styleable.BlockView_block_solid, true);
        textColor = a.getColor(R.styleable.BlockView_block_textColor, Color.parseColor("#ffffff"));
        solid_color = a.getColor(R.styleable.BlockView_block_solid_color, Color.parseColor("#ffffff"));
        text = a.getString(R.styleable.BlockView_block_text);
        enable = a.getBoolean(R.styleable.BlockView_block_enable, true);
        text = TextUtils.isEmpty(text) ? "" : text;
        a.recycle();
    }

    @SuppressLint("NewApi")
    private void init() {
        mpaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mpaint.setAntiAlias(true);
        rectF = new RectF();

        mtitlePaint = new Paint(Paint.FILTER_BITMAP_FLAG);
        mtitlePaint.setAntiAlias(true);
        mtitlePaint.setTextSize(dpsize);

        mtitleBound = new Rect();
        mtitlePaint.getTextBounds(text, 0, text.length(), mtitleBound);
        setEnabled(enable);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mtitlePaint.setTextSize(dpsize);
            mtitlePaint.getTextBounds(text, 0, text.length(), mtitleBound);
            int desired = getPaddingLeft() + mtitleBound.width() + getPaddingRight();
//            width = desired <= widthSize ? desired : widthSize;
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mtitlePaint.setTextSize(dpsize);
            mtitlePaint.getTextBounds(text, 0, text.length(), mtitleBound);
            int desired = getPaddingTop() + mtitleBound.height() + getPaddingBottom();
//            height = desired <= heightSize ? desired : heightSize;
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawColor(Color.WHITE);
        // 画空心圆角矩形
        RectF rec1 = new RectF();
        if (isSolid) {
            // 实心效果
            mpaint.setStyle(Paint.Style.FILL);
            rec1.left = rec1.top = 0;
            rec1.right = getMeasuredWidth();
            rec1.bottom = getMeasuredHeight();
        } else {
            // 空心效果
            rec1.left = rec1.top = 0.5f * stroke_width;//由于 Paint 笔触是以中线为准
            rec1.right = getMeasuredWidth() - 0.5f * stroke_width;
            rec1.bottom = getMeasuredHeight() - 0.5f * stroke_width;
            //先画一个白色背景
            mpaint.setColor(solid_color);
            mpaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(rec1, radius, radius, mpaint);

            mpaint.setStrokeWidth(stroke_width);
            mpaint.setStyle(Paint.Style.STROKE);
        }

        mpaint.setColor(bgcolor);
        canvas.drawRoundRect(rec1, radius, radius, mpaint);

        mtitlePaint.setColor(textColor);
        mtitlePaint.setTextSize(dpsize);
        Paint.FontMetricsInt fontMetrics = mtitlePaint.getFontMetricsInt();
        mtitlePaint.setTextAlign(Paint.Align.CENTER);
        int baseliney = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        //设置了 setTextAlign(Paint.Align.CENTER); drawText的第二三个参数表示文字中心的x,y
        if (!TextUtils.isEmpty(text)) {
            canvas.drawText(text + "", getMeasuredWidth() / 2, baseliney, mtitlePaint);
        }
    }


    public void setBackgroundColor(@ColorInt int bgcolor) {
        this.bgcolor = bgcolor;
        invalidate();
    }

    public void setBackgroundColor(String bgcolor) {
        this.bgcolor = Color.parseColor(bgcolor);
        invalidate();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        invalidate();
    }

    public void setText(String text) {
        if (text == null) return;
        this.text = text;
        invalidate();
    }

    public void setTextSize(int dpsize) {
        this.dpsize = dpsize;
        invalidate();
    }

    public boolean isSolid() {
        return isSolid;
    }

    public void setSolid(boolean solid) {
        isSolid = solid;
        invalidate();
    }

    public int getStroke_width() {
        return stroke_width;
    }

    public void setStroke_width(int stroke_width) {
        this.stroke_width = stroke_width;
        invalidate();
    }

    public void setScale(float scale) {
        setScaleX(scale);
        setScaleY(scale);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
