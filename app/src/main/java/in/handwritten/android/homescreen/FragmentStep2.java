package in.handwritten.android.homescreen;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.splashscreen.R;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class FragmentStep2 extends Fragment implements View.OnClickListener {

    private Context context;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ConstraintLayout quickModeLayout;
    ConstraintLayout deepModeLayout;
    CardView moreOptionsCard;
    TextView fairQuality;
    TextView bestQuality;
    TextView averageQuality;
    View pickColor;
    int userSelectedColor = -16777066;
    TextToHandWritingSubmitRequest textToHandWritingSubmitRequest;
    NestedScrollView nestedScrollView;
    homeScreenPresenter presenter;
    textSubmissionFragment textSubmissionFragmentContext;
    public FragmentStep2(Context context,TextToHandWritingSubmitRequest textToHandWritingSubmitRequest,homeScreenPresenter presenter) {
        super(R.layout.fragment_step_2);
        this.context = context;
        this.textToHandWritingSubmitRequest = textToHandWritingSubmitRequest;
        this.presenter = presenter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deepModeLayout = getActivity().findViewById(R.id.deep_mode_layout);
        quickModeLayout = getActivity().findViewById(R.id.quick_mode_layout);
        moreOptionsCard = getActivity().findViewById(R.id.MoreOptionsCard);
        TextView modeText = deepModeLayout.findViewById(R.id.mode_text);
        TextView modeDesc = deepModeLayout.findViewById(R.id.mode_desc);
        TextView modeDescTime = deepModeLayout.findViewById(R.id.mode_desc_time);
        TextView modeDescPercentage = deepModeLayout.findViewById(R.id.mode_percentage);
        TextView modeDescPercentageDesc = deepModeLayout.findViewById(R.id.mode_percentage_desc);
        AppCompatRadioButton checkBoxQuickMode = quickModeLayout.findViewById(R.id.quick_mode);
        AppCompatRadioButton checkBoxDeepMode = deepModeLayout.findViewById(R.id.quick_mode);
        AppCompatButton proceedButton = getActivity().findViewById(R.id.proceedButton);
        checkBoxQuickMode.setTag("quickMode");
        checkBoxQuickMode.setOnClickListener(this);
        checkBoxDeepMode.setOnClickListener(this);
        modeText.setText("DEEP MODE (AI Powered)");
        modeDesc.setText("Quite slow, High accuracy");
        modeDescTime.setText("Estimated time ~ 45 - 60 minutes");
        modeDescPercentage.setText("95%");
        modeDescPercentage.setTextColor(ContextCompat.getColor(context,R.color.green));
        modeDescPercentageDesc.setTextColor(ContextCompat.getColor(context,R.color.green));
        bestQuality = getActivity().findViewById(R.id.best_quality);
        averageQuality = getActivity().findViewById(R.id.average_quality);
        fairQuality = getActivity().findViewById(R.id.bad_quality);
        pickColor = getActivity().findViewById(R.id.pick_color);
        nestedScrollView = getActivity().findViewById(R.id.nestedScroll);

        pickColor.setOnClickListener(this);
        bestQuality.setOnClickListener(this);
        averageQuality.setOnClickListener(this);
        fairQuality.setOnClickListener(this);
        quickModeLayout.setOnClickListener(this);
        deepModeLayout.setOnClickListener(this);

        imageView1 = getView().findViewById(R.id.imageBack1);
        imageView2 = getView().findViewById(R.id.imageBack2);
        imageView3 = getView().findViewById(R.id.imageBack3);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        proceedButton.setOnClickListener(this);

        /*new SimpleTooltip.Builder(getContext())
                .anchorView(imageView1)
                .text("Double Click to enlarge")
                .gravity(Gravity.BOTTOM)
                .backgroundColor(ContextCompat.getColor(context,R.color.skyblue))
                .arrowColor(ContextCompat.getColor(context,R.color.skyblue))
                .animated(true)
                .transparentOverlay(false)
                .build()
                .show();
         */
    }

    public void setLocked(ImageView v) {

        //v.setColorFilter(ContextCompat.getColor(context,R.color.light_grey));
        v.setBackground(ContextCompat.getDrawable(context,R.drawable.image_background_container_clicked));
        v.setImageAlpha(255); // 128 = 0.5
    }

    public void setUnlocked(ImageView v) {
        v.setColorFilter(null);
        v.setImageAlpha(180);
        v.setBackground(ContextCompat.getDrawable(context,R.drawable.image_background_container));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack1:
                setLocked((ImageView) view);
                setUnlocked(imageView2);
                setUnlocked(imageView3);
                textToHandWritingSubmitRequest.setPageType(1);
                break;
            case R.id.imageBack2:
                setLocked((ImageView) view);
                setUnlocked(imageView1);
                setUnlocked(imageView3);
                textToHandWritingSubmitRequest.setPageType(2);
                break;
            case R.id.imageBack3:
                setLocked((ImageView) view);
                setUnlocked(imageView1);
                setUnlocked(imageView2);
                textToHandWritingSubmitRequest.setPageType(3);
                break;
            case R.id.quick_mode_layout:
                //YoYo.with(Techniques.SlideOutUp).playOn(moreOptionsCard);
                textToHandWritingSubmitRequest.setMode(1);
                moreOptionsCard.setVisibility(View.INVISIBLE);
                View dummyView = getActivity().findViewById(R.id.dummyView);
                dummyView.setVisibility(View.GONE);
                setHandwritingModeCheckBox(view,deepModeLayout,quickModeLayout);
                break;
            case R.id.deep_mode_layout:
                textToHandWritingSubmitRequest.setMode(2);
                if(moreOptionsCard.getVisibility() != View.VISIBLE) {
                    YoYo.with(Techniques.SlideInLeft).playOn(moreOptionsCard);
                    moreOptionsCard.setVisibility(View.VISIBLE);
                }
                dummyView = getActivity().findViewById(R.id.dummyView);
                dummyView.setVisibility(View.VISIBLE);
                nestedScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedScrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
                setHandwritingModeCheckBox(view,quickModeLayout,deepModeLayout);
                break;
            case R.id.quick_mode:
                if(view.getTag()!= null && view.getTag()=="quickMode"){
                    //YoYo.with(Techniques.SlideOutUp).playOn(moreOptionsCard);
                    textToHandWritingSubmitRequest.setMode(1);
                    moreOptionsCard.setVisibility(View.INVISIBLE);
                    dummyView = getActivity().findViewById(R.id.dummyView);
                    dummyView.setVisibility(View.GONE);
                    setHandwritingModeCheckBox(view,deepModeLayout,quickModeLayout);
                }else {

                    if(moreOptionsCard.getVisibility() != View.VISIBLE) {
                        YoYo.with(Techniques.SlideInLeft).playOn(moreOptionsCard);
                        moreOptionsCard.setVisibility(View.VISIBLE);
                        dummyView = getActivity().findViewById(R.id.dummyView);
                        dummyView.setVisibility(View.VISIBLE);
                    }
                    nestedScrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            nestedScrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                    textToHandWritingSubmitRequest.setMode(2);
                    setHandwritingModeCheckBox(view, quickModeLayout, deepModeLayout);
                }
                break;
            case R.id.best_quality:
                setClickedBackground((TextView) view);
                setUnClickedBackground(averageQuality);
                setUnClickedBackground(fairQuality);
                textToHandWritingSubmitRequest.setQuality(TextToHandWritingSubmitRequest.qualityIndex.BEST);
                break;
            case R.id.bad_quality:
                setClickedBackground((TextView) view);
                setUnClickedBackground(bestQuality);
                setUnClickedBackground(averageQuality);
                textToHandWritingSubmitRequest.setQuality(TextToHandWritingSubmitRequest.qualityIndex.FAIR);
                break;
            case R.id.average_quality:
                setClickedBackground((TextView) view);
                setUnClickedBackground(bestQuality);
                setUnClickedBackground(fairQuality);
                textToHandWritingSubmitRequest.setQuality(TextToHandWritingSubmitRequest.qualityIndex.AVERAGE);
                break;
            case R.id.pick_color:
                openColorPicker();
                break;

            case R.id.proceedButton:
                if(textToHandWritingSubmitRequest.getMode()!=0 && textToHandWritingSubmitRequest.getPageType()!=0) {
                    presenter.submitComputerText(textToHandWritingSubmitRequest);
                    textSubmissionFragmentContext = new textSubmissionFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.container, textSubmissionFragmentContext, null).commit();
                }
                break;
        }
    }
    private void setClickedBackground(TextView view){
        view.setBackground(ContextCompat.getDrawable(context,R.drawable.image_background_container_clicked));
    }

    private void setUnClickedBackground(TextView view){
        view.setBackground(null);
    }

    private void openColorPicker(){
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(userSelectedColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        userSelectedColor = selectedColor;
                        String hexColor = String.format("#%06X", (0xFFFFFF & selectedColor));
                        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.color_picker_background);
                        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                        DrawableCompat.setTint(wrappedDrawable, selectedColor);
                        pickColor.setBackground(wrappedDrawable);
                        //changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void setHandwritingModeCheckBox(View view,ConstraintLayout constraintLayout,ConstraintLayout constraintLayoutToEnable){
        AppCompatRadioButton checkBox = view.findViewById(R.id.quick_mode);
        AppCompatRadioButton checkBoxDisable = constraintLayout.findViewById(R.id.quick_mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            constraintLayout.setElevation(0f);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            constraintLayoutToEnable.setElevation(8f);
        }
        checkBoxDisable.setChecked(false);
        checkBoxDisable.jumpDrawablesToCurrentState();
        checkBox.setChecked(true);
    }

    public void onComputerTextSubmitted(String dummyText,boolean isSuccess){
        textSubmissionFragmentContext.onComputerTextSubmitted(dummyText,isSuccess);
    }
}
