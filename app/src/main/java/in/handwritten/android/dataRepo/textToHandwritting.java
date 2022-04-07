package in.handwritten.android.dataRepo;


import in.handwritten.android.objects.GetAllUserFilesResponse;
import in.handwritten.android.objects.TextFromImageRequest;
import in.handwritten.android.objects.TextFromImageResponse;
import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.objects.textToHandwritingSubmitResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface textToHandwritting {

    @POST("/api/v1/submitText")
    Call<textToHandwritingSubmitResponse> submitComputerText(@Body TextToHandWritingSubmitRequest textToHandWritingSubmitRequest);

    @GET("/userFiles")
    Call<GetAllUserFilesResponse> getGeneratedPdf(@Query("email") String email);

    @POST("/api/v1/uploadImage")
    Call<TextFromImageResponse> getTextFromImage(@Part MultipartBody.Part file);
}
