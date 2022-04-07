package in.handwritten.android.objects;

import java.util.List;

public class TextToHandWritingSubmitRequest {

    private String text;
    private String email;
    private int pageType;
    private int mode;
    private String handwritingColor;
    private qualityIndex quality;
    private List<HeadingIndexObject> headingIndexObject;
    private float xAxis;
    private float yAxis;
    private float yDiff;

    public enum qualityIndex {
        BEST,
        AVERAGE,
        FAIR
    }

    public TextToHandWritingSubmitRequest(String text,String email){
        this.text = text;
        this.email = email;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public qualityIndex getQuality() {
        return quality;
    }

    public void setQuality(qualityIndex quality) {
        this.quality = quality;
    }

    public String getHandwritingColor() {
        return handwritingColor;
    }

    public void setHandwritingColor(String handwritingColor) {
        this.handwritingColor = handwritingColor;
    }

    public void setHeadingIndexObject(List<HeadingIndexObject> headingIndexObject) {
        this.headingIndexObject = headingIndexObject;
    }

    public List<HeadingIndexObject> getHeadingIndexObject() {
        return headingIndexObject;
    }

    public float getxAxis() {
        return xAxis;
    }

    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public float getyDiff() {
        return yDiff;
    }

    public void setyDiff(float yDiff) {
        this.yDiff = yDiff;
    }
}
