package com.android.apps.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

    private Drawable mDrawable;
    private Bitmap mBitmap;
    private Rect mBitmapRect;
    private Paint mPaint;
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;
    private Bitmap mDstB = null;
    private PorterDuffXfermode mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mBitmapRect = new Rect();
        mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.parseColor("#ffffffff"));
        c.drawOval(new RectF(0, 0, w, h), p);
        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawable = getDrawable();
        if (mDrawable == null) return;
        mBitmap = ((BitmapDrawable) mDrawable).getBitmap();
        if (mBitmap == null) return;

        if (mDstB == null) {
            mDstB = makeDst(getWidth(), getHeight());
        }

        mBitmapRect.set(0, 0, getWidth(), getHeight());
        canvas.save();
        canvas.setDrawFilter(mPaintFlagsDrawFilter);
        canvas.drawBitmap(mDstB, 0, 0, mPaint);
        mPaint.setXfermode(mPorterDuffXfermode);
        canvas.drawBitmap(mBitmap, null, mBitmapRect, mPaint);
        mPaint.setXfermode(null);
        canvas.restore();
    }

}
