package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.networkmanager.StringResource;
import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

import java.util.BitSet;

/***
 * Class defining an Action travelling through a Kademlia Network.
 * Syntax for a Message is as follows:
 * [ACTION TYPE] [OPERATION ID] [PART K] [MAX PARTS] [PAYLOAD TYPE] [PAYLOAD]
 * @author  Pardeep Kumar, Riccardo De Zen
 */

public class KadAction implements DistributedNetworkAction<String, SMSPeer, SMSMessage> {

    private static final String SEPARATOR =  "\r";
    private static final int DEFAULT_PARTS = 1;
    private static final int DEFAULT_MAX_PARTS = 1;

    private SMSPeer actionPeer;
    private ActionType actionType;
    private int operationId;
    private int currentPart;
    private int totalParts;
    private PayloadType payloadType;
    //Possible payload contents
    private SMSPeer carriedPeer;
    private BitSet carriedNodeId;
    private StringResource carriedResource;

    private static final int ID_POSITION = 0,CURRENT_MESSAGE_POSITION=1, TOTAL_MESSAGES_POSITION = 2, ACTION_POSITION = 3
            , PARAM_POSITION=4, EXTRA_POSITION=5, PAYLOAD_POSITION=6;
    private static final int TOTAL_PARAMS=7;


    private final String ACTION_CODE_NOT_FOUND_ERROR_MSG = "Expected ActionType as int number, found not parsable String instead";
    private final String ID_NOT_FOUND_ERROR_MSG = "Expected Id as int number, found not parsable String instead";
    private final String TOTAL_MESSAGES_NOT_FOUND_ERROR_MSG = "Expected totalMessages as int number, found not parsable String instead";
    private final String CURRENT_MESSAGE_NOT_FOUND_ERROR_MSG = "Expected currentMessage as int number, found not parsable String instead";
    private final String FORMATTED_ACTION_NOT_FOUND_ERROR_MSG = "This message does not contain a formatted KadAction";
    private final String NOT_FORMATTED_PARAMS = "Params can't build a formatted KadAction";

    public static final String DEFAULT_IGNORED = " ";

    /**
     * Enum for Action type
     */
    public enum ActionType {
        INVALID(-1),
        //Codes should be even for Requests, odd for Responses
        PING(0),
        PING_ANSWER(1),
        INVITE(2),
        INVITE_ANSWER(3),
        STORE(4),
        STORE_ANSWER(5),
        FIND_NODE(6),
        FIND_NODE_ANSWER(7),
        FIND_VALUE(8),
        FIND_VALUE_ANSWER(9);

        private final int code;

        /**
         * Constructor. Used only by enum.
         * @param code the value for code field.
         */
        ActionType(int code){
            this.code = code;
        }

        /**
         * @return the numerical value for the instance
         */
        public int getCode() {
            return code;
        }

        /**
         * @return true if this is a Request Action type, false if it is a Response type
         */
        public boolean isRequest(){
            return (code >= 0) && (code % 2 == 0);
        }

        /**
         * @return true if this is a Request Action type, false if it is a Response type
         */
        public boolean isResponse(){
            return (code >= 0) && (code % 2 != 0);
        }

        /**
         * @param code the code for the Action
         * @return the ActionType with the corresponding code, or INVALID if an invalid code is passed.
         */
        public static ActionType getTypeFromVal(int code){
            for(ActionType type : ActionType.values()){
                if(type.code == code) return type;
            }
            return INVALID;
        }
    }

    /**
     * Enum for type of content attached to the Action, if any
     */
    public enum PayloadType {

        INVALID(-1),
        EMPTY(0),
        PEER_ADDRESS(1),
        NODE_ID(2),
        RESOURCE(3);

        private final int code;

        /**
         * Constructor. Used only by enum.
         * @param code the value for code field.
         */
        PayloadType(int code){
            this.code = code;
        }

        /**
         * @return the numerical value for the instance.
         */
        public int getCode() {
            return code;
        }

        /**
         * @param code the code for the Action
         * @return the ActionType with the corresponding code, or invalid if an invalid code is passed.
         */
        public static PayloadType getTypeFromCode(int code){
            for(PayloadType type : PayloadType.values()){
                if(type.code == code) return type;
            }
            return INVALID;
        }
    }

    /**
     * Private constructor. Parameters should already come in valid.
     * @param actionType
     * @param id
     * @param part
     * @param maxParts
     * @param payload
     */
    private KadAction(@NonNull ActionType actionType, int id, int part, int maxParts, @NonNull String payload){
        //TODO assign fields
    }
    public KadAction(@NonNull SMSMessage buildingMessage){
        String messageBody = buildingMessage.getData();
        String[] parameteres = messageBody.split(SEPARATOR);
        if(buildingMessage.getPeer() != null && buildingMessage.getPeer().isValid())
            actionPeer = buildingMessage.getPeer();
        if(parameteres.length == TOTAL_PARAMS){
            int actionType;
            try {
                actionType = Integer.parseInt(parameteres[ACTION_POSITION]);
            }
            catch (NumberFormatException e){
                throw new IllegalArgumentException(e.getMessage() + "\n" + ACTION_CODE_NOT_FOUND_ERROR_MSG);
            }
            int nodeId;
            try {
                nodeId = Integer.parseInt(parameteres[ID_POSITION]);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + ID_NOT_FOUND_ERROR_MSG);
            }
            int totalMessages;
            try {
                totalMessages = Integer.parseInt(parameteres[TOTAL_MESSAGES_POSITION]);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + TOTAL_MESSAGES_NOT_FOUND_ERROR_MSG);
            }
            int currentMessage;
            try {
                currentMessage = Integer.parseInt(parameteres[CURRENT_MESSAGE_POSITION]);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + CURRENT_MESSAGE_NOT_FOUND_ERROR_MSG);
            }
            if (areValidParameters(actionType, parameteres[PARAM_POSITION], parameteres[EXTRA_POSITION],parameteres [PAYLOAD_POSITION])){
                 //= actionType;
                 //TODO
            }
        }
        else throw new IllegalArgumentException(FORMATTED_ACTION_NOT_FOUND_ERROR_MSG);
    }

    @Override
    public boolean isValid() {
        return false;
    }

    public boolean areValidParameters(int i, String s1, String s2, String s3){
        return false;
    }

    @Override
    public SMSMessage toMessage() {
        if (isValid()){
            //TODO
        }
        return null;
    }

    public int getActionID() {
        return operationId;
    }

    public ActionType getAction() {
        return actionType;
    }

    public int getProgress() {
        return currentPart;
    }

    public int getTotalExpectedMessages() {
        return totalParts;
    }

    @Override
    public String getPayload() {
        //TODO
        return "";
    }

    @Override
    public SMSPeer getPeer() {
        return actionPeer;
    }
}
