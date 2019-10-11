package demo.xy.com.xytdcq.androidIPC.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import demo.xy.com.mylibrary.log.LogUtil;
import demo.xy.com.xytdcq.R;
import demo.xy.com.xytdcq.base.BaseActivity;

public class ThirdPayJavaActivity extends BaseActivity {


    @BindView(R.id.pay_main_layout)
    RelativeLayout main_layout;
    @BindView(R.id.pay_hint)
    TextView payHint;

    private String orderInfo;
    private Float payMoney;
    private ThirdPayService.PayAction payAction;

    @Override
    protected int getLayout() {
        return R.layout.activity_third_pay;
    }

    @Override
    protected void setDataAndEvent() {
        Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        Window window = getWindow();
        android.view.WindowManager.LayoutParams windowLayoutParams = window.getAttributes(); // 获取对话框当前的参数值
        windowLayoutParams.width = (int) (display.getWidth()); // 宽度设置为屏幕
        windowLayoutParams.height = (int) (display.getHeight()); // 宽度设置为屏幕
        //状态栏文字根据状态栏颜色变化成深色或者淡色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        Intent intent = getIntent();
        if(null != intent){
            orderInfo =  intent.getStringExtra("orderInfo");
            payMoney =  intent.getFloatExtra("payMoney",0.0f);
            payHint.setText("id:"+orderInfo+" money:"+payMoney);

        }
        //绑定本进程的支付服务进行回调
        Intent mIntent = new Intent(this, ThirdPayService.class);
        bindService(mIntent,mResultConn , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mResultConn);
        super.onDestroy();
    }

    private ServiceConnection mResultConn = new  ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.d("third pay activity onServiceConnected");
            payAction = (ThirdPayService.PayAction) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.d("third pay activity onServiceDisconnected");
        }
    };
    public void btnClick(View v){
        if(v.getId() == R.id.pay_comfirm){
            if(null != payAction){
                payAction.pay(payMoney);
            }
        }else if(v.getId() == R.id.pay_main_layout){
            if(null != payAction){
                payAction.onUserCancel();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(null != payAction){
            payAction.onUserCancel();
            finish();
        }
    }
}
