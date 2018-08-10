package demo.xy.com.xytdcq.surfaceView.doodle;

/**
 * Created by dong on 2017/6/21.
 */

public class PicPage extends PageChannel {

    private String picUrl;

    public PicPage() {
    }

    public PicPage(String picUrl) {
        super();
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
