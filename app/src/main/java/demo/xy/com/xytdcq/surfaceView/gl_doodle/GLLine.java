package demo.xy.com.xytdcq.surfaceView.gl_doodle;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by xy on 2018/8/29.
 */
public class GLLine {

    /**顶点字节数组**/
    private ByteBuffer pointByteBuffer;
    /**顶点RGBA字节数组**/
    private ByteBuffer colorByteBuffer;
    /**顶点坐标数组**/
    private FloatBuffer pointBuffer = null;
    /**顶点RGBA数组**/
    private FloatBuffer colorBuffer = null;
    /**正在写入第几个顶点float**/
    private int pointBufferPos = 0;
    /**正在写入第几个颜色float**/
    private int colorBufferPos = 0;
    /**初始化时的顶点数目**/
    private int initVertexCount = 1 * 1024;

    public void drawLine(float x, float y) {
        //按初始化大小初始化顶点字节数组和顶点数组
        if (pointBuffer == null) {
            pointByteBuffer = ByteBuffer.allocateDirect(initVertexCount * 4);    //顶点数 * sizeof(float)
            pointByteBuffer.order(ByteOrder.nativeOrder());
            pointBuffer = pointByteBuffer.asFloatBuffer();
            pointBuffer.position(0);
            pointBufferPos = 0;
        }
        //按初始化大小初始化RGBA字节数组和RGBA数组
        if (colorBuffer == null) {
            colorByteBuffer = ByteBuffer.allocateDirect(initVertexCount * 4);
            colorByteBuffer.order(ByteOrder.nativeOrder());
            colorBuffer = colorByteBuffer.asFloatBuffer();
            colorBuffer.position(0);
            colorBufferPos = 0;
        }
        //写入坐标值x,y,z
        pointBuffer.put(pointBufferPos++, x);
        pointBuffer.put(pointBufferPos++, y);
        pointBuffer.put(pointBufferPos++, 0f);
        //写入颜色值r,g,b,a
        colorBuffer.put(colorBufferPos++, 1f);
        colorBuffer.put(colorBufferPos++, (float) Math.random());//彩色
        colorBuffer.put(colorBufferPos++, 1f);//白色
        colorBuffer.put(colorBufferPos++, 1f);
        colorBuffer.put(colorBufferPos++, 1f);
        //如果写入的颜色数超过初始值，将顶点数和颜色数组容量翻倍
        if (colorBufferPos * 4 >= initVertexCount) {
            Log.i("GLLines", "扩容点数到:" + initVertexCount);
            initVertexCount *= 2;

            ByteBuffer qbb = ByteBuffer.allocateDirect(initVertexCount * 4);    //顶点数 * sizeof(float) ;
            qbb.order(ByteOrder.nativeOrder());
            System.arraycopy(pointByteBuffer.array(), 0, qbb.array(), 0, (pointBufferPos) * 4);   //顶点数 * sizeof(float)
            pointByteBuffer = qbb;
            pointBuffer = pointByteBuffer.asFloatBuffer();

            ByteBuffer qbb2 = ByteBuffer.allocateDirect(initVertexCount * 4);    //顶点数 * sizeof(float) ;
            qbb2.order(ByteOrder.nativeOrder());
            System.arraycopy(colorByteBuffer.array(), 0, qbb2.array(), 0, (colorBufferPos ) * 4);  //sizeof(R,G,B,Alpha) * sizeof(float)
            colorByteBuffer = qbb2;
            colorBuffer = colorByteBuffer.asFloatBuffer();

        }
    }

    public int getVertexCount(){
        return pointBufferPos / 3;
    }

    public void drawTo(GL10 gl) {
        if (pointBuffer != null && colorBuffer != null) {
            pointBuffer.position(0);
            colorBuffer.position(0);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, pointBuffer);
            gl.glColorPointer(4, GL10.GL_FLOAT,0, colorBuffer);
            gl.glLineWidth(3f);
            gl.glDrawArrays(GL10.GL_LINE_STRIP,0, pointBufferPos / 3); //添加的point浮点数/3才是坐标数（因为一个坐标由x,y,z3个float构成，不能直接用）, 第三个参数count如果超过实际点数就会不断有指向0的点在最后
//            gl.glDrawElements(GL10.GL_LINE_STRIP,0, pointBufferPos / 3, null);  //第一个参数是点的类型，第二个参数是点的个数，第三个是第四个参数的类型，第四个参数是点的存储绘制顺序。
        }
    }

}
