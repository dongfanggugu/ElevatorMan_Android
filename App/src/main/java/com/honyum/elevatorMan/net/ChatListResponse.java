package com.honyum.elevatorMan.net;


import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseHead;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class ChatListResponse extends Response {

    private ResponseHead head;

    private List<ChatListBody> body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public List<ChatListBody> getBody() {
        return body;
    }

    public void setBody(List<ChatListBody> body) {
        this.body = body;
    }

    public static class ChatListBody extends DataSupport  implements Serializable{

        private Long code;

        private String content;

        private String alarmId;

        @Column(ignore = true)

        //这里忽略了ID 字段 因为冲突，所以数据库中存储的ID 是litepal自动生成d的。 而不是数据里的ID
        private String id;

        private String sendTime;

        private String senderName;

        private String senderId;
        //1.文字  2.语音 3.图片 4.视频
        private String type;

        private int timeLength;

        private boolean isLoad =false;

        public int getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(int timeLength) {
            this.timeLength = timeLength;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(String alarmId) {
            this.alarmId = alarmId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isLoad() {
            return isLoad;
        }

        public void setLoad(boolean load) {
            isLoad = load;
        }
    }

    public static ChatListResponse getChatList(String json) {
        return (ChatListResponse) parseFromJson(ChatListResponse.class, json);
    }
}
