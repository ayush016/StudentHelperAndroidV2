package in.handwritten.android.homescreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Timer;
import java.util.TimerTask;

import in.handwritten.android.splashscreen.R;
import in.handwritten.android.customviews.TypewriterView;

public class textSubmissionFragment extends Fragment {

    final int[] i = new int[1];
    TypewriterView mSwitcher;
    final String textToShow[] = {"Hemant is doing your work ....", "You can chill & sit back","Hemant likes to do work in a quiet environment", "Hemant has learned handwriting after analysing thousand's of humans ","Please be patient ...."};
    int textToShowIndex = 0;
    YoYo.YoYoString animation;
    boolean isRunning;
    public  textSubmissionFragment(){
        super(R.layout.activity_main2);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwitcher = (TypewriterView) getActivity().findViewById(R.id.textSwitcher);
        mSwitcher.type(textToShow[0],50);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!mSwitcher.isRunning && mSwitcher.getText().toString().length()==textToShow[textToShowIndex].length()) {
                    if(!isRunning) {
                        animation = YoYo.with(Techniques.FadeInDown).playOn(getActivity().findViewById(R.id.textSwitcher));
                        textToShowIndex++;
                        if (textToShowIndex > textToShow.length - 1) {
                            textToShowIndex = 0;
                        }
                        mSwitcher.setText("");
                        mSwitcher.type(textToShow[textToShowIndex]);
                    }
                }
                if(animation!=null) {
                    isRunning = animation.isRunning();
                }
                handler.postDelayed(this,2000);
            }
        }, 1000);
    }

    private int getScreenWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return displayMetrics.widthPixels;
    }

    public void setText(final String s, final TextView tv)
    {
        i[0] = 0;
        final int length = s.length();

        final Timer timer = new Timer();
        TimerTask taskEverySplitSecond = new TimerTask() {
            @Override
            public void run() {
                handleTextAnimation(s, tv);
                if (i[0] == length) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(taskEverySplitSecond, 1, 80);
    }

    private void handleTextAnimation(final String s,final TextView tv) {
        if (getActivity() != null && !getActivity().isFinishing()) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (i[0] == s.length()) {
                        //YoYo.with(Techniques.Pulse).playOn(getActivity().findViewById(R.id.textSwitcher));
                        i[0] = 0;
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText("");
                                textToShowIndex++;
                                if(textToShowIndex>textToShow.length-1){
                                    textToShowIndex = 0;
                                }
                                setText(textToShow[textToShowIndex], mSwitcher);
                            }
                        }, 2500);

                    } else if (i[0] < s.length()) {
                        char c = s.charAt(i[0]);
                        tv.append(String.valueOf(c));
                        i[0]++;
                    }else{
                        i[0] = 0;
                    }
                }
            });
        }
    }

    public void onComputerTextSubmitted(String dummyText,boolean isSuccess) {

        View line1 =  (View) getActivity().findViewById(R.id.line1);
        View line2 =  (View) getActivity().findViewById(R.id.line2);
        LottieAnimationView lottieAnimation = (LottieAnimationView) getActivity().findViewById(R.id.progress);
        YoYo.with(Techniques.FadeOut).playOn(getActivity().findViewById(R.id.progress));
        lottieAnimation.setVisibility(View.GONE);
        mSwitcher.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);
        LottieAnimationView lottieAnimationComplete;
        if(isSuccess) {
            lottieAnimationComplete = (LottieAnimationView) getActivity().findViewById(R.id.progressComplete);
            YoYo.with(Techniques.FadeIn).playOn(getActivity().findViewById(R.id.progressComplete));
        }else{
            lottieAnimationComplete = (LottieAnimationView) getActivity().findViewById(R.id.progressFail);
            YoYo.with(Techniques.FadeIn).playOn(getActivity().findViewById(R.id.progressFail));
            TextView failedText = (TextView) getActivity().findViewById(R.id.progressFailText);
            failedText.setText("Sorry we failed, Hemant is still a noob");
            failedText.setVisibility(View.VISIBLE);
        }
        lottieAnimationComplete.setVisibility(View.VISIBLE);

        if(isSuccess) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    downloadAndOpenPDF(dummyText);
                }
            }, 3000);
        }

    }

    void downloadAndOpenPDF(String dummyText) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(dummyText));
        startActivity(browserIntent);
    }


}
