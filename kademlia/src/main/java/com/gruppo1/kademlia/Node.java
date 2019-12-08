package com.gruppo1.kademlia;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

/**
 * Key is generated with SHA-1 as cryptographic hash function which produces a hash of 160 bit.
 * @param <K> key
 * @param <V> value
 *
 * @author Giorgia Bortoletti
 */

public abstract class Node<K extends String, V extends String> {

    private String id;
    private String value;

    /**
     * Constructor
     */
    //TODO? control if id generated is unique
    public Node(String key, String value){

        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            id = new String(digest);
        }catch(NoSuchAlgorithmException e){}
        this.value = value;
    }
    
    /**
     * Returns a bitset containing the values in bytes.
     * @param bytes to convert in BitSet object
     */
    private static BitSet fromByteArray(byte[] bytes) {
      BitSet bitSet = new BitSet();
      for (int i = 0; i < bytes.length * 8; i++) {
        if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) {
        	bitSet.set(i);
        }
      }
      return bitSet;
    }
    
    /**
     * @return key
     */
    public String getID(){return id;}

    /**
     *
     * @return value
     */
    public String getValue(){return value;}



}
