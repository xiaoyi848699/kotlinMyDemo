package demo.xy.com.xytdcq.surfaceView.doodle;

/**
 * 形状枚举
 */
public enum ActionTypeEnum {
    Eraser(-1),
    UnKnow(0),
    Path(1),
    Line(2),
    triangle(3),
    Rect(4),
    Circle(5),
    FilledRect(6),
    FilledCircle(7);

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