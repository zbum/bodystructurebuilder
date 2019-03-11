package kr.co.manty.mail.imap.model;


import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private Node<T> root;
    
    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<>();
    }
    
    public static class Node<T> {
        private T data;
        private T parent;
        private List<Node<T>> children;  
    }
}
