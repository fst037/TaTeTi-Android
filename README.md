# TicTacTron

https://github.com/fst037/TaTeTi-Android

**TicTacTron** es un juego de **Ta Te Ti** desarrollado en **Kotlin** para dispositivos Android utilizando **Android Studio**. Este proyecto implementa una interfaz moderna y atractiva con animaciones y un diseño inspirado en el estilo de la película "Tron".

## Descripción del Juego

El objetivo del juego es alinear tres símbolos iguales (X u O) en una fila, columna o diagonal antes que el oponente. El jugador puede elegir su símbolo (X u O) y enfrentarse a la CPU, que cuenta con una lógica básica para intentar ganar o bloquear al jugador.

El juego incluye las siguientes características:
- **Selección de equipo**: El jugador puede elegir si quiere jugar con "X" o "O".
- **CPU**: La CPU realiza movimientos estratégicos para intentar ganar o bloquear al jugador.
- **Tabla de posiciones**: Se registra el número de victorias de cada jugador en un sistema de almacenamiento local.
- **Interfaz animada**: Fondo dinámico con partículas y colores inspirados en el estilo "Tron".

## Cómo Funciona

1. **Pantalla Principal**:  
   - El jugador ingresa su nombre y selecciona su equipo (X u O).
   - Puede iniciar una partida o consultar la tabla de posiciones.

2. **Pantalla de Juego**:  
   - El tablero tiene 9 celdas (3x3). El jugador y la CPU alternan turnos para colocar sus símbolos.
   - La CPU analiza el tablero para intentar ganar o bloquear al jugador.
   - Si un jugador alinea tres símbolos iguales o si el tablero se llena sin ganador, el juego termina.

3. **Tabla de Posiciones**:  
   - Muestra una lista de jugadores con el número de partidas ganadas, almacenadas en el dispositivo.

4. **Opciones**:  
   - Reiniciar el juego.
   - Volver al menú principal.
   - Consultar la tabla de posiciones.

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Almacenamiento**: SharedPreferences para guardar las victorias de los jugadores.
- **IDE**: Android Studio
