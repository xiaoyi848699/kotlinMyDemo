package demo.xy.com.xytdcq.view.popup;

import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by xy on 2018/4/16.
 */
public class MyPopupWindow extends PopupWindow {
    private   MyPopupWindowDismiss myPopupWindowDismiss;

    public void setMyPopupWindowDismiss(MyPopupWindowDismiss myPopupWindowDismiss) {
        this.myPopupWindowDismiss = myPopupWindowDismiss;
    }

    public MyPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }


    @Override
    public void dismiss() {
        if(null != myPopupWindowDismiss){
            myPopupWindowDismiss.onDismiss();
            myPopupWindowDismiss = null;
        }
        super.dismiss();
    }

    public interface  MyPopupWindowDismiss{
        void onDismiss();
    }
}
