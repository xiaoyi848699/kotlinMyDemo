package demo.xy.com.xytdcq.surfaceView.doodle;

/**
 * 形状枚举
 */
public enum ActionTypeEnum {
    UnKnow(-2),
    Eraser(-1),
    Path(0),
    Line(1),
    Circle(2),
    Rectangle(3),// 矩形
    Triangle(4), // 三角形
    Rect(5),
    FilledRect(6),
    FilledCircle(7),
    Cancel(8);

    private int value;

    ActionTypeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

//    public static ActionTypeEnum typeOfValue(int value) {
//        for (ActionTypeEnum e : values()) {
//            if (e.getValue() == value) {
//                return e;
//            }
//        }
//        return UnKnow;
//    }
}