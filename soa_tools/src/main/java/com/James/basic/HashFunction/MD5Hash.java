package com.James.basic.HashFunction;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/**
 * Created by James on 16/6/2.
 */
public class MD5Hash implements iHashFunction {
  public ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<MessageDigest>();

  @Override
  public long hash(String key, Charset charset) throws UnsupportedEncodingException {
    return hash(key.getBytes(charset));
  }

  @Override
  public long hash(byte[] key) {
    try {
      if (md5Holder.get() == null) {
        md5Holder.set(MessageDigest.getInstance("MD5"));
      }
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("++++ no md5 algorythm found");
    }
    MessageDigest md5 = md5Holder.get();

    md5.reset();
    md5.update(key);
    byte[] bKey = md5.digest();
    return ((long) (bKey[3] & 0xFF) << 24) | ((long) (bKey[2] & 0xFF) << 16) | ((long) (bKey[1] & 0xFF) << 8) | (long) (bKey[0] & 0xFF);
  }
}
