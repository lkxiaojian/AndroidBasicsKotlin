package com.zky.basics.common.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.zky.basics.common.R;
import com.zky.basics.common.util.DisplayUtil;
import com.zky.basics.common.util.MultiMediaUtil;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import java.util.List;


public class PhotoSelectDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String TAG = PhotoSelectDialog.class.getSimpleName();
    private OnPhotoClickLisener mOnClickLisener;
    private String mPhotoPath;
    private int count=1;//图片选择的数量


    public void setOnClickLisener(OnPhotoClickLisener onPhotoClickLisener) {
        mOnClickLisener = onPhotoClickLisener;
    }

    public static PhotoSelectDialog newInstance() {
        return new PhotoSelectDialog();
    }

    public interface OnPhotoClickLisener {
        void onTakePhototClick(String path);

        void onSelectPhotoClick(List<String> list);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(getResources().getDisplayMetrics().widthPixels - DisplayUtil.dip2px(16) * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_select, container, false);
        Button btnSelectPhoto = (Button) view.findViewById(R.id.btn_select_photo);
        Button btnTakephoto = (Button) view.findViewById(R.id.btn_take_photo);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);

        btnSelectPhoto.setOnClickListener(this);
        btnTakephoto.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_take_photo) {
            mPhotoPath = MultiMediaUtil.getPhotoPath(getActivity());
            MultiMediaUtil.takePhoto(this, mPhotoPath, MultiMediaUtil.TAKE_PHONE);

        } else if (i == R.id.btn_select_photo) {
            if(count<1){
                count=1;
            }
            MultiMediaUtil.pohotoSelect(this, count, MultiMediaUtil.SELECT_IMAGE);

        } else if (i == R.id.btn_cancel) {
            dismiss();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MultiMediaUtil.SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    if (mOnClickLisener != null) {
                        mOnClickLisener.onSelectPhotoClick(path);
                    }
                    dismiss();
                }
                break;
            case MultiMediaUtil.TAKE_PHONE:
                Log.v(TAG, "img path:" + mPhotoPath);
                if (mOnClickLisener != null) {
                    mOnClickLisener.onTakePhototClick(mPhotoPath);
                }
                dismiss();
                break;
        }
    }

    public void setCount(int count) {
        this.count = count;
    }
}
