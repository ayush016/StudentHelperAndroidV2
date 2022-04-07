package in.handwritten.android.homescreen;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.material.card.MaterialCardView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Locale;

import in.handwritten.android.customviews.StarRatingView;
import in.handwritten.android.customviews.YoYoAnimatorWrapper;
import in.handwritten.android.customviews.alertDialogView;
import in.handwritten.android.interfaces.alertDialogInterface;
import in.handwritten.android.objects.HeadingIndexObject;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.splashscreen.R;
import in.handwritten.android.utils.SharedPreferenceManager;
import in.handwritten.android.utils.SimpleTooltipV2;
import in.handwritten.android.utils.Utils;

public class FragmentStep1 extends Fragment implements alertDialogInterface {

    EditText editText;
    TextView totalWords;
    AppCompatButton proceedButton;
    homeScreenPresenter presenter;
    //textSubmissionFragment textSubmissionFragmentContext;
    FragmentStep2 textSubmissionFragmentContext;
    String email;
    MaterialCardView micCard;
    TextView step1;
    TextView step1_hint;
    ImageView headingSelectButton;
    CharSequence convertedText;
    ArrayList<HeadingIndexObject> headingIndexObjects = new ArrayList<HeadingIndexObject>();
    SimpleTooltipV2 headingBuilder;
    DrawerLayout drawer;
    private static int REQUEST_CODE_SPEECH_INPUT = 1;

    public FragmentStep1(homeScreenPresenter presenter, DrawerLayout drawer){
        super(R.layout.fragment_step_1);
        this.presenter = presenter;
        this.drawer = drawer;
    }

    public FragmentStep1(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        editText = (EditText) getActivity().findViewById(R.id.assignmetText);
        totalWords = (TextView) getActivity().findViewById(R.id.totalWords);
        ImageView menuOptionView = getActivity().findViewById(R.id.menu_img);
        proceedButton = (AppCompatButton) getActivity().findViewById(R.id.proceedButton);
        micCard = getActivity().findViewById(R.id.mic_card);
        step1 = getActivity().findViewById(R.id.step1);
        step1_hint = getActivity().findViewById(R.id.step1_hint);
        headingSelectButton = getActivity().findViewById(R.id.heading_selector);
        ImageView hideKeyboard = getActivity().findViewById(R.id.hide_keyboard);
        new YoYoAnimatorWrapper(Techniques.ZoomInDown,getActivity().findViewById(R.id.assignmetText),-1).safeCallToYoYo(true);
        //YoYo.with(Techniques.ZoomInDown).playOn(getActivity().findViewById(R.id.assignmetText));

        if(SharedPreferenceManager.getTooltipDisplayCounter(getActivity())<1) {
            showHeadingToolTip();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText.setBackgroundResource(R.drawable.round_border_edittext);
                if(editText.getText()!=null && getWordsCountFromString(editText.getText().toString())>1){
                    totalWords.setText("Total words : "+getWordsCountFromString(editText.getText().toString()));
                    totalWords.setVisibility(View.VISIBLE);
                }else {
                    totalWords.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editText.getText()!=null && getWordsCountFromString(editText.getText().toString())>1){
                    totalWords.setText("Total words : "+getWordsCountFromString(editText.getText().toString()));
                    totalWords.setVisibility(View.VISIBLE);
                }else {
                    totalWords.setVisibility(View.GONE);
                }
            }
        });
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText()!=null && getWordsCountFromString(editText.getText().toString()) < 2 ) {
                    editText.setBackgroundResource(R.drawable.round_border_edittext_error);
                    new YoYoAnimatorWrapper(Techniques.Shake,getActivity().findViewById(R.id.assignmetText),-1).safeCallToYoYo(true);
                    //YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.assignmetText));
                }else if(editText.getText()!=null && getWordsCountFromString(editText.getText().toString()) < 51 ){
                    alertDialogView alertDialog = new alertDialogView();
                    alertDialog.showDialog(getActivity(),"Hemant Bot is Greedy"," He will do your job only if words are more than 50",FragmentStep1.this);
                }else if(editText.getText()!=null) {
                    TextToHandWritingSubmitRequest textToHandWritingSubmitRequest = new TextToHandWritingSubmitRequest(getSpannableText().toString(),email);
                    String htmlString= Html.toHtml(editText.getText());
                    textSubmissionFragmentContext = new FragmentStep2(getActivity(),textToHandWritingSubmitRequest,presenter);//new textSubmissionFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    ).replace(R.id.container, textSubmissionFragmentContext, null).addToBackStack("SelectMode").commit();
                }
            }
        });

        micCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                micCard.setCardElevation(0);
                Intent intent
                        = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e) {
                }
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    step1.setVisibility(View.VISIBLE);
                }
            }
        });

        headingSelectButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                int start = editText.getSelectionStart();
                int end = editText.getSelectionEnd();

                if(start != end) {
                    //HeadingIndexObject headingIndexObject = new HeadingIndexObject(start, end);
                    //headingIndexObjects.add(headingIndexObject);
                    //editText.setText(insertString(editText.getText(),"<h>",start));
                    //editText.setText(insertString(editText.getText().toString(),"</h>",end+3));

                    convertedText = editText.getText();
                    final StyleSpan bss_A = new StyleSpan(android.graphics.Typeface.BOLD);
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(0, 0, 0));
                    SpannableStringBuilder sb = new SpannableStringBuilder(convertedText);
                    //sb.insert(start,"<h>");
                    //sb.insert(end+3,"</h>");
                    sb.setSpan(bss_A, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    sb.setSpan(fcs, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    editText.setText(sb);
                    editText.setSelection(end);
                }else {
                    Utils.showWorkInProgressToast(getActivity(),"Select words after pasting above to make it a heading");
                }

            }
        });

        hideKeyboard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                step1.setVisibility(View.VISIBLE);
                //step1_hint.setVisibility(View.GONE);
                //pushHeadingSelectorButton(headingSelectButton, false);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) editText.getLayoutParams();
                ConstraintLayout.LayoutParams paramsHeading = (ConstraintLayout.LayoutParams) headingSelectButton.getLayoutParams();
                paramsHeading.bottomToBottom = -1;
                params.bottomToTop = -1;
                params.bottomMargin = getResources().getDimensionPixelSize(R.dimen._200sdp);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable(){
                    @Override
                    public void run(){
                        proceedButton.setVisibility(View.VISIBLE);
                        micCard.setVisibility(View.VISIBLE);
                    }
                }, 200);
                editText.setSelection(editText.getText().length());
                scaleView(editText, 1.1f, 1f);
            }
        });

        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                getViewLifecycleOwner(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                            step1.setVisibility(View.GONE);
                            step1_hint.setVisibility(View.VISIBLE);
                            new YoYoAnimatorWrapper(Techniques.FlipInX,step1_hint,-1).safeCallToYoYo(true);
                            //YoYo.with(Techniques.FlipInX).playOn(step1_hint);
                            //pushHeadingSelectorButton(headingSelectButton, true);
                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) editText.getLayoutParams();
                            ConstraintLayout.LayoutParams paramsHeading = (ConstraintLayout.LayoutParams) headingSelectButton.getLayoutParams();
                            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen._2sdp);
                            params.bottomToTop = headingSelectButton.getId();
                            paramsHeading.bottomToBottom = 0;
                            paramsHeading.bottomMargin = getResources().getDimensionPixelSize(R.dimen._8sdp);
                            proceedButton.setVisibility(View.GONE);
                            micCard.setVisibility(View.GONE);
                            //editText.setSelection(editText.getText().length());
                            hideKeyboard.setVisibility(View.INVISIBLE);
                            //menuOptionView.setVisibility(View.GONE);
                            new YoYoAnimatorWrapper(Techniques.SlideOutRight,menuOptionView,-1).safeCallToYoYo(false);
                            //YoYo.with(Techniques.SlideOutRight).playOn(menuOptionView);
                        } else {
                            step1.setVisibility(View.VISIBLE);
                            hideKeyboard.setVisibility(View.GONE);
                            new YoYoAnimatorWrapper(Techniques.FlipOutX,step1_hint,-1).safeCallToYoYo(true);
                            //YoYo.with(Techniques.FlipOutX).playOn(step1_hint);
                            //pushHeadingSelectorButton(headingSelectButton, false);
                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) editText.getLayoutParams();
                            ConstraintLayout.LayoutParams paramsHeading = (ConstraintLayout.LayoutParams) headingSelectButton.getLayoutParams();
                            paramsHeading.bottomToBottom = -1;
                            params.bottomToTop = -1;
                            params.bottomMargin = getResources().getDimensionPixelSize(R.dimen._200sdp);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable(){
                                @Override
                                public void run(){
                                    proceedButton.setVisibility(View.VISIBLE);
                                    micCard.setVisibility(View.VISIBLE);
                                }
                            }, 200);

                            //editText.setSelection(editText.getText().length());
                            scaleView(editText, 1.1f, 1f);
                            menuOptionView.setVisibility(View.VISIBLE);
                            new YoYoAnimatorWrapper(Techniques.SlideInRight,menuOptionView,-1).safeCallToYoYo(true);
                            //YoYo.with(Techniques.SlideInRight).playOn(menuOptionView);
                        }
                    }
                });

        if(drawer!=null) {

            drawer.addDrawerListener(new DrawerLayout.DrawerListener()
            {
                // other overridden methods not shown

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset){
                }
                @Override
                public void onDrawerOpened(View drawerView)
                {
                    menuOptionView.setVisibility(View.GONE);
                }
                @Override
                public void onDrawerClosed( View drawerView) {
                    menuOptionView.setVisibility(View.VISIBLE);
                    new YoYoAnimatorWrapper(Techniques.SlideInRight,menuOptionView,-1).safeCallToYoYo(true);
                    //YoYo.with(Techniques.SlideInRight).playOn(menuOptionView);
                }
                @Override
                public void onDrawerStateChanged(int newState) {
                }
            });

            menuOptionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawer != null) drawer.openDrawer(GravityCompat.END);
                }
            });
        }

        if(shouldShowRateUsPopUp()){
            showRateUsPopup();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SPEECH_INPUT){
            micCard.setCardElevation(48);
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(result!=null && result.size()>0) {
                    SpannableStringBuilder sb = new SpannableStringBuilder(editText.getText());
                    sb.insert(editText.getSelectionStart(),result.get(0));
                    editText.setText(sb);
                }
            }
        }
    }

    private int getWordsCountFromString(String paragraph){
        return paragraph.split(" ").length;
    }

    @Override
    public void alertDialogClosed() {
        new YoYoAnimatorWrapper(Techniques.Tada,getActivity().findViewById(R.id.totalWords),-1).safeCallToYoYo(true);
        //YoYo.with(Techniques.Tada).playOn(getActivity().findViewById(R.id.totalWords));
        editText.setBackgroundResource(R.drawable.round_border_edittext_error);
    }

    public void onComputerTextSubmitted(String dummyText,boolean isSuccess){
        if(textSubmissionFragmentContext!=null) {
            textSubmissionFragmentContext.onComputerTextSubmitted(dummyText, isSuccess);
        }
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void pushHeadingSelectorButton(ImageView view,boolean isOpen){
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        ConstraintLayout.LayoutParams paramsStep = (ConstraintLayout.LayoutParams) step1_hint.getLayoutParams();

        if(isOpen) {
            params.bottomToTop = editText.getId();
            params.topToBottom = ConstraintLayout.LayoutParams.UNSET;
            params.height = 64;
            params.width = 64;
            params.bottomMargin = 10;
            paramsStep.topToTop = view.getId();
            paramsStep.bottomToBottom = view.getId();
        }else {
            params.bottomToTop = ConstraintLayout.LayoutParams.UNSET;
            params.topToBottom = editText.getId();
            params.height = 84;
            params.width = 84;
        }
    }

    // Function to insert string
    public static StringBuilder insertString(
            CharSequence originalString,
            String stringToBeInserted,
            int index)
    {

        StringBuilder originalStringTemp = new StringBuilder(originalString);
        // Create a new string
        StringBuilder newString = originalStringTemp.insert(index, stringToBeInserted);

        // return the modified String
        return newString;
    }

    public Editable getSpannableText(){
        StyleSpan[] ss = editText.getText().getSpans(0,editText.getText().length(),StyleSpan.class);
        Editable eText = editText.getText();
        for(StyleSpan span : ss){
            if(span.getStyle() == android.graphics.Typeface.BOLD) {
                int start = eText.getSpanStart(span);
                int end = eText.getSpanEnd(span);
                if(start>=0 && end >= start) {
                    SpannableStringBuilder sb = new SpannableStringBuilder(eText);
                    sb.insert(start,"<h>");
                    sb.insert(end+3,"</h>");
                    eText = sb;
                }
            }
        }
        return eText;
        //editText.setText(eText);
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    public void showHeadingToolTip(){
        headingBuilder = new SimpleTooltipV2.Builder(getContext())
                .anchorView(headingSelectButton)
                .text("Now you can make the selected text Black & bold")
                .gravity(Gravity.BOTTOM)
                .textColor(ContextCompat.getColor(getActivity(),R.color.colorAccent))
                .highlightShape(0)
                .backgroundColor(ContextCompat.getColor(getActivity(),R.color.indigoV2))
                .arrowColor(ContextCompat.getColor(getActivity(),R.color.indigoV2))
                .animated(true)
                .transparentOverlay(false)
                .build();
        headingBuilder.show();
        SharedPreferenceManager.setTooltipDisplayCounter(getActivity());
    }

    public void changeEditText(String convertedText){
        editText.setText(convertedText);
    }

    private boolean shouldShowRateUsPopUp(){
        if(SharedPreferenceManager.isOneTimeDocumentGenerated(requireContext())){

            if(SharedPreferenceManager.isUserWentToPlayStore(requireContext())){
                return false;
            }else if(!SharedPreferenceManager.isUserWentToPlayStore(requireContext())
                    && SharedPreferenceManager.getPlayStorePopUpTime(requireContext()) == -1L){
                return true;
            }else if(!SharedPreferenceManager.isUserWentToPlayStore(requireContext())
                    && SharedPreferenceManager.getPlayStorePopUpTime(requireContext()) != -1L
                    && (System.currentTimeMillis() - SharedPreferenceManager.getPlayStorePopUpTime(requireContext())) > 604800000){
                return true;
            }

        }
        return false;
    }

    private void showRateUsPopup(){
        SharedPreferenceManager.setPlayStorePopUpTime(requireContext());
        StarRatingView starRatingView = new StarRatingView();
        starRatingView.showDialog(requireActivity());
    }
}
