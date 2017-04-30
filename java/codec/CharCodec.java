package codec;

public interface CharCodec {

    char[] encode(byte[] data);

    byte[] decode(char[] data);

    default void test() {
        byte[] test = new byte[Byte.MAX_VALUE-Byte.MIN_VALUE];
        for (int b = Byte.MIN_VALUE; b < Byte.MAX_VALUE; b++)
            test[b - Byte.MIN_VALUE] = (byte) b;

        String expected = new String(test);
        String actual = new String(decode(encode(test)));
        System.out.println(getClass().getSimpleName() + " Pass: " + expected.equals(actual));
    }

    static void main(String[] args) {
        new BinCodec().test();
        new HexCodec().test();
        new Bin2Codec().test();
    }

}
