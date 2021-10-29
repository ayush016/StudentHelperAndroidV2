package in.handwritten.android.homescreen;

import android.os.Handler;
import android.util.Log;


import in.handwritten.android.dataRepo.textToHandwritting;
import in.handwritten.android.interfaces.TextSubmitScreenView;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.objects.textToHandwritingSubmitResponse;
import in.handwritten.android.splashscreen.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class homeScreenPresenter {

    private TextSubmitScreenView textSubmitScreenView;


    public homeScreenPresenter(TextSubmitScreenView textSubmitScreenView){
        this.textSubmitScreenView = textSubmitScreenView;
    }


    public void submitComputerText(TextToHandWritingSubmitRequest textToHandWritingSubmitRequest){
        textToHandwritting service = RetrofitClientInstance.getRetrofitInstance().create(textToHandwritting.class);
        Call<textToHandwritingSubmitResponse> call = service.submitComputerText(textToHandWritingSubmitRequest);
        call.enqueue(new Callback<textToHandwritingSubmitResponse>() {
            @Override
            public void onResponse(Call<textToHandwritingSubmitResponse> call, Response<textToHandwritingSubmitResponse> response) {
                if(response.body() !=null) {
                    textSubmitScreenView.onComputerTextSubmitted(response.body().getKey(),true);
                    Log.d("submitTextApiSuccess",response.toString());
                }else {
                    textSubmitScreenView.onComputerTextSubmitted("",false);
                    Log.d("submitTextApiSuccess","null response");
                }
            }

            @Override
            public void onFailure(Call<textToHandwritingSubmitResponse> call, Throwable t) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        textSubmitScreenView.onComputerTextSubmitted("",false);
                    }
                }, 7000);

                Log.d("submitTextApiFail",t.toString());
            }
        });
    }

    public void getGeneratedPdf(){
        textToHandwritting service = RetrofitClientInstance.getRetrofitInstance().create(textToHandwritting.class);
        Call<Object> call = service.getGeneratedPdf();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                textSubmitScreenView.onComputerTextSubmitted("",false);
                Log.d("pdffile",response.toString());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("pdffile",t.toString());
                textSubmitScreenView.onComputerTextSubmitted("",false);
            }
        });
    }
}
