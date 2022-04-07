package in.handwritten.android.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

import in.handwritten.android.splashscreen.R;
import in.handwritten.android.utils.SharedPreferenceManager;
import in.handwritten.android.utils.Utils;

public class StarRatingView {

    public void showDialog(final Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.star_rating_view);
        final AppCompatRatingBar ratingBar = (AppCompatRatingBar) dialog.findViewById(R.id.simpleRatingBar);
        AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.submitButton);
        ImageView close = (ImageView) dialog.findViewById(R.id.close);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(v>=4){
                    button.setText("Submit & Rate us on PlayStore");
                }else if(v>0){
                    button.setText("Submit (We will try to improve)");
                }else {
                    button.setText("SUBMIT");
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ratingBar.getRating() >= 4){
                    Utils.openPlayStore(activity);
                    SharedPreferenceManager.setUserWentToPlayStore(activity);
                    dialog.dismiss();
                }else if(ratingBar.getRating()>0){
                    Utils.showWorkInProgressToast(activity,"Thank you for submitting your response. we will work on it");
                    SharedPreferenceManager.setUserWentToPlayStore(activity);
                    dialog.dismiss();
                }else {
                    Utils.showWorkInProgressToast(activity,"Please select start ratings to submit");

                }


            }
        });
        dialog.show();

    }
}
