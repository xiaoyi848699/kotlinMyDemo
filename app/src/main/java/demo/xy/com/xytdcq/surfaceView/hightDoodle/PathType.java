package demo.xy.com.xytdcq.surfaceView.hightDoodle;

/**
 * 画笔类型
 */
public enum PathType {
    LINE {
        String getStr(){
            return "line";
        }
    },
    IMAGE{
        String getStr(){
            return "image";
        }
    },
    TEXT{
        String getStr(){
            return "text";
        }
    },
    AUDIO{
        String getStr(){
            return "audio";
        }
    }

}
