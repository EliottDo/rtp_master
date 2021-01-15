package com.humaxdigital.rtp.rtsp;

import com.humaxdigital.rtp.MainActivity;
import com.humaxdigital.rtp.media.h264data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by user111 on 2018/3/14.
 */

public class ScreenInputStream extends InputStream {

    private long ts = 0;
    private ByteBuffer mBuffer = null;
    private h264data encodedData = null;

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException{
        int min = 0;

        if(mBuffer == null){
            encodedData = MainActivity.h264Queue.poll();
            if(encodedData == null) return 0;
            ts = encodedData.ts;
            mBuffer = ByteBuffer.wrap(encodedData.data);
            mBuffer.position(0);
        }
        min = length < encodedData.data.length - mBuffer.position() ? length : encodedData.data.length - mBuffer.position();
        mBuffer.get(buffer, offset, min);
        if (mBuffer.position()>= encodedData.data.length) {
            mBuffer = null;
        }
        return min;
    }


    @Override
    public int read() throws IOException {
        return 0;
    }

    public int available() {
        if (mBuffer != null)
            return encodedData.data.length - mBuffer.position();
        else
            return 0;
    }


    public long getLastTimestamp(){
        return ts;
    }
}
