package codec;

public class PadCodec implements CharCodec {

    private final char pad;
    private final int bits;
    private final CharCodec codec;

    public PadCodec(char pad, int bits, CharCodec codec) {
        this.pad = pad;
        this.bits = bits;
        this.codec = codec;
    }

    @Override
    public char[] encode(byte[] data) {
        int newLength = data.length;
        while (newLength * 8 % bits != 0)
            newLength++;
        byte[] newData = new byte[newLength];
        System.arraycopy(data, 0, newData, 0, data.length);
        return codec.encode(newData);
    }

    @Override
    public byte[] decode(char[] data) {
        int newLength = data.length;
        while (newLength * bits % 8 != 0)
            newLength++;
        char[] newData = new char[newLength];
        System.arraycopy(data, 0, newData, 0, data.length);
        for (int i = data.length; i < newLength; i++)
            newData[i] = pad;
        return codec.decode(newData);
    }

}
