package in.handwritten.android.objects;

import com.google.gson.annotations.SerializedName;

public class TextFromImageResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("convertedText")
    private String convertedText;

    public TextFromImageResponse(boolean success,String convertedText){
        this.success = success;
        this.convertedText = convertedText;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getKey() {
        return convertedText;
    }

    public void setKey(String convertedText) {
        this.convertedText = convertedText;
    }
}
