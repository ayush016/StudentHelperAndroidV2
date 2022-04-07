package in.handwritten.android.homescreen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import in.handwritten.android.customviews.WorkInProgressBottomSheet;
import in.handwritten.android.customviews.YoYoAnimatorWrapper;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.splashscreen.R;
import in.handwritten.android.utils.SharedPreferenceManager;
import in.handwritten.android.utils.SimpleTooltipV2;
import in.handwritten.android.utils.Utils;

public class FragmentStep2 extends Fragment implements View.OnClickListener, WorkInProgressBottomSheet.WorkInProgress {

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
    SimpleTooltipV2 selectPageBuilder;
    SimpleTooltipV2 selectModeBuilder;
    ConstraintLayout selectStep2;
    boolean isToolTipShown;
    int LAUNCH_SECOND_ACTIVITY = 10111;
    float xAxis;
    float yAxis;
    float yDiff;

    public FragmentStep2(Context context,TextToHandWritingSubmitRequest textToHandWritingSubmitRequest,homeScreenPresenter presenter) {
        super(R.layout.fragment_step_2);
        this.context = context;
        this.textToHandWritingSubmitRequest = textToHandWritingSubmitRequest;
        this.presenter = presenter;
    }

    public FragmentStep2(){

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
        TextView proBanner = deepModeLayout.findViewById(R.id.pro_banner);
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
        proBanner.setVisibility(View.VISIBLE);
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
        selectStep2 = getActivity().findViewById(R.id.selectStep2);

        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        proceedButton.setOnClickListener(this);
        if(!isToolTipShown && SharedPreferenceManager.getTooltipDisplayCounter(getActivity())<2) {
            inflateTooltipView();
        }
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

    private void inflateTooltipView(){
        selectPageBuilder = new SimpleTooltipV2.Builder(getContext())
                .anchorView(getView().findViewById(R.id.selectPageCard))
                .text("Select Page for your work")
                .gravity(Gravity.BOTTOM)
                .textColor(ContextCompat.getColor(context,R.color.colorAccent))
                .highlightShape(0)
                .backgroundColor(ContextCompat.getColor(context,R.color.indigoV2))
                .arrowColor(ContextCompat.getColor(context,R.color.indigoV2))
                .animated(true)
                .transparentOverlay(false)
                .build();

        selectPageBuilder.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack1:
                setLocked((ImageView) view);
                setUnlocked(imageView2);
                setUnlocked(imageView3);
                textToHandWritingSubmitRequest.setPageType(1);
                if(!isToolTipShown && SharedPreferenceManager.getTooltipDisplayCounter(getActivity())<2 && textToHandWritingSubmitRequest.getMode() == 0) {
                    showSelectModeBuilder();
                }
                break;
            case R.id.imageBack2:
                setLocked((ImageView) view);
                setUnlocked(imageView1);
                setUnlocked(imageView3);
                textToHandWritingSubmitRequest.setPageType(2);
                if(!isToolTipShown && SharedPreferenceManager.getTooltipDisplayCounter(getActivity())<2 && textToHandWritingSubmitRequest.getMode() == 0) {
                    showSelectModeBuilder();
                }
                break;
            case R.id.imageBack3:
                //Utils.showWorkInProgressToast(getContext(),"We are working");
                WorkInProgressBottomSheet workInProgressBottomSheet = new WorkInProgressBottomSheet(getString(R.string.wip_custom_background),false,this);
                workInProgressBottomSheet.show(getActivity().getSupportFragmentManager(),"CustomUploadWIP");
                //Intent i = new Intent(getActivity(), ImageUploadActivity.class);
                //startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);
                /*setLocked((ImageView) view);
                setUnlocked(imageView1);
                setUnlocked(imageView2);
                textToHandWritingSubmitRequest.setPageType(3);*/
                break;
            case R.id.quick_mode_layout:
                textToHandWritingSubmitRequest.setMode(1);
                moreOptionsCard.setVisibility(View.INVISIBLE);
                View dummyView = getActivity().findViewById(R.id.dummyView);
                dummyView.setVisibility(View.GONE);
                setHandwritingModeCheckBox(view,deepModeLayout,quickModeLayout);
                break;
            case R.id.deep_mode_layout:
                textToHandWritingSubmitRequest.setMode(2);
                if(moreOptionsCard.getVisibility() != View.VISIBLE) {
                    new YoYoAnimatorWrapper(Techniques.SlideInLeft,moreOptionsCard,-1).safeCallToYoYo(true);
                    //YoYo.with(Techniques.SlideInLeft).playOn(moreOptionsCard);
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
                    textToHandWritingSubmitRequest.setMode(1);
                    moreOptionsCard.setVisibility(View.INVISIBLE);
                    dummyView = getActivity().findViewById(R.id.dummyView);
                    dummyView.setVisibility(View.GONE);
                    setHandwritingModeCheckBox(view,deepModeLayout,quickModeLayout);
                }else {

                    if(moreOptionsCard.getVisibility() != View.VISIBLE) {
                        new YoYoAnimatorWrapper(Techniques.SlideInLeft,moreOptionsCard,-1).safeCallToYoYo(true);
                        //YoYo.with(Techniques.SlideInLeft).playOn(moreOptionsCard);
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
                if(textToHandWritingSubmitRequest.getMode()!=0 && textToHandWritingSubmitRequest.getPageType()!=0 && textToHandWritingSubmitRequest.getMode()!=2) {
                    presenter.submitComputerText(textToHandWritingSubmitRequest);
                    textSubmissionFragmentContext = new textSubmissionFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.container, textSubmissionFragmentContext, null).commit();
                }else if(textToHandWritingSubmitRequest.getMode()==2){
                    workInProgressBottomSheet = new WorkInProgressBottomSheet(getString(R.string.wip_deep_mode),true,this);
                    workInProgressBottomSheet.show(getActivity().getSupportFragmentManager(),"DeepModeWIP");
                }else{
                    Utils.showWorkInProgressToast(getContext(),"Please select all options to proceed");
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
        if(textSubmissionFragmentContext!=null) {
            textSubmissionFragmentContext.onComputerTextSubmitted(dummyText, isSuccess);
        }
    }
    private void showSelectModeBuilder(){
        isToolTipShown = true;
        selectModeBuilder = new SimpleTooltipV2.Builder(getContext())
                .anchorView(getView().findViewById(R.id.selectModeCard))
                .text(makeStringBold(getString(R.string.select_mode_builder_text),"Deep","Quick","Remember"))
                .gravity(Gravity.TOP)
                .textColor(ContextCompat.getColor(context,R.color.colorAccent))
                .highlightShape(0)
                .backgroundColor(ContextCompat.getColor(context,R.color.indigoV2))
                .arrowColor(ContextCompat.getColor(context,R.color.indigoV2))
                .animated(true)
                .transparentOverlay(false)
                .build();
        selectModeBuilder.show();
        SharedPreferenceManager.setTooltipDisplayCounter(getActivity());
    }

    private SpannableStringBuilder makeStringBold(String text,String bold_A,String bold_B,String italic){
        final SpannableStringBuilder sb = new SpannableStringBuilder(text);

        final StyleSpan bss_A = new StyleSpan(android.graphics.Typeface.BOLD);
        final StyleSpan bss_B = new StyleSpan(android.graphics.Typeface.BOLD);
        final StyleSpan iss = new StyleSpan(android.graphics.Typeface.ITALIC);
        int italicStart = text.indexOf(italic);
        int boldStartA = text.indexOf(bold_A);
        int boldStartB = text.indexOf(bold_B);
        sb.setSpan(bss_A, boldStartA, boldStartA+bold_A.length()+5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(bss_B, boldStartB, boldStartB+bold_B.length()+5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(iss, italicStart, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

       return sb;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LAUNCH_SECOND_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                xAxis = data.getFloatExtra("xAxis",0f);
                yAxis = data.getFloatExtra("yAxis",0f);
                yDiff = data.getFloatExtra("yDiff",0f);
                textToHandWritingSubmitRequest.setxAxis(xAxis);
                textToHandWritingSubmitRequest.setyAxis(yAxis);
                textToHandWritingSubmitRequest.setyDiff(yDiff);
                Log.d("headingIndexObjectsNew",xAxis+","+yAxis+","+yDiff);
            }
        }
    }

    @Override
    public void selectQuickMode() {
        quickModeLayout.performClick();
    }
}
