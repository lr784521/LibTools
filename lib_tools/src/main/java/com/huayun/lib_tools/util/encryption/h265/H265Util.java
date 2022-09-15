package com.huayun.lib_tools.util.encryption.h265;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class H265Util {

    /**
     * sha256加密2
     *
     * @param value
     * @param key
     */
    public static String hmac_sha256(String value, String key) {
        try {
            // Get an hmac_sha256 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

            // Get an hmac_sha256 Mac instance and initialize with the signing
            // key
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            String hexBytes = byte2hex(rawHmac);
            return hexBytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static String byte2hex(final byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0xFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }
}
