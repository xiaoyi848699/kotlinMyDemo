package demo.xy.com.xytdcq.surfaceView.doodle;

import android.graphics.Canvas;
import android.graphics.Color;

import demo.xy.com.xytdcq.uitls.ScreenCenter;

/**
 * 形状基类，所有涂鸦板上的绘制的形状继承该基类
 */
public abstract class Action {
    protected float startX;
    protected float startY;
    protected float stopX;
    protected float stopY;
    protected int color = Color.BLACK;
    protected int size;
    protected int selectColor = 0xFF0000; // 背景颜色
    //撤销画笔使用、判断是否是自己的画笔(1:自己、0:对方)、默认是自己的接收到的时候修改
    public byte isSelf = 1;
    protected String account;
    protected float startIndexX = 0;
    protected float startIndexY = 0;
    protected float endIndexX = 0;
    protected float endIndexY = 0;

    public float getStartIndexX() {
        return startIndexX;
    }

    public void setStartIndexX(float startIndexX) {
        this.startIndexX = startIndexX;
    }

    public float getStartIndexY() {
        return startIndexY;
    }

    public void setStartIndexY(float startIndexY) {
        this.startIndexY = startIndexY;
    }

    public float getEndIndexX() {
        return endIndexX;
    }

    public void setEndIndexX(float endIndexX) {
        this.endIndexX = endIndexX;
    }

    public float getEndIndexY() {
        return endIndexY;
    }

    public void setEndIndexY(float endIndexY) {
        this.endIndexY = endIndexY;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    Action(float startX, float startY, int color, int size) {
        this.startX = startX;
        this.startY = startY;
        this.stopX = startX;
        this.stopY = startY;
        this.color = getRGBColor(color);
        this.size = ScreenCenter.dip2px(size);
    }
    //为了数据color保持与IOS一致，但是android显示需要转换
    private int getRGBColor(int color){
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
       return Color.rgb(red, green, blue);
    }
    /**
     * 是否是连续的图形（onMove所有帧都要画上）
     * 连续的：Path
     * 非连续的：Rect,Circle
     */
//    public boolean isSequentialAction() {
//        return false;
//    }

    /**
     * ACTION_DOWN事件触发时调用（一个新的形状开始时，构造ACTION之后调用）
     * 某些图形在ACTION_DOWN时需要绘制，重载此方法
     *
     * @param canvas
     */
    public void onStart(Canvas canvas) {
    }

    //新添加画线改成贝塞尔曲线、画笔结束
    public void onEnd(Canvas canvas){
    }

    /**
     * ACTION_MOVE事件触发时调用
     *
     * @param mx 当前MOVE到的手指位置x
     * @param my 当前MOVE到的手指位置y
     */
    public abstract void onMove(float mx, float my);

    /**
     * ACTION_MOVE过程或者ACTION_UP后形状的绘制
     *
     * @param canvas
     */
    public abstract void onDraw(Canvas canvas);
}