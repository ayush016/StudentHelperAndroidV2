package in.handwritten.android.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllUserFilesResponse {

    @SerializedName("filePath")
    private String filePath;

    @SerializedName("fileList")
    private List<FileList> fileList;

    @SerializedName("fileInitials")
    private String fileInitials;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileList(List<FileList> fileList) {
        this.fileList = fileList;
    }

    public List<FileList> getFileList() {
        return fileList;
    }

    public String getFileInitials() {
        return fileInitials;
    }

    public void setFileInitials(String fileInitials) {
        this.fileInitials = fileInitials;
    }

    public class FileList{
        @SerializedName("count")
        private int fileCount;

        @SerializedName("fileName")
        private String fileName;

        @SerializedName("fileNameDisplay")
        private String fileNameDisplay;

        @SerializedName("fileNamePdf")
        private String fileNamePdf;

        public int getFileCount() {
            return fileCount;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileNameDisplay() {
            return fileNameDisplay;
        }

        public String getFileNamePdf() {
            return fileNamePdf;
        }
    }
}

