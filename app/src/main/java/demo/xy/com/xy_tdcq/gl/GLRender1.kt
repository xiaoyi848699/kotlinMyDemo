package demo.xy.com.xy_tdcq.gl

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by xy on 2018/7/23.
 */
class GLRender1 : GLSurfaceView.Renderer {


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // gl.glDisable用于禁用OpenGL某方面的特性，该处表示关闭抗抖动，可以提高性能
        gl?.glDisable(GL10.GL_DITHER)
        //该方法用于修正，本处用于设置对透视进行修正
        gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST)
        //设置OpenGL清屏所用的颜色，四个参数分别代表红、绿、蓝和透明度值，范围为0-1，此处表示黑色
        gl?.glClearColor(0f, 0f, 0f, 0f)
        //设置阴影平滑模式
        gl?.glShadeModel(GL10.GL_SMOOTH)
        //启用某方面的性能，此处为启动深度测试，负责跟踪每个物体在Z轴上的深度，避免后面的物体遮挡前面的物体
        gl?.glEnable(GL10.GL_DEPTH_TEST)
        //设置深度测试的类型，此处为如果输入的深度值小于或等于参考值，则通过
        gl?.glDepthFunc(GL10.GL_LEQUAL)
    }

    //控制旋转的角度
    private var rotate: Float = 0.toFloat()


    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        //设置3D视窗的大小及位置
        gl.glViewport(0, 0,
                width, height)
        //将当前矩阵模式设为投影矩形
        gl.glMatrixMode(GL10.GL_PROJECTION)
        //初始化单位矩阵
        gl.glLoadIdentity()
        //计算透视窗口的宽度高度比
        val ratio = width.toFloat() / height
        //调用此方法设置透视窗口的空间大小
        //设置透视投影的空间大小，前两个参数用于设置X轴的最小值与最大值，中间两个参数用于设置y轴的最小值最大值 ，后两个参数用于设置Z轴的最小值最大值
        gl.glFrustumf(-ratio,
                ratio, -1f, 1f, 1f, 10f)
    }
    /**
     * 每隔16ms调用一次
     */
    override fun onDrawFrame(gl: GL10) {
        //清除屏幕缓存和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        //启用顶点坐标数据
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        //启用顶点颜色数据
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
        //设置当前矩阵堆栈为模型堆栈
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        //------绘制第一个图形-----
        //重置当前的模型视图矩阵
        gl.glLoadIdentity()
        //中心点在屏幕最中间然后向各个方向移动
        gl.glTranslatef(0.2f,
                -0.8f, -1.5f)
        //这里的x,y,z值相当于一个布尔值，0.0表示假，而非零参数则表示真。
        // 如果设置的旋转值（x,y,z的值）为正数，那么旋转的方向是逆时针的，如果旋转值是负数，那么旋转的方向是顺时针的。
        gl.glRotatef(rotate, 0f, 0f, 0.1f)
        //设置顶点位置数据
        gl.glVertexPointer(3,
                GL10.GL_FLOAT, 0,
                PointDataKotlin.getTriangleDataBuffer())
        //设置顶点颜色数据
        gl.glColorPointer(4,
                GL10.GL_FIXED, 0,
                PointDataKotlin.getTriangleColorBuffer())
        //根据顶点数据绘制平面图形
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3)
//        GL_POINTS - 单独的将顶点画出来。
//        GL_LINES - 单独地将直线画出来。行为和 GL_TRIANGLES 类似。
//        GL_LINE_STRIP - 连贯地将直线画出来。行为和 GL_TRIANGLE_STRIP 类似。
//        GL_LINE_LOOP - 连贯地将直线画出来。行为和 GL_LINE_STRIP 类似，但是会自动将最后一个顶点和第一个顶点通过直线连接起来。
//        GL_TRIANGLES - 这个参数意味着OpenGL使用三个顶点来组成图形。所以，在开始的三个顶点，将用顶点1，顶点2，顶点3来组成一个三角形。完成后，在用下一组的三个顶点(顶点4，5，6)来组成三角形，直到数组结束。
//        GL_TRIANGLE_STRIP - OpenGL的使用将最开始的两个顶点出发，然后遍历每个顶点，这些顶点将使用前2个顶点一起组成一个三角形。

        //-----绘制第二个图形-----
        //重置当前的模型视图矩阵
        gl.glLoadIdentity()
        gl.glTranslatef(0.4f, 0.5f,
                -1.5f)
        gl.glRotatef(rotate, 0f, 0.1f, 0f)
        //设置顶点位置数据
        gl.glVertexPointer(3,
                GL10.GL_FLOAT, 0,
                PointDataKotlin.getRectDataBuffer())
        //设置顶点颜色数据
        gl.glColorPointer(4,
                GL10.GL_FIXED, 0,
                PointDataKotlin.getRectColorBuffer())
        //更具顶点数据绘制平面图形
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4)

        //-----绘制第三个3D图形----
        //重置当前的模型视图矩阵
        gl.glLoadIdentity()
        //平移函数
        gl.glTranslatef(-0.3f, 0.2f,
                -1.5f)
        // 沿着xY轴旋转
        gl.glRotatef(rotate, 1f, 1f, 0f)
        //设置顶点位置数据
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, PointDataKotlin.getVerticesBuffer())
        // 设置顶点的颜色数据
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, PointDataKotlin.getColorbuffer())
        //绘制三角形
        gl.glDrawElements(GL10.GL_TRIANGLES, PointDataKotlin.indices.size, GL10.GL_UNSIGNED_SHORT, PointDataKotlin.getIndexbuffer())


        //绘制结束
        gl.glFinish()
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        //旋转角度+1
        rotate += 1f
    }
}