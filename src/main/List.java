package main;

public class List<T> {

    private ListRecord head;
    private ListRecord tail;

    private ListRecord iterator;


    public List() {
        head = null;
        tail = null;
        iterator = null;
    }

    public void addToHead(T obj) {
        if (obj != null) {
            ListRecord newRecord = new ListRecord(obj);
            if (head == null) {
                head = newRecord;
                tail = newRecord;
            } else {
                newRecord.next = head;
                head = newRecord;
            }
        }
    }

    public void append(T obj) {
        if (obj != null) {
            ListRecord newRecord = new ListRecord(obj);
            if (head == null) {
                head = newRecord;
                tail = newRecord;
            } else {
                tail.next = newRecord;
                tail = newRecord;
            }
        }
    }

    public void remove( T obj ) {
        if (head == null) return;

        // is object at head?
        if (head.data.equals(obj)) {
            ListRecord rec = head;
            if (tail == head) {   // one item in list
                tail = tail.next;
            }
            head = head.next;
            rec.next = null;
        } else {
            // find the object
            ListRecord before = head;
            ListRecord rec = head.next;
            while ((rec != null) && !rec.data.equals(obj)) {
                rec = rec.next;
                before = before.next;
            }

            if (rec != null) {
                before.next = rec.next;
                rec.next = null;
            }
        }
    }

    public int getLength() {
        int count = 0;
        ListRecord rec = head;
        while (rec != null) {
            count++;
            rec = rec.next;
        }
        return count;
    }

    public void initIteration() {
        iterator = head;
    }

    public T next() {
        if (iterator == null)
            return null;
        else {
            T object = iterator.data;
            iterator = iterator.next;
            return object;
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    private class ListRecord {
        public T data;
        public ListRecord next;

        public ListRecord(T obj) {
            data = obj;
        }
    }
}
