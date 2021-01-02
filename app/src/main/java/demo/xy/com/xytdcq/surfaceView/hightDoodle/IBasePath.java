package demo.xy.com.xytdcq.surfaceView.hightDoodle;

public interface IBasePath {
    String getVid();

    boolean isSelect();

    void setSelect(boolean select);

    boolean isRemove();

    void setRemove(boolean remove);

    Point getStartPoint();

    void setStartPoint(Point startPoint);

    Point getEndPoint();

    void setEndPoint(Point endPoint);

    float getViewWidth();

    void setViewWidth(float viewWidth);

    float getViewHeight();

    void setViewHeight(float viewHeight);

    int getColor();

    void setColor(int color);

    int getSize();

    void setSize(int size);

    void move(float moveX, float moveY, boolean isMoveEnd);

    void checkIsSelect(float minX,float maxX,float minY,float maxY);

    boolean isNeedInvalidateOnMoveStatusChane();
}
