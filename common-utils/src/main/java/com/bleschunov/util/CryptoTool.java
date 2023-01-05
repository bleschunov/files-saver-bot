package com.bleschunov.util;

import org.hashids.Hashids;

/**
 * @author Bleschunov Dmitry
 */
public class CryptoTool {
    private final Hashids hashids;

    public CryptoTool(String salt) {
        int minHashLength = 10;
        this.hashids = new Hashids(salt, minHashLength);
    }

    public String getHashOf(long id) {
        return hashids.encode(id);
    }

    public Long getIdOf(String hash) {
        long[] res = hashids.decode(hash);
        if (res != null && res.length > 0) {
            return res[0];
        }
        return null;
    }
}
