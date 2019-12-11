package com.gruppo1.distributednetworkmanager;

import androidx.annotation.NonNull;

import com.dezen.riccardo.smshandler.SMSMessage;
import com.dezen.riccardo.smshandler.SMSPeer;

/***
 * @author  Pardeep Kumar
 */

public class DistributedNetworkAction extends NodeActionStructure<String> {

    private static final String SEPARATOR =  "\r";
    private int nodeId;
    private final int DEFAULT_VALUE_FOR_MESSAGES=1;
    private int currentMessage=DEFAULT_VALUE_FOR_MESSAGES;
    private int totalMessages=DEFAULT_VALUE_FOR_MESSAGES;
    private int actionCommand;
    private String param;
    private String extra;
    private String payload;
    private PeerNode currentPeerNode;
    private SMSPeer currentPeer;
    private static final int ID_POSITION = 0,CURRENT_MESSAGE_POSITION=1, TOTAL_MESSAGES_POSITION = 2, ACTION_POSITION = 3
            , PARAM_POSITION=4, EXTRA_POSITION=5, PAYLOAD_POSITION=6;
    private static final int TOTAL_PARAMS=7;


    private final String ACTION_CODE_NOT_FOUND_ERROR_MSG = "Expected ActionType as int number, found not parsable String instead";
    private final String ID_NOT_FOUND_ERROR_MSG = "Expected Id as int number, found not parsable String instead";
    private final String TOTAL_MESSAGES_NOT_FOUND_ERROR_MSG = "Expected totalMessages as int number, found not parsable String instead";
    private final String CURRENT_MESSAGE_NOT_FOUND_ERROR_MSG = "Expected currentMessage as int number, found not parsable String instead";
    private final String FORMATTED_ACTION_NOT_FOUND_ERROR_MSG = "This message does not contain a formatted DistributedNetworkAction";
    private final String NOT_FORMATTED_PARAMS = "Params can't build a formatted DistributedNetworkAction";

    public static final String DEFAULT_IGNORED = " ";

    public static class Type {

        /**
         * @param action the int value to be checked
         * @return true if the int matches an action, false if not
         */

        static private boolean isValid(int action) {

            return action >= MIN_ACTION && action <= MAX_ACTION;

        }


        /**
         * @param action the int value to be checked
         * @return true if the given action uses the "PARAM" part of the message
         */

        static private boolean usesParam(int action) {

            return isValid(action);

        }


        /**
         * @param action the int value to be checked
         * @return true if the given action uses the "EXTRA" part of message
         */

        static private boolean usesExtra(int action) {

            return action == FIND_VALUE || action==ANSWER_FIND_VALUE;

        }

        /**
         * @param action the int value to be checked
         * @return true if the given action uses the "PAYLOAD" part of message
         */

        static private boolean usesPayload(int action) {

            return action == FIND_VALUE || action==ANSWER_FIND_VALUE || action==STORE;

        }

        static private final int MIN_ACTION = 0;
        static private final int MAX_ACTION = 9;
        //<#>[ID] [k/N] [ACTION] [PARAM] [EXTRA] [PAYLOAD]

        //<#>[ID] [1/1] PING [NODE_ID] [IGNORED] [IGNORED]
        static final int PING = 0;
        //<#>[ID] [1/1] <#>ANSWER_PING [NODE_ID] [IGNORED] [IGNORED]
        static final int ANSWER_PING = 1;
        //<#>[ID] [k/N] STORE [NODE_ID] [IGNORED] [VALUE]
        static final int STORE = 2;
        //<#>[ID] [k/N] ANSWER_STORE [NODE_ID] [IGNORED] [IGNORED]
        static final int ANSWER_STORE = 3;
        //<#>[ID] [k/N] FIND_NODE [NODE_ID] [IGNORED] [IGNORED]
        static final int FIND_NODE = 4;
        //<#>[ID] [k/N] ANSWER_FIND_NODE [NODE_ID] [IGNORED] [PHONENUMBER]
        static final int ANSWER_FIND_NODE = 5;
        //<#>[ID] [k/N] FIND_VALUE [NODE_ID] [YES_RESOURCE] [IGNORED]
        static final int FIND_VALUE = 6;
        //<#>[ID] [k/N] ANSWER_FIND_VALUE [NODE_ID] [YES_RESOURCE] [VALUE]
        static final int ANSWER_FIND_VALUE = 7;
        //<#>[ID] [k/N] INVITE [NODE_ID] [IGNORED] [IGNORED]
        static final int INVITE = 8;
        //<#>[ID] [k/N] ANSWER_INVITE [NODE_ID] [IGNORED] [IGNORED]
        static final int ANSWER_INVITE = 9;
    }

    public DistributedNetworkAction setNodeId(int nodeId)
    {
        this.nodeId=nodeId;
        return this;
    }
    public DistributedNetworkAction setTotalMessages(int totalMessages)
    {
        this.totalMessages=totalMessages;
        return this;
    }
    public DistributedNetworkAction setCurrentMessage(int currentMessage)
    {
        this.currentMessage=currentMessage;
        return this;
    }

    public DistributedNetworkAction(int actionCommand, @NonNull String param, @NonNull String extra,@NonNull String payload){

        if(areValidParameters(actionCommand, param, extra,payload )){

            this.actionCommand=actionCommand;

            this.param=param;

            this.extra=extra;

            this.payload=extra;

        }
        else throw new IllegalArgumentException(NOT_FORMATTED_PARAMS);

    }
    public DistributedNetworkAction(@NonNull SMSMessage buildingMessage){

        String messageBody = buildingMessage.getData();

        String[] parameteres = messageBody.split(SEPARATOR);

        if(buildingMessage.getPeer() != null && buildingMessage.getPeer().isValid())

            currentPeer = buildingMessage.getPeer();

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

                actionCommand = actionType;

                param = parameteres[PARAM_POSITION];

                extra = parameteres[EXTRA_POSITION];

            }

        }

        else throw new IllegalArgumentException(FORMATTED_ACTION_NOT_FOUND_ERROR_MSG);

    }

    public boolean areValidParameters(int actionType, String param, String extra,String payload)
    {
     return Type.isValid(actionType) && (param.equals(DEFAULT_IGNORED) ^ Type.usesParam(actionType)) &&
        (extra.equals(DEFAULT_IGNORED) ^ Type.usesParam(actionType))&&(payload.equals(DEFAULT_IGNORED) ^ Type.usesParam(actionType));
    }
    @Override
    public boolean isValid() {
        return currentPeerNode !=null && (
                ((actionCommand == Type.INVITE || actionCommand == Type.ANSWER_INVITE || actionCommand==Type.PING ||
                        actionCommand==Type.ANSWER_PING || actionCommand==Type.ANSWER_STORE || actionCommand==Type.FIND_NODE)
                        && extra.equals(DEFAULT_IGNORED) && payload.equals(DEFAULT_IGNORED)&& !param.equals(DEFAULT_IGNORED))

                || ((actionCommand==Type.FIND_VALUE || actionCommand==Type.ANSWER_FIND_VALUE ) &&
                        payload.equals(DEFAULT_IGNORED) && !extra.equals(DEFAULT_IGNORED) &&!extra.equals(DEFAULT_IGNORED))

                || ((actionCommand==Type.ANSWER_FIND_VALUE) && !extra.equals(DEFAULT_IGNORED)  &&
                        !payload.equals(DEFAULT_IGNORED) &&!param.equals(DEFAULT_IGNORED))

                || ((actionCommand==Type.ANSWER_FIND_NODE) && !param.equals(DEFAULT_IGNORED) &&
                        !payload.equals(DEFAULT_IGNORED) && extra.equals(DEFAULT_IGNORED))

                );
    }

    @Override
    public SMSMessage generateMessage() {
        if (isValid()){
            String body=nodeId+SEPARATOR+currentMessage+SEPARATOR+totalMessages+SEPARATOR+
                    actionCommand+SEPARATOR+param+SEPARATOR+extra+SEPARATOR+payload;
            return new SMSMessage(currentPeer, body);
        }
        return null;
    }

    @Override
    public String getActionID() {
        return Integer.toString(nodeId);
    }

    @Override
    public int getAction() {
        return actionCommand;
    }

    @Override
    public int getProgress() {
        return currentMessage;
    }

    @Override
    public int getTotalExpectedMessages() {
        return totalMessages;
    }

    @Override
    public String getParam() {
        return param;
    }

    @Override
    public String getExtra() {
        return extra;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public void setDestination(@NonNull Node node) {

    }

    @Override
    public Node getDestination() {
        return null;
    }

    @Override
    public Node getSender() {
        return null;
    }
}
