package com.navyattack.view;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Representa la vista de transición entre turnos durante la batalla PvP.
 * Se utiliza para evitar que los jugadores vean el tablero del oponente
 * antes de continuar con su turno.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class TurnTransitionView {

    /** Ventana principal donde se mostrará la vista. */
    private Stage stage;

    /** Escena original del juego antes de la transición. */
    private Scene originalGameScene;

    /** Nombre del jugador que tomará el siguiente turno. */
    private String currentPlayerName;

    /** Acción a ejecutar al continuar con el siguiente turno. */
    private Runnable onContinue;

    /**
     * Crea una nueva vista de transición entre turnos.
     *
     * @param stage              ventana principal donde se mostrará la vista
     * @param originalGameScene  escena original del juego
     * @param currentPlayerName  nombre del jugador actual
     * @param onContinue         acción que se ejecutará al presionar continuar
     */
    public TurnTransitionView(Stage stage, Scene originalGameScene, String currentPlayerName, Runnable onContinue) {
        this.stage = stage;
        this.originalGameScene = originalGameScene;
        this.currentPlayerName = currentPlayerName;
        this.onContinue = onContinue;
    }

    /**
     * Muestra la vista de transición en la ventana principal.
     * Permite al jugador confirmar que el otro no está mirando antes de continuar.
     */
    public void show() {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #34495e;");

        Label title = new Label("⏸️ TURN CHANGE ⏸️");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(javafx.scene.paint.Color.WHITE);

        VBox messageBox = createMessageBox();

        Button btnContinue = new Button("I'M READY - CONTINUE");
        btnContinue.setPrefWidth(250);
        btnContinue.setPrefHeight(50);
        styleButton(btnContinue);
        btnContinue.setOnAction(e -> {
            stage.setScene(originalGameScene);

            if (onContinue != null) {
                onContinue.run();
            }
        });

        root.getChildren().addAll(title, messageBox, btnContinue);

        Scene transitionScene = new Scene(root, 800, 600);
        stage.setScene(transitionScene);
    }

    /**
     * Crea el cuadro de mensajes informativos que se muestra durante la transición.
     *
     * @return un contenedor VBox con las instrucciones visuales para el jugador
     */
    private VBox createMessageBox() {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));
        box.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.2);" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;"
        );
        box.setMaxWidth(600);

        Label instruction1 = new Label("It's now " + currentPlayerName + "'s turn!");
        instruction1.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        instruction1.setTextFill(javafx.scene.paint.Color.YELLOW);

        Label instruction2 = new Label("⚠️ Make sure the other player is not looking!");
        instruction2.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        instruction2.setTextFill(javafx.scene.paint.Color.web("#FFB6C1"));
        instruction2.setWrapText(true);

        Label instruction3 = new Label("When you're ready, click the button below to continue");
        instruction3.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        instruction3.setTextFill(javafx.scene.paint.Color.WHITE);
        instruction3.setWrapText(true);

        box.getChildren().addAll(instruction1, instruction2, instruction3);
        return box;
    }

    /**
     * Aplica estilo al botón principal, incluyendo color, tipografía y efectos de hover.
     *
     * @param btn botón al que se aplicará el estilo visual
     */
    private void styleButton(Button btn) {
        btn.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #2980b9;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;" +
                        "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 25px;" +
                        "-fx-padding: 15px 30px;"
        ));
    }
}
