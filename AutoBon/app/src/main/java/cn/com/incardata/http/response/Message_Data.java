package cn.com.incardata.http.response;

import java.util.List;

/**
 * Created by wanghao on 16/4/14.
 */
public class Message_Data {
    private int page;
    private int totalElements;
    private int totalPages;
    private int pageSize;
    private int count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Message_Data_List> getList() {
        return list;
    }

    public void setList(List<Message_Data_List> list) {
        this.list = list;
    }

    private List<Message_Data_List> list;

    public class Message_Data_List {
        private int id;
        private String title;
        private String content;
        private int sendTo;
        private long createTime;
        private long publishTime;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getSendTo() {
            return sendTo;
        }

        public void setSendTo(int sendTo) {
            this.sendTo = sendTo;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(long publishTime) {
            this.publishTime = publishTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
