package demo.xy.com.xytdcq.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import demo.xy.com.xytdcq.R;

/**
 * Created by xy on 2018/5/24.
 */
public class MyProgressDialog extends Dialog {
    private final int CHECK_DISMISS = 100;
    private TextView myMessage;
    private String msg;
    private Context mContext;
//    private int maxWaitTime;
//    private Handler handler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message msg) {
//            if(msg.what == CHECK_DISMISS){
//                LogUtil.e("auto dismiss wait"+maxWaitTime);
//                try {
//                    dismiss();
//                } catch (Exception e) {
//                    LogUtil.e("auto dismiss Exception");
//                }
//            }
//            return false;
//        }
//    });
    public MyProgressDialog(Context context, String msg) {
        super(context);
        this.msg = msg;
        this.mContext = context;
    }

//    public MyProgressDialog(Context context, int theme, String msg, int maxWaitTime) {
////        super(context, theme);
//        super(context, theme);
//        this.msg = msg;
//        this.mContext = context;
//        this.maxWaitTime = maxWaitTime;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        myMessage = findViewById(R.id.message_info);
        if (TextUtils.isEmpty(msg)) {
            myMessage.setText("加载中..");
        } else {
            myMessage.setText(msg);
        }
//        if(null != handler){
//            handler.sendEmptyMessageDelayed(CHECK_DISMISS,maxWaitTime);
//        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        if(null != handler){
//            handler.removeMessages(CHECK_DISMISS);
//        }
//        handler = null;
    }
    public void setText(String text){
        if(null != myMessage && isShowing()){
            myMessage.setText(text);
        }
    }
}
