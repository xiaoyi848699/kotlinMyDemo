package demo.xy.com.mylibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.TextView;

import demo.xy.com.mylibrary.R;
import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.mylibrary.log.Write;


/**
 * Created by xy on 2018/5/24.
 */
public class MyProgressDialog extends Dialog {
    private final int CHECK_DISMISS = 100;
    private TextView myMessage;
    private String msg;
    private Context mContext;
    private int maxWaitTime;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == CHECK_DISMISS){
                LogUtil.e("auto dismiss wait"+maxWaitTime);
                Write.debug("auto dismiss wait"+maxWaitTime);
                try {
                    dismiss();
                } catch (Exception e) {
                    LogUtil.e("auto dismiss Exception");
                    Write.debug("auto dismiss Exception");
                }
            }
            return false;
        }
    });
    public MyProgressDialog(Context context, String msg) {
        super(context);
        this.msg = msg;
        this.mContext = context;
    }

    public MyProgressDialog(Context context, int theme, String msg, int maxWaitTime) {
//        super(context, theme);
        super(context, theme);
        this.msg = msg;
        this.mContext = context;
        this.maxWaitTime = maxWaitTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog);
        myMessage = findViewById(R.id.message_info);
        if (TextUtils.isEmpty(msg)) {
            myMessage.setText(mContext.getString(R.string.base_loading));
        } else {
            myMessage.setText(msg);
        }
        if(null != handler){
            handler.sendEmptyMessageDelayed(CHECK_DISMISS,maxWaitTime);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(null != handler){
            handler.removeMessages(CHECK_DISMISS);
        }
        handler = null;
    }
}
