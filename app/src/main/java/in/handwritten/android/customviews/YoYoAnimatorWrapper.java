package in.handwritten.android.customviews;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class YoYoAnimatorWrapper {
    private Techniques techniques;
    private View view;
    private long duration;

    public YoYoAnimatorWrapper(Techniques techniques,View view,long duration){
        this.techniques = techniques;
        this.view = view;
        this.duration = duration;
    }

    /**
     * This is done to avoid any crashes in production
     * since YoYo is still in testing for older device
     */

    public void safeCallToYoYo(boolean isVisibility){
        try {
            if(duration == -1) {
                YoYo.with(techniques).playOn(view);
            }else{
                YoYo.with(techniques).duration(duration).playOn(view);
            }
        }catch (Exception e){
            if(!isVisibility) view.setVisibility(View.GONE);
        }
    }
}
