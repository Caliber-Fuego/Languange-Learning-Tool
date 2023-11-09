package Model;

import javax.swing.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedText<Object> implements Iterable<Object> {
    private LTNode<Object> head;

    //Iterator
    @Override
    public Iterator<Object> iterator(){
        return new LinkedTextIterator();
    }

    private class LinkedTextIterator implements Iterator<Object> {
        private LTNode<Object> current = head;

        @Override
        public boolean hasNext(){
            return current != null;
        }

        @Override
        public Object next(){
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            Object data = current.data;
            current = current.next;
            return data;
        }
    }

    private static class LTNode<Object>{
        Object data;
        LTNode<Object> next;

        LTNode(Object data){
            this.data = data;
            next = null;
        }

        public void setNext(LTNode<Object> nextNode) {
            this.next = nextNode;
        }
    }

    public LinkedText(){
        head = null;
    }

    public boolean isEmpty(){
        return head == null;
    }

    public Object getFirst() {
        return head.data ;
    }

    public void addAtFirst(Object data){
        LTNode<Object> newNode = new LTNode<>(data);
        if(isEmpty()){
            newNode.data = data;
            newNode.next = null;
            head = newNode;
        } else {
            newNode.data = data;
            newNode.next = head;
            head = newNode;
        }
    }

    public void error_message(String msg){
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
