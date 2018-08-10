package demo.xy.com.xytdcq.surfaceView.doodle;


import java.io.Serializable;

import demo.xy.com.xytdcq.uitls.LogUtil;

/**
 * 发送数据类
 */
public class TransactionData implements Serializable {
    private  static final String TAG = TransactionData.class.getSimpleName();

    public interface ActionType {
        byte POINT = 1;
        byte UNDOLINE = 2;
        byte CLEARLINE = 3;
        byte SYNCHRONOUS = 4;
        byte CHANGEPAGE = 5;
        byte CHANGEPPT = 6;
        byte INSERTPAGE = 7;
        byte AUTHORITY = 8;
        byte CALLBACK = 9;
    }
    public interface ActionPointType {
        byte DOWN = 1;
        byte MOVE = 2;
        byte UP = 3;
    }

    private byte type = ActionType.POINT;
    private int page;
    private int colorHex;
    private float penSize;
    private int penType;//整形画笔类型，枚举值创建曲线(1)、直线(2)、三角形(3)、矩形(4)、圆形(5)、橡皮擦(-1)
    private byte pointType;//整形点类型，枚举值创建开始(1)、移动(2)、结束(3)
    private float x = 0.0f;
    private float y = 0.0f;
    private String uid; //account帐号
    private String[] images; //图片
    private int data;
    private int disabled;


    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getColorHex() {
        return colorHex;
    }

    public float getPenSize() {
        return penSize;
    }

    public int getPenType() {
        return penType;
    }

    public byte getPointType() {
        return pointType;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getUid() {
        return uid;
    }

    public String[] getImages() {
        return images;
    }


    public int getData() {
        return data;
    }

    public int getDisabled() {
        return disabled;
    }


    /**
     * 绘画指令
     */
    public TransactionData(byte type, String uid, int page, int colorHex, float penSize, int penType, byte pointType, float x, float y) {
        this.type = type;
        this.uid = uid;
        this.page = page;
        this.colorHex = colorHex;
        this.penSize = penSize;
        this.penType = penType;
        this.pointType = pointType;
        this.x = x;
        this.y = y;
    }

    /**
     * 翻页指令
     * @param type
     * @param page
     */
    public TransactionData(byte type, int page) {
        this.type = type;
        this.page = page;
    }

    /**
     * 插入或者PPT指令
     * @param type
     * @param images
     */
    public TransactionData(byte type, String[] images){
        this.type = type;
        this.images = images;
    }

    /**
     * 回退指令
     * @param type
     */
    public TransactionData(byte type) {
        this.type = type;
    }

    /**
     * 清除指令
     * @param type
     * @param uid
     */
    public TransactionData(byte type, String uid) {
        this.type = type;
        this.uid = uid;
    }

    /**
     * 权限命令或者同步指令
     * @param type
     * @param data  权限指令时为权限控制类型，同步指令时为时间
     * @param disabled
     */
    public TransactionData(byte type, int data, int disabled) {
        this.type = type;
        this.data = data;
        this.disabled = disabled;
    }

    public static String pack(TransactionData t) {
        switch (t.getType()){
            case ActionType.UNDOLINE:
                return String.format("%d:;", t.getType());
            case ActionType.CLEARLINE:
                return String.format("%d:%s;", t.getType(),t.getUid());
            case ActionType.SYNCHRONOUS:
                return String.format("%d:%d,%d;", t.getType(),t.getDisabled(),t.getData());
            case ActionType.CHANGEPAGE:
                return String.format("%d:%s;", t.getType(),t.getPage());
            case ActionType.CHANGEPPT:
            case ActionType.INSERTPAGE:
                StringBuffer sb = new StringBuffer();
                sb.append(t.getType()).append(":");
                String[] images = t.getImages();
                for (int i = 0;i<images.length;i++){
                    if(i == images.length-1){
                        sb.append(images[i]);
                    }else{
                        sb.append(images[i]).append(",");
                    }
                }
                sb.append(";");
                return sb.toString();
            case ActionType.AUTHORITY:
                return String.format("%d:%d,%d;", t.getType(),t.getData(),t.getDisabled());
            case ActionType.CALLBACK:
                break;
        }
        //正常绘画
        return String.format("%d:%s,%d,%d,%f,%d,%d,%f,%f;", t.getType(),t.getUid(),t.getPage(), t.getColorHex(), t.getPenSize(), t.getPenType(), t.getPointType(), t.getX(), t.getY());
    }

    public static TransactionData unpack(String data) {
        int sp1 = data.indexOf(":");
        byte type;
        try {
            if (sp1 <= 0) {
                type = Byte.parseByte(data);
            } else {
                type = Byte.parseByte(data.substring(0, sp1));
            }
            switch (type){
                case ActionType.POINT:
                    String dataT = data.substring(sp1+1, data.length());
                    String[] datas =  dataT.split(",");
                    String uid = datas[0];
                    int page = Integer.parseInt(datas[1]);
                    int colorHex = Integer.parseInt(datas[2]);
                    float penSize = Float.parseFloat(datas[3]);
                    byte penType = Byte.parseByte(datas[4]);
                    byte pointType = Byte.parseByte(datas[5]);
                    float x = Float.parseFloat(datas[6]);
                    float y = Float.parseFloat(datas[7]);
                    return new TransactionData(type,uid,page,colorHex,penSize,penType,pointType,x,y);
                case ActionType.UNDOLINE:
                    return new TransactionData(type);
                case ActionType.CLEARLINE:
                    dataT = data.substring(sp1+1, data.length());
                    datas =  dataT.split(",");
                    return new TransactionData(type,datas[0]);
                case ActionType.SYNCHRONOUS:
                    dataT = data.substring(sp1+1, data.length());
                    datas =  dataT.split(",");
                    int status = Integer.parseInt(datas[0]);
                    int time = Integer.parseInt(datas[1]);
                    return new TransactionData(type,time,status);
                case ActionType.CHANGEPAGE:
                    dataT = data.substring(sp1+1, data.length());
                    datas =  dataT.split(",");
                    return new TransactionData(type,Integer.parseInt(datas[0]));
                case ActionType.CHANGEPPT:
                case ActionType.INSERTPAGE:
                    LogUtil.i(TAG, "Receive INSERTPAGE or CHANGEPPT Data");
                    dataT = data.substring(sp1+1, data.length());
                    String[] images =  dataT.split(",");
                    return new TransactionData(type,images);
                case ActionType.AUTHORITY:
                    dataT = data.substring(sp1+1, data.length());
                    datas =  dataT.split(",");
                    int authorityType = Integer.parseInt(datas[0]);
                    int disable = Integer.parseInt(datas[1]);
                    return new TransactionData(type,authorityType,disable);
                case ActionType.CALLBACK:
                    break;
                default:
                    LogUtil.i(TAG, "recieve type:" + type);
                    // 其他控制指令
                    return new TransactionData(type);

            }
        } catch (Exception e) {
           LogUtil.e("unpack exception:"+e.getMessage());
        }

        return null;
    }
    public boolean isPaint() {
        return type == ActionType.POINT;
    }
    public boolean isRevoke() {
        return type == ActionType.UNDOLINE;
    }
    public boolean isClear() {
        return type == ActionType.CLEARLINE;
    }
    public boolean isFlip() {
        return type == ActionType.INSERTPAGE;
    }
    public boolean isPPT() {
        return type == ActionType.CHANGEPPT;
    }
    public boolean isAuthorty() {
        return type == ActionType.AUTHORITY;
    }
    public boolean isSync() {
        return type == ActionType.SYNCHRONOUS;
    }
    public boolean isChangePage() {
        return type == ActionType.CHANGEPAGE;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "type=" + type +
                ", page=" + page +
                ", colorHex=" + colorHex +
                ", penSize=" + penSize +
                ", penType=" + penType +
                ", pointType=" + pointType +
                ", x=" + x +
                ", y=" + y +
                ", uid='" + uid + '\'' +
                '}';
    }
}
