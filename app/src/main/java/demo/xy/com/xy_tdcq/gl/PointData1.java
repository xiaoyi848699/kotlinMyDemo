package demo.xy.com.xy_tdcq.gl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

/**
 * Created by xy on 2018/7/23.
 */
public class PointData1 {
    static float[] triangleData = new float[]{
        0.1f,0.4f,0.0f,//上顶点
         -0.3f,0.0f,0.0f,//左顶点
         0.3f,0.1f,0.0f//右顶点
         };
    static int[] triangleColor = new int[]{
           65535,0,0,0,//上顶点红色
            0,65535,0,0,//左顶点绿色
            0,0,65535,0//右顶点蓝色
            };

    static float[] rectData = new float[]{
           0.3f,0.3f,0.0f,//右上顶点
            0.3f,-0.3f,0.0f,//右下顶点
            -0.3f,0.3f,0.0f,//左上顶点
            -0.3f,-0.3f,0.0f//左下顶点
            };
    static int[] rectColor = new int[]{
            0,65535,0,0,//右上顶点绿色
           0,0,65535,0,//右下顶点蓝色
            65535,0,0,0,//左上顶点红色
            65535,65535,0,0//左下顶点黄色
            };
      //依然是正方形的四个顶点，只是顺序交换了一下
      static float[] rectData2 = new float[]{
            -0.4f,0.4f,0.0f,//左上顶点
            0.4f,0.4f,0.0f,//右上顶点
            0.4f,-0.4f,0.0f,//右下顶点
            -0.4f,-0.4f,0.0f//左下顶点
            };
    static  float[] pentacle = new float[]{
            0.4f,0.4f,0.0f,
           -0.2f,0.3f,0.0f,
            0.5f,0.0f,0f,
            -0.4f,0.0f,0f,
           -0.1f,-0.3f,0f
           };

    /**
     * 1.定义立方体的8个顶点 6个面
     * */
    static float[] cubeVertices = {
            //左面
            -0.2f,0.2f,0.2f,
            -0.2f,-0.2f,0.2f,
            -0.2f,0.2f,-0.2f,
            -0.2f,-0.2f,-0.2f,

            //右面
            0.2f, 0.2f,0.2f,
            0.2f,-0.2f,0.2f,
            0.2f,-0.2f,-0.2f,
            0.2f,0.2f,-0.2f ,

            //前面
            -0.2f,0.2f,0.2f,
            -0.2f,-0.2f,0.2f,
            0.2f,-0.2f,0.2f,
            0.2f, 0.2f,0.2f,

            //后面
            0.2f,-0.2f,-0.2f,
            0.2f,0.2f,-0.2f,
            -0.2f,0.2f,-0.2f,
            -0.2f,-0.2f,-0.2f,


            //上面
            -0.2f,0.2f,0.2f,
            0.2f, 0.2f,0.2f,
            0.2f,0.2f,-0.2f,
            -0.2f,0.2f,-0.2f,

            //下面
            -0.2f,-0.2f,0.2f,
            0.2f,-0.2f,0.2f,
            0.2f,-0.2f,-0.2f,
            -0.2f,-0.2f,-0.2f
    };
    /**
     * 索引数组（6个面）
     * */
    static short[] indices={
            //对应左边面(两个三角形组成)
            0,1,2,//对应一个三角形
            1,2,3,//对应一个三角形

            4,5,6,
            4,6,7,

            8,9,10,
            8,10,11,

            12,13,14,
            12,14,15,

            16,17,18,
            16,18,19,

            20,21,22,
            20,22,23,
    };
    /**
     * 2.各个位置对应的颜色数组
     * */
    static float []  cubeColors = {
            1f,0f,0f,1f ,
            1f,0f,0f,1f ,
            1f,0f,0f,1f ,
            1f,0f,0f,1f ,

            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,
            0f,1f,0f,1f,

            0f,0f,1f,1f,
            0f,0f,1f,1f,
            0f,0f,1f,1f,
            0f,0f,1f,1f,

            //灰色
            0.5f,0.5f,0.5f,1f,
            0.5f,0.5f,0.5f,1f,
            0.5f,0.5f,0.5f,1f,
            0.5f,0.5f,0.5f,1f,

            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,

            1f,0f,0f,1f ,
            0f,1f,0f,1f,
            0f,0f,1f,1f,
            1f,0f,0f,1f,
    };




    public static Buffer getTriangleDataBuffer() {
//        return FloatBuffer.wrap(triangleData);
        return bufferFloatUtil(triangleData);
    }

    public static Buffer getTriangleColorBuffer() {
        return bufferIntUtil(triangleColor);
//        return IntBuffer.wrap(triangleColor);
    }

    public static Buffer getRectDataBuffer() {
        return bufferFloatUtil(rectData);
//        return FloatBuffer.wrap(rectData);
    }

    public static Buffer getRectColorBuffer() {
        return bufferIntUtil(rectColor);
    }

    public static FloatBuffer getRectDataBuffer2() {
        return FloatBuffer.wrap(rectData2);
    }

    public static FloatBuffer getPentacleBuffer() {
        return FloatBuffer.wrap(pentacle);
    }


    public static Buffer getVerticesBuffer() {//浮点形缓冲数据
        return bufferFloatUtil(cubeVertices);
    }

    public static Buffer getColorbuffer() {//浮点型颜色数据
        return bufferFloatUtil(cubeColors);
    }

    public static Buffer getIndexbuffer() {//浮点型索引数据
        return bufferShortUtil(indices);
    }

    /*
     * OpenGL 是一个非常底层的画图接口，它所使用的缓冲区存储结构是和我们的 java 程序中不相同的。
     * Java 是大端字节序(BigEdian)，而 OpenGL 所需要的数据是小端字节序(LittleEdian)。
     * 所以，我们在将 Java 的缓冲区转化为 OpenGL 可用的缓冲区时需要作一些工作。建立buff的方法如下
     * 因为openGL是C语言实现的，所以要读缓冲区的数据就要先设置顶点指针再把缓冲区指针位置定位到0位置。
     * */
    public static Buffer bufferIntUtil(int []arr){
        IntBuffer mBuffer ;
        //先初始化buffer,数组的长度*4,因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        //数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mBuffer = qbb.asIntBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }
    public static Buffer bufferFloatUtil(float []arr){
        FloatBuffer mBuffer ;
        //先初始化buffer,数组的长度*4,因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        //数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mBuffer = qbb.asFloatBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }
    public static Buffer bufferShortUtil(short []arr){
        ShortBuffer mBuffer ;
        //先初始化buffer,数组的长度*4,因为一个int占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(arr.length * 4);
        //数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mBuffer = qbb.asShortBuffer();
        mBuffer.put(arr);
        mBuffer.position(0);
        return mBuffer;
    }
}
