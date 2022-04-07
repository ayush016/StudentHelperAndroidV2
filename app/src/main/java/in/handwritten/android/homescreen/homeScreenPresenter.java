package in.handwritten.android.homescreen;

import android.os.Handler;
import android.util.Log;


import java.io.File;

import in.handwritten.android.dataRepo.textToHandwritting;
import in.handwritten.android.interfaces.TextSubmitScreenView;
import in.handwritten.android.objects.GetAllUserFilesResponse;
import in.handwritten.android.objects.TextFromImageResponse;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.objects.textToHandwritingSubmitResponse;
import in.handwritten.android.splashscreen.RetrofitClientInstance;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void getAllUserFiles(String email){
        textToHandwritting service = RetrofitClientInstance.getRetrofitInstance().create(textToHandwritting.class);
        Call<GetAllUserFilesResponse> call = service.getGeneratedPdf(email);
        call.enqueue(new Callback<GetAllUserFilesResponse>() {
            @Override
            public void onResponse(Call<GetAllUserFilesResponse> call, Response<GetAllUserFilesResponse> response) {
                if(response.body()!=null){
                    textSubmitScreenView.onGetAllUsersResponse(response.body(),true);
                }else{
                    textSubmitScreenView.onGetAllUsersResponse(null,false);
                }
            }

            @Override
            public void onFailure(Call<GetAllUserFilesResponse> call, Throwable t) {
                textSubmitScreenView.onGetAllUsersResponse(null,false);
            }
        });
    }

    public void getTextFromImage(File file){
        textToHandwritting service = RetrofitClientInstance.getRetrofitInstance().create(textToHandwritting.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        Call<TextFromImageResponse> call = service.getTextFromImage(body);
        call.enqueue(new Callback<TextFromImageResponse>() {
            @Override
            public void onResponse(Call<TextFromImageResponse> call, Response<TextFromImageResponse> response) {
               textSubmitScreenView.onTextFromImageResponse(response.body().getKey(),true);
            }

            @Override
            public void onFailure(Call<TextFromImageResponse> call, Throwable t) {
                //textSubmitScreenView.onGetAllUsersResponse(null,false);
            }
        });
    }
}
