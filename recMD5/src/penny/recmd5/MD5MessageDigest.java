package penny.recmd5;

import java.security.MessageDigest;

/**
 * recoverable md5
 * <p>
 * This library is a rewrite of the fast-md5 implementation in java. The goal of
 * this library is to make the MD5 state recoverable for cases where the programmer
 * does not want to recalculate an MD5 for partial hashes of files or streams.
 * It is not meant to be fast and does not use the native code, although, it
 * would be easy for a programmer to implement the native code.
 * <p>
 * Fast implementation of RSA's MD5 hash generator in Java JDK Beta-2 or higher.
 * <p>
 * Originally written by Santeri Paavolainen, Helsinki Finland 1996.<br>
 * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
 * Many changes Copyright (c) 2002 - 2010 Timothy W Macinta<br>
 * Updates to use MessageDigest abstraction and to allow the internal state to
 * be recoverable 2011 John Mercier<br>
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *<p>
 * MD5MessageDigest is not made to be fast and therefore does not use the native
 * libraries. It does, however, use the original non-native implementation of
 * fast-md5, so it should be fairly fast. The idea behind this Digest is to allow
 * users to stop a Digest, save the state, and restart the Digest from the same
 * point it was stopped. The key is to use getState(), save the state and
 * later pass that state in a MD5MessageDigest constructor to reuse it.
 * <p>
 * Another goal that was in the original implementation is to allow a Digest have
 * a running display of the current updates. To do this use digest() and get the
 * hash string from the MD5State. The hash string will not be current unless
 * digest is called first.
 * <p>
 * Future developers are welcome to submit ideas, errors, or requests to
 * <moaxcp@gmail.com>
 * <p>
 * "It is my hope that this library will be useful to programmers calculating
 * md5s for input from ranged/recoverable protocols such as http and ftp."
 * - John Mercier
 *
 * @author Santeri Paavolainen <sjpaavol@cc.helsinki.fi>
 * @author Timothy W Macinta (twm@alum.mit.edu) (optimizations and bug fixes)
 * @author John Mercier <moaxcp@gmail.com>
 */
public class MD5MessageDigest extends MessageDigest {

    private MD5State state;

    private static byte padding[] = {
        (byte) 0x80, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    private static final char[] HEX_CHARS = {'0', '1', '2', '3',
        '4', '5', '6', '7',
        '8', '9', 'a', 'b',
        'c', 'd', 'e', 'f',};

    /**
     * creates a MessageDigest with the name "md5"
     */
    public MD5MessageDigest() {
        super("md5");
        state = new MD5State();
    }
    /**
     * creates a MessageDigest with the name "md5" and uses the state passed.
     * @param state The state of this MessageDigest
     */
    public MD5MessageDigest(MD5State state) {
        super("md5");
        this.state = state;
    }

    protected byte[] encode(int input[], int len) {
        int i, j;
        byte out[];

        out = new byte[len];

        for (i = j = 0; j < len; i++, j += 4) {
            out[j] = (byte) (input[i] & 0xff);
            out[j + 1] = (byte) ((input[i] >>> 8) & 0xff);
            out[j + 2] = (byte) ((input[i] >>> 16) & 0xff);
            out[j + 3] = (byte) ((input[i] >>> 24) & 0xff);
        }

        return out;
    }

    protected void decode(byte buffer[], int shift, int[] out) {
        /*len += shift;
        for (int i = 0; shift < len; i++, shift += 4) {
        out[i] = ((int) (buffer[shift] & 0xff)) |
        (((int) (buffer[shift + 1] & 0xff)) << 8) |
        (((int) (buffer[shift + 2] & 0xff)) << 16) |
        (((int)  buffer[shift + 3]) << 24);
        }*/

        // unrolled loop (original loop shown above)

        out[0] = ((int) (buffer[shift] & 0xff))
                | (((int) (buffer[shift + 1] & 0xff)) << 8)
                | (((int) (buffer[shift + 2] & 0xff)) << 16)
                | (((int) buffer[shift + 3]) << 24);
        out[1] = ((int) (buffer[shift + 4] & 0xff))
                | (((int) (buffer[shift + 5] & 0xff)) << 8)
                | (((int) (buffer[shift + 6] & 0xff)) << 16)
                | (((int) buffer[shift + 7]) << 24);
        out[2] = ((int) (buffer[shift + 8] & 0xff))
                | (((int) (buffer[shift + 9] & 0xff)) << 8)
                | (((int) (buffer[shift + 10] & 0xff)) << 16)
                | (((int) buffer[shift + 11]) << 24);
        out[3] = ((int) (buffer[shift + 12] & 0xff))
                | (((int) (buffer[shift + 13] & 0xff)) << 8)
                | (((int) (buffer[shift + 14] & 0xff)) << 16)
                | (((int) buffer[shift + 15]) << 24);
        out[4] = ((int) (buffer[shift + 16] & 0xff))
                | (((int) (buffer[shift + 17] & 0xff)) << 8)
                | (((int) (buffer[shift + 18] & 0xff)) << 16)
                | (((int) buffer[shift + 19]) << 24);
        out[5] = ((int) (buffer[shift + 20] & 0xff))
                | (((int) (buffer[shift + 21] & 0xff)) << 8)
                | (((int) (buffer[shift + 22] & 0xff)) << 16)
                | (((int) buffer[shift + 23]) << 24);
        out[6] = ((int) (buffer[shift + 24] & 0xff))
                | (((int) (buffer[shift + 25] & 0xff)) << 8)
                | (((int) (buffer[shift + 26] & 0xff)) << 16)
                | (((int) buffer[shift + 27]) << 24);
        out[7] = ((int) (buffer[shift + 28] & 0xff))
                | (((int) (buffer[shift + 29] & 0xff)) << 8)
                | (((int) (buffer[shift + 30] & 0xff)) << 16)
                | (((int) buffer[shift + 31]) << 24);
        out[8] = ((int) (buffer[shift + 32] & 0xff))
                | (((int) (buffer[shift + 33] & 0xff)) << 8)
                | (((int) (buffer[shift + 34] & 0xff)) << 16)
                | (((int) buffer[shift + 35]) << 24);
        out[9] = ((int) (buffer[shift + 36] & 0xff))
                | (((int) (buffer[shift + 37] & 0xff)) << 8)
                | (((int) (buffer[shift + 38] & 0xff)) << 16)
                | (((int) buffer[shift + 39]) << 24);
        out[10] = ((int) (buffer[shift + 40] & 0xff))
                | (((int) (buffer[shift + 41] & 0xff)) << 8)
                | (((int) (buffer[shift + 42] & 0xff)) << 16)
                | (((int) buffer[shift + 43]) << 24);
        out[11] = ((int) (buffer[shift + 44] & 0xff))
                | (((int) (buffer[shift + 45] & 0xff)) << 8)
                | (((int) (buffer[shift + 46] & 0xff)) << 16)
                | (((int) buffer[shift + 47]) << 24);
        out[12] = ((int) (buffer[shift + 48] & 0xff))
                | (((int) (buffer[shift + 49] & 0xff)) << 8)
                | (((int) (buffer[shift + 50] & 0xff)) << 16)
                | (((int) buffer[shift + 51]) << 24);
        out[13] = ((int) (buffer[shift + 52] & 0xff))
                | (((int) (buffer[shift + 53] & 0xff)) << 8)
                | (((int) (buffer[shift + 54] & 0xff)) << 16)
                | (((int) buffer[shift + 55]) << 24);
        out[14] = ((int) (buffer[shift + 56] & 0xff))
                | (((int) (buffer[shift + 57] & 0xff)) << 8)
                | (((int) (buffer[shift + 58] & 0xff)) << 16)
                | (((int) buffer[shift + 59]) << 24);
        out[15] = ((int) (buffer[shift + 60] & 0xff))
                | (((int) (buffer[shift + 61] & 0xff)) << 8)
                | (((int) (buffer[shift + 62] & 0xff)) << 16)
                | (((int) buffer[shift + 63]) << 24);
    }

    protected void transform(int[] state, byte buffer[], int shift, int[] decode_buf) {
        int a = state[0],
                b = state[1],
                c = state[2],
                d = state[3],
                x[] = decode_buf;

        decode(buffer, shift, decode_buf);

        /* Round 1 */
        a += ((b & c) | (~b & d)) + x[ 0] + 0xd76aa478; /* 1 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[ 1] + 0xe8c7b756; /* 2 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[ 2] + 0x242070db; /* 3 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[ 3] + 0xc1bdceee; /* 4 */
        b = ((b << 22) | (b >>> 10)) + c;

        a += ((b & c) | (~b & d)) + x[ 4] + 0xf57c0faf; /* 5 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[ 5] + 0x4787c62a; /* 6 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[ 6] + 0xa8304613; /* 7 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[ 7] + 0xfd469501; /* 8 */
        b = ((b << 22) | (b >>> 10)) + c;

        a += ((b & c) | (~b & d)) + x[ 8] + 0x698098d8; /* 9 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[ 9] + 0x8b44f7af; /* 10 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[10] + 0xffff5bb1; /* 11 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[11] + 0x895cd7be; /* 12 */
        b = ((b << 22) | (b >>> 10)) + c;

        a += ((b & c) | (~b & d)) + x[12] + 0x6b901122; /* 13 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[13] + 0xfd987193; /* 14 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[14] + 0xa679438e; /* 15 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[15] + 0x49b40821; /* 16 */
        b = ((b << 22) | (b >>> 10)) + c;


        /* Round 2 */
        a += ((b & d) | (c & ~d)) + x[ 1] + 0xf61e2562; /* 17 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[ 6] + 0xc040b340; /* 18 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[11] + 0x265e5a51; /* 19 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[ 0] + 0xe9b6c7aa; /* 20 */
        b = ((b << 20) | (b >>> 12)) + c;

        a += ((b & d) | (c & ~d)) + x[ 5] + 0xd62f105d; /* 21 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[10] + 0x02441453; /* 22 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[15] + 0xd8a1e681; /* 23 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[ 4] + 0xe7d3fbc8; /* 24 */
        b = ((b << 20) | (b >>> 12)) + c;

        a += ((b & d) | (c & ~d)) + x[ 9] + 0x21e1cde6; /* 25 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[14] + 0xc33707d6; /* 26 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[ 3] + 0xf4d50d87; /* 27 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[ 8] + 0x455a14ed; /* 28 */
        b = ((b << 20) | (b >>> 12)) + c;

        a += ((b & d) | (c & ~d)) + x[13] + 0xa9e3e905; /* 29 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[ 2] + 0xfcefa3f8; /* 30 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[ 7] + 0x676f02d9; /* 31 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[12] + 0x8d2a4c8a; /* 32 */
        b = ((b << 20) | (b >>> 12)) + c;


        /* Round 3 */
        a += (b ^ c ^ d) + x[ 5] + 0xfffa3942;      /* 33 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[ 8] + 0x8771f681;      /* 34 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[11] + 0x6d9d6122;      /* 35 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[14] + 0xfde5380c;      /* 36 */
        b = ((b << 23) | (b >>> 9)) + c;

        a += (b ^ c ^ d) + x[ 1] + 0xa4beea44;      /* 37 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[ 4] + 0x4bdecfa9;      /* 38 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[ 7] + 0xf6bb4b60;      /* 39 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[10] + 0xbebfbc70;      /* 40 */
        b = ((b << 23) | (b >>> 9)) + c;

        a += (b ^ c ^ d) + x[13] + 0x289b7ec6;      /* 41 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[ 0] + 0xeaa127fa;      /* 42 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[ 3] + 0xd4ef3085;      /* 43 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[ 6] + 0x04881d05;      /* 44 */
        b = ((b << 23) | (b >>> 9)) + c;

        a += (b ^ c ^ d) + x[ 9] + 0xd9d4d039;      /* 33 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[12] + 0xe6db99e5;      /* 34 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[15] + 0x1fa27cf8;      /* 35 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[ 2] + 0xc4ac5665;      /* 36 */
        b = ((b << 23) | (b >>> 9)) + c;


        /* Round 4 */
        a += (c ^ (b | ~d)) + x[ 0] + 0xf4292244; /* 49 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[ 7] + 0x432aff97; /* 50 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[14] + 0xab9423a7; /* 51 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[ 5] + 0xfc93a039; /* 52 */
        b = ((b << 21) | (b >>> 11)) + c;

        a += (c ^ (b | ~d)) + x[12] + 0x655b59c3; /* 53 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[ 3] + 0x8f0ccc92; /* 54 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[10] + 0xffeff47d; /* 55 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[ 1] + 0x85845dd1; /* 56 */
        b = ((b << 21) | (b >>> 11)) + c;

        a += (c ^ (b | ~d)) + x[ 8] + 0x6fa87e4f; /* 57 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[15] + 0xfe2ce6e0; /* 58 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[ 6] + 0xa3014314; /* 59 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[13] + 0x4e0811a1; /* 60 */
        b = ((b << 21) | (b >>> 11)) + c;

        a += (c ^ (b | ~d)) + x[ 4] + 0xf7537e82; /* 61 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[11] + 0xbd3af235; /* 62 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[ 2] + 0x2ad7d2bb; /* 63 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[ 9] + 0xeb86d391; /* 64 */
        b = ((b << 21) | (b >>> 11)) + c;

        state[0] += a;
        state[1] += b;
        state[2] += c;
        state[3] += d;
    }

    protected void updateFinal(byte[] buffer, int offset, int length) {
        int index, partlen, i, start;
        //finals = null;

        /* Length can be told to be shorter, but not inter */
        if ((length - offset) > state.finalBuffer.length) {
            length = state.finalBuffer.length - offset;
        }

        /* compute number of bytes mod 64 */

        index = (int) (state.finalCount & 0x3f);
        state.finalCount += length;

        partlen = 64 - index;

        if (length >= partlen) {
            int[] decode_buf = new int[16];
            if (partlen == 64) {
                partlen = 0;
            } else {
                for (i = 0; i < partlen; i++) {
                    state.finalBuffer[i + index] = buffer[i + offset];
                }
                transform(state.finalState, state.finalBuffer, 0, decode_buf);
            }
            for (i = partlen; (i + 63) < length; i += 64) {
                transform(state.finalState, buffer, i + offset, decode_buf);
            }
            index = 0;
        } else {
            i = 0;
        }

        /* buffer remaining input */
        if (i < length) {
            start = i;
            for (; i < length; i++) {
                state.finalBuffer[index + i - start] = buffer[i + offset];
            }
        }
    }

    protected void updateString(byte[] hash) {
        char buf[] = new char[hash.length * 2];
        for (int i = 0, x = 0; i < hash.length; i++) {
            buf[x++] = HEX_CHARS[(hash[i] >>> 4) & 0xf];
            buf[x++] = HEX_CHARS[hash[i] & 0xf];
        }
        StringBuilder hashString = state.getHash();
        hashString.delete(0, hashString.length());
        hashString.append(buf);
    }

    @Override
    protected void engineUpdate(byte input) {
        byte buffer[] = new byte[1];
        buffer[0] = input;

        engineUpdate(buffer, 0, 1);
    }

    @Override
    protected void engineUpdate(byte[] buffer, int offset, int length) {
        int index, partlen, i, start;

        /* Length can be told to be shorter, but not inter */
        if ((length - offset) > buffer.length) {
            length = buffer.length - offset;
        }

        /* compute number of bytes mod 64 */

        index = (int) (state.count & 0x3f);
        state.count += length;

        partlen = 64 - index;

        if (length >= partlen) {
            int[] decode_buf = new int[16];
            if (partlen == 64) {
                partlen = 0;
            } else {
                for (i = 0; i < partlen; i++) {
                    state.buffer[i + index] = buffer[i + offset];
                }
                transform(state.state, state.buffer, 0, decode_buf);
            }
            for (i = partlen; (i + 63) < length; i += 64) {
                transform(state.state, buffer, i + offset, decode_buf);
            }
            index = 0;
        } else {
            i = 0;
        }

        /* buffer remaining input */
        if (i < length) {
            start = i;
            for (; i < length; i++) {
                state.buffer[index + i - start] = buffer[i + offset];
            }
        }
    }

    @Override
    protected byte[] engineDigest() {
        byte bits[];
        int index, padlen;
        state.copyToFinal();

        int[] count_ints = {(int) (state.finalCount << 3), (int) (state.finalCount >> 29)};
        bits = encode(count_ints, 8);

        index = (int) (state.finalCount & 0x3f);
        padlen = (index < 56) ? (56 - index) : (120 - index);

        updateFinal(padding, 0, padlen);
        updateFinal(bits, 0, 8);

        byte[] hash = encode(state.finalState, 16);
        updateString(hash);
        return hash;
    }

    @Override
    protected void engineReset() {
        state = new MD5State();
    }

    /**
     * returns the state of the Digest for use in the future.
     * @return the state
     */
    public MD5State getState() {
        return state;
    }
}
