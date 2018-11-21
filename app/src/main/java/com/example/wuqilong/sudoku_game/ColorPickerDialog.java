package com.example.wuqilong.sudoku_game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerDialog extends Dialog {

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }


    private OnColorChangedListener mListener;
    private int mInitialColor;

    private static class ColorPickerView extends View {
        private Paint mPaint;
        private Paint mCenterPaint;
        private final int[] mColors;
        private OnColorChangedListener mListener;
        private int fullcolor;
        private float color_RANGE;
        private float color_S;
        private float color_V;
        private String title;
        ColorPickerView(Context c, OnColorChangedListener l, int color,String title) {
            super(c);
            this.title=title;
            mListener = l;
            mColors = new int[] {
                    0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                    0xFFFFFF00, 0xFFFF0000
            };
            Shader s = new SweepGradient(0, 0, mColors, null);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//平滑
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);//空心
            mPaint.setStrokeWidth(300);

            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(color);

            int r=Color.red(color);
            int g=Color.green(color);
            int b=Color.blue(color);
            int max=(r>g)?r:g;
            max=(max>b)?max:b;
            int min=(r<g)?r:g;
            min=(min<b)?min:b;


            color_V=(float)(max)/(255);
            if(max==0)
                color_S=0;
            else
                color_S=1-((float)min/max);

            float angle = (float) Math.acos( (((r-g)+(r-b))/2) / Math.sqrt( (r-g)*(r-g) + (r-b)*(g-b) ));
            if(b<g) angle=2*PI-angle;
            float unit = angle/(2*PI);

            while(unit<0)unit+=1;
            while(unit>1)unit-=1;
            color_RANGE=unit;
            fullcolor=interpColor(mColors, unit);
           // color_L=1-color_L;

            mCenterPaint.setStrokeWidth(5);
        }
        private boolean mTrackingCenter;
        private boolean mHighlightCenter;

        @Override
        protected void onDraw(Canvas canvas) {
            float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;

            canvas.translate(CENTER_X+CENTER_X_MOVE, CENTER_X+CENTER_Y_MOVE);

            canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
            //canvas.drawRect(new RectF(-r+10, r+140, r-10, r+200), mPaint);

            Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
            Shader s = new LinearGradient(-CENTER_X+40, 0,CENTER_X-40,0, 0xffffffff,fullcolor, Shader.TileMode.CLAMP);
            p.setShader(s);
            canvas.drawRect(-CENTER_X+40,CENTER_X+40,CENTER_X-40,CENTER_X+190,p);

            int list[]={0xffffffff,fullcolor};
            int color=interpColor(list, color_S);
            Paint p2=new Paint(Paint.ANTI_ALIAS_FLAG);
            Shader s2 = new LinearGradient(-CENTER_X+40, 0,CENTER_X-40,0, 0xff000000,color, Shader.TileMode.CLAMP);
            p2.setShader(s2);
            canvas.drawRect(-CENTER_X+40,CENTER_X+230,CENTER_X-40,CENTER_X+380,p2);

            //顏色標記點
            Paint ppp=new Paint(Paint.ANTI_ALIAS_FLAG);
            ppp.setColor(0xff808080);
            canvas.drawCircle(-CENTER_X+40 +(CENTER_X*2-80)*color_S, CENTER_X+115, 20, ppp);
            canvas.drawCircle(-CENTER_X+40 +(CENTER_X*2-80)*color_V, CENTER_X+305, 20, ppp);
            canvas.drawCircle((CENTER_X-125)*(float) Math.cos(color_RANGE*2*PI),(CENTER_X-125)*(float) Math.sin(color_RANGE*2*PI), 20, ppp);

            canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);


            Paint mPaint = new Paint();
            mPaint.setStrokeWidth(3);
            mPaint.setTextSize(150);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextAlign(Paint.Align.CENTER);
            Rect bounds = new Rect();
            mPaint.getTextBounds(title, 0, title.length(), bounds);

            canvas.drawText(title,  0,  -CENTER_X-CENTER_X_MOVE, mPaint);

            mPaint.setStrokeWidth(3);
            mPaint.setTextSize(100);
            mPaint.setColor(Color.BLACK);
            mPaint.setTextAlign(Paint.Align.CENTER);
            bounds = new Rect();
            mPaint.getTextBounds(getContext().getString(R.string.choos), 0, getContext().getString(R.string.choos).length(), bounds);
            canvas.drawText(getContext().getString(R.string.choos),  0,  bounds.height()/2, mPaint);

            if (mTrackingCenter) {
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);

                if (mHighlightCenter) {
                    mCenterPaint.setAlpha(0xFF);

                } else {
                    mCenterPaint.setAlpha(0x80);
                }
                canvas.drawCircle(0, 0,
                        CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
                        mCenterPaint);

                mCenterPaint.setStyle(Paint.Style.FILL);
                mCenterPaint.setColor(c);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(CENTER_X*2+CENTER_X_MOVE*2, CENTER_Y*2);
        }

        private static final int CENTER_X = 450;
        private static final int CENTER_X_MOVE = 50;
        private static final int CENTER_Y_MOVE = 300;
        private static final int CENTER_Y = 800;
        private static final int CENTER_RADIUS = 200;

        private int floatToByte(float x) {
            int n = Math.round(x);
            return n;
        }
        private int pinToByte(int n) {
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            return n;
        }

        private int ave(int s, int d, float p) {
            return s + Math.round(p * (d - s));
        }

        private int interpColor(int colors[], float unit) {
            if (unit <= 0) {
                return colors[0];
            }
            if (unit >= 1) {
                return colors[colors.length - 1];
            }

            float p = unit * (colors.length - 1);
            int i = (int)p;
            p -= i;

            // now p is just the fractional part [0...1) and i is the index
            int c0 = colors[i];
            int c1 = colors[i+1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);

            return Color.argb(a, r, g, b);
        }

        private int rotateColor(int color, float rad) {
            float deg = rad * 180 / 3.1415927f;
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);

            ColorMatrix cm = new ColorMatrix();
            ColorMatrix tmp = new ColorMatrix();

            cm.setRGB2YUV();
            tmp.setRotate(0, deg);
            cm.postConcat(tmp);
            tmp.setYUV2RGB();
            cm.postConcat(tmp);

            final float[] a = cm.getArray();

            int ir = floatToByte(a[0] * r +  a[1] * g +  a[2] * b);
            int ig = floatToByte(a[5] * r +  a[6] * g +  a[7] * b);
            int ib = floatToByte(a[10] * r + a[11] * g + a[12] * b);

            return Color.argb(Color.alpha(color), pinToByte(ir),
                    pinToByte(ig), pinToByte(ib));
        }

        private static final float PI = 3.1415926f;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX() - CENTER_X - CENTER_X_MOVE;
            float y = event.getY() - CENTER_X - CENTER_Y_MOVE;
            if(Math.hypot(x, y)>CENTER_X){//超出半徑
                //canvas.drawRect(-CENTER_X+40,CENTER_X+40,CENTER_X-40,CENTER_X+190,p);
                //canvas.drawRect(-CENTER_X+40,CENTER_X+230,CENTER_X-40,CENTER_X+380,p2);
                if(x>=(-CENTER_X+40) && x<=(CENTER_X-40)){
                    boolean s_or_i=true;
                    if(y>=(CENTER_X+40) && y<=(CENTER_X+190)){
                        s_or_i=true;
                    }else if(y>=(CENTER_X+230) && y<=(CENTER_X+380)){
                        s_or_i=false;
                    }else{
                        return true;
                    }
                    if(event.getAction()==MotionEvent.ACTION_MOVE){
                        float unit=(x+CENTER_X-40)/(CENTER_X*2-80);
                        if(s_or_i){
                            int list[]={0xffffffff,fullcolor};
                            color_S=unit;
                            int color=interpColor(list, color_S);
                            int list2[]={0xff000000,color};
                            //int color2=interpColor(list2, color_s);
                            mCenterPaint.setColor(interpColor(list2, color_V));
                            invalidate();

                        }else{
                            int list[]={0xffffffff,fullcolor};
                            int color=interpColor(list, color_S);
                            int list2[]={0xff000000,color};
                            //int color2=interpColor(list2, color_s);
                            color_V=unit;
                            mCenterPaint.setColor(interpColor(list2, color_V));
                            invalidate();

                        }
                    }
                }
                return true;
            }
            boolean inCenter = Math.hypot(x, y) <= CENTER_RADIUS;//受否是中間選定點?
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTrackingCenter = inCenter;
                    if (inCenter) {
                        mHighlightCenter = true;
                        invalidate();
                        break;
                    }
                case MotionEvent.ACTION_MOVE:
                    if (mTrackingCenter) {
                        if (mHighlightCenter != inCenter) {
                            mHighlightCenter = inCenter;
                            invalidate();
                        }
                    } else {
                        float angle = (float) Math.atan2(y, x);
                        // need to turn angle [-PI ... PI] into unit [0....1]
                        float unit = angle/(2*PI);
                        if (unit < 0) {
                            unit += 1;
                        }
                        color_RANGE=unit;
                        fullcolor=interpColor(mColors, unit);
                        int list[]={0xffffffff,fullcolor};
                        int color=interpColor(list, color_S);
                        int list2[]={0xff000000,color};
                        //int color2=interpColor(list2, color_s);
                        mCenterPaint.setColor(interpColor(list2, color_V));
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mTrackingCenter) {
                        if (inCenter) {
                            mListener.colorChanged(mCenterPaint.getColor());
                        }
                        mTrackingCenter = false;    // so we draw w/o halo
                        invalidate();
                    }
                    break;
            }
            return true;
        }
    }
    private String title;

    public ColorPickerDialog(Context context,
                             OnColorChangedListener listener,
                             int initialColor,
                             String title) {
        super(context);
        this.title=title;
        mListener = listener;
        mInitialColor = initialColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                mListener.colorChanged(color);
                dismiss();
            }
        };

        setContentView(new ColorPickerView(getContext(), l, mInitialColor,title));
        setTitle(title);
    }
}
