package demo.xy.com.xy_tdcq.gl

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import demo.xy.com.xy_tdcq.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10




/**
 * Created by xy on 2018/7/23.
 */
class GLRender2 : GLSurfaceView.Renderer {

    var mSwitch = true
    var xrot: Float = 0.toFloat()
    var yrot:Float = 0.toFloat()
    var xspeed: Float = 0.toFloat()
    var yspeed:Float = 0.toFloat()
    var z = -5.0f

    var mBitmap: Bitmap? = null

    lateinit var activity:Activity
    constructor(activity:Activity){
        this.activity = activity
        mBitmap = BitmapFactory.decodeResource(activity.resources, R.mipmap.ic_launcher)
    }

    //光线参数
    var lightAmbient = FloatBuffer.wrap(floatArrayOf(0.5f, 0.5f, 0.5f, 1.0f))
    var lightDiffuse = FloatBuffer.wrap(floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f))
    var lightPosition = FloatBuffer.wrap(floatArrayOf(0.0f, 0.0f, 2.0f, 1.0f))

    lateinit var normalsBuf: FloatBuffer
    lateinit var verticesBuf: FloatBuffer
    lateinit var texCoordsBuf: FloatBuffer
    lateinit var indicesBuf: ByteBuffer

    var filter = 1
    lateinit var texture: IntArray
    var vertices = floatArrayOf(
            -1f, -1f, 1f,
            1f, -1f, 1f,
            1f, 1f, 1f,
            -1f, 1f, 1f,

            -1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, -1f, -1f,

            -1f, 1f, -1f, -1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, -1f,

            -1f, -1f, -1f, 1f, -1f, -1f, 1f, -1f, 1f, -1f, -1f, 1f,

            1f, -1f, -1f, 1f, 1f, -1f, 1f, 1f, 1f, 1f, -1f, 1f,

            -1f, -1f, -1f, -1f, -1f, 1f, -1f, 1f, 1f, -1f, 1f, -1f)

    var normals = floatArrayOf(0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,

            0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f,

            0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f,

            0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f,

            1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f, 1f, 0f, 0f,

            -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f, -1f, 0f, 0f)

    var texCoords = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 1f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f, 0f, 1f, 1f, 1f, 1f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 1f, 1f)

    var indices = byteArrayOf(
            0, 1, 3, 2,
            4, 5, 7, 6,
            8, 9, 11, 10,
            12, 13, 15, 14,
            16, 17, 19, 18,
            20, 21, 23, 22)


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
//        //gl?.glDisable用于禁用OpenGL某方面的特性，该处表示关闭抗抖动，可以提高性能
//        gl?.glDisable(GL10.GL_DITHER)
//        //该方法用于修正，本处用于设置对透视进行修正
//        gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
//                GL10.GL_FASTEST)
//        //设置OpenGL清屏所用的颜色，四个参数分别代表红、绿、蓝和透明度值，范围为0-1，此处表示黑色
//        gl?.glClearColor(0f, 0f, 0f, 0f)
//        //设置阴影平滑模式
//        gl?.glShadeModel(GL10.GL_SMOOTH)
//        //启用某方面的性能，此处为启动深度测试，负责跟踪每个物体在Z轴上的深度，避免后面的物体遮挡前面的物体
//        gl?.glEnable(GL10.GL_DEPTH_TEST)
//        //设置深度测试的类型，此处为如果输入的深度值小于或等于参考值，则通过
//        gl?.glDepthFunc(GL10.GL_LEQUAL)

        //启用顶点缓冲数组
//        gl?.glEnableClientState( GL10.GL_VERTEX_ARRAY )
       gl?.glDisable(GL10.GL_DITHER)

        // 告诉系统对透视进行修正  
       gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
        // 黑色背景  
       gl?.glClearColor(0f, 0f, 0f, 0f)

       gl?.glEnable(GL10.GL_CULL_FACE)
        // 启用阴影平滑  
       gl?.glShadeModel(GL10.GL_SMOOTH)
        // 启用深度测试  
       gl?.glEnable(GL10.GL_DEPTH_TEST)
        //设置光线,,1.0f为全光线，a=50%  
       gl?.glColor4f(1.0f, 1.0f, 1.0f, 0.5f)
        // 基于源象素alpha通道值的半透明混合函数  
       gl?.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE)

        //纹理相关  
        val textureBuffer = IntBuffer.allocate(3)
       gl?.glGenTextures(3, textureBuffer)
        texture = textureBuffer.array()

       gl?.glBindTexture(GL10.GL_TEXTURE_2D, texture[0])
       gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST)
       gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST)
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0)

       gl?.glBindTexture(GL10.GL_TEXTURE_2D, texture[1])
       gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
       gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0)

       gl?.glBindTexture(GL10.GL_TEXTURE_2D, texture[2])
       gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR)
       gl?.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR)
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap, 0)

        //深度测试相关  
       gl?.glClearDepthf(1.0f)
       gl?.glDepthFunc(GL10.GL_LEQUAL)
       gl?.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST)
       gl?.glEnable(GL10.GL_TEXTURE_2D)

        //设置环境光  
       gl?.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient)
        //设置漫射光  
       gl?.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse)
        //设置光源位置  
       gl?.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition)
        //开启一号光源  
       gl?.glEnable(GL10.GL_LIGHT1)
        //开启混合  
       gl?.glEnable(GL10.GL_BLEND)

        val nbb = ByteBuffer.allocateDirect(normals.size * 4)
        nbb.order(ByteOrder.nativeOrder())
        normalsBuf = nbb.asFloatBuffer()
        normalsBuf.put(normals)
        normalsBuf.position(0)

        val vbb = ByteBuffer.allocateDirect(vertices.size * 4)
        vbb.order(ByteOrder.nativeOrder())
        verticesBuf = vbb.asFloatBuffer()
        verticesBuf.put(vertices)
        verticesBuf.position(0)

        val tbb = ByteBuffer.allocateDirect(texCoords.size * 4)
        tbb.order(ByteOrder.nativeOrder())
        texCoordsBuf = tbb.asFloatBuffer()
        texCoordsBuf.put(texCoords)
        texCoordsBuf.position(0)

        indicesBuf = ByteBuffer.allocateDirect(indices.size)
        indicesBuf.put(indices)
        indicesBuf.position(0)

        mSwitch = false
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        //设置3D视窗的大小及位置
       gl?.glViewport(0, 0,
                width, height)
        //将当前矩阵模式设为投影矩形
       gl?.glMatrixMode(GL10.GL_PROJECTION)
        //初始化单位矩阵
       gl?.glLoadIdentity()
        //计算透视窗口的宽度高度比
        var ratio = width.toFloat() / height
        //调用此方法设置透视窗口的空间大小
        //设置透视投影的空间大小，前两个参数用于设置X轴的最小值与最大值，中间两个参数用于设置y轴的最小值最大值 ，后两个参数用于设置Z轴的最小值最大值
       gl?.glFrustumf(-ratio,
                ratio, -1f, 1f, 1f, 10f)


    }
    /**
     * 每隔16ms调用一次
     */
    override fun onDrawFrame(gl: GL10) {
        // 清除屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)

        gl.glMatrixMode(GL10.GL_MODELVIEW)
        // 重置当前的模型观察矩阵
        gl.glLoadIdentity()
        //使能光照
        gl.glEnable(GL10.GL_LIGHTING)

        ////////////////
        gl.glTranslatef(0.0f, 0.0f, z)

        //设置旋转
        gl.glRotatef(xrot, 1.0f, 0.0f, 0.0f)
        gl.glRotatef(yrot, 0.0f, 1.0f, 0.0f)

        //设置纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[filter])

        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalsBuf)
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuf)
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordsBuf)

        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY)
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)

        //绘制四边形
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 24, GL10.GL_UNSIGNED_BYTE, indicesBuf)

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY)
        //修改旋转角度
        xrot += 0.3f
        yrot += 0.2f

        //混合开关
        if (mSwitch) {
            gl.glEnable(GL10.GL_BLEND)         // 打开混合
            gl.glDisable(GL10.GL_DEPTH_TEST)   // 关闭深度测试
        } else {
            gl.glDisable(GL10.GL_BLEND)        // 关闭混合
            gl.glEnable(GL10.GL_DEPTH_TEST)    // 打开深度测试
        }

    }

}