package com.navyattack.controller;

import javafx.stage.Stage;
import javafx.application.Application;

import java.util.Map;
import java.util.HashMap;

import com.navyattack.view.*;
import com.navyattack.model.User;
import com.navyattack.model.Board;

public class NavigationController {

   private IView currentView;
   private final MenuController menuController;
   private final Map<String, ViewFactory> viewRegistry;

   public NavigationController(MenuController menuController) {
      this.menuController = menuController;
      this.viewRegistry = new HashMap<>();
      registerDefaultViews();
   }

   /**
    * Registra todas las vistas por defecto del sistema.
    * Este método puede ser extendido sin modificar el código existente.
    */
   private void registerDefaultViews() {
      // Vistas de autenticación
      registerView("login", () -> new LoginView(menuController, this));
      registerView("signup", () -> new SignUpView(menuController, this));
      
      // Vistas principales
      registerView("menu", () -> new MenuView(menuController, this));
      registerView("play", () -> new PlayView(menuController, this));
      
      // Vista de historial (placeholder - requiere parámetros dinámicos)
      registerView("history", () -> new HistoryView(this, null, null));
   }

   /**
    * Permite registrar nuevas vistas sin modificar el código existente.
    * Cumple con el principio Open/Closed.
    * 
    * @param viewName Nombre único de la vista
    * @param factory Factory que crea la instancia de la vista
    */
   public void registerView(String viewName, ViewFactory factory) {
      viewRegistry.put(viewName, factory);
   }

   /**
    * Obtiene el Stage actual desde la vista activa.
    */
   private Stage getCurrentStage() {
      if (currentView != null && currentView.getScene() != null) {
         return (Stage) currentView.getScene().getWindow();
      }
      System.err.println("ERROR: No se pudo obtener el Stage actual");
      return null;
   }

   /**
    * Navega a una vista registrada por su nombre.
    */
   public void navigateToView(String viewName) {
      Stage stage = getCurrentStage();
      if (stage == null) return;

      ViewFactory factory = viewRegistry.get(viewName);
      if (factory == null) {
         System.err.println("ERROR: Vista no registrada: " + viewName);
         return;
      }

      currentView = factory.create();
      currentView.start(stage);
   }

   /**
    * Navega a una vista específica ya instanciada.
    * Útil para vistas que requieren parámetros dinámicos.
    */
   public void navigateToView(IView view) {
      Stage stage = getCurrentStage();
      if (stage == null) return;

      currentView = view;
      currentView.start(stage);
   }

   // ==================== MÉTODOS DE INICIALIZACIÓN ====================

   /**
    * Lanza la aplicación JavaFX.
    */
   public static void launchApp(String[] args) {
      Application.launch(NavigationApplication.class, args);
   }

   /**
    * Inicializa la primera vista de la aplicación.
    */
   public void initializeView(Stage primaryStage) {
      currentView = new LoginView(menuController, this);
      currentView.start(primaryStage);
   }

   // ==================== MÉTODOS DE NAVEGACIÓN - VISTAS CON PARÁMETROS ====================

   /**
    * Navega a la vista de historial con datos específicos del usuario.
    */
   public void navigateToHistory(String username, java.util.List<com.navyattack.model.History> history) {
      IView historyView = new HistoryView(this, username, history);
      navigateToView(historyView);
   }

   /**
    * Navega a la vista de deployment (colocación de barcos) del primer jugador.
    */
   public void navigateToDeployment(String gameMode) {
      Stage stage = getCurrentStage();
      if (stage == null) return;

      Board playerBoard = new Board();
      User currentPlayer = menuController.getLoggedUsers().isEmpty() ? 
                           null : menuController.getLoggedUsers().get(0);

      DeploymentView deploymentView = new DeploymentView(this, gameMode, currentPlayer.getUsername());
      currentView = deploymentView;
      deploymentView.start(stage);

      new DeploymentController(playerBoard, deploymentView, this);
   }

   /**
    * Navega a la vista de transición entre jugadores en modo PvP.
    */
   public void navigateToTransition(String gameMode) {
      User nextPlayer = menuController.getLoggedUsers().size() > 1 ?
                        menuController.getLoggedUsers().get(1) : null;

      if (nextPlayer == null) {
         System.err.println("ERROR: No hay segundo jugador para la transición");
         return;
      }

      TransitionView transitionView = new TransitionView(this, nextPlayer.getUsername(), gameMode);
      navigateToView(transitionView);
   }

   /**
    * Navega a la vista de deployment del segundo jugador en modo PvP.
    */
   public void navigateToSecondPlayerDeployment(String gameMode) {
      Stage stage = getCurrentStage();
      if (stage == null) return;

      Board player2Board = new Board();
      User player2 = menuController.getLoggedUsers().size() > 1 ?
                     menuController.getLoggedUsers().get(1) : null;

      if (player2 == null) {
         System.err.println("ERROR: No hay segundo jugador registrado");
         return;
      }

      DeploymentView deploymentView = new DeploymentView(this, gameMode, player2.getUsername());
      currentView = deploymentView;
      deploymentView.start(stage);

      new DeploymentController(player2Board, deploymentView, this);
   }

   /**
    * Navega a la vista principal del juego (batalla).
    */
   public void navigateToGame(Board player1Board, Board player2Board, String gameMode) {
      Stage stage = getCurrentStage();
      if (stage == null) return;

      String player1 = menuController.getLoggedUsers().isEmpty() ? 
                       null : menuController.getLoggedUsers().get(0).getUsername();
      String player2 = null;

      if (gameMode.equals("PVC")) {
         // Modo Player vs CPU: generar tablero aleatorio para la CPU
         player2Board = new Board();
         Board.placeShipsRandomly(player2Board);
         player2 = null;
      } else {
         // Modo Player vs Player: usar segundo jugador registrado
         player2 = menuController.getLoggedUsers().size() > 1 ?
                   menuController.getLoggedUsers().get(1).getUsername() : null;
      }

      GameView gameView = new GameView(this, player1, player2, gameMode);
      currentView = gameView;
      gameView.start(stage);

      new GameController(player1Board, player2Board, gameView, menuController, this);
   }

   /**
    * Navega a la vista de victoria con todas las estadísticas de la partida.
    */
   public void navigateToVictory(String winner, String loser, String gameMode, int totalTurns,
                                 String timePlayed, long timePlayedMillis,
                                 int winnerShipsSunk, int loserShipsSunk) {
      VictoryView victoryView = new VictoryView(this, winner, loser, gameMode, totalTurns,
                                                timePlayed, timePlayedMillis,
                                                winnerShipsSunk, loserShipsSunk);
      navigateToView(victoryView);
   }

   // ==================== MÉTODOS DE GESTIÓN DE USUARIOS ====================

   /**
    * Cierra la sesión de un usuario y navega a la vista apropiada.
    */
   public void logoutUser(String username) {
      menuController.logoutUser(username);

      if (menuController.hasNoUsers()) {
         navigateToView("login");
      } else {
         navigateToView("menu");
      }
   }

   // ==================== CLASE INTERNA ====================

   /**
    * Clase interna para iniciar la aplicación JavaFX.
    */
   public static class NavigationApplication extends Application {
      @Override
      public void start(Stage primaryStage) {
         MenuController menuController = new MenuController();
         NavigationController controller = new NavigationController(menuController);
         controller.initializeView(primaryStage);
      }
   }
}