<h1 align="center">
    Reproductor de musica offline
</h1>

<p align="center">
  <img src="preview-1.png" alt="preview" height="300">
</p>

["preview"]()

## Características

- Interfaz en JavaFX
- Reproduccion de audio con VLC
- Visualizacion de metadatos (titulo,caratula,artista,etc).

---

## 🚀 Requisitos para ejecutar
 
- VLC 3.x instalado o carpeta con las librerias de VLC.
- Sistema Operativo Windows o Linux.


### Ejecucion en entorno de desarrollo

#### 1. descarga las dependencias
```bash
mvn clean install
```

#### 2. ejecutar
```bash
mvn javafx:run
```


---

## 🛠️ Empaquetacion

### Requisitos
- Java 25 o superior
- Maven, JLink y JPackage
- JavaFX 25 o superior
- Jmods de JAVAFX

### 1. ejecuta el script para empaquetar la app

Especificar la ubicacion de los jmods de javafx y el sdk

```bash
./build.sh <ruta/a/tu/javafx_jmods/> <ruta/a/tu/javafx_sdk>
```

