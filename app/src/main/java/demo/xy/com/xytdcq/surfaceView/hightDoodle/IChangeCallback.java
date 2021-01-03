package demo.xy.com.xytdcq.surfaceView.hightDoodle;

import java.util.List;

public interface IChangeCallback {
    void changeCallBack(String viewId, Point startPoint, Point endPoint,float width, float height);
    void changeSelectCallBack(String viewId);
    void deleteSelfCallBack(String viewId);
    void eraserPath(List<String> viewIds);
}
