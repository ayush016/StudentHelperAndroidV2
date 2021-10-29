package in.handwritten.android.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import in.handwritten.android.homescreen.FragmentStep1;
import in.handwritten.android.homescreen.FragmentStep2;
import in.handwritten.android.homescreen.homeScreenPresenter;
import in.handwritten.android.interfaces.TextSubmitScreenView;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;


public class MainActivity extends AppCompatActivity implements TextSubmitScreenView,GoogleApiClient.OnConnectionFailedListener {

    private static int RC_SIGN_IN = 100;
    homeScreenPresenter presenter;
    FragmentStep1 fragmentStep1;
    FragmentStep2 fragmentStep2;
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new homeScreenPresenter(this);
        TextToHandWritingSubmitRequest textToHandWritingSubmitRequest = new TextToHandWritingSubmitRequest("","");

        fragmentStep1 = new FragmentStep1(presenter);
        initGoogleLogIn();

        //getSupportFragmentManager().beginTransaction()
         //       .replace(R.id.container, fragmentStep2, null).commit();

        /*
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragmentStep1, null)
                .commit();
         */
    }


    private void initGoogleLogIn(){
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
    }


    @Override
    public void onComputerTextSubmitted(String dummyText,boolean isSuccess) {
        fragmentStep1.onComputerTextSubmitted(dummyText,isSuccess);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            GoogleSignInAccount account=result.getSignInAccount();
            if(account!=null && account.getEmail()!=null) {
                fragmentStep1.setEmail(account.getEmail());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragmentStep1, null)
                        .commit();
            }
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed()
    {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }
}
