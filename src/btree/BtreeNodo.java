/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btree;

/**
 *
 * @author Trabajo
 */
class BtreeNodo{

    int[] claves; // claves de nodo
    int MinDeg; // El grado mínimo del nodo B-tree
    BtreeNodo[] hijos; // nodos secundarios
    int num; // El número de claves de nodos
    boolean esHoja; // Verdadero cuando es un nodo hoja
 
     
// constructor
    public BtreeNodo(int deg,boolean esHoja){

        this.MinDeg = deg;
        this.esHoja = esHoja;
        this.claves = new int[2*this.MinDeg-1]; // El nodo tiene como máximo 2 * claves MinDeg-1
        this.hijos = new BtreeNodo[2*this.MinDeg];
        this.num = 0;
    }
    

    // Encuentra el primer índice de posición igual o mayor que la clave
    public int encontrarClave(int clave){
        int idx = 0;
        while (idx < num && claves[idx] < clave)
            ++idx;
        return idx;
    }


    public void eliminar(int clave){
        int idx = encontrarClave(clave);
        if (idx < num && claves[idx] == clave){ // Encontrar la llave
            if (esHoja) // la clave está en el nodo hoja
                eliminarDeHoja(idx);
            else // la clave no está en el nodo hoja
                eliminarDeNoHoja(idx);
        }
        else{
            if (esHoja){ // Si el nodo es un nodo hoja, entonces el nodo no está en el árbol B
                System.out.printf("La clave %d no existe en el árbol",clave);
                return;
            }

            // De lo contrario, la clave que se eliminará existe en el subárbol enraizado en el nodo 
            boolean flag = idx == num; 
            
            if (hijos[idx].num < MinDeg) // Cuando el nodo hijo de este nodo no está lleno, llámalo primero
                rellenar(idx);

            if (flag && idx > num)
                hijos[idx-1].eliminar(clave);
            else
                hijos[idx].eliminar(clave);
        }
    }

    public void eliminarDeHoja(int idx){
        // Retroceder de idx
        for (int i = idx +1;i < num;++i)
            claves[i-1] = claves[i];
        num --;
    }

    public void eliminarDeNoHoja(int idx){
        int clave = claves[idx];

        if (hijos[idx].num >= MinDeg){
            int pred = getPred(idx);
            claves[idx] = pred;
            hijos[idx].eliminar(pred);
        }
        else if (hijos[idx+1].num >= MinDeg){
            int succ = getSucc(idx);
            claves[idx] = succ;
            hijos[idx+1].eliminar(succ);
        }
        else{
            unir(idx);
            hijos[idx].eliminar(clave);
        }
    }

    // El nodo predecesor siempre está buscando el nodo más a la derecha del subárbol izquierdo
    public int getPred(int idx){ 
        BtreeNodo cur = hijos[idx];
        while (!cur.esHoja)
            cur = cur.hijos[cur.num];
        return cur.claves[cur.num-1];
    }

    // El sucesor siempre está mirando desde el subárbol derecho hacia la izquierda
    public int getSucc(int idx){
        BtreeNodo cur = hijos[idx+1];
        while (!cur.esHoja)
            cur = cur.hijos[0];
        return cur.claves[0];
    }

    // Rellene hijos [idx] que tiene menos de MinDeg claves
    public void rellenar(int idx){
        // Si el nodo secundario anterior tiene varias claves MinDeg-1, pídalo prestado
        if (idx != 0 && hijos[idx-1].num >= MinDeg)
            pedirDePrev(idx);
        // El último nodo hijo tiene varias claves MinDeg-1, pídalo prestado
        else if (idx != num && hijos[idx+1].num >= MinDeg)
            pedirDeNext(idx);
        else{
            // Fusionar hijos [idx] y su hermano
            if (idx != num)
                unir(idx);
            else
                unir(idx-1);
        }
    }

    // Pedir prestada una clave de hijos [idx-1] e insertarla en hijos [idx]
    public void pedirDePrev(int idx){

        BtreeNodo child = hijos[idx];
        BtreeNodo sibling = hijos[idx-1];

        // La última clave de los hijos [idx-1] se desbordará al nodo padre
        for (int i = child.num-1; i >= 0; --i) // hijos [idx] avanzar
            child.claves[i+1] = child.claves[i];

        if (!child.esHoja){ // Cuando hijo [idx] no es un nodo hoja, mueve su nodo hijo hacia atrás
            for (int i = child.num; i >= 0; --i)
                child.hijos[i+1] = child.hijos[i];
        }

        // Establecer la primera clave del nodo secundario a las claves del nodo actual [idx-1]
        child.claves[0] = claves[idx-1];
        if (!child.esHoja) // Usa el último nodo hijo de hermano como el primer nodo hijo de hijos [idx]
            child.hijos[0] = sibling.hijos[sibling.num];

        // Mueve la última clave del hermano hasta la última del nodo actual
        claves[idx-1] = sibling.claves[sibling.num-1];
        child.num += 1;
        sibling.num -= 1;
    }

    
    public void pedirDeNext(int idx){
        BtreeNodo child = hijos[idx];
        BtreeNodo sibling = hijos[idx+1];

        child.claves[child.num] = claves[idx];

        if (!child.esHoja)
            child.hijos[child.num+1] = sibling.hijos[0];

        claves[idx] = sibling.claves[0];

        for (int i = 1; i < sibling.num; ++i)
            sibling.claves[i-1] = sibling.claves[i];

        if (!sibling.esHoja){
            for (int i= 1; i <= sibling.num;++i)
                sibling.hijos[i-1] = sibling.hijos[i];
        }
        child.num += 1;
        sibling.num -= 1;
    }

    // unir hijo [idx + 1] en hijo [idx]
    public void unir(int idx){
        BtreeNodo child = hijos[idx];
        BtreeNodo sibling = hijos[idx+1];

        // Inserta la última clave del nodo actual en la posición MinDeg-1 del nodo secundario
        child.claves[MinDeg-1] = claves[idx];

        // claves: hijos [idx + 1] copiados a hijos [idx]
        for (int i =0 ; i< sibling.num; ++i)
            child.claves[i+MinDeg] = sibling.claves[i];

        // hijos: hijos [idx + 1] copiado a hijos [idx]
        if (!child.esHoja){
            for (int i = 0;i <= sibling.num; ++i)
                child.hijos[i+MinDeg] = sibling.hijos[i];
        }

        
        for (int i = idx+1; i<num; ++i)
            claves[i-1] = claves[i];
        
        for (int i = idx+2;i<=num;++i)
            hijos[i-1] = hijos[i];

        child.num += sibling.num + 1;
        num--;
    }


    public void insertarNoFull(int clave){
        int i = num -1; // inicializa i al índice del valor más a la derecha

        if (esHoja){ // Cuando es un nodo hoja
            // Encuentra dónde se debe insertar la nueva clave
            while (i >= 0 && claves[i] > clave){
                claves[i+1] = claves[i];
                i--;
            }
            claves[i+1] = clave;
            num = num +1;
        }
        else{
            // Encuentra la posición del nodo hijo que debe insertarse
            while (i >= 0 && claves[i] > clave)
                i--;
            if (hijos[i+1].num == 2*MinDeg - 1){ // Cuando el nodo hijo está lleno
                divHijos(i+1,hijos[i+1]);
                // Después de dividir, la clave en el medio del nodo secundario se mueve hacia arriba, el nodo secundario se divide en dos
                if (claves[i+1] < clave)
                    i++;
            }
            hijos[i+1].insertarNoFull(clave);
        }
    }


    public void divHijos(int i ,BtreeNodo y){
        // Primero cree un nodo que contenga las claves de MinDeg-1 de y
        BtreeNodo z = new BtreeNodo(y.MinDeg,y.esHoja);
        z.num = MinDeg - 1;

        // Pase todos las claves y a z
        for (int j = 0; j < MinDeg-1; j++)
            z.claves[j] = y.claves[j+MinDeg];
        if (!y.esHoja){
            for (int j = 0; j < MinDeg; j++)
                z.hijos[j] = y.hijos[j+MinDeg];
        }
        y.num = MinDeg-1;

        // Inserta el nuevo nodo hijo en el nodo hijo
        for (int j = num; j >= i+1; j--)
            hijos[j+1] = hijos[j];
        hijos[i+1] = z;

        // Mueve una clave a este nodo
        for (int j = num-1;j >= i;j--)
            claves[j+1] = claves[j];
        claves[i] = y.claves[MinDeg-1];

        num = num + 1;
    }


    public void camino(){
        int i;
        for (i = 0; i< num; i++){
            if (!esHoja)
                hijos[i].camino();
            System.out.printf(" %d",claves[i]);
        }

        if (!esHoja){
            hijos[i].camino();
        }
    }


    public BtreeNodo buscar(int clave){
        int i = 0;
        while (i < num && clave > claves[i])
            i++;

        if (claves[i] == clave)
            return this;
        if (esHoja)
            return null;
        return hijos[i].buscar(clave);
    }
}
