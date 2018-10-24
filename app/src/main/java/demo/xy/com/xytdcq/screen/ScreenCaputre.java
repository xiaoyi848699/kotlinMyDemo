package demo.xy.com.xytdcq.screen;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import demo.xy.com.xytdcq.uitls.LogUtil;

import static android.media.MediaCodec.CONFIGURE_FLAG_ENCODE;
import static android.media.MediaFormat.KEY_BIT_RATE;
import static android.media.MediaFormat.KEY_FRAME_RATE;
import static android.media.MediaFormat.KEY_I_FRAME_INTERVAL;

public class ScreenCaputre {

    private static final String TAG = ScreenCaputre.class.getSimpleName();

    public interface ScreenCaputreListener {
        void onImageData(byte[] buf);
    }
    private ScreenCaputreListener screenCaputreListener;

    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private int width = 720;
    private int height = 1080;

    public ScreenCaputre(int width, int height, MediaProjection mMediaProjection) {
        this.width = width;
        this.height = height;
        this.mMediaProjection = mMediaProjection;
    }

    public void setScreenCaputreListener(ScreenCaputreListener screenCaputreListener) {
        this.screenCaputreListener = screenCaputreListener;
    }

    public void start() {
        try {
            prepareVideoEncoder();
            startVideoEncode();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        videoEncoderLoop = false;
        if (null != vEncoder) {
            vEncoder.stop();
        }
        if(mVirtualDisplay != null) mVirtualDisplay.release();
        if(mMediaProjection!= null) mMediaProjection.stop();

    }
    public static final int NAL_SLICE = 1;
    public static final int NAL_SLICE_DPA = 2;
    public static final int NAL_SLICE_DPB = 3;
    public static final int NAL_SLICE_DPC = 4;
    public static final int NAL_SLICE_IDR = 5;
    public static final int NAL_SEI = 6;
    public static final int NAL_SPS = 7;
    public static final int NAL_PPS = 8;
    public static final int NAL_AUD = 9;
    public static final int NAL_FILLER = 12;

    private MediaCodec.BufferInfo vBufferInfo = new MediaCodec.BufferInfo();
    private MediaCodec.BufferInfo aBufferInfo = new MediaCodec.BufferInfo();
    private MediaCodec vEncoder;
    private MediaCodec aEncoder;
    private AudioRecord audioRecord;
    private Thread videoEncoderThread;
    private Thread audioEncoderThread;
    private boolean videoEncoderLoop;
    private MediaCodec mediaCodec = null;

    public static final int DEFAULT_FREQUENCY = 44100;
    public static final int DEFAULT_MAX_BPS = 64;
    public static final int DEFAULT_MIN_BPS = 32;
    public static final int DEFAULT_ADTS = 0;
    public static final String DEFAULT_MIME = "audio/mp4a-latm";
    public static final int DEFAULT_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    public static final int DEFAULT_AAC_PROFILE = MediaCodecInfo.CodecProfileLevel.AACObjectLC;
    public static final int DEFAULT_CHANNEL_COUNT = 2;
    public static final boolean DEFAULT_AEC = true;
    MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private byte[] mRecordBuffer;
    public static int getRecordBufferSize() {
        int frequency = DEFAULT_FREQUENCY;
        int audioEncoding = DEFAULT_AUDIO_ENCODING;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        if(DEFAULT_CHANNEL_COUNT == 2) {
            channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        }
        int size = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        return size;
    }

    public void prepareVideoEncoder() throws IOException {
        MediaFormat encodeFormat = MediaFormat.createAudioFormat(DEFAULT_MIME, DEFAULT_FREQUENCY, DEFAULT_CHANNEL_COUNT);
        encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, DEFAULT_AAC_PROFILE);
        encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, DEFAULT_MAX_BPS * 1024);
        encodeFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, DEFAULT_FREQUENCY);
        int maxInputSize = getRecordBufferSize();
        encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, maxInputSize);
        encodeFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, DEFAULT_CHANNEL_COUNT);


        try {
            mediaCodec = MediaCodec.createEncoderByType(DEFAULT_MIME);
            mediaCodec.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            aEncoder = mediaCodec;
        } catch (Exception e) {
            e.printStackTrace();
            if (mediaCodec != null) {
                mediaCodec.stop();
                mediaCodec.release();
                mediaCodec = null;
            }
        }

        int frequency = DEFAULT_FREQUENCY;
        int audioEncoding = DEFAULT_AUDIO_ENCODING;
        int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
        channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        int audioSource = MediaRecorder.AudioSource.MIC;
        audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
        audioRecord = new AudioRecord(audioSource, frequency,
                channelConfiguration, audioEncoding, getRecordBufferSize());
        mRecordBuffer =  new byte[getRecordBufferSize()];
        try {
            audioRecord.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
        }



        MediaFormat format = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);//MIME_TYPE = "video/avc",H264的MIME类型，宽，高
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);//设置颜色格式
        format.setInteger(KEY_BIT_RATE, width * height);//设置比特率
        format.setInteger(KEY_FRAME_RATE, 30);//设置帧率
        format.setInteger(KEY_I_FRAME_INTERVAL, 1);
        MediaCodec vencoder = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);//创建编码器
//        四个参数，第一个是media格式，第二个是解码器播放的surfaceview，第三个是MediaCrypto，第四个是编码解码的标识
        vencoder.configure(format, null, null, CONFIGURE_FLAG_ENCODE);
        Surface surface = vencoder.createInputSurface();//我的输入源

        mVirtualDisplay = mMediaProjection.createVirtualDisplay("-display", width, height, 1,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, surface, null, null);
        vEncoder = vencoder;
    }

    public void startVideoEncode() {
        if (vEncoder == null) {
            throw new RuntimeException("请初始化视频编码器");
        }
        if (videoEncoderLoop) {
            throw new RuntimeException("必须先停止");
        }
        videoEncoderThread = new Thread() {
            @Override
            public void run() {
                vEncoder.start();
                while (videoEncoderLoop && !Thread.interrupted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        ByteBuffer[] outputBuffers = vEncoder.getOutputBuffers();
                        int outputBufferId = vEncoder.dequeueOutputBuffer(vBufferInfo, 12000);//获取输出区的缓冲的索引
                        if (outputBufferId >= 0) {
                            ByteBuffer bb = outputBuffers[outputBufferId];
                            onEncodedAvcFrame(bb, vBufferInfo);
                            vEncoder.releaseOutputBuffer(outputBufferId, false);//释放缓存的资源
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        videoEncoderLoop = true;
        videoEncoderThread.start();

        audioEncoderThread = new Thread() {
            @Override
            public void run() {
//                aEncoder.start();
                mediaCodec.start();
                while (videoEncoderLoop && !Thread.interrupted()) {
                    try {

                        int readLen = audioRecord.read(mRecordBuffer, 0, getRecordBufferSize());
                        if (readLen > 0) {
                            LogUtil.e("audioRecord："+readLen);
                            ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
                            ByteBuffer[] outputBuffersA = mediaCodec.getOutputBuffers();
                            int inputBufferIndex = mediaCodec.dequeueInputBuffer(12000);
                            if (inputBufferIndex >= 0) {
                                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                                inputBuffer.clear();
                                inputBuffer.put(mRecordBuffer);
                                mediaCodec.queueInputBuffer(inputBufferIndex, 0, mRecordBuffer.length, 0, 0);
                            }

                            int outputBufferIndex = mediaCodec.dequeueOutputBuffer(mBufferInfo, 12000);
                            while (outputBufferIndex >= 0) {
                                ByteBuffer outputBuffer = outputBuffersA[outputBufferIndex];
                                onEncodedAccFrame(outputBuffer, mBufferInfo);
//                                if(mListener != null) {
//                                    mListener.onAudioEncode(outputBuffer, mBufferInfo);
//                                }
                                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                                outputBufferIndex = mediaCodec.dequeueOutputBuffer(mBufferInfo, 0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        audioEncoderThread.start();
    }

    private byte[] sps_pps_buf;
    private void onEncodedAvcFrame(ByteBuffer bb, final MediaCodec.BufferInfo vBufferInfo) {
        int offset = 4;
        //判断帧的类型
        if (bb.get(2) == 0x01) {
            offset = 3;
        }
        int type = bb.get(offset) & 0x1f;
        if (type == NAL_SPS) {
            //[0, 0, 0, 1, 103, 66, -64, 13, -38, 5, -126, 90, 1, -31, 16, -115, 64, 0, 0, 0, 1, 104, -50, 6, -30]
            //打印发现这里将 SPS帧和 PPS帧合在了一起发送
            // SPS为 [4，len-8]
            // PPS为后4个字节

            sps_pps_buf = new byte[vBufferInfo.size];
            bb.get(sps_pps_buf);
            //TODO
            /*
            final byte[] pps = new byte[4];
            final byte[] sps = new byte[vBufferInfo.size - 12];
            bb.getInt();// 抛弃 0,0,0,1
            bb.get(sps, 0, sps.length);
            bb.getInt();
            bb.get(pps, 0, pps.length);
            Log.d(TAG, "解析得到 sps:" + Arrays.toString(sps) + ",PPS=" + Arrays.toString(pps));
            */
        } else if (type == NAL_SLICE  /* || type == NAL_SLICE_IDR */) {
            final byte[] bytes = new byte[vBufferInfo.size];
            bb.get(bytes);
            if (null != screenCaputreListener) {
                screenCaputreListener.onImageData(bytes);
            }
            Log.e(TAG, "视频数据  " + Arrays.toString(bytes));

        } else if (type == NAL_SLICE_IDR) {
            // I帧，前面添加sps和pps
            final byte[] bytes = new byte[vBufferInfo.size];
            bb.get(bytes);

            byte[] newBuf = new byte[sps_pps_buf.length + bytes.length];
            System.arraycopy(sps_pps_buf, 0, newBuf, 0, sps_pps_buf.length);
            System.arraycopy(bytes, 0, newBuf, sps_pps_buf.length, bytes.length);
            if (null != screenCaputreListener) {
                screenCaputreListener.onImageData(newBuf);
            }
            Log.e(TAG, "sps pps  " + Arrays.toString(sps_pps_buf));
            Log.e(TAG, "视频数据  " + Arrays.toString(newBuf));
        }
    }

    private byte[] header = {0x00, 0x00, 0x00, 0x01};   //H264的头文件
    private void onEncodedAccFrame(ByteBuffer bb, final MediaCodec.BufferInfo bi) {

        bb.position(bi.offset);
        bb.limit(bi.offset + bi.size);
        byte[] audio = new byte[bi.size];
        bb.get(audio);
        //一般第一帧都是2个字节
        int length = 7 + audio.length;
        ByteBuffer tempBb = ByteBuffer.allocate(length + 4);
        tempBb.put(header);
        tempBb.put(getADTSHeader(length));
        tempBb.put(audio);
        if (null != screenCaputreListener) {
            screenCaputreListener.onImageData(tempBb.array());
        }
        Log.e(TAG, "音频数据  " + Arrays.toString(tempBb.array()));

    }
    /**
     * 给编码出的aac裸流添加adts头字段
     *
     * @param packetLen
     */
    private byte[] getADTSHeader(int packetLen) {
        byte[] packet = new byte[7];
        int profile = 2;  //AAC LC
        int freqIdx = 4;  //16.0KHz
        int chanCfg = 2;  //CPE 声道数
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
        return packet;
    }
}
