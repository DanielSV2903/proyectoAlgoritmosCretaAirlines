package model.tda;

/* *
 *
 * @author Profesor Lic. Gilberth Chaves A.
 * Binary Search Tree AVL (Arbol de Búsqueda Binaria AVL)
 * AVL = Arbol de busqueda binaria auto balanceado
 * */

import util.Utility;

import java.util.ArrayList;

public class AVL implements  Tree {
    private BTreeNode root; //se refiere a la raiz del arbol

    @Override
    public int size() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return size(root);
    }

    private int size(BTreeNode node){
        if(node==null) return 0;
        else return 1 + size(node.left) + size(node.right);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element){
        if(node==null) return false;
        else if(Utility.compare(node.data, element)==0) return true;
        else if(Utility.compare(element, node.data)<0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
       this.root = add(root, element, "root");
    }

    private BTreeNode add(BTreeNode node, Object element, String path){
        if(node==null)
            node = new BTreeNode(element, path);
        else if(Utility.compare(element, node.data)<0)
            node.left = add(node.left, element, path+"/left");
        else if(Utility.compare(element, node.data)>0)
            node.right = add(node.right, element, path+"/right");

        //una vez agregado el nuevo nodo, debemos determinar si se requiere rebalanceo para siga siendo BST-AVL
        node = reBalance(node, element);
        return node;
    }

    private BTreeNode reBalance(BTreeNode node, Object element) {
        //debemos obtener el factor de balanceo, si es 0, -1, 1 está balanceado, si es <=-2, >=2 hay que rebalancear
        int balance = getBalanceFactor(node);

        // Caso-1. Left Left Case
        if (balance > 1 && Utility.compare(element, node.left.data)<0){
            node.path += "/Simple-Right-Rotate";
            return rightRotate(node);
        }

        // Caso-2. Right Right Case
        if (balance < -1 && Utility.compare(element, node.right.data)>0){
            node.path += "/Simple-Left-Rotate";
            return leftRotate(node);
        }

        // Caso-3. Left Right Case
        if (balance > 1 && Utility.compare(element, node.left.data)>0) {
            node.path += "/Double-Left-Right-Rotate";
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Caso-4. Right Left Case
        if (balance < -1 && Utility.compare(element, node.right.data)<0) {
            node.path += "/Double-Right-Left-Rotate";
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    //retorna el factor de balanceo del árbol a partir del nodo nado
    private int getBalanceFactor(BTreeNode node){
        if(node==null){
            return 0;
        }else{
            return height(node.left) - height(node.right);
        }
    }

    private BTreeNode leftRotate(BTreeNode node) {
        BTreeNode node1 = node.right;
        if (node1 != null){ //importante para evitar NullPointerException
            BTreeNode node2 = node1.left;
            //se realiza la rotacion (perform rotation)
            node1.left = node;
            node.right = node2;
        }
        return node1;
    }

    private BTreeNode rightRotate(BTreeNode node) {
        BTreeNode node1 = node.left;
        if (node1 != null) { //importante para evitar NullPointerException
            BTreeNode node2 = node1.right;
            //se realiza la rotacion (perform rotation)
            node1.right = node;
            node.left = node2;
        }
        return node1;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element) throws TreeException{
        if(node!=null){
            if(Utility.compare(element, node.data)<0)
              node.left = remove(node.left, element);
            else if(Utility.compare(element, node.data)>0)
                node.right = remove(node.right, element);
            else if(Utility.compare(node.data, element)==0){
                //caso 1. es un nodo si hijos, es una hoja
                if(node.left==null && node.right==null) return null;
                //caso 2-a. el nodo solo tien un hijo, el hijo izq
                else if (node.left!=null&&node.right==null) {
                    return node.left;
                } //caso 2-b. el nodo solo tien un hijo, el hijo der
                else if (node.left==null&&node.right!=null) {
                    return node.right;
                }
                //caso 3. el nodo tiene dos hijos
                else{
                //else if (node.left!=null&&node.right!=null) {
                    /* *
                     * El algoritmo de supresión dice que cuando el nodo a suprimir
                     * tiene 2 hijos, entonces busque una hoja del subarbol derecho
                     * y sustituya la data del nodo a suprimir por la data de esa hoja,
                     * luego elimine esa hojo
                     * */
                    Object value = min(node.right);
                    node.data = value;
                    node.right = remove(node.right, value);
                }
            }
        }
        return node; //retorna el nodo modificado o no
    }

    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return height(root, element, 0);
    }

    //devuelve la altura de un nodo (el número de ancestros)
    private int height(BTreeNode node, Object element, int level){
        if(node==null) return 0;
        else if(Utility.compare(node.data, element)==0) return level;
        else return Math.max(height(node.left, element, ++level),
                    height(node.right, element, level));
    }

    @Override
    public int height() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        //return height(root, 0); //opción-1
        return height(root); //opción-2
    }

    //devuelve la altura del árbol (altura máxima de la raíz a
    //cualquier hoja del árbol)
    private int height(BTreeNode node, int level){
        if(node==null) return level-1;//se le resta 1 al nivel pq no cuente el nulo
        return Math.max(height(node.left, ++level),
                height(node.right, level));
    }

    //opcion-2
    private int height(BTreeNode node){
        if(node==null) return -1; //retorna valor negativo para eliminar el nivel del nulo
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    @Override
    public Object min() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return min(root);
    }

    private Object min(BTreeNode node){
        if(node.left!=null)
            return min(node.left);
        return node.data;
    }

    @Override
    public Object max() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return max(root);
    }

    private Object max(BTreeNode node){
        if(node.right!=null)
            return max(node.right);
        return node.data;
    }

    @Override
    public String preOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return preOrder(root);
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result = node.data+" ";
            result += preOrder(node.left);
            result += preOrder(node.right);
        }
        return  result;
    }

    //recorre el árbol de la forma: nodo-hijo izq-hijo der
    private String preOrderPath(BTreeNode node){
        String result="";
        if(node!=null){
            result  = node.data+"("+node.path+")"+" ";
            result += preOrderPath(node.left);
            result += preOrderPath(node.right);
        }
        return  result;
    }

    @Override
    public String inOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return inOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-nodo-hijo der
    private String inOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = inOrder(node.left);
            result += node.data+" ";
            result += inOrder(node.right);
        }
        return  result;
    }

    //para mostrar todos los elementos existentes
    @Override
    public String postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return postOrder(root);
    }

    //recorre el árbol de la forma: hijo izq-hijo der-nodo,
    private String postOrder(BTreeNode node){
        String result="";
        if(node!=null){
            result  = postOrder(node.left);
            result += postOrder(node.right);
            result += node.data+" ";
        }
        return result;
    }
    public Object father(Object element) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return father(root,element);
    }

    private Object father(BTreeNode node, Object element) {
        if (node == null)
            return null;

        //Caso 1 si uno de los hijos es el elemento buscado, el nodo actual es el padre
        if ((node.left != null && Utility.compare(node.left.data, element) == 0) ||
                (node.right != null && Utility.compare(node.right.data, element) == 0)) {
            return node.data;
        }

        //Recursividad
        //si el elemento es menor, busco en el subárbol izquierdo
        if (Utility.compare(element, node.data) < 0) {
            return father(node.left, element);
        } else {
            //si el elemento es mayor, busco en el subárbol derecho
            return father(node.right, element);
        }//recursividad
    }

    //Algoritmo que devuelva el hermano (izquierdo o derecho) del elemento dado.
    public Object brother(Object element) {
        if(isEmpty())
            throw new RuntimeException("AVL Binary Search Tree is empty");
        return brother(root, element);
    }

    private Object brother(BTreeNode node, Object element){
        if(node==null) return null;

        if (node.left != null && Utility.compare(node.left.data, element) == 0) {
            if (node.right != null) return node.right.data;
            else return "No tiene hermano";
        }
        if (node.right != null && Utility.compare(node.right.data, element) == 0) {
            if (node.left != null) return node.left.data;
            else return "No tiene hermano";
        }
        if (Utility.compare(element, node.data) < 0) return brother(node.left, element);
        else return brother(node.right, element);
    }

    //Algoritmo que devuelva los hijos (uno, dos o ninguno) del elemento dado
    public String children(Object element) {
        if (isEmpty())
            throw new RuntimeException("AVL Binary Search Tree is empty");
        return children(root, element);
    }
    private String children(BTreeNode node, Object element){
        if (node==null) return null;

        String result="";

        if (Utility.compare(node.data, element) == 0){
            if (node.left != null) result += "\nHijo Izquierdo: " + node.left.data;
            if (node.right != null) result += "\nHijo Derecho: " + node.right.data;
            if (node.right == null && node.left == null) result += "No tiene hijos";
        } else {
            if (Utility.compare(element, node.data) < 0) return children(node.left, element);
            else return children(node.right, element);
        }

        return result;
    }

    @Override
    public String toString() {
        String result="AVL Binary Search Tree Content:";
        try {
            result = "PreOrder: "+preOrderPath(root);
            result+= "\nPreOrder: "+preOrder();
            result+= "\nInOrder: "+inOrder();
            result+= "\nPostOrder: "+postOrder();

        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public BTreeNode getRoot() {
        return root;
    }

    public boolean isBalanced() throws TreeException {
        if (isEmpty())
            throw new TreeException("Binary Search Tree is empty");
        return isBalanced(root);
    }

    private boolean isBalanced(BTreeNode node) {
        if (node == null) return true;
        int balanceFactor = getBalanceFactor(node);

        if (balanceFactor < -1 || balanceFactor > 1)//verifica ambas alturas para comprobar que no pasen de (-1,1)
            return false;

        return isBalanced(node.left) && isBalanced(node.right);//revisar recursivamente el arbol
    }

    @SuppressWarnings("unchecked")
    public <T> java.util.List<T> toTypedList(Class<T> clazz) throws TreeException {
        if (isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");

        java.util.List<T> typedList = new ArrayList<>();
        toTypedListInOrder(root, typedList, clazz);
        return typedList;
    }

    private <T> void toTypedListInOrder(BTreeNode node, java.util.List<T> list, Class<T> clazz) {
        if (node != null) {
            toTypedListInOrder(node.left, list, clazz);
            list.add((T) node.data); // El cast es seguro si se conoce el tipo real
            toTypedListInOrder(node.right, list, clazz);
        }
    }
    public static <T> AVL fromList(java.util.List<T> list) {
        AVL avl = new AVL();
        for (Object element : list) {
            avl.add(element);
        }
        return avl;
    }
}
