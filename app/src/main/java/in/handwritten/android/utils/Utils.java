package in.handwritten.android.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.regex.Pattern;

import in.handwritten.android.splashscreen.BuildConfig;

public class Utils {

    public static void showWorkInProgressToast(Context context,String text){
        Toast.makeText(context,text ,Toast.LENGTH_LONG).show();
    }

    public static void openDefaultBrowser(Context context,String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void shareApp(Context context){
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "HandWritten");
            String shareMessage= "\nThis App has made my life so simple. Can you imagine? Text to handwriting \n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    public static void openPlayStore(Context context){
        try{
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+ BuildConfig.APPLICATION_ID)));
        }
        catch (ActivityNotFoundException e){
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID)));
        }
    }

    public static boolean isEmailValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
