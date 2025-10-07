<h1 align="center">
    Reproductor de musica offline
</h1>


<div style="display: flex; justify-content: center; align-items: center;">
<img src="preview-1.png" alt="preview" height="300">
</div>

## Características

- Interfaz en JavaFX
- Reproduccion de audio con VLC
- Visualizacion de metadatos (titulo,caratula,artista,etc).

---

## 🚀 Requisitos para ejecutar
 
- VLC 3.x instalado o carpeta con las librerias de VLC.
- Sistema Operativo Windows o Linux.

---

## 📦 Requisitos para compilar

- Java 25 o superior
- JavaFX 25 o superior
- Jmods de JAVAFX
- Maven

---

## 🛠️ Compilación

### 1. descarga las dependencias
```bash
mvn clean install
```

### 2. ejecuta el script para empaquetar la app
```bash
./build.sh
```

