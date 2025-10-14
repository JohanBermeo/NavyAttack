package com.navyattack.controller;

import javafx.stage.Stage;
import javafx.application.Application;

import com.navyattack.view.*;
import com.navyattack.model.User;
import com.navyattack.model.Board;

public class NavigationController {

   private GameView gameView;
   private MenuView menuView;
   private PlayView playView;
   private LoginView loginView;
   private SignUpView signUpView;
   private HistoryView historyView;
   private VictoryView victoryView;
   private DeploymentView deploymentView;
   private TransitionView transitionView;

   private final MenuController menuController;

   public NavigationController(MenuController menuController) {
      this.menuController = menuController;
   }

   private void clearViewReferences() {
      this.gameView = null;
      this.playView = null;
      this.menuView = null;
      this.loginView = null;
      this.signUpView = null;
      this.victoryView = null;
      this.historyView = null;
      this.deploymentView = null;
      this.transitionView = null;
   }

   // ==================== MÉTODOS DE INICIALIZACIÓN ====================

   public static void launchApp(String[] args) {
      Application.launch(NavigationApplication.class, args);
   }

   public void initializeView(Stage primaryStage) {
      this.loginView = new LoginView(menuController, this);
      loginView.start(primaryStage);
   }

   // ==================== MÉTODOS DE NAVEGACIÓN ====================

   public void navigateToGameMenu() {
      Stage currentStage = null;

      if (loginView != null && loginView.getScene() != null) {
         currentStage = (Stage) loginView.getScene().getWindow();
      } else if (signUpView != null && signUpView.getScene() != null) {
         currentStage = (Stage) signUpView.getScene().getWindow();
      } else if (historyView != null && historyView.getScene() != null) {
         currentStage = (Stage) historyView.getScene().getWindow();
      } else if (playView != null && playView.getScene() != null) {
         currentStage = (Stage) playView.getScene().getWindow();
      } else if (menuView != null && menuView.getScene() != null) {
         currentStage = (Stage) menuView.getScene().getWindow();
      } else if (deploymentView != null && deploymentView.getScene() != null) {
         currentStage = (Stage) deploymentView.getScene().getWindow();
      } else if (gameView != null && gameView.getScene() != null) {
         currentStage = (Stage) gameView.getScene().getWindow();
      } else if (victoryView != null && victoryView.getScene() != null) {
         currentStage = (Stage) victoryView.getScene().getWindow();
      } else {
         System.err.println("ERROR: No se pudo obtener el Stage actual");
         return;
      }

      clearViewReferences();

      this.menuView = new MenuView(menuController, this, menuController.getLoggedUsers());
      menuView.start(currentStage);
   }

   public void logoutUser(User user) {
      menuController.logoutUser(user);

      if (menuController.hasNoUsers()) {
         navigateToLogin();
      } else {
         navigateToGameMenu();
      }

   }

   public void navigateToSecondUserLogin() {
      Stage currentStage;
      if (menuView != null && menuView.getScene() != null) {
         currentStage = (Stage) menuView.getScene().getWindow();
      } else {
         System.err.println("ERROR: menuView is null");
         return;
      }

      clearViewReferences();
      this.loginView = new LoginView(menuController, this);
      loginView.start(currentStage);
   }

   public void navigateToSignUp() {
      Stage currentStage;
      if (loginView != null && loginView.getScene() != null) {
         currentStage = (Stage) loginView.getScene().getWindow();
      } else {
         System.err.println("ERROR: loginView is null");
         return;
      }

      clearViewReferences();
      this.signUpView = new SignUpView(menuController, this);
      signUpView.start(currentStage);
   }

   public void navigateToHistory(User user) {
      Stage currentStage;
      if (menuView != null && menuView.getScene() != null) {
         currentStage = (Stage) menuView.getScene().getWindow();
      } else {
         System.err.println("ERROR: menuView is null");
         return;
      }

      clearViewReferences();
      this.historyView = new HistoryView(this, user);
      historyView.start(currentStage);
   }

   public void navigateToPlay() {
      Stage currentStage = null;

      if (menuView != null && menuView.getScene() != null) {
         currentStage = (Stage) menuView.getScene().getWindow();
      } else if (victoryView != null && victoryView.getScene() != null) {
         currentStage = (Stage) victoryView.getScene().getWindow();
      } else {
         System.err.println("ERROR: No se pudo obtener el Stage para navigateToPlay");
         return;
      }

      clearViewReferences();
      this.playView = new PlayView(menuController, this);
      playView.start(currentStage);
   }

   public void navigateToLogin() {
      Stage currentStage = null;

      if (signUpView != null && signUpView.getScene() != null) {
         currentStage = (Stage) signUpView.getScene().getWindow();
      } else if (menuView != null && menuView.getScene() != null) {
         currentStage = (Stage) menuView.getScene().getWindow();
      } else {
         System.err.println("ERROR: No se pudo obtener el Stage");
         return;
      }

      clearViewReferences();
      this.loginView = new LoginView(menuController, this);
      loginView.start(currentStage);
   }

   public void navigateToDeployment(String gameMode) {
      Stage currentStage = null;

      if (playView != null && playView.getScene() != null) {
         currentStage = (Stage) playView.getScene().getWindow();
      } else {
         System.err.println("ERROR: playView is null or has no scene");
         return;
      }

      clearViewReferences();

      Board playerBoard = new Board();
      User currentPlayer = menuController.getLoggedUsers().isEmpty() ? null : menuController.getLoggedUsers().get(0);

      this.deploymentView = new DeploymentView(this, gameMode, currentPlayer);
      deploymentView.start(currentStage);

      new DeploymentController(playerBoard, deploymentView, this);
   }

   public void navigateToTransition(String gameMode) {
      Stage currentStage = null;

      if (deploymentView != null && deploymentView.getScene() != null) {
         currentStage = (Stage) deploymentView.getScene().getWindow();
      } else {
         System.err.println("ERROR: deploymentView is null");
         return;
      }

      clearViewReferences();

      User nextPlayer = menuController.getLoggedUsers().size() > 1 ?
               menuController.getLoggedUsers().get(1) : null;

      this.transitionView = new TransitionView(this, nextPlayer, gameMode);
      transitionView.start(currentStage);
   }

   public void navigateToSecondPlayerDeployment(String gameMode) {
      Stage currentStage = null;

      if (transitionView != null && transitionView.getScene() != null) {
         currentStage = (Stage) transitionView.getScene().getWindow();
      } else {
         System.err.println("ERROR: transitionView is null");
         return;
      }

      clearViewReferences();

      Board player2Board = new Board();
      User player2 = menuController.getLoggedUsers().size() > 1 ?
               menuController.getLoggedUsers().get(1) : null;

      this.deploymentView = new DeploymentView(this, gameMode, player2);
      deploymentView.start(currentStage);

      new DeploymentController(player2Board, deploymentView, this);
   }

   public void navigateToGame(Board player1Board, Board player2Board, String gameMode) {
      Stage currentStage = null;

      if (deploymentView != null && deploymentView.getScene() != null) {
         currentStage = (Stage) deploymentView.getScene().getWindow();
      } else {
         System.err.println("ERROR: deploymentView is null");
         return;
      }

      clearViewReferences();

      User player1 = menuController.getLoggedUsers().isEmpty() ? null : menuController.getLoggedUsers().get(0);
      User player2 = null;

      if (gameMode.equals("PVC")) {
         player2Board = new Board();
         Board.placeShipsRandomly(player2Board);
         player2 = null;
      } else {
         player2 = menuController.getLoggedUsers().get(1);
      }

      this.gameView = new GameView(this, player1, player2, gameMode);
      gameView.start(currentStage);

      new GameController(player1Board, player2Board, gameView, menuController, this);
   }

   public void navigateToVictory(User winner, User loser, String gameMode, int totalTurns,
                                 String timePlayed, long timePlayedMillis,
                                 int winnerShipsSunk, int loserShipsSunk) {
      Stage currentStage = null;

      if (gameView != null && gameView.getScene() != null) {
         currentStage = (Stage) gameView.getScene().getWindow();
      } else {
         System.err.println("ERROR: gameView is null");
         return;
      }

      clearViewReferences();

      this.victoryView = new VictoryView(this, winner, loser, gameMode, totalTurns,
               timePlayed, timePlayedMillis,
               winnerShipsSunk, loserShipsSunk);
      victoryView.start(currentStage);
   }

   // ==================== CLASE INTERNA ====================

   public static class NavigationApplication extends Application {
      @Override
      public void start(Stage primaryStage) {
         MenuController menuController = new MenuController();
         NavigationController controller = new NavigationController(menuController);
         controller.initializeView(primaryStage);
      }
   }

}