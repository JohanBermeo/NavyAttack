package com.navyattack.main;

import com.navyattack.view.MenuView;
import com.navyattack.controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MenuView menuView = new MenuView();
        primaryStage.setTitle("NavyAttack - Batalla Naval");
        primaryStage.setScene(menuView.getScene());
        primaryStage.show();
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        // try {
        //     // Configuración mínima del Stage (ventana principal)
        //     primaryStage.setTitle("NavyAttack - Batalla Naval");
        //     primaryStage.setResizable(true);
        //     primaryStage.centerOnScreen();
            
        //     // Crear el controlador principal de la aplicación
        //     //ApplicationController appController = new ApplicationController(primaryStage);
            
        //     // Inicializar la aplicación (mostrará el menú)
        //     //appController.initializeApplication();
            
        //     // Manejar cierre de aplicación
        //     /*primaryStage.setOnCloseRequest(e -> {
        //         appController.handleApplicationClose();
        //     });*/
            
        // } catch (Exception e) {
        //     System.err.println("Error crítico al inicializar NavyAttack: " + e.getMessage());
        //     e.printStackTrace();
        //     System.exit(1);
        // }
    }
    
    /**
     * Punto de entrada del programa
     */
    public static void main(String[] args) {
        // Configuraciones opcionales del sistema
        System.setProperty("javafx.preloader", "com.navyattack.utils.AppPreloader");
        
        // Iniciar aplicación JavaFX
        launch(args);
    }
}