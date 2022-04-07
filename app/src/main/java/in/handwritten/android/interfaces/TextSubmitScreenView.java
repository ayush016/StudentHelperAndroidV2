package in.handwritten.android.interfaces;

import in.handwritten.android.objects.GetAllUserFilesResponse;

public interface TextSubmitScreenView {

    void onComputerTextSubmitted(String dummyText,boolean isSuccess);

    void onGetAllUsersResponse(GetAllUserFilesResponse getAllUserFilesResponse,boolean isSuccess);

    void onTextFromImageResponse(String convertedText,boolean isSuccess);
}
