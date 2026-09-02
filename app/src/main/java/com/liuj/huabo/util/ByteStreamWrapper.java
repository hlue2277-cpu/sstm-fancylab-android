package com.liuj.huabo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author gebiao
 * 用于OutputStream 和 InputStream 共用buff，减少内存操作。主要用于图片处理
 */
public class ByteStreamWrapper extends ByteArrayOutputStream {
    public ByteArrayInputStream getInputStream() {
        return new ByteArrayInputStream(this.buf, 0, this.count);
    }
}
