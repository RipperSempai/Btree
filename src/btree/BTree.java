/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btree;

/**
 *
 * @author Dariel
 */
class BTree{
    BtreeNodo raiz;
    int MinDeg;

 
    public BTree(int deg){
        this.raiz = null;
        this.MinDeg = deg;
    }

    public void camino(){
        if (raiz != null){
            raiz.camino();
        }
    }

    
    public BtreeNodo buscar(int clave){
        return raiz == null ? null : raiz.buscar(clave);
    }

    public void insertar(int clave){

        if (raiz == null){

            raiz = new BtreeNodo(MinDeg,true);
            raiz.claves[0] = clave;
            raiz.num = 1;
        }
        else {
            // Cuando el nodo raíz está lleno, el árbol crecerá más alto
            if (raiz.num == 2*MinDeg-1){
                BtreeNodo s = new BtreeNodo(MinDeg,false);
                // El antiguo nodo raíz se convierte en hijo del nuevo nodo raíz
                s.hijos[0] = raiz;
                // Separa el antiguo nodo raíz y dale una clave al nuevo nodo
                s.divHijos(0,raiz);
                // El nuevo nodo raíz tiene 2 nodos secundarios, debe mover el antiguo nodo raíz
                int i = 0;
                if (s.claves[0]< clave)
                    i++;
                s.hijos[i].insertarNoFull(clave);

                raiz = s;
            }
            else
                raiz.insertarNoFull(clave);
        }
    }

    public void eliminar(int clave){
        if (raiz == null){
            System.out.println("El árbol está vacío");
            return;
        }

        raiz.eliminar(clave);

        if (raiz.num == 0){
            // Si tiene un nodo hijo, use su primer nodo hijo como el nuevo nodo raíz,de lo contrario, establezca el nodo raíz en nulo
            if (raiz.esHoja)
                raiz = null;
            else
                raiz = raiz.hijos[0];
        }
    }
}
