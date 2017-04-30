package codec;

@SuppressWarnings({"PointlessBitwiseExpression", "PointlessArithmeticExpression"})
public class BinCodec implements CharCodec {

    @Override
    public char[] encode(byte[] data) {
        char[] result = new char[data.length * 8];
        for (int i = 0; i < data.length; i++) {
            result[i * 8 + 0] = (data[i] & (1 << 7)) == 0 ? '0' : '1';
            result[i * 8 + 1] = (data[i] & (1 << 6)) == 0 ? '0' : '1';
            result[i * 8 + 2] = (data[i] & (1 << 5)) == 0 ? '0' : '1';
            result[i * 8 + 3] = (data[i] & (1 << 4)) == 0 ? '0' : '1';
            result[i * 8 + 4] = (data[i] & (1 << 3)) == 0 ? '0' : '1';
            result[i * 8 + 5] = (data[i] & (1 << 2)) == 0 ? '0' : '1';
            result[i * 8 + 6] = (data[i] & (1 << 1)) == 0 ? '0' : '1';
            result[i * 8 + 7] = (data[i] & (1 << 0)) == 0 ? '0' : '1';
        }
        return result;
    }

    @Override
    public byte[] decode(char[] data) {
        byte[] result = new byte[data.length / 8];
        for (int i = 0; i < result.length; i++) {
            result[i] |= (byte) (data[i * 8 + 0] == '0' ? 0 : 1 << 7);
            result[i] |= (byte) (data[i * 8 + 1] == '0' ? 0 : 1 << 6);
            result[i] |= (byte) (data[i * 8 + 2] == '0' ? 0 : 1 << 5);
            result[i] |= (byte) (data[i * 8 + 3] == '0' ? 0 : 1 << 4);
            result[i] |= (byte) (data[i * 8 + 4] == '0' ? 0 : 1 << 3);
            result[i] |= (byte) (data[i * 8 + 5] == '0' ? 0 : 1 << 2);
            result[i] |= (byte) (data[i * 8 + 6] == '0' ? 0 : 1 << 1);
            result[i] |= (byte) (data[i * 8 + 7] == '0' ? 0 : 1 << 0);
        }
        return result;
    }


}
