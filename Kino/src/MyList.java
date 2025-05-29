// MyList.java
public class MyList<T> {
    private MyNode<T> head;
    private int size;

    public MyList() {
        head = null;
        size = 0;
    }

    public void add(T data) {
        MyNode<T> newNode = new MyNode<>(data);
        if (head == null) {
            head = newNode;
        } else {
            MyNode<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        MyNode<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        return current.data;
    }

    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        size = 0;
    }

    public void set(int index, T data) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        MyNode<T> current = head;
        for (int i = 0; i < index; i++) current = current.next;
        current.data = data;
    }

    public void insert(int index, T data) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        MyNode<T> newNode = new MyNode<>(data);
        if (index == 0) {
            newNode.next = head;
            head = newNode;
        } else {
            MyNode<T> current = head;
            for (int i = 0; i < index - 1; i++) current = current.next;
            newNode.next = current.next;
            current.next = newNode;
        }
        size++;
    }

    public void removeAt(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        if (index == 0) {
            head = head.next;
        } else {
            MyNode<T> current = head;
            for (int i = 0; i < index - 1; i++) current = current.next;
            current.next = current.next.next;
        }
        size--;
    }

    public boolean remove(T data) {
        if (head == null) return false;
        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return true;
        }
        MyNode<T> current = head;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }
        if (current.next != null) {
            current.next = current.next.next;
            size--;
            return true;
        }
        return false;
    }

    public MyNode<T> getHead() {
        return head;
    }
}
