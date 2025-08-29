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
- **Interfaz**: Java Swing / JavaFX (por definir)

## 🏗️ Arquitectura del Proyecto

```
src/
├── controller/
│   ├── GameController.java
│   ├── PlayerController.java
│   └── AIController.java
├── model/
│   ├── Game.java
│   ├── Board.java
│   ├── Ship.java
│   ├── Player.java
│   └── Position.java
├── view/
│   ├── GameView.java
│   ├── BoardView.java
│   └── MenuView.java
└── main/
    └── NavyAttack.java
```

## 🎮 Reglas del Juego

1. **Preparación**:
   - Cada jugador coloca sus 10 barcos en el tablero de 20x20
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
- Java JDK 8 o superior
- IDE compatible con Java (IntelliJ IDEA, Eclipse, VS Code, etc.)

### Pasos de Instalación

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/tuusuario/NavyAttack.git
   cd NavyAttack
   ```

2. **Compilar el proyecto**:
   ```bash
   javac -d bin src/**/*.java
   ```

3. **Ejecutar el juego**:
   ```bash
   java -cp bin main.NavyAttack
   ```

## 🎯 Características Planificadas

- [ ] Interfaz gráfica intuitiva
- [ ] IA con diferentes niveles de dificultad
- [ ] Sistema de puntuación
- [ ] Guardado y carga de partidas
- [ ] Efectos de sonido y música
- [ ] Estadísticas de jugador
- [ ] Modo multijugador en red

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para contribuir:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

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
