package demo.xy.com.mylibrary.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import demo.xy.com.mylibrary.R;


/**
 * @Description: 创建dialog
 */

public class DialogFactory {

    //默认提示框、无title、不可定义取消与确定按钮
    public static Dialog createTipDialog(Context context, String msg, View.OnClickListener listener) {
        return createTipDialog(context, msg, null, null, listener);
    }

    //默认提示框、无title、不可定义取消与确定按钮
    public static Dialog createTipDialog(Context context, SpannableStringBuilder style, View.OnClickListener listener) {
        return createTipDialog(context, style, null, null, listener);
    }

    //默认提示框、无title、可定义取消与确定按钮
    public static Dialog createTipDialog(Context context, CharSequence msg, String cancel, String confirm, View.OnClickListener listener) {
        return createTipDialog(context, null, msg, cancel, confirm, listener);
    }

    /**
     * 弹出对话框
     *
     * @param context
     * @param title    标题
     * @param msg      提示内容
     * @param cancel   取消按钮自定义
     * @param confirm  确定按钮自定义
     * @param listener
     * @return
     */
    public static Dialog createTipDialog(Context context, String title, CharSequence msg,
                                         String cancel, String confirm, View.OnClickListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.base_dialog_tip, null);

        ((TextView) view.findViewById(R.id.tip_msg)).setText(msg);
        TextView tipCancel = (TextView) view.findViewById(R.id.tip_cancel);
        TextView tipComfirm = (TextView) view.findViewById(R.id.tip_comfirm);
        if (!TextUtils.isEmpty(cancel)) tipCancel.setText(cancel);
        if (!TextUtils.isEmpty(confirm)) tipComfirm.setText(confirm);

        //不为空显示标题
        if (!TextUtils.isEmpty(title)) {
            (view.findViewById(R.id.title_lay)).setVisibility(View.VISIBLE);
            TextView titleTv = (TextView) view.findViewById(R.id.title);
            titleTv.setText(title);
        }
        if (null != listener) {
            tipCancel.setOnClickListener(listener);
            tipComfirm.setOnClickListener(listener);
        }
        return CreateSimpleDialog(context, view);
    }


    //只带有确定按钮
    public static Dialog createConfirmDialog(Context context, String title, SpannableStringBuilder contentStyle, View.OnClickListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.base_dialog_confirm, null);

        ((TextView) view.findViewById(R.id.tip_msg)).setText(contentStyle);
        TextView tipComfirm = (TextView) view.findViewById(R.id.tip_comfirm);
        //不为空显示标题
        if (!TextUtils.isEmpty(title)) {
            LinearLayout title_lay = (LinearLayout) view.findViewById(R.id.title_lay);
            TextView titleTv = (TextView) view.findViewById(R.id.title);
            title_lay.setVisibility(View.VISIBLE);
            titleTv.setText(title);
        }
        if (null != listener) {
            tipComfirm.setOnClickListener(listener);
        }
        return CreateSimpleDialog(context, view);
    }


    public static Dialog createInputDialog(Context context, String title, String tip, View.OnClickListener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.base_dialog_input, null);
        ((TextView) view.findViewById(R.id.dialog_title)).setText(title);
        TextView tipCancel = (TextView) view.findViewById(R.id.tip_cancel);
        final TextView tipComfirm = (TextView) view.findViewById(R.id.tip_comfirm);

        EditText editText = (EditText) view.findViewById(R.id.et_input_text);
        editText.setHint(tip);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tipComfirm.setTag(s.toString().trim());
            }
        });

        if (null != listener) {
            tipCancel.setOnClickListener(listener);
            tipComfirm.setOnClickListener(listener);
        }
        return CreateSimpleDialog(context, view);
    }


    /**
     * 创建自定义不加dialog
     *
     * @param context   上下文对象
     * @param view      界面view
     * @param canCancel 能否点击界面取消
     * @return
     */
    public static Dialog createSelfDialog(Context context, View view, boolean canCancel) {
        Dialog dialog = CreateSimpleDialog(context, view);
        dialog.setCancelable(canCancel);
        return dialog;
    }

    /**
     * 创建dialog
     *
     * @param context
     * @param msg
     * @return
     */
    public static ProgressDialog createProgressDialog(Context context, String msg) {
        ProgressDialog dialog;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
        } else {
            dialog = new ProgressDialog(context);
        }
        if (TextUtils.isEmpty(msg)) {
            dialog.setMessage(context.getString(R.string.base_loading));
        } else {
            dialog.setMessage(msg);
        }
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
        return dialog;
    }
    public static Dialog createLoadingDialog(Context context, String msg, int maxWaitTime) {
        MyProgressDialog dialog = new MyProgressDialog(context,R.style.loading_dialog,msg,maxWaitTime);
//        dialog.setCancelable(false);
        return dialog;
    }
    //生成dialog
    private static Dialog CreateSimpleDialog(Context context, View view) {
        Dialog dialog = new Dialog(context, R.style.dialog_tip);
        dialog.setCanceledOnTouchOutside(false); //禁止外部点击取消
//        dialog.setCancelable(false);//禁止返回键取消 --- 外部去设置
        dialog.setContentView(view, new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Window window = dialog.getWindow();//添加动画
        window.setWindowAnimations(R.style.dialog_anim_sytle);
        return dialog;
    }
}
