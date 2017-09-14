package site.chniccs.basefrm.widget;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import site.chniccs.basefrm.R;
import site.chniccs.basefrm.listener.IMessageDialogClickListener;
import site.chniccs.basefrm.utils.StringUtils;
import site.chniccs.basefrm.widget.base.BaseDialog;

/**
 * Created by chniccs on 2017/9/8 16:12.
 */

public class MessageDialog extends BaseDialog {
    private Builder mBuilder;

    private MessageDialog(Activity context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    @Override
    protected void canOperationView() {

    }

    @Override
    public void init() {
        TextView mTvTitle = (TextView) mContent.findViewById(R.id.tv_title);
        TextView mTvMessage = (TextView) mContent.findViewById(R.id.dialog_tv_middle);
        TextView mBtnLeft = (TextView) mContent.findViewById(R.id.common_dialog_btn_left);
        TextView mBtnRight = (TextView) mContent.findViewById(R.id.common_dialog_btn_right);
        if (StringUtils.isEmpty(mBuilder.getBtnLeft())) {
            mBtnLeft.setVisibility(View.GONE);
        } else {
            mBtnLeft.setVisibility(View.VISIBLE);
            mBtnLeft.setText(mBuilder.getBtnLeft());
            mBtnLeft.setOnClickListener(this);
        }
        if (StringUtils.isEmpty(mBuilder.getBtnRight())) {
            mBtnRight.setVisibility(View.GONE);
        } else {
            mBtnRight.setVisibility(View.VISIBLE);
            mBtnRight.setText(mBuilder.getBtnRight());
            mBtnRight.setOnClickListener(this);
        }
        if (StringUtils.isEmpty(mBuilder.getMessage())) {
            mTvMessage.setVisibility(View.GONE);
        } else {
            mTvMessage.setVisibility(View.VISIBLE);
            mTvMessage.setText(mBuilder.getMessage());
        }
        if (StringUtils.isEmpty(mBuilder.getTitle())) {
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mTvTitle.setText(mBuilder.getTitle());
        }

    }

    @Override
    public int getChildView() {
        return R.layout.dialog_message;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.common_dialog_btn_left) {
            if (mBuilder.getListener() != null) {
                mBuilder.getListener().onDisagree();
                if (mBuilder.isDismissOnClickBtn()) {
                    this.dismiss();
                }
            }
        } else if (id == R.id.common_dialog_btn_right) {
            if (mBuilder.getListener() != null) {
                mBuilder.getListener().onAgree();
                if (mBuilder.isDismissOnClickBtn()) {
                    this.dismiss();
                }
            }
        }
    }

    public final static class Builder {
        private String message;
        private String title;
        private String btnLeft;
        private String btnRight;
        private boolean dismissOnClickBtn;
        private IMessageDialogClickListener mListener;

        public boolean isDismissOnClickBtn() {
            return dismissOnClickBtn;
        }

        public Builder setDismissOnClickBtn(boolean dismissOnClickBtn) {
            this.dismissOnClickBtn = dismissOnClickBtn;
            return this;
        }


        public Builder() {
        }

        public String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setBtnLeft(String btnLeft) {
            this.btnLeft = btnLeft;
            return this;
        }

        public Builder setBtnRight(String btnRight) {
            this.btnRight = btnRight;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public String getBtnLeft() {
            return btnLeft;
        }

        public String getBtnRight() {
            return btnRight;
        }

        public IMessageDialogClickListener getListener() {
            return mListener;
        }

        public Builder setListener(IMessageDialogClickListener listener) {
            mListener = listener;
            return this;
        }

        public MessageDialog build(Activity activity) {
            return new MessageDialog(activity, this);
        }
    }
}
