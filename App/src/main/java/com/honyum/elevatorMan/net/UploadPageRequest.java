package com.honyum.elevatorMan.net;

import com.honyum.elevatorMan.net.base.RequestBean;
import com.honyum.elevatorMan.net.base.RequestBody;
import com.honyum.elevatorMan.utils.Utils;

/**
 * Created by Star on 2017/6/17.
 */

public class UploadPageRequest extends RequestBean {



    private UploadPageRequestBody body;

    public UploadPageRequestBody getBody() {
        return body;
    }

    public UploadPageRequest setBody(UploadPageRequestBody body) {
        this.body = body;
        return this;
    }

    public class UploadPageRequestBody extends RequestBody
    {


        private String  branchId;
        private int page;

        private int rows;

        public int getPage() {
            return page;
        }

        public UploadPageRequestBody setPage(int page) {
            this.page = page;
            return this;
        }

        public int getRows() {
            return rows;
        }

        public UploadPageRequestBody setRows(int rows) {
            this.rows = rows;
            return this;
        }

        public String getBranchId() {
            return branchId;
        }

        public UploadPageRequestBody setBranchId(String branchId) {
            this.branchId = branchId; return this;
        }
    }
}
