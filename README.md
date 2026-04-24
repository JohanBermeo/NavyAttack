# NavyAttack 🚢

Un juego de batalla naval desarrollado en Java que implementa el clásico juego de estrategia donde los jugadores deben hundir todos los barcos enemigos para ganar.

## 📋 Descripción

NavyAttack es una implementación moderna del tradicional juego de batalla naval (Battleship) que permite enfrentamientos tanto contra la computadora como contra otro jugador humano. El juego utiliza el patrón de arquitectura MVC (Modelo-Vista-Controlador) para una estructura de código limpia y mantenible.

## ⚓ Características del Juego

### Modos de Juego
- **Jugador vs Computadora**: Enfréntate a una IA inteligente
- **Jugador vs Jugador**: Modo multijugador local para dos personas

### Especificaciones del Tablero
- **Dimensiones**: 10x10 casillas
- **Total de barcos por jugador**: 10 unidades

### Flota Naval

Cada jugador cuenta con la siguiente flota:

| Tipo de Barco | Cantidad | Tamaño | Total de Casillas |
|---------------|----------|--------|-------------------|
| Portaviones   | 1        | 6      | 6                 |
| Cruceros      | 2        | 4      | 8                 |
| Destructores  | 3        | 3      | 9                 |
| Submarinos    | 4        | 2      | 8                 |
| **TOTAL**     | **10**   | -      | **31**            |

### Objetivo del Juego
El primer jugador que logre hundir completamente todos los barcos enemigos será declarado ganador.

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Java
- **Arquitectura**: MVC (Modelo-Vista-Controlador)
- **Interfaz**: JavaFX

## 🏗️ Arquitectura del Proyecto

```
src/
├── controller/
│   ├── MenuController.java
│   ├── DeploymentController.java
│   ├── GameController.java
│   ├── DeploymentState.java
│   ├── NavigationController.java
├── model/
│   ├── CPU.java
│   ├── Authentication.java
│   ├── AttackResult.java
│   ├── UserStatistics.java
│   ├── Orientation.java
│   ├── GameTimer.java
│   ├── DataManager.java
│   ├── User.java
│   ├── History.java
│   ├── Position.java
│   ├── Game.java
│   ├── Player.java
│   ├── Board.java
│   ├── CellState.java
│   ├── ShipType.java
│   └── Ship.java
├── view/
│   ├── components/
│       ├── BoardGridComponent.java
│   ├── GameView.java
│   ├── DeploymentView.java
│   ├── HistoryView.java
│   ├── IView.java
│   ├── LoginView.java
│   ├── PlayView.java
│   ├── SignUpView.java
│   ├── TransitionView.java
│   ├── TurnTransitionView.java
│   ├── UtilsMenuView.java
│   ├── VictoryView.java
│   ├── ViewFactory.java
│   └── MenuView.java
└── main/
    └── NavyAttack.java
```

## 🎮 Reglas del Juego

1. **Preparación**:
   - Cada jugador coloca sus 10 barcos en el tablero de 10x10
   - Los barcos pueden colocarse horizontal o verticalmente
   - Los barcos no pueden superponerse ni tocarse

2. **Desarrollo**:
   - Los jugadores se turnan para atacar casillas del tablero enemigo
   - Se indica "Agua" si no hay barco, "Impacto" si se acierta
   - Cuando todas las casillas de un barco son impactadas, se declara "Hundido"

3. **Victoria**:
   - El primer jugador en hundir todos los barcos enemigos gana la partida

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java JDK 14 o superior
- Maven 4
- IDE compatible con Java (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Pasos de Instalación

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/JohanBermeo/NavyAttack.git
   cd NavyAttack
   ```

2. **Compilar el proyecto**:
   ```bash
   javac -d bin src/main/NavyAttack.java
   ```

3. **Ejecutar el juego**:
   ```bash
   [Cambiarlo por el code de maven]
   ```

## 🎯 Características Planificadas

- [ ] Interfaz gráfica intuitiva
- [ ] Guardado y carga de partidas
- [ ] Estadísticas de jugador
- [ ] Modo multijugador en local
- [ ] Modo de juego vs Machine

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👥 Autores

- **Juan Manuel Otálora Hernández** - [Otalorah](https://github.com/Otalorah)
- **Johan Stevan Bermeo Buitrago** - [JohanBermeo](https://github.com/JohanBermeo)

## 🙏 Agradecimientos

- Inspirado en el clásico juego Battleship
- Agradecimientos especiales a la comunidad de Java

---

⭐ ¡No olvides darle una estrella al proyecto si te gusta!
