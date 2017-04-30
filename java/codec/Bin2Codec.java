package codec;

@SuppressWarnings({"PointlessBitwiseExpression", "PointlessArithmeticExpression"})
public class Bin2Codec implements CharCodec {

    @Override
    public char[] encode(byte[] data) {
        char[] result = new char[data.length * 8];
        for (int i = 0; i < result.length; i++)
            result[i] = (data[i / 8] & (0b10000000 >> i % 8)) == 0 ? '0' : '1';
        return result;
    }

    @Override
    public byte[] decode(char[] data) {
        byte[] result = new byte[data.length / 8];
        for (int i = 0; i < data.length; i++)
            result[i / 8] |= (byte) (data[i] == '0' ? 0 : 1 << (7 - (i % 8)));
        return result;
    }

}
