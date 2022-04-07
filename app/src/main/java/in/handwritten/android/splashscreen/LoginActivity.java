package in.handwritten.android.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import in.handwritten.android.customviews.YoYoAnimatorWrapper;
import in.handwritten.android.utils.SharedPreferenceManager;
import in.handwritten.android.utils.Utils;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private static int RC_GOOGLE_SIGN_IN = 1000;
    private static int RC_SIGN_IN_Complete = 100;
    private static int RC_SIGN_IN_BACK_PRESSED = 1012;
    TextView signInButton;
    TextView manualSignInButton;
    TextView nameManual;
    TextView emailManual;
    TextView quoteTitle;
    TextView quoteSubTitle;
    boolean isManualLogin;
    AppCompatButton proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initGoogleLogIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_GOOGLE_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            if(opr.isDone()) {
                GoogleSignInResult signInResult = opr.get();
                GoogleSignInAccount account = signInResult.getSignInAccount();
                if (account != null && account.getEmail() != null) {
                    SharedPreferenceManager.setUserEmail(getApplicationContext(), account.getEmail());
                    SharedPreferenceManager.setUserName(getApplicationContext(), account.getDisplayName());
                    if(account.getPhotoUrl()!=null) {
                        SharedPreferenceManager.setGoogleAccountDataImage(getApplicationContext(), account.getPhotoUrl().toString());
                    }
                    Intent intent = new Intent();
                    intent.putExtra("gmailAccount",account);
                    intent.putExtra("isManualLogin",isManualLogin);
                    setResult(RC_SIGN_IN_Complete,intent);
                    finish();
                }
            }
        }else{
            Toast.makeText(getApplicationContext(),"Sign in failed, please try to enter details manually... google issue :(",Toast.LENGTH_LONG).show();
        }
    }

    private void initGoogleLogIn(){
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        signInButton = (TextView)findViewById(R.id.sign_in_button);
        manualSignInButton = (TextView)findViewById(R.id.sign_in_button_manual);

        nameManual = findViewById(R.id.nameManual);
        emailManual = findViewById(R.id.emailManual);
        quoteTitle = findViewById(R.id.quote_title);
        quoteSubTitle = findViewById(R.id.quote_sub_title);
        proceedButton = findViewById(R.id.loginButton);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utils.isEmailValid(emailManual.getText().toString())){
                    Utils.showWorkInProgressToast(getApplicationContext(),getString(R.string.manual_email_error));
                }else if(nameManual.getText().toString().isEmpty()){
                    Utils.showWorkInProgressToast(getApplicationContext(),getString(R.string.manual_name_error));
                }else {
                    SharedPreferenceManager.setUserEmail(getApplicationContext(), emailManual.getText().toString());
                    SharedPreferenceManager.setUserName(getApplicationContext(), nameManual.getText().toString());
                    Intent intent = new Intent();
                    intent.putExtra("isManualLogin",isManualLogin);
                    setResult(RC_SIGN_IN_Complete,intent);
                    finish();
                }
            }
        });

        emailManual.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isOpen) {
                if(!isOpen) {
                    hideKeyboard(view);
                    if (Utils.isEmailValid(emailManual.getText().toString())) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            emailManual.setBackground(getDrawable(R.drawable.login_background_success));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            emailManual.setBackground(getDrawable(R.drawable.login_background_error));
                        }
                    }
                }
            }
        });

        nameManual.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isOpen) {
                if(!isOpen) {
                    hideKeyboard(view);
                    if (!nameManual.getText().toString().isEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            nameManual.setBackground(getDrawable(R.drawable.login_background_success));
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            nameManual.setBackground(getDrawable(R.drawable.login_background_error));
                        }
                    }
                }
            }
        });


        manualSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateManualLogin(true);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isManualLogin = false;
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_GOOGLE_SIGN_IN);
            }
        });
    }

    private void initiateManualLogin(boolean isManualLogin){
        this.isManualLogin = isManualLogin;
        if(isManualLogin) {
            new YoYoAnimatorWrapper(Techniques.SlideOutLeft,manualSignInButton,-1).safeCallToYoYo(false);
            new YoYoAnimatorWrapper(Techniques.SlideOutRight,signInButton,-1).safeCallToYoYo(false);
            new YoYoAnimatorWrapper(Techniques.FadeIn,nameManual,1000).safeCallToYoYo(true);
            new YoYoAnimatorWrapper(Techniques.FadeIn,emailManual,1000).safeCallToYoYo(true);
            //YoYo.with(Techniques.SlideOutLeft).playOn(manualSignInButton);
            //YoYo.with(Techniques.SlideOutRight).playOn(signInButton);
            //YoYo.with(Techniques.FadeIn).duration(1000).playOn(nameManual);
            //YoYo.with(Techniques.FadeIn).duration(1000).playOn(emailManual);
            emailManual.setVisibility(View.VISIBLE);
            nameManual.setVisibility(View.VISIBLE);
            //manualSignInButton.setVisibility(View.GONE);
            //signInButton.setVisibility(View.GONE);
            quoteTitle.setVisibility(View.GONE);
            quoteSubTitle.setVisibility(View.GONE);
            proceedButton.setVisibility(View.VISIBLE);
            //YoYo.with(Techniques.SlideInUp).playOn(proceedButton);
            new YoYoAnimatorWrapper(Techniques.SlideInUp,proceedButton,-1).safeCallToYoYo(true);
        }else {
            new YoYoAnimatorWrapper(Techniques.SlideInLeft,manualSignInButton,-1).safeCallToYoYo(true);
            new YoYoAnimatorWrapper(Techniques.SlideInRight,signInButton,-1).safeCallToYoYo(true);
            //YoYo.with(Techniques.SlideInLeft).playOn(manualSignInButton);
            //YoYo.with(Techniques.SlideInRight).playOn(signInButton);
            emailManual.setVisibility(View.GONE);
            nameManual.setVisibility(View.GONE);
            manualSignInButton.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.VISIBLE);
            quoteTitle.setVisibility(View.VISIBLE);
            quoteSubTitle.setVisibility(View.VISIBLE);
            //proceedButton.setVisibility(View.GONE);
            //YoYo.with(Techniques.SlideOutDown).playOn(proceedButton);
            new YoYoAnimatorWrapper(Techniques.SlideOutDown,proceedButton,-1).safeCallToYoYo(false);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        if (isManualLogin) {
            isManualLogin = false;
            initiateManualLogin(false);
            return;
        }
        super.onBackPressed();
        setResult(RC_SIGN_IN_BACK_PRESSED);
    }
}


