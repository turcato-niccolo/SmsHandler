package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;
import com.dezen.riccardo.smshandler.exceptions.InvalidMessageException;

/***
 * Class defining an Action travelling through a Kademlia Network.
 * Syntax for a Message is as follows:
 * [ACTION TYPE] [OPERATION ID] [PART K] [MAX PARTS] [PAYLOAD TYPE] [PAYLOAD]
 * @author  Pardeep Kumar, Riccardo De Zen
 */

public class KadAction implements DistributedNetworkAction<String, SMSPeer, SMSMessage> {
    // The length of the Node_ID
    private static final int ID_LENGTH = 128;
    private static final String SEPARATOR = "\r";
    private static final int DEFAULT_PARTS = 1;
    private static final int DEFAULT_MAX_PARTS = 1;
    private static final int ACTION_TYPE_POSITION = 0, OPERATION_ID_POSITION = 1, CURRENT_MESSAGE_POSITION = 2,
            TOTAL_MESSAGES_POSITION = 3, PAYLOAD_TYPE_POSITION = 4, PAYLOAD_POSITION = 5;
    private static final int TOTAL_PARAMS = 6;

    private SMSPeer actionPeer;
    private ActionType actionType;
    private int operationId;
    private int currentPart;
    private int totalParts;
    private PayloadType payloadType;
    private String payload;

    private final String ACTION_CODE_NOT_FOUND_ERROR_MSG = "Expected ActionType as int number, found not parsable String instead";
    private final String ID_NOT_FOUND_ERROR_MSG = "Expected Id as int number, found not parsable String instead";
    private final String TOTAL_MESSAGES_NOT_FOUND_ERROR_MSG = "Expected totalMessages as int number, found not parsable String instead";
    private final String CURRENT_MESSAGE_NOT_FOUND_ERROR_MSG = "Expected currentMessage as int number, found not parsable String instead";
    private final String PAYLOAD_TYPE_NOT_FOUND_ERROR_MSG = "Expected payload as int number, found not parsable String instead";
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
         *
         * @param code the value for code field.
         */
        ActionType(int code) {
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
        public boolean isRequest() {
            return (code >= 0) && (code % 2 == 0);
        }

        /**
         * @return true if this is a Request Action type, false if it is a Response type
         */
        public boolean isResponse() {
            return (code >= 0) && (code % 2 != 0);
        }

        /**
         * @param code the code for the Action
         * @return the ActionType with the corresponding code, or INVALID if an invalid code is passed.
         */
        public static ActionType getTypeFromVal(int code) {
            for (ActionType type : ActionType.values()) {
                if (type.code == code) return type;
            }
            return INVALID;
        }
    }

    /**
     * Enum for type of content attached to the Action, if any
     */
    public enum PayloadType {

        INVALID(-1),
        IGNORED(0),
        BOOLEAN(1),
        PEER_ADDRESS(2),
        NODE_ID(3),
        RESOURCE(4);

        private final int code;

        /**
         * Constructor. Used only by enum.
         *
         * @param code the value for code field.
         */
        PayloadType(int code) {
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
        public static PayloadType getTypeFromCode(int code) {
            for (PayloadType type : PayloadType.values()) {
                if (type.code == code) return type;
            }
            return INVALID;
        }
    }

    /**
     *  Constructor of KadAction.
     *
     * @param actionType The type of the action.
     * @param id The id of the message.
     * @param part The number of the current message part.
     * @param maxParts The number of the total messages.
     * @param payloadType The type of the payload.
     * @param payload The value of the payload.
     * @throws IllegalArgumentException if the parameters are not valid.
     */
    public KadAction(@NonNull SMSPeer smsPeer, @NonNull ActionType actionType, int id, int part, int maxParts, @NonNull PayloadType payloadType, @NonNull String payload) {
        this.actionPeer = smsPeer;
        this.actionType = actionType;
        this.operationId = id;
        this.currentPart = part;
        this.totalParts = maxParts;
        this.payloadType = payloadType;
        this.payload = payload;
        if (!isValid())
            throw new IllegalArgumentException();
    }

    /**
     * Constructor of KadDction given SMSMessage.
     *
     * @param buildingMessage the given SMSMessage.
     * @throws IllegalArgumentException if the parameters are not valid.
     */
    public KadAction(@NonNull SMSMessage buildingMessage) {
        String messageBody = buildingMessage.getData();
        String[] parameteres = messageBody.split(SEPARATOR);
        actionPeer = buildingMessage.getPeer();
        if (parameteres.length == TOTAL_PARAMS) {
            try {
                actionType = ActionType.getTypeFromVal(Integer.parseInt(parameteres[ACTION_TYPE_POSITION]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + ACTION_CODE_NOT_FOUND_ERROR_MSG);
            }

            try {
                operationId = Integer.parseInt(parameteres[OPERATION_ID_POSITION]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + ID_NOT_FOUND_ERROR_MSG);
            }

            try {
                totalParts = Integer.parseInt(parameteres[TOTAL_MESSAGES_POSITION]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + TOTAL_MESSAGES_NOT_FOUND_ERROR_MSG);
            }
            try {
                currentPart = Integer.parseInt(parameteres[CURRENT_MESSAGE_POSITION]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + CURRENT_MESSAGE_NOT_FOUND_ERROR_MSG);
            }
            try {
                payloadType = PayloadType.getTypeFromCode(Integer.parseInt(parameteres[PAYLOAD_TYPE_POSITION]));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e.getMessage() + "\n" + PAYLOAD_TYPE_NOT_FOUND_ERROR_MSG);
            }
            payload = parameteres[PAYLOAD_POSITION];
            if (!isValid())
                throw new IllegalArgumentException();
        }
    }

    /**
     * Check if all the action's parameters are valid
     *
     * @return True if the defined action is valid and fits into a Message.
     */
    @Override
    public boolean isValid() {
        if (this.actionType.equals(ActionType.INVALID))
            return false;
        if (this.payloadType.equals(PayloadType.INVALID))
            return false;
        if (currentPart <= 0 || totalParts <= 0 || currentPart > totalParts)
            return false;
        if (!payloadMatchesType(payloadType, payload))
            return false;
        try {
            toMessage();
        } catch (InvalidMessageException e) {
            return false;
        }
        return true;
    }

    /**
     * Check if payload's content type matches with payloadType passed as parameter.
     *
     * @param payloadType The type of the payload.
     * @param payload The string which contains the value of the payload.
     * @return True if the payload string contains a value of the correct type,false otherwise.
     */
    public static boolean payloadMatchesType(PayloadType payloadType, String payload) {
        switch (payloadType) {
            case IGNORED:
            case BOOLEAN:
                return true;
            case NODE_ID:
                BitSet bitset=BitSetUtils.decodeHexString(payload);
                return bitset.length()==ID_LENGTH;
            case PEER_ADDRESS:
                return SMSPeer.isAddressValid(payload) == SMSPeer.PhoneNumberValidity.ADDRESS_VALID;
            case RESOURCE:
                try {
                    return StringResource.parseString(payload, SEPARATOR).isValid();
                }
                catch (IllegalArgumentException e){
                    return false;
                }
            default:
                return false;
        }

    }

    /**
     * Get a message with correct formatted action command ready to be sent
     *
     * @return A Message containing the formatted action command ready to be sent.
     */
    @Override
    public SMSMessage toMessage() {
        StringBuilder body = new StringBuilder();
        body.append(actionType.getCode()).append(SEPARATOR).append(operationId)
                .append(SEPARATOR).append(currentPart).append(SEPARATOR).append(totalParts)
                .append(payloadType.getCode()).append(SEPARATOR).append(payload);

        return new SMSMessage(actionPeer, body.toString());
    }

    /**
     * Get the action type from the enum.
     *
     * @return The type of the action.
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * Get the ID which identifies the action.
     *
     * @return The operation ID.
     */
    public int getOperationId() {
        return operationId;
    }
    /**
     * Get the k part of the current message.
     *
     * @return The currentPart k  of the message.
     */

    public int getCurrentPart() {
        return currentPart;
    }
    /**
     * Get the number of the total messages expected .
     *
     * @return The total number of messages.
     */

    public int getTotalParts() {
        return totalParts;
    }
    /**
     * Get the payload type from the enum.
     *
     * @return The type of the payload.
     */

    public PayloadType getPayloadType() {
        return payloadType;
    }

    /**
     * Get the SMSPeer which is sending or receiving the action message.
     *
     * @return SMSPeer the peer sending or receiving the action message.
     */
    @Override
    public SMSPeer getPeer() {
        return actionPeer;
    }

    /**
     * Get a string containing the value of the payload
     *
     * @return A string containing the value of the payload.
     */
    @Override
    public String getPayload() {
        return payload;
    }
}
