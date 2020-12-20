package demo.xy.com.xytdcq.uitls;

import demo.xy.com.mylibrary.log.LogUtil;

public class Utils {
    //求交集
    public static boolean getInterval(float s1, float e1, float s2, float e2){
        float s11 = Math.min(s1, s2);
        float s22 = Math.max(s1, s2);
        float e11 = Math.min(e1, e2);
        float e22 = Math.max(e1, e2);
        if(s22 < e11 || e22 < s11) {
//        if(e11 < s22 || e22 < s11) {
//            demo.xy.com.mylibrary.log.LogUtil.e("无交集:" + s11 + "->" + s22 + ",:" + e11 + "->" + e22);
            return false;
        }else{
//            demo.xy.com.mylibrary.log.LogUtil.i("有交集:" + s11 + "->" + s22 + ",:" + e11 + "->" + e22);
//            LogUtil.i("两集合的交集为：[" + Math.max(s1,s2) + ","  +  Math.min(e1,e2) + "]");
            return true;
        }
    }
}
