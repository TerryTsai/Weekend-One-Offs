package codec;

@SuppressWarnings({"PointlessArithmeticExpression", "PointlessBitwiseExpression"})
public class HexCodec implements CharCodec {

    @Override
    public char[] encode(byte[] data) {
        char[] result = new char[data.length * 2];
        for (int i = 0; i < result.length; i++) {
            int val = ((i % 2 == 0) ? data[i / 2] >> 4 : data[i / 2]) & 0b1111;
            result[i] = (char) ((val < 10) ? val + 48 : val + 87);
        }
        return result;
    }

    @Override
    public byte[] decode(char[] data) {
        byte[] result = new byte[data.length / 2];
        for (int i = 0; i < result.length; i++) {
            int hi = data[i * 2 + 0];
            int lo = data[i * 2 + 1];
            result[i] |= ((hi > 57) ? hi - 87 : hi - 48) << 4;
            result[i] |= ((lo > 57) ? lo - 87 : lo - 48) << 0;
        }
        return result;
    }

}
