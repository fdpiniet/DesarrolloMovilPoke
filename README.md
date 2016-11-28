DesarrolloMovilPoke
===================

Simulador de batallas Pokémon sencillas. Usa información de [pokeapi.co](http://pokeapi.co). No sigue las reglas de combate de los videojuegos originales.

*Presentado como proyecto final, para el curso "Desarrollo Móvil".*

Juego
-----

Es un juego de dos jugadores, en donde cada jugador (usuario y máquina) usa un Pokémon elegido al azar. Los Pokémon elegidos pueden estar desde el número 1 hasta el número 721, inclusivo (generaciónes I, II, III, IV, V y VI, en otra palabras) en el Pokédex Nacional.

Los datos de los Pokémon son cargados de la web. Se requiere una conexión a Internet para jugar.

El juego no sigue las reglas de combate de los juegos orginales. No se toman en cuenta los stats reales de cada Pokémon. El usuario y la máquina tienen una probabilidad igual de atacar primero en un turno. Cada ataque peude hacer de 1 a 20 daño, al azar.

Todo Pokémon inicia con 100 puntos de vida. El primer entrenador cuyo Pokémon tenga un valor de vida igual a 0 será declarado perdedor, y el otro será declarado ganador.

Screenshots
-----------

![Capturas de pantalla de aplicación.](https://dl.dropboxusercontent.com/u/92267203/Static/uni/Desarrollo%20M%C3%B3vil/Poke/screenshot-poke.png)

Cambios
-------

Los siguientes cambios fueron hechos después de hora de clases.

* Agregada dependencia *"[Glide](https://github.com/bumptech/glide)"*; usado para facilitar la descarga y muestra de sprites de Pokémon en ImageViews.
* Muchos cambios estéticos en el archivo *activity_main.xml*.
* Añadidos un par de mensajes para hacer la UI un poco mas intuitiva.

Todos los cambios hechos afectan únicamente la apariencia / presentación de la aplicación.
