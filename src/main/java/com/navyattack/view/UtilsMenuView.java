package com.navyattack.view;

import javafx.scene.Cursor;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * Clase utilitaria que contiene métodos de apoyo para aplicar estilos
 * y comportamientos visuales en los elementos del menú de la aplicación.
 * 
 * Proporciona funcionalidades para dar formato a botones, textos,
 * mostrar mensajes de estado y crear imágenes de iconos con un estilo uniforme.
 * 
 * @author Juan Manuel Otálora Hernández - Johan Stevan Bermeo Buitrago
 * @version 1.0
 */
public class UtilsMenuView {

    /**
     * Aplica un estilo personalizado a un botón, incluyendo colores base y de hover,
     * texto, tamaño de fuente y relleno interno.
     *
     * @param button      botón al que se le aplicará el estilo
     * @param color       color de fondo por defecto del botón
     * @param colorHover  color de fondo cuando el cursor pasa sobre el botón
     * @param colorText   color del texto del botón
     * @param padding     valor de relleno interno en formato CSS (ejemplo: "10px 20px")
     */
    public static void styleButton(Button button, String color, String colorHover, String colorText, String padding) {
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: " + colorText + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 5;" +
 	        "-fx-padding: " + padding + ";" 
        );
        
        button.setOnMouseEntered(e -> {
                button.setStyle(
                    "-fx-background-color: " + colorHover + ";" +
                    "-fx-text-fill: " + colorText + ";" +
                    "-fx-font-size: 12px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-background-radius: 5;" +
		            "-fx-padding: " + padding + ";"
                );
                button.setCursor(Cursor.HAND);
            }
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: " + colorText + ";" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: " + padding + ";"
            )
        );
    }

    /**
     * Aplica estilo a un texto interactivo, incluyendo color, tamaño de fuente,
     * subrayado y cambio de color cuando el cursor pasa sobre él.
     *
     * @param text texto al que se le aplicará el estilo
     */
    public static void styleText(Text text) {
        text.setStyle(
            "-fx-fill: #0066cc;" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-underline: true;"
        );
        text.setOnMouseEntered(e -> 
            text.setStyle(
                "-fx-fill: #004499;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-underline: true;" +
                "-fx-cursor: hand;"
            )
        );
        text.setOnMouseExited(e -> 
            text.setStyle(
                "-fx-fill: #0066cc;" +
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-underline: true;"
            )
        );
    }

    /**
     * Muestra un mensaje informativo o de error en una etiqueta de texto.
     * 
     * @param message       texto del mensaje a mostrar
     * @param type          tipo de mensaje ("success" o "error")
     * @param messageLabel  etiqueta de texto donde se mostrará el mensaje
     */
    public static void showMessage(String message, String type, Text messageLabel) {
        messageLabel.setText(message);
        
        if (type.equals("success")) {
            messageLabel.setStyle("-fx-fill: green;");
        } else {
            messageLabel.setStyle("-fx-fill: red;");
        }
    }

    /**
     * Crea y devuelve una imagen con dimensiones ajustadas y proporciones preservadas.
     * 
     * @param path ruta del archivo de imagen (local o recurso)
     * @return objeto {@link ImageView} configurado con la imagen cargada
     */
    public static ImageView createImage(String path) {
        ImageView iconImage = new ImageView();
        try {
            Image image = new Image(path); 
            iconImage.setImage(image);
            iconImage.setFitWidth(40); 
            iconImage.setFitHeight(40); 
            iconImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la imagen: " + e.getMessage());
        }
        return iconImage;
    }
}
