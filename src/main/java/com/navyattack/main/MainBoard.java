package com.navyattack.main;

import com.navyattack.controller.BoardController;
import com.navyattack.model.Board;
import com.navyattack.view.BoardView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class - Punto de entrada de NavyAttack
 * Inicializa JavaFX y muestra el menú principal
 */
public class MainBoard extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Iniciando NavyAttack...");

        try {
            // Configuración básica de la ventana
            primaryStage.setTitle("NavyAttack - Batalla Naval");
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();

            // Crear modelo y vista
            Board board = new Board();
            BoardView boardView = new BoardView();

            // Crear controlador con modelo y vista
            BoardController boardController = new BoardController(board, boardView);

            // Mostrar la escena
            primaryStage.setScene(boardView.getScene());
            primaryStage.show();

            System.out.println("Tablero cargado exitosamente!");

        } catch (Exception e) {
            System.err.println("Error al inicializar NavyAttack: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Iniciando NavyAttack...");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("JavaFX Version: " + System.getProperty("javafx.version"));

        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Error crítico al lanzar JavaFX: " + e.getMessage());
            e.printStackTrace();
        }
    }
}