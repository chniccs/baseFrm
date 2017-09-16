package site.chniccs.basefrm.widget;

import android.app.Activity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import site.chniccs.basefrm.R;
import site.chniccs.basefrm.listener.IInputDialogClickListener;
import site.chniccs.basefrm.utils.StringUtils;
import site.chniccs.basefrm.utils.ToastUtil;
import site.chniccs.basefrm.widget.base.BaseDialog;

/**
 * Created by chniccs on 2017/9/16 14:10.
 * 输入框的dialog
 */

public class InputDialog extends BaseDialog {
    private Builder mBuilder;
    private HashMap<String, EditText> mInputEts;

    @Override
    protected void canOperationView() {

    }

    @Override
    public void init() {
        TextView mTvMessage = (TextView) mContent.findViewById(site.chniccs.basefrm.R.id.dialog_tv_middle);
        TextView mBtnLeft = (TextView) mContent.findViewById(site.chniccs.basefrm.R.id.common_dialog_btn_left);
        TextView mBtnRight = (TextView) mContent.findViewById(site.chniccs.basefrm.R.id.common_dialog_btn_right);
        LinearLayout llInput = (LinearLayout) mContent.findViewById(site.chniccs.basefrm.R.id.ll_input_container);
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
        if (mBuilder.getInputs() != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContent.getContext());
            mInputEts = new HashMap<>();
            for (Map.Entry<String, InputInfo> stringInputInfoEntry : mBuilder.getInputs().entrySet()) {
                View view = layoutInflater.inflate(R.layout.dialog_item_input, llInput, false);
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                EditText etInput = (EditText) view.findViewById(R.id.et_content);
                tvTitle.setText(stringInputInfoEntry.getKey());
                InputInfo inputInfo = stringInputInfoEntry.getValue();
                etInput.setText(StringUtils.getString(inputInfo.defaultValue));
                etInput.setInputType(inputInfo.inputType);
                etInput.setHint(StringUtils.getString(inputInfo.hint));
                mInputEts.put(stringInputInfoEntry.getKey(), etInput);
                llInput.addView(view);
            }
        }
    }

    @Override
    public int getChildView() {
        return R.layout.dialog_input;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == site.chniccs.basefrm.R.id.common_dialog_btn_left) {
            if (mBuilder.getListener() != null) {
                mBuilder.getListener().onDisagree();
                if (mBuilder.isDismissOnClickBtn()) {
                    this.dismiss();
                }
            }
        } else if (id == site.chniccs.basefrm.R.id.common_dialog_btn_right) {
            if (mBuilder.getListener() != null) {
                //创建一个返回的map
                LinkedHashMap<String, String> inputCallBack = new LinkedHashMap<>();
                boolean allRight = true;
                //对所有输入项目进行验证
                for (Map.Entry<String, InputInfo> stringInputInfoEntry : mBuilder.getInputs().entrySet()) {
                    InputInfo value = stringInputInfoEntry.getValue();
                    String key = stringInputInfoEntry.getKey();
                    EditText editText = mInputEts.get(key);
                    //如果有不能为空的未输入，则中止
                    if (value.notNull && StringUtils.isEmpty(editText.getText().toString().trim())) {
                        ToastUtil.show(mContent.getContext(), "请输入" + key);
                        allRight = false;
                        break;
                    }
                    inputCallBack.put(key, editText.getText().toString().trim());
                }
                if (!allRight) {
                    return;
                }
                mBuilder.getListener().onAgree(inputCallBack);
                if (mBuilder.isDismissOnClickBtn()) {
                    this.dismiss();
                }
            }
        }
    }

    private InputDialog(Activity context, Builder builder) {
        super(context);
        mBuilder = builder;
    }

    public final static class Builder {
        private String message;
        private String btnLeft;
        private String btnRight;
        private boolean dismissOnClickBtn;
        private IInputDialogClickListener mListener;
        private LinkedHashMap<String, InputInfo> mInputs;

        public LinkedHashMap<String, InputInfo> getInputs() {
            return mInputs;
        }

        public Builder setInputs(LinkedHashMap<String, InputInfo> inputs) {
            mInputs = inputs;
            return this;
        }

        public boolean isDismissOnClickBtn() {
            return dismissOnClickBtn;
        }

        public Builder setDismissOnClickBtn(boolean dismissOnClickBtn) {
            this.dismissOnClickBtn = dismissOnClickBtn;
            return this;
        }

        public Builder() {
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

        public IInputDialogClickListener getListener() {
            return mListener;
        }

        public Builder setListener(IInputDialogClickListener listener) {
            mListener = listener;
            return this;
        }

        public InputDialog build(Activity activity) {
            return new InputDialog(activity, this);
        }
    }

    public static class InputInfo {
        public InputInfo(String hint, boolean notNull, String defaultValue, int inputType) {
            this.hint = hint;
            this.notNull = notNull;
            this.defaultValue = defaultValue;
            this.inputType = inputType;
        }
        public InputInfo(String hint, boolean notNull, String defaultValue) {
            this.hint = hint;
            this.notNull = notNull;
            this.defaultValue = defaultValue;
            this.inputType = InputType.TYPE_CLASS_TEXT;
        }

        //提示字符
        public String hint;
        //不能为空
        public boolean notNull = false;
        //默认值
        public String defaultValue;
        /**
         * android.text.InputType
         */
        public int inputType;
    }
}
