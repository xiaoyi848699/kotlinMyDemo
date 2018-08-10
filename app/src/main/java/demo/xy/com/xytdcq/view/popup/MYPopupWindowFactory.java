package demo.xy.com.xytdcq.view.popup;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.uitls.FastClick;
import demo.xy.com.xytdcq.uitls.LogUtil;
import demo.xy.com.xytdcq.uitls.ScreenCenter;


/**
 * Created by xy on 2017/10/18.
 */

public class MYPopupWindowFactory {
    private static final int SHADOW_WIDTH = ScreenCenter.dip2px(4);
    private static MyPopupWindow popupWindow;
    public static void showPopUp(final Activity activity , View showView, String[] data, int width,int selectIndex,View.OnClickListener callBackListener) {
        if(FastClick.isFastClick()){
            return;
        }
        dissmiss();
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_bottom_layout,null);
        LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
//        content_layout.setBackgroundResource(R.drawable.popupbg_black);
        for (int i = 0; i< data.length ;i++){
            View item = layoutInflater.inflate(R.layout.popup_bottom_list_item,null);
            TextView textView = (TextView) item.findViewById(R.id.tv_item);
            ImageView check_ic = (ImageView) item.findViewById(R.id.check_ic);
            View line = (View) item.findViewById(R.id.line);
            if(selectIndex == i){
                check_ic.setVisibility(View.VISIBLE);
            }else{
                check_ic.setVisibility(View.GONE);
            }
            if(i== data.length-1){
                line.setVisibility(View.GONE);
//                item.setBackgroundColor(activity.getResources().getColor(R.color.black));
            }
            item.setTag(""+i);
            item.setOnClickListener(callBackListener);
            textView.setText(data[i]);
            content_layout.addView(item);
        }
        //固定item高度40dp
        popupWindow = new MyPopupWindow(view,width,data.length * (ScreenCenter.dip2px(40)+2)+ScreenCenter.dip2px(20));
        //指定透明背景，back键相关
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        //无需动画
        popupWindow.setAnimationStyle(R.style.PopupAnimaFade);
        int[] location = new int[2];
        showView.getLocationOnScreen(location);
        int showViewWidth = showView.getMeasuredWidth();
        LogUtil.e("showViewWidth"+showViewWidth);
        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]-(width - showViewWidth)/2, location[1]-popupWindow.getHeight());
//        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());
//        //在控件的下方
//        popupWindow.showAsDropDown(showView);
//        //在控件的左边
//        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);
//        //在控件的右边
//        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]+showView.getWidth(), location[1]);
    }
//    public static void showPopUpCenter(final Activity activity , View showView, String[] data, int width,int selectIndex,View.OnClickListener callBackListener) {
//        if(FastClick.isFastClick()){
//            return;
//        }
//        dissmiss();
//        LayoutInflater layoutInflater = activity.getLayoutInflater();
//        View view = layoutInflater.inflate(R.layout.popup_bottom_layout,null);
//        LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
////        content_layout.setBackgroundResource(R.drawable.popupbg_black);
//        for (int i = 0; i< data.length ;i++){
//            View item = layoutInflater.inflate(R.layout.popup_bottom_list_item,null);
//            TextView textView = (TextView) item.findViewById(R.id.tv_item);
//            ImageView check_ic = (ImageView) item.findViewById(R.id.check_ic);
//            View line = (View) item.findViewById(R.id.line);
//            if(selectIndex == i){
//                check_ic.setVisibility(View.VISIBLE);
//            }else{
//                check_ic.setVisibility(View.GONE);
//            }
//            if(i== data.length-1){
//                line.setVisibility(View.GONE);
////                item.setBackgroundColor(activity.getResources().getColor(R.color.black));
//            }
//            item.setTag(""+i);
//            item.setOnClickListener(callBackListener);
//            textView.setText(data[i]);
//            content_layout.addView(item);
//        }
//        //固定item高度40dp
//        popupWindow = new MyPopupWindow(view,width,data.length * (ScreenCenter.dip2px(40)+2)+ScreenCenter.dip2px(20));
//        //指定透明背景，back键相关
//        popupWindow.setBackgroundDrawable(new ColorDrawable());
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(false);
//        //无需动画
//        popupWindow.setAnimationStyle(R.style.PopupAnimaFade);
//        int[] location = new int[2];
//        showView.getLocationOnScreen(location);
//        int showViewWidth = showView.getMeasuredWidth();
//        LogUtil.e("showViewWidth"+showViewWidth);
//        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]-(width - showViewWidth)/2, location[1]-popupWindow.getHeight());
////        //在控件的下方
////        popupWindow.showAsDropDown(showView);
////        //在控件的左边
////        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);
////        //在控件的右边
////        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]+showView.getWidth(), location[1]);
//    }
    public static void showColorPopUp(final Activity activity , View showView, int width,View.OnClickListener callBackListener) {
        if(FastClick.isFastClick()){
            return;
        }
        dissmiss();
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.popup_color_bottom_layout,null);
        LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        int size = 4;
        for (int i = 0; i< size ;i++){
            View item = layoutInflater.inflate(R.layout.popup_bottom_list_item,null);
            TextView textView = (TextView) item.findViewById(R.id.tv_item);
            View line = (View) item.findViewById(R.id.line);
            line.setVisibility(View.GONE);
            item.setTag(""+i);
            item.setOnClickListener(callBackListener);
            textView.setText("");
            switch (i){
                case 0:
                    item.setBackgroundResource(R.drawable.bg_blue_corners_top);
                    break;
                case 1:
                    item.setBackgroundColor(activity.getResources().getColor(R.color.rts_red));
                    break;
                case 2:
                    item.setBackgroundColor(activity.getResources().getColor(R.color.rts_black));
                    break;
                case 3:
                    item.setBackgroundResource(R.drawable.bg_pink_corners_bottom);
                    break;
            }
            content_layout.addView(item);
        }
        //固定item高度40dp
        popupWindow = new MyPopupWindow(view,width,size * ScreenCenter.dip2px(40)+ScreenCenter.dip2px(16));
        //指定透明背景，back键相关
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        //无需动画
        popupWindow.setAnimationStyle(R.style.PopupAnimaFade);
        int[] location = new int[2];
        showView.getLocationOnScreen(location);
        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());
//        //在控件的下方
//        popupWindow.showAsDropDown(showView);
//        //在控件的左边
//        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth(), location[1]);
//        //在控件的右边
//        popupWindow.showAtLocation(showView, Gravity.NO_GRAVITY, location[0]+showView.getWidth(), location[1]);
    }
    public static void dissmiss(){
        if(null !=  popupWindow && popupWindow.isShowing() ){
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    public static boolean isShowing(){
        if(null !=  popupWindow  ){
            return popupWindow.isShowing();
        }
        return false;
    }
}
