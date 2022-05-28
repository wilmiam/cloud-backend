package com.zq.common.http;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;

/**
 * @author wilmiam
 * @since 2019-03-14
 */
public class FileBytesPart {

    private String filename;
    private byte[] data;

    public static Builder custom() {
        return new Builder();
    }

    public FileBytesPart toBytesPart() {
        if (StringUtils.isBlank(filename) || data == null || data.length == 0) {
            return null;
        }
        FileBytesPart bytesPart = new FileBytesPart();
        bytesPart.setFilename(filename);
        bytesPart.setData(data);
        return bytesPart;
    }

    public FileInputStreamPart toInputStreamPart() {
        if (StringUtils.isBlank(filename) || data == null || data.length == 0) {
            return null;
        }
        FileInputStreamPart insPart = new FileInputStreamPart();
        insPart.setFilename(filename);
        insPart.setData(new ByteArrayInputStream(data));
        return insPart;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public static class Builder {

        private String filename;
        private byte[] data;

        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder data(byte[] data) {
            this.data = data;
            return this;
        }

        public FileBytesPart build() {
            FileBytesPart part = new FileBytesPart();
            part.setFilename(this.filename);
            part.setData(this.data);
            return part;
        }

    }

}
