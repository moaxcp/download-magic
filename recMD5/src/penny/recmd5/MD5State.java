package penny.recmd5;

import java.io.Serializable;
import java.util.Arrays;

/**
 * recoverable md5
 * <p>
 * This library is a rewrite of the fast-md5 implementation in java. The goal of
 * this library is to make the MD5 state recoverable for cases where the programmer
 * does not want to recalculate an MD5 for partial hashes of files or streams.
 * It is not meant to be fast and does not use the native code, although, it
 * would be easy for a programmer to implement the native code.
 * <p>
 * Fast implementation of RSA's MD5 hash generator in Java JDK Beta-2 or higher<br>
 * Originally written by Santeri Paavolainen, Helsinki Finland 1996 <br>
 * (c) Santeri Paavolainen, Helsinki Finland 1996 <br>
 * Some changes Copyright (c) 2002 Timothy W Macinta <br>
 * Updates to use MessageDigest abstraction and to allow the internal state to
 * be recoverable in case of crash 2011 John Mercier<br>
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
 * <p>
 * See http://www.twmacinta.com/myjava/fast_md5.php for more information
 * on this file.
 * <p>
 * Contains internal state of the MD5MessageDigest and the final state of the
 * MD5MessageDigest. Also holds the final string of the MD5 hash for convienence.
 * <p>
 * "It is my hope that this library will be useful to programmers calculating
 * md5s for input from ranged protocols such as http and ftp." - John Mercier
 *
 * @author	Santeri Paavolainen <sjpaavol@cc.helsinki.fi>
 * @author	Timothy W Macinta (twm@alum.mit.edu) (optimizations and bug fixes)
 * @author      John Mercier <moaxcp@gmail.com>
 **/
public class MD5State implements Serializable {

    /**
     * 128-bit state
     */
    protected int state[];

    protected int finalState[];

    /**
     * 64-bit character count
     */
    protected long count;

    protected long finalCount;

    /**
     * 64-byte buffer (512 bits) for storing to-be-hashed characters
     */
    protected byte buffer[];

    protected byte finalBuffer[];

    private StringBuilder hash;

    /**
     * creates a default MD5State
     */
    public MD5State() {
        buffer = new byte[64];
        finalBuffer = new byte[64];
        count = 0;
        finalCount = 0;
        state = new int[4];
        finalState = new int[4];

        state[0] = 0x67452301;
        state[1] = 0xefcdab89;
        state[2] = 0x98badcfe;
        state[3] = 0x10325476;

        hash = new StringBuilder();

    }

    public void copy(MD5State md5State) {
        count = md5State.count;
        finalCount = md5State.finalCount;
        hash.delete(0, hash.length());
        hash.append(md5State.hash);
        for(int i = 0; i < buffer.length; i++) {
            buffer[i] = md5State.buffer[i];
        }
        for(int i = 0; i < state.length; i++) {
            state[i] = md5State.state[i];
        }
        for(int i = 0; i < finalBuffer.length; i++) {
            finalBuffer[i] = md5State.finalBuffer[i];
        }
        for(int i = 0; i < finalState.length; i++) {
            finalState[i] = md5State.finalState[i];
        }
    }

    /**
     * copies the state to the final variables.
     */
    void copyToFinal() {
        if(finalState == null) {
            finalState = new int[4];
        }
        System.arraycopy(buffer, 0, finalBuffer, 0, finalBuffer.length);
        System.arraycopy(state, 0, finalState, 0, finalState.length);
        finalCount = count;
    }

    /**
     * returns the String representation of the hash. Users must use digest from
     * MD5MessageDigest for this to be accurate.
     */
    @Override
    public String toString() {
        return hash.toString();
    }

    /**
     * @param hash the hash to set
     */
    protected StringBuilder getHash() {
        return hash;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Arrays.hashCode(this.state);
        hash = 89 * hash + (int) (this.count ^ (this.count >>> 32));
        hash = 89 * hash + Arrays.hashCode(this.buffer);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof MD5State) {
            MD5State s = (MD5State) o;
            return Arrays.equals(this.buffer, s.buffer) && Arrays.equals(this.state, s.state) && this.count == s.count;
        }
        return false;
    }
}
