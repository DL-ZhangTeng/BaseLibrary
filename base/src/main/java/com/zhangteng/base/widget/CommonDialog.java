package com.zhangteng.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.zhangteng.base.R;
import com.zhangteng.base.base.BaseDialog;
import com.zhangteng.base.utils.AntiShakeUtils;

public class CommonDialog extends BaseDialog implements View.OnClickListener {
    private TextView contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;
    private View devider;
    private View titleDevider;

    private Context mContext;
    private String content;
    private String positiveName;
    private String negativeName;
    private String title;
    private OnCloseListener listener;

    public CommonDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public CommonDialog(Context context, String content) {
        super(context);
        this.mContext = context;
        this.content = content;
        if (!TextUtils.isEmpty(content))
            contentTxt.setText(content);
    }

    public CommonDialog setPositiveButton(String name) {
        this.positiveName = name;
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }
        return this;
    }

    public CommonDialog setNegativeButton(String name) {
        this.negativeName = name;
        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
            cancelTxt.setVisibility(View.VISIBLE);
            devider.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public CommonDialog setTitle(String title) {
        this.title = title;
        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
            titleTxt.setVisibility(View.VISIBLE);
            titleDevider.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public CommonDialog setContent(String content) {
        this.content = content;
        if (!TextUtils.isEmpty(content)) {
            contentTxt.setText(content);
        }
        return this;
    }

    public CommonDialog setContent(SpannableString content) {
        this.content = content.toString();
        if (!TextUtils.isEmpty(content)) {
            contentTxt.setHighlightColor(Color.TRANSPARENT);
            contentTxt.setMovementMethod(LinkMovementMethod.getInstance());
            contentTxt.setText(content);
        }
        return this;
    }

    public CommonDialog setDialogCancelable(boolean flag) {
        setCancelable(flag);
        return this;
    }

    public CommonDialog setDialogCanceledOnTouchOutside(boolean cancel) {
        setCanceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public int getSelfTitleView() {
        return 0;
    }

    @Override
    public int getSelfContentView() {
        return R.layout.dialog_common;
    }

    @Override
    public int getSelfButtonView() {
        return R.layout.dialog_common_button;
    }

    @Override
    public void initView(View view) {
        contentTxt = view.findViewById(R.id.content);
        titleTxt = view.findViewById(R.id.title);
        titleDevider = view.findViewById(R.id.titleDevider);
        devider = view.findViewById(R.id.devider);
        submitTxt = view.findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = view.findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (AntiShakeUtils.isInvalidClick(v))
            return;
        if (v.getId() == R.id.cancel) {
            if (listener != null) {
                listener.onClick(this, false);
            }
            this.dismiss();
        } else if (v.getId() == R.id.submit) {
            if (listener != null) {
                listener.onClick(this, true);
            }
            this.dismiss();
        }
    }

    public CommonDialog setListener(OnCloseListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnCloseListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}
