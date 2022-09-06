import algorithms.DistanceVectorRouting;
import algorithms.Flooding;
import algorithms.LinkStateRouting;

import java.util.Scanner;

public class Main {

    public static void cleanScreen(){
        for(int i = 0; i < 50; i++){
            System.out.println();
        }
    }

    public static void main(String[] args) {
        boolean salida = true;

        while (salida){
            cleanScreen();
            System.out.println("Elija el algoritmo que desea usar: ");
            System.out.println("1. Flooding");
            System.out.println("2. Link State Routing");
            System.out.println("3. Distance Vector Routing");
            System.out.println("4. Exit");

            System.out.print("\nOpcion: ");
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();

            cleanScreen();

            switch (option){
                case 1:
                    Flooding.start();
                    break;
                case 2:
                    LinkStateRouting.start();
                    break;
                case 3:
                    DistanceVectorRouting.start();
                    break;
                case 4:
                    salida = false;
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
        System.exit(0);
    }
}
