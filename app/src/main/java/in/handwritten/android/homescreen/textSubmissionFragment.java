package in.handwritten.android.homescreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Timer;
import java.util.TimerTask;

import in.handwritten.android.customviews.YoYoAnimatorWrapper;
import in.handwritten.android.splashscreen.R;
import in.handwritten.android.customviews.TypewriterView;
import in.handwritten.android.utils.SharedPreferenceManager;
import in.handwritten.android.utils.Utils;

public class textSubmissionFragment extends Fragment {

    final int[] i = new int[1];
    final String textToShow[] = {"Hemant is doing your work ....", "You can chill & sit back","Hemant likes to do work in a quiet environment", "Hemant has learned handwriting after analysing thousand's of humans ","Please be patient ...."};
    int textToShowIndex = 0;
    YoYo.YoYoString animation;
    boolean isRunning;
    final Handler handler = new Handler();
    public  textSubmissionFragment(){
        super(R.layout.activity_main2);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() ==  null){
            return;
        }
        TypewriterView mSwitcher = (TypewriterView) getActivity().findViewById(R.id.textSwitcher);
        mSwitcher.type(textToShow[0],50);
        mSwitcher.animate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               if(!mSwitcher.isRunning){
                   textToShowIndex++;
                   if (textToShowIndex > textToShow.length - 1) {
                       textToShowIndex = 0;
                   }
                   mSwitcher.setText("");
                   mSwitcher.type(textToShow[textToShowIndex]);
               }
                handler.postDelayed(this,2000);
            }
        },1000);
    }


    public void onComputerTextSubmitted(String dummyText,boolean isSuccess) {
        if(getActivity() == null || getView() == null) return;
        View line1 =  (View) getActivity().findViewById(R.id.line1);
        View line2 =  (View) getActivity().findViewById(R.id.line2);
        LottieAnimationView lottieAnimation = (LottieAnimationView) getActivity().findViewById(R.id.progress);
        new YoYoAnimatorWrapper(Techniques.FadeOut,getActivity().findViewById(R.id.progress),-1).safeCallToYoYo(false);
        //YoYo.with(Techniques.FadeOut).playOn(getActivity().findViewById(R.id.progress));
        lottieAnimation.setVisibility(View.GONE);
        TypewriterView mSwitcher = getActivity().findViewById(R.id.textSwitcher);
        mSwitcher.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        mSwitcher.clearAnimation();
        LottieAnimationView lottieAnimationComplete;
        handler.removeCallbacksAndMessages(null);
        if(isSuccess) {
            lottieAnimationComplete = (LottieAnimationView) getActivity().findViewById(R.id.progressComplete);
            new YoYoAnimatorWrapper(Techniques.FadeIn,getActivity().findViewById(R.id.progressComplete),-1).safeCallToYoYo(true);
            //YoYo.with(Techniques.FadeIn).playOn(getActivity().findViewById(R.id.progressComplete));
        }else{
            lottieAnimationComplete = (LottieAnimationView) getActivity().findViewById(R.id.progressFail);
            new YoYoAnimatorWrapper(Techniques.FadeIn,getActivity().findViewById(R.id.progressFail),-1).safeCallToYoYo(true);
            //YoYo.with(Techniques.FadeIn).playOn(getActivity().findViewById(R.id.progressFail));
            TextView failedText = (TextView) getActivity().findViewById(R.id.progressFailText);
            failedText.setText("Sorry we failed, Hemant is still a noob");
            failedText.setVisibility(View.VISIBLE);
        }
        lottieAnimationComplete.setVisibility(View.VISIBLE);
        AppCompatButton downLoadPdf = getActivity().findViewById(R.id.downloadPdf);

        downLoadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadAndOpenPDF(dummyText);
            }
        });

        if(isSuccess) {
            SharedPreferenceManager.setOneTimeDocumentGenerated(getActivity());
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    downLoadPdf.setVisibility(View.VISIBLE);
                    downloadAndOpenPDF(dummyText);
                }
            }, 3000);
        }else {
            downLoadPdf.setVisibility(View.GONE);
        }

    }

    void downloadAndOpenPDF(String dummyText) {
        if(dummyText != null && !dummyText.isEmpty()) {
            Utils.openDefaultBrowser(getActivity(),dummyText);
        }
    }


}
