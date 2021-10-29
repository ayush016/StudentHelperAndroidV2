package in.handwritten.android.objects;

import com.google.gson.annotations.SerializedName;

public class textToHandwritingSubmitResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("key")
    private String key;

    public textToHandwritingSubmitResponse(boolean success,String key){
        this.success = success;
        this.key = key;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
