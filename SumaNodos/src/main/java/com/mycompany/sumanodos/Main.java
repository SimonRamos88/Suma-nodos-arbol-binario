
package com.mycompany.sumanodos;

import java.util.Scanner;

public class Main{
    
        
    private int i; /*debemos usar un atributo i dado que, cuando tenemos un numero de mas de
                    1 digito, debemos correr el indice a donde termina este numero. Para 
                    hacer esto manejamos un parametro externo*/
    
    public Main(){
        this.i = 0;
    }
    
    //METODOS PARA HACER EL ARBOL A PARTIR DEL INPUT
    
    //Dado que hay numeros de mas de 1 digito, esta funcion nos procesa estos numeros   
    public int getNumber(String input, int index){
        String res = "";
        char ch;
        boolean bandera = true;
        
        while(bandera && index < input.length()){
            ch = input.charAt(index);
            if((int) ch <48 || (int) ch>57){
                bandera = false;
            }else{
                res += String.valueOf( ch);
                index ++;
            }           
            
        }
        this.i = index;
        return Integer.parseInt(res);
    }
    
    
    /*Esta funcion toma el input y lo tranforma en un arbol.
    Los elementos que se insertan a la pila son aquellos que tienen al menos un hijo no nulo
    Sabemos lo anterior pq estos nodos tienen despues el caracter :
    El nodo inmediatamente posterior a un [ es el nodo izquierdo del ultimo insertado a la pila
    El nodo inmediatamente despues de , es el nodo derecho del ultimo insertado a la pila
    El caracter ] indica que al ultimo isnertado a la pila no se le agregaran mas elementos, por lo que este se popea
    
    Podriamos prescindir de este metodo si usaramos la funcion insert del arbol pasandole solo los caracteres  numericos.
    Pero ya que el formato de la entrada permitia implementar pilas para hacer las inserciones, decidimos irnos por esta alternativa */
    
    
    public NodeTree processInput(String input){
        Pila<NodeTree> pila = new Pila();
        this.i = 0;
        while(i<input.length() - 2){ //Terminamos el ciclo antes para evitar que se popee el primer elemnto (osea, el root)
            char ch = input.charAt(i);
            if(ch == '['){
                i ++;
                NodeTree node = new NodeTree( getNumber(input,i) ); //Obtenemos el numero despues del [
                this.i --;
                if(!pila.empty()){
                     NodeTree parent = pila.pop();
                    
                    /*Este if es necesario ya que en ocasiones, cuando solo se tiene un hijo, el nodo
                    despues del [ puede ser el hijo derecho*/
                    if(node.getKey() > parent.getKey()){ 
                        parent.setRight(node);
                    }else{
                         parent.setLeft(node); //Como esta despues de un [, es nodo izquierdo
                    }
                    
                    pila.push(parent);
                    
                }
                
                if(input.charAt(i+1) ==  ':'){
                    pila.push(node);               
                }
                
                
            }
            
            else if(ch == ','){
                i +=2;
                NodeTree node = new NodeTree( getNumber(input,i) );
                this.i --;
                if(!pila.empty()){
                    NodeTree parent = pila.pop();
                    parent.setRight( node );
                    pila.push(parent);
                }
                if(input.charAt(i + 1) ==  ':'){
                    pila.push(node);               
                }
            }
            else if(ch == ']'){
                if(!pila.empty()){
                    NodeTree n = pila.pop();
                }
                
            }
            
            i ++;
        }
        return pila.pop();
    
    }
    
    public Tree makeTree(String input){
        NodeTree root = processInput(input);
        Tree tree = new Tree(root);
        return tree;
        
    }
    
    //METODOS PARA SACAR LA SUMA
    
    //Nos apoyamos del metodo next del arbol
    
    /*Nota: Suponga el intervalo [a,b]. Aqui el "a" que se pase como parametro al llamar la funcion debe ser, en realidad,
    el limite inferior del intervalo -1 . Asi, si "a" esta en el arbol, este se tendra en cuenta en la suma. Ya que
    si pasamos directamente a, el primer termino de la suma sera directamente el next de a*/
    
    public int calculateSum(Tree tree, int a, int b){
        NodeTree root = tree.getRoot();
        boolean bandera = true;
        int res = 0;
        while(bandera){
            NodeTree next = tree.nextNode(a, root, root);
            if(next == null || next.getKey()>b){
                bandera = false;
            }else{
                a = next.getKey();
                res += a;
            }
        }
        return res;
    }
    
 
    
    
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Main pp = new Main();
        
        String inputTree = sc.nextLine();
        String inputInter = sc.nextLine();
        int a = pp.getNumber(inputInter,1);       
        int b =  pp.getNumber(inputInter, pp.i + 2);
        
        Tree tree = pp.makeTree(inputTree);
        
        System.out.println(pp.calculateSum(tree,a-1,b));
    }
}


//PARA EL ARBOL
class NodeTree {
    
    private int key;
    private int height;
    private NodeTree right;
    private NodeTree left;
    
    //CONSTRUCTORES
    
    public NodeTree(int key, int height){
        this.key = key;
        this.height = height;
        this.right = this.left = null;
    }
    
    public NodeTree(int key){
        this(key,1);
    }
    
    public NodeTree(){
        this(0,1);
    }
    
    //GETTERS AND SETTERS

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    

    public NodeTree getRight() {
        return right;
    }

    public void setRight(NodeTree right) {
        this.right = right;
    }

    public NodeTree getLeft() {
        return left;
    }

    public void setLeft(NodeTree left) {
        this.left = left;
    }
    
    
}


class Tree {
    //NO ADJUNTAMOS TODOS LOS METODOS QUE PROGRAMAMOS PARA NUESTRA ESTRUCTURA DE ARBOL, SOLO LOS METODOS NECESARIOS
    
    private NodeTree root;
    
    //CONSTRUCTORES
    
    public Tree(NodeTree root){
        this.root = root;
    }
    
      
    //METODOS
    
    
    //INSERT
    private NodeTree insertBST(NodeTree root, int key) {
        if (root == null) {
            return new NodeTree(key);
        } else {
            if (root.getKey() > key) {
                root.setLeft(insertBST(root.getLeft(), key));
            } else if (root.getKey() < key) {
                root.setRight(insertBST(root.getRight(), key));
            }
            return root;
        }
    }
    
   
    public void insert(int key) {
        this.setRoot( insertBST(this.getRoot(),key )  );
    }
    
   //NEXT
    public NodeTree nextNode(int i, NodeTree n, NodeTree parent) {

        /*buscamos el nodo*/
        if (n.getKey() > i) { //lo buscamos en el lado izquierdo

            if (n.getLeft() != null) { // como en find
                NodeTree root = nextNode(i, n.getLeft(), n);
                if (root == null) {
                    if (parent.getKey() > i) {
                        root = parent;
                    } else {
                        return null;
                    }
                }
                return root;
                //return nextNode(i, n.getLeft(), n);
            } else { //Implicitamente tambien estamos buscando el minimo
                /*si el izq es nulo, entonces el nodo de key i no esta en el arbol
            luego, al tratarse del hijo izquierdo, el siguiente a este es el nodo mas pequeño cercano*/
                return n;
            }

        } else { //Si lo encontramos o esta en el derecho
            if (n.getRight() != null) {
                NodeTree root = nextNode(i, n.getRight(), n);
                if (root == null) {
                    if (parent.getKey() > i) {
                        root = parent;
                    }

                }
                return root;
                //return nextNode(i, n.getRight(), n);
            } else {
                if (parent.getKey() > i) {
                    return parent;
                } else {
                    return null;
                }
            }
        }

    }

 
    //DFS
    public void preOrder(NodeTree p) {
        if (p != null) {

            System.out.println(p.getKey());

            if (p.getLeft() != null) {
                preOrder(p.getLeft());
            }

            if (p.getRight() != null) {
                preOrder(p.getRight());
            }
        }
    }

    public void inOrder(NodeTree p) {
        if (p != null) {
            //System.out.println("p: " + p.getKey());                     
            if (p.getLeft() != null) {
                inOrder(p.getLeft());
            }

            System.out.println(p.getKey());

            if (p.getRight() != null) {
                inOrder(p.getRight());
            }
        }

    }

    public void postOrder(NodeTree p) {
        if (p != null) {

            if (p.getLeft() != null) {
                postOrder(p.getLeft());
            }

            if (p.getRight() != null) {
                postOrder(p.getRight());
            }

            System.out.println(p.getKey());
        }

    }

    //BFS
    public void bfs(NodeTree p) {
        Cola<NodeTree> cola = new Cola();
        if (this.root != null) {
            cola.Encolar(this.root);
        }

        while (!cola.empty()) {
            NodeTree n = cola.Desencolar();
            if (n.getLeft() != null) {
                cola.Encolar(n.getLeft());
            }
            if (n.getRight() != null) {
                cola.Encolar(n.getRight());
            }
            System.out.println(n.getKey() + " lvl: " + n.getHeight());

        }

    }
    
    
     //GETTERS AND SETTERS
    public NodeTree getRoot() {
        return root;
    }
    
     
    public void setRoot(NodeTree root) {
        this.root = root;
    }

}



//PARA LA COLA ENLAZADA Y PILA ENLAZADA

class Node<T> {
    T data;
    Node next;
    
    public T getData(){
        return this.data;
    }
    
    public void setData(T Data){
        this.data = Data;
    }
    
    public Node getNext(){
        return this.next;
    }
    
    public void setNext(Node n){
        this.next = n;
    }
    
    public Node(){
        this(null);
    }
    public Node(T Data){
        this.data = Data;
        this.next = null;
    }
    
}


class Cola<T> {
    Node rear;
    Node front;
   
    public boolean empty(){
        return rear == null;
    }
    
    
    public void Encolar(T data){
        Node element = new Node(data);
        if(!empty()){
            this.rear.setNext(element); 
            this.rear = element; 
            
        }
        else{
            this.rear = element;
            this.front = element;
        }
    }
    
    public T Desencolar(){
        if(empty()){
            throw new IllegalArgumentException("Esta vacío pa");
        }
        T data = (T) front.getData();
        if(this.rear == this.front){
            front = null;
            rear = null;           
        }else{
             front = front.getNext();
        }      
        return data;
    }
    
    public Cola(){
           this.rear = null;
           this.front = null;
       }
     
}

class Pila<T> {
    
    private Node top;    
    
    public boolean empty(){
        return top.getNext() == null;
    }
    
    public boolean full(){ 
        return false;
    }
    
    public void push(T value){
       Node aux = new Node(value);
       aux.setNext(this.top);
       this.top = aux;
    }
    
    public T pop(){
        if(!empty()){
            T value = (T) this.top.getData();
            top = this.top.getNext(); 
            return value;
        }else{
            throw new IllegalArgumentException("Esta vacio pa");
        }
        
    }
    
      public int toString(Node node){
        if(node.getNext()== null){
            return 0;
        }
        else{
            System.out.println(node.getData());
            return toString(node.getNext());
        }
    }
    
    public Pila(){
        this.top = new Node();
    }
    
    public Pila(int value){
        Node node = new Node(value);
        this.top = node;
    }
    
    
    
}