package in.handwritten.android.homescreen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import in.handwritten.android.customviews.alertDialogView;
import in.handwritten.android.homescreen.homeScreenPresenter;
import in.handwritten.android.interfaces.alertDialogInterface;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.splashscreen.R;

public class FragmentStep1 extends Fragment implements alertDialogInterface {

    EditText editText;
    TextView totalWords;
    AppCompatButton proceedButton;
    homeScreenPresenter presenter;
    //textSubmissionFragment textSubmissionFragmentContext;
    FragmentStep2 textSubmissionFragmentContext;
    String email;

    public FragmentStep1(homeScreenPresenter presenter){
        super(R.layout.fragment_step_1);
        this.presenter = presenter;
    }

    @Override
    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editText = (EditText) getActivity().findViewById(R.id.assignmetText);
        totalWords = (TextView) getActivity().findViewById(R.id.totalWords);
        proceedButton = (AppCompatButton) getActivity().findViewById(R.id.proceedButton);

        YoYo.with(Techniques.ZoomInDown).playOn(getActivity().findViewById(R.id.assignmetText));



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
                    YoYo.with(Techniques.Shake).playOn(getActivity().findViewById(R.id.assignmetText));
                }else if(editText.getText()!=null && getWordsCountFromString(editText.getText().toString()) < 101 ){
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.137.246:5000/downloadPdf"));
                    //startActivity(browserIntent);
                    alertDialogView alertDialog = new alertDialogView();
                    alertDialog.showDialog(getActivity(),"Hemant Bot is Greedy"," He will do your job only if words are more than 100",FragmentStep1.this);
                }else if(editText.getText()!=null) {
                    YoYo.with(Techniques.ZoomOut).playOn(getActivity().findViewById(R.id.fragment1));
                    //totalWords.setVisibility(View.VISIBLE);
                    //totalWords.setText(email);
                    //presenter.submitComputerText(editText.getText().toString(),email);
                    TextToHandWritingSubmitRequest textToHandWritingSubmitRequest = new TextToHandWritingSubmitRequest(editText.getText().toString(),email);
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
    }

    private int getWordsCountFromString(String paragraph){
        return paragraph.split(" ").length;
    }

    @Override
    public void alertDialogClosed() {
        YoYo.with(Techniques.Tada).playOn(getActivity().findViewById(R.id.totalWords));
        editText.setBackgroundResource(R.drawable.round_border_edittext_error);
    }

    public void onComputerTextSubmitted(String dummyText,boolean isSuccess){
        textSubmissionFragmentContext.onComputerTextSubmitted(dummyText,isSuccess);
    }

    public void setEmail(String email){
        this.email = email;
    }

}
