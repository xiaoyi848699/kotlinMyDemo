package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import demo.xy.com.xytdcq.uitls.ScreenCenter;
import demo.xy.com.xytdcq.uitls.Utils;

/**
 * 形状基类，所有涂鸦板上的绘制的形状继承该基类
 */
public abstract class BasePath extends View implements IBasePath{
    protected String vid;
    protected String sid = "root";
    protected String uid;
    protected String rect;
    protected String drawType = PathType.LINE.toString();
    protected int index = 0;
    protected boolean isSelect = false; // 是否选择
    protected boolean isMoveEnd = true; // 是否在移动结束
    protected boolean isRemove = false; // 是否删除
    protected int listIndex; // 存放对象的列表中的位置，用于刷新数据

    protected ArrayList<Point> points = new ArrayList<>();
    protected Point startPoint;
    protected Point endPoint;
    protected float viewWidth;
    protected float viewHeight;
    protected float offsetX = 0;
    protected float offsetY = 0;

    protected int color = Color.RED;
    protected int size = 1;
    protected Paint paint;
    protected Path path;

    protected IChangeCallback changeCallback;

    public BasePath(Context context) {
        super(context);
        vid = String.valueOf(System.currentTimeMillis());
    }

    public BasePath(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        vid = String.valueOf(System.currentTimeMillis());
    }

    public BasePath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        vid = String.valueOf(System.currentTimeMillis());
    }

    public BasePath(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        vid = String.valueOf(System.currentTimeMillis());
    }

    public void setChangeCallback(IChangeCallback changeCallback) {
        this.changeCallback = changeCallback;
    }

    public String getVid() {
        return vid;
    }

    public int getListIndex() {
        return listIndex;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
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
        // 状态改变才刷新界面
        if (isSelect != select) {
            isSelect = select;
            invalidate();
        }
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
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
        this.color = getRGBColor(color);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = ScreenCenter.dip2px(size);
    }

    private int getRGBColor(int color){
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return Color.rgb(red, green, blue);
    }

    public void checkIsSelect(float minX,float maxX,float minY,float maxY) {
        if (Utils.getInterval(minX,startPoint.getX(),maxX,endPoint.getX()) &&
                Utils.getInterval(minY,startPoint.getY(),maxY,endPoint.getY())) {
            setSelect(true);
        } else {
            // 没有选中
            setSelect(false);
        }
    }

    public void move(float moveX, float moveY, boolean isMoveEnd) {
        offsetX = moveX;
        offsetY = moveY;
        startPoint.setY(startPoint.getY() + moveY);
        startPoint.setX(startPoint.getX() + moveX);
        endPoint.setX(startPoint.getX() + getViewWidth());
        endPoint.setY(startPoint.getY() + getViewHeight());
        if (isMoveEnd != this.isMoveEnd) {
            this.isMoveEnd = isMoveEnd;
            if (isNeedInvalidateOnMoveStatusChane()) {
                invalidate();
            }
        }
    }

    public abstract void relaseData();
}