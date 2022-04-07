package in.handwritten.android.customviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import in.handwritten.android.splashscreen.R;

public class WorkInProgressBottomSheet extends BottomSheetDialogFragment {

    String workText;
    boolean showButton;
    WorkInProgress callback;

    public WorkInProgressBottomSheet(String workText,boolean showButton,WorkInProgress callback){
        this.workText = workText;
        this.showButton = showButton;
        this.callback = callback;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogThemeV2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.work_in_progress_bottomsheet,container,false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgCross = view.findViewById(R.id.imgCross);
        TextView workTextView = view.findViewById(R.id.workText);
        Button buttonNavigate = view.findViewById(R.id.buttonNavigate);

        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        if(workText!=null) workTextView.setText(workText);
        if(showButton) buttonNavigate.setVisibility(View.VISIBLE);
        else buttonNavigate.setVisibility(View.GONE);

        buttonNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.selectQuickMode();
                dismiss();
            }
        });
    }

    public interface WorkInProgress {
        void selectQuickMode();
    }
}
