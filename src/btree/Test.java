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
public class Test {
    public static void main(String[] args) {

        BTree a = new BTree(5);
        a.insertar(70);
        a.insertar(50);
        a.insertar(30);
        a.insertar(40);
        a.insertar(20);
        a.insertar(80);
        a.insertar(25);
        a.insertar(90);
        a.insertar(75);
        a.insertar(10);
        a.insertar(51);
        a.insertar(62);
        a.insertar(6);
        a.insertar(4);
        a.insertar(71);
        a.insertar(21);
        a.insertar(15);

        System.out.println("Recorrido del árbol-B construido");
        a.camino();
        System.out.println();

        a.eliminar(15);
        System.out.println("Recorrido del árbol-B después de eliminar el 15");
        a.camino();
        System.out.println();

        a.eliminar(80);
        System.out.println("Recorrido del árbol-B después de eliminar el 80");
        a.camino();
        System.out.println();

        a.eliminar(30);
        System.out.println("Recorrido del árbol-B después de eliminar el 30");
        a.camino();
        System.out.println();

        a.eliminar(90);
        System.out.println("Recorrido del árbol-B después de eliminar el 90");
        a.camino();
        System.out.println();

        a.eliminar(10);
        System.out.println("Recorrido del árbol-B después de eliminar el 10");
        a.camino();
        System.out.println();
    }
}

