package utils.io;

public class BinaryArrayWriter {

    private final byte[] buf;
    private       int    pos;
    private       int mark;
    private final int offset;

    public BinaryArrayWriter(byte[] buf, int offset) {
        this.buf = buf;
        this.offset = offset;
        this.pos = offset;
        this.mark = offset;
    }

    public void writeUInt8(int value) throws Exception {
        buf[pos] = (byte) value;
        pos++;
    }

    public void writeUInt16(int value) throws Exception {
        BinaryWriter.writeUInt16(buf, pos, value);
        pos += 2;
    }

    public void writeUInt32(long value) throws Exception {
        BinaryWriter.writeUInt32(buf, pos, value);
        pos += 4;
    }

    public void mark(){
        mark = pos;
    }

    public void reset(){
        pos = mark;
    }

    public void jumpRel(int relOffset){
        pos = offset + relOffset;
    }

}
