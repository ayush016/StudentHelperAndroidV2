package in.handwritten.android.dataRepo;


import in.handwritten.android.objects.TextToHandWritingSubmitRequest;
import in.handwritten.android.objects.textToHandwritingSubmitResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface textToHandwritting {

    @POST("/api/v1/submitText")
    Call<textToHandwritingSubmitResponse> submitComputerText(@Body TextToHandWritingSubmitRequest textToHandWritingSubmitRequest);

    @GET("/downloadPdf")
    Call<Object> getGeneratedPdf();
}
