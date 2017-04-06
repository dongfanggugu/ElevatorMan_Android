package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.Response;
import com.honyum.elevatorMan.net.base.ResponseBody;
import com.honyum.elevatorMan.net.base.ResponseHead;

import java.util.List;

/**
 * Created by chang on 2015/11/3.
 */
public class MainDetailResponse extends Response {

    private ResponseHead head;

    private MainDetailRspBody body;

    @Override
    public ResponseHead getHead() {
        return head;
    }

    @Override
    public void setHead(ResponseHead head) {
        this.head = head;
    }

    public MainDetailRspBody getBody() {
        return body;
    }

    public void setBody(MainDetailRspBody body) {
        this.body = body;
    }

    /**
     * 根据下发的json字符串返回对象
     * @param json
     * @return
     */
    public static MainDetailResponse getMainDetailResponse(String json) {
        return (MainDetailResponse) parseFromJson(MainDetailResponse.class, json);
    }

    public class MainDetailRspBody extends ResponseBody {

        private String mainId;

        private List<String> mainPics;

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }

        public List<String> getMainPics() {
            return mainPics;
        }

        public void setMainPics(List<String> mainPics) {
            this.mainPics = mainPics;
        }
    }
}
