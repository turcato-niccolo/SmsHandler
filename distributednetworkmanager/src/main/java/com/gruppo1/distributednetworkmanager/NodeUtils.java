package com.gruppo1.distributednetworkmanager;

import com.dezen.riccardo.networkmanager.Resource;
import com.dezen.riccardo.smshandler.Peer;

import java.util.BitSet;

/**
 * @author niccoloturcato
 * Class used to quickly create BitSet binary key and PeerNodes from Peer and resources
 * Uses SHA-1 Hashing from class BitSetUtils
 */
class NodeUtils {
    private static String KEY_LENGTH_INVALID_MSG = "invalid key length, must be > 0 && <=160";
    private static String PEER_INVALID_MSG = "invalid or null Peer";
    private static String RESOURCE_INVALID_MSG = "invalid or null Resource";

    /**
     * @param peer      the peer for which generate the binary key
     * @param keyLength number of bits required for the generated Key (> 0 && <=160)
     * @return the binary key generated with SHA-1 hashing for the given peer
     * @throws IllegalArgumentException if key length or given peer are invalid or null
     */
    public static BitSet getIdForPeer(Peer<String> peer, int keyLength) {
        if (keyLength > 0 && keyLength <= 160) {
            if (peer != null && peer.isValid()) {
                return BitSetUtils.hash(peer.getAddress().getBytes(), keyLength);
            }
            throw new IllegalArgumentException(PEER_INVALID_MSG);
        }
        throw new IllegalArgumentException(KEY_LENGTH_INVALID_MSG);
    }

    /**
     * @param resource  the resource for which generate the binary key
     * @param keyLength number of bits required for the generated Key (> 0 && <=160)
     * @return the binary key generated with SHA-1 hashing from the given resource's name
     * @throws IllegalArgumentException if key length or given resource are invalid or null
     */
    public static BitSet getIdForResource(Resource<String, String> resource, int keyLength) {
        if (keyLength > 0 && keyLength <= 160) {
            if (resource != null && resource.isValid()) {
                return BitSetUtils.hash(resource.getName().getBytes(), keyLength);
            }
            throw new IllegalArgumentException(RESOURCE_INVALID_MSG);
        }
        throw new IllegalArgumentException(KEY_LENGTH_INVALID_MSG);
    }

    /**
     * @param peer      the peer for which generate the binary key
     * @param keyLength number of bits required for the generated Key (> 0 && <=160)
     * @return an instance of PeerNode with binary key generated with SHA-1 hashing from the given peer
     * @throws IllegalArgumentException if key length or given peer are invalid or null
     */
    public static PeerNode getNodeForPeer(Peer<String> peer, int keyLength) {
        if (keyLength > 0 && keyLength <= 160) {
            if (peer != null && peer.isValid()) {
                BinarySet set = new BinarySet(BitSetUtils.hash(peer.getAddress().getBytes(), keyLength));
                return new PeerNode(set);
            }
            throw new IllegalArgumentException(PEER_INVALID_MSG);
        }
        throw new IllegalArgumentException(KEY_LENGTH_INVALID_MSG);
    }
}
