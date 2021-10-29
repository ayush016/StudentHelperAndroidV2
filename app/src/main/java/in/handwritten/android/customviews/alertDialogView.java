package in.handwritten.android.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import in.handwritten.android.splashscreen.R;
import in.handwritten.android.interfaces.alertDialogInterface;

public class alertDialogView {

    public void showDialog(final Activity activity, String msg, String msg1, final alertDialogInterface callback){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textSub = (TextView) dialog.findViewById(R.id.text_dialog_2);
        text.setText(msg);
        textSub.setText(msg1);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.alertDialogClosed();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}