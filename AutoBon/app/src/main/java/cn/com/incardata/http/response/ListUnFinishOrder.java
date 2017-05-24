package cn.com.incardata.http.response;

/**二期 未完成订单列表
 * Created by yang on 2016/11/10.
 */
public class ListUnFinishOrder {
    private int totalElements;
    private int totalPages;
    private boolean last;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean first;
    private OrderInfo[] content;
    private Object sort;


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

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public OrderInfo[] getContent() {
        return content;
    }

    public void setContent(OrderInfo[] content) {
        this.content = content;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }
}
