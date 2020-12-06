package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import demo.xy.com.mylibrary.log.LogUtil;

/**
 * 形状基类，所有涂鸦板上的绘制的形状继承该基类
 */
public abstract class BasePath extends View {
    protected String vid;
    protected String sid = "root";
    protected String uid;
    protected String rect;
    protected String drawType = PathType.LINE.toString();
    protected int index = 0;
    protected boolean isSelect = false; // 是否选择

    protected ArrayList<Point> points = new ArrayList<>();
    protected Point startPoint;
    protected Point endPoint;
    protected float viewWidth;
    protected float viewHeight;
    protected float offsetX = 0;
    protected float offsetY = 0;

    protected int color = Color.RED;
    protected int size = 2;
    protected Paint paint;
    protected Path path;

    public BasePath(Context context) {
        super(context);
    }

    public BasePath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasePath(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public String getDrawType() {
        return drawType;
    }

    public void setDrawType(String drawType) {
        this.drawType = drawType;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
        invalidate();
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public float getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(float viewWidth) {
        this.viewWidth = viewWidth;
    }

    public float getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(float viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void checkIsSelect(int minX,int maxX,int minY,int maxY) {
        if (isSelect()) {
            return;
        }
        if (getInterval(minX,startPoint.getX(),maxX,startPoint.getX() + getViewWidth()) ||
            getInterval(minY,startPoint.getY(),maxY,startPoint.getY() + getViewHeight())) {
            setSelect(true);
        }
    }

    public void move(float moveX, float moveY) {
        offsetX = moveX;
        offsetY = moveY;
        startPoint.setY(startPoint.getY() + moveY);
        startPoint.setX(startPoint.getX() + moveX);
    }


    //求交集
    private boolean getInterval(int s1,float e1,int s2,float e2){
        if(e1 < s2 || e2 < s1) {
            LogUtil.i("两集合无交集");
            return false;
        }else{
            LogUtil.i("两集合的交集为：[" + Math.max(s1,s2) + ","  +  Math.min(e1,e2) + "]");
            return true;
        }
    }

}