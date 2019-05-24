package com.fxk.android_tv.hatatv;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ErrorFragment extends android.support.v17.leanback.app.ErrorFragment {

    private static final String TAG = ErrorFragment.class.getSimpleName();
    private static final boolean TRANSLUCENT = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setTitle(getResources().getString(R.string.app_name));
        setErrorContent();
    }

    void setErrorContent() {
        setImageDrawable(getActivity().getDrawable(R.drawable.lb_ic_sad_cloud));

        if(getArguments() != null && getArguments().containsKey("error_type")){
            setMessage("Возникла ошибка, какая - точно ен скажу! В обработке..");
            setDefaultBackground(TRANSLUCENT);

            setButtonText("Попробовать снова");
            setButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    getParentFragment().getActivity().
                    getFragmentManager().beginTransaction().remove(ErrorFragment.this).commit();
                }
            });
        }else {
            getFragmentManager().beginTransaction().remove(ErrorFragment.this).commit();
        }
    }
}
