package in.handwritten.android.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import in.handwritten.android.homescreen.FragmentStep1;
import in.handwritten.android.homescreen.FragmentStep2;
import in.handwritten.android.homescreen.ViewDownloadsFragment;
import in.handwritten.android.homescreen.homeScreenPresenter;
import in.handwritten.android.homescreen.textSubmissionFragment;
import in.handwritten.android.interfaces.TextSubmitScreenView;
import in.handwritten.android.objects.GetAllUserFilesResponse;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.utils.SharedPreferenceManager;
import in.handwritten.android.utils.Utils;

import java.io.File;
import java.time.Duration;
import java.time.Instant;

public class MainActivity extends AppCompatActivity implements TextSubmitScreenView, NavigationView.OnNavigationItemSelectedListener {

    private static int RC_SIGN_IN = 100;
    homeScreenPresenter presenter;
    FragmentStep1 fragmentStep1;
    FragmentStep2 fragmentStep2;
    TextView signInButton;
    private GoogleSignInAccount gmailAccount;
    private String userEmail;
    private String userName;
    private DrawerLayout drawer;
    NavigationView navigationView;
    ViewDownloadsFragment viewDownloadsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new homeScreenPresenter(this);
        TextToHandWritingSubmitRequest textToHandWritingSubmitRequest = new TextToHandWritingSubmitRequest("", "");
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        fragmentStep1 = new FragmentStep1(presenter, drawer);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        userEmail = SharedPreferenceManager.getUserEmail(getApplicationContext());
        userName = SharedPreferenceManager.getUserName(getApplicationContext());

        if (userEmail == null || userName == null) {
            initiateSignInProcess();
        } else {
            //navigationView.bringToFront();
            Toast.makeText(getApplicationContext(), "Signed in as " + userEmail, Toast.LENGTH_SHORT).show();
            gotoProfile();
        }
    }


    @Override
    public void onComputerTextSubmitted(String dummyText, boolean isSuccess) {
        fragmentStep1.onComputerTextSubmitted(dummyText, isSuccess);

    }

    @Override
    public void onGetAllUsersResponse(GetAllUserFilesResponse getAllUserFilesResponse, boolean isSuccess) {
        if(viewDownloadsFragment!=null && isSuccess){
            viewDownloadsFragment.inflateFileDownloadAdapter(getAllUserFilesResponse);
        }else{
            getSupportFragmentManager().popBackStack();
            Utils.showWorkInProgressToast(this,getString(R.string.something_went_wrong));
        }
    }

    @Override
    public void onTextFromImageResponse(String convertedText, boolean isSuccess) {
        fragmentStep1.changeEditText(convertedText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && data != null) {
            if (resultCode == RC_SIGN_IN) {
                if(!(data.getExtras().getBoolean("isManualLogin"))){
                    gmailAccount = data.getParcelableExtra("gmailAccount");
                }
                gotoProfile();
            }
        } else if (requestCode == RC_SIGN_IN && resultCode == 0) {
            finish();
        }else if(requestCode == 90){

            File file = (File) data.getExtras().get("file");
            presenter.getTextFromImage(file);
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                gotoProfile();
                break;
            case R.id.nav_downloads:
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
                if(frag instanceof textSubmissionFragment){
                    getSupportFragmentManager().popBackStack();
                }
                if(viewDownloadsFragment == null || !viewDownloadsFragment.isVisible()) {
                    viewDownloadsFragment = new ViewDownloadsFragment(presenter);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, viewDownloadsFragment, null).addToBackStack("ViewDownloads")
                            .commitAllowingStateLoss();
                    getSupportFragmentManager().executePendingTransactions();
                }
                break;
            case R.id.nav_instruction:
                //Intent intent = new Intent(MainActivity.this, ImageUploadActivity.class);
                //startActivityForResult(intent, 90);

                break;
            case R.id.nav_share:
                    Utils.shareApp(this);
                break;

            case R.id.nav_contact:
                Utils.openDefaultBrowser(this,"https://forms.gle/rDfdF7oKdsAjWswj7");
                break;
            case R.id.nav_rate:
                Utils.openPlayStore(this);
                break;

            case R.id.nav_logout:
                SharedPreferenceManager.clearSharePreferenceData(this);
                signOut();
                break;

            case R.id.nav_privacy_policy:
                Utils.openDefaultBrowser(this,"https://storage.googleapis.com/artifacts.handwritten-328414.appspot.com/Privacy%20Policy/privacy_policy.html");
                break;

        }

        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onBackPressed() {
        //fragmentStep1.removeTooltipCallbacks();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
        else super.onBackPressed();
    }

    private void initiateSignInProcess() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void gotoProfile() {
        inflateMenuItems();
        if (gmailAccount != null && gmailAccount.getEmail() != null) {
            fragmentStep1.setEmail(gmailAccount.getEmail());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentStep1, null)
                    .commit();
        } else if (userEmail != null && userName != null) {
            fragmentStep1.setEmail(userEmail);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentStep1, null)
                    .commit();
        }
    }

    private boolean isServerWakeUpRequired(){
        if(SharedPreferenceManager.getServerWakeUpUtc(this) == -1){
            long currentTime = System.currentTimeMillis();
            SharedPreferenceManager.setServerWakeUpUtc(this,currentTime);
            return true;
        }
            long currentTime = System.currentTimeMillis();
            long wakeUpTime = SharedPreferenceManager.getServerWakeUpUtc(this);
            if(currentTime - wakeUpTime > 100000){
                SharedPreferenceManager.setServerWakeUpUtc(this,currentTime);
                return true;
            }
            return false;
    }

    private void inflateMenuItems() {
        TextView userNameView = navigationView.getHeaderView(0).findViewById(R.id.userName);
        TextView userEmailView = navigationView.getHeaderView(0).findViewById(R.id.userEmail);
        ImageView userPhotoView = navigationView.getHeaderView(0).findViewById(R.id.userPhoto);
        if (userEmail == null || userName == null) {
            userEmail = SharedPreferenceManager.getUserEmail(getApplicationContext());
            userName = SharedPreferenceManager.getUserName(getApplicationContext());
        }
        userEmailView.setText(userEmail);
        userNameView.setText("Hi " + userName);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        if (SharedPreferenceManager.getGoogleAccountDataImage(getApplicationContext()) != null) {
            Picasso.with(this).load(SharedPreferenceManager.
                    getGoogleAccountDataImage(getApplicationContext())).into(userPhotoView);
        }

    }

    private void signOut() {
        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        if(task.isSuccessful()){
                            restartApp();
                        }else {
                            Utils.showWorkInProgressToast(MainActivity.this,getString(R.string.something_went_wrong));
                        }
                    }
                });
    }

    private void restartApp() {
        Utils.showWorkInProgressToast(this,"Restarting App....");
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        /*Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        int mPendingIntentId = 1234;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);*/
    }
}
