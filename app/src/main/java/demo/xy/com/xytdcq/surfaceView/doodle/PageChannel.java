package demo.xy.com.xytdcq.surfaceView.doodle;


import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 画板对象
 */
public class PageChannel {

    public DoodleChannel paintChannel; // 绘图通道，自己本人使用
    public List<TransactionData> userDataMap;//记录所有的画笔
    public List<TransactionData> userDataListT;//同步的临时存放数据，画完清空

    public PageChannel() {
        paintChannel = new DoodleChannel();
        userDataMap = new ArrayList<>();
        userDataListT = new ArrayList<>();

    }

    public void clearAll() {
        if (paintChannel.actions != null) {
            paintChannel.actions.clear();
        }
        userDataMap.clear();
    }


    /**
     * 清除画笔
     * @param account 清除账号
     */
    public void clearAccountPaint(String account) {
        if(TextUtils.isEmpty(account)){
            return;
        }
        if (null != paintChannel.actions && paintChannel.actions.size() > 0) {
            for (int i = paintChannel.actions.size()-1; i >= 0; i--) {
                Action action = paintChannel.actions.get(i);
                if(null == action || TextUtils.isEmpty(action.getAccount())){
                    paintChannel.actions.remove(i);
                }else if (action.getAccount().equals(account)){
                    paintChannel.actions.remove(action);
                }
            }
        }
        for (int i = userDataMap.size()-1; i >= 0; i--){
            TransactionData transactionData =  userDataMap.get(i);
            if(null == transactionData || TextUtils.isEmpty(transactionData.getUid())){
                userDataMap.remove(i);
            }else if(account.equals(userDataMap.get(i).getUid())){
                userDataMap.remove(i);
            }
        }
    }
}
