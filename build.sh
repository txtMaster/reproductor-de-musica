set -e
trap 'echo "❌ Error en la ejecución del script."; exit 1;' ERR

# ========================
# Modo verbose
# ========================
VERBOSE=false

if [[ "$1" == "--verbose" || "$1" == "-v" ]]; then
  VERBOSE=true
fi

# ========================
# Función para ejecutar comandos
# ========================
run() {
  if $VERBOSE; then
    echo "➤ Ejecutando: $*"
    "$@"
  else
    "$@" > /dev/null 2>&1
  fi
}

echo "Porfavor asegurarse que se tiene los archivos necesarios para embeber vlc (archivos libvlc* y carpeta /vlc/plugins)"
echo "Agregarlos a la carpeta ./vlc/"
echo ".."
echo "Editar este script y personalize los directorios LIB y JMODS de javafx por los suyos propios"
echo "...."
echo "....."
echo "!!!!!INICIANDO EMPAQUETACION!!!!!! usa el parametro --verbose o -v para ver los detalles"
echo "..."
if [ ! -d "./vlc" ]; then
  echo "❌ Error: La carpeta './vlc' no existe. Abortando."
  exit 1
fi
run rm -rf vlc/plugins/gui \
       vlc/plugins/spu \
       vlc/plugins/text_renderer \
       vlc/plugins/vaapi \
       vlc/plugins/vdpau \
       vlc/plugins/video_*


# Configuraciones
APP_NAME="DemoApp"
MAIN_CLASS="libraries.demo.application.App"
MAIN_JAR="Demo-1.0-SNAPSHOT.jar"
RUNTIME="javafx-runtime"
LIB="$JAVAFX_HOME/lib"
JMODS="$JAVAFX_JMODS"

# ========================
# 1. Limpiar y compilar
# ========================
echo "🧹 Limpiando y compilando el proyecto..."
run mvn clean package -Pprod

# ========================
# 2. Copiar dependencias necesarias
# ========================
echo "📦 Copiando dependencias..."
run mvn dependency:copy-dependencies -DincludeScope=runtime

# ========================
# 3. Copiar VLC plugins a target/
# ========================
echo "🎵 Agregando archivos nativos de VLC..."
run cp -r -f vlc/ target/

# ========================
# 5. Crear runtime con jlink
# ========================
echo "⚙️  Generando runtime personalizado..."
run rm -rf "./$RUNTIME"
run jlink \
  --module-path "$JMODS:$LIB" \
  --add-modules java.base,java.desktop,java.naming,java.logging,javafx.controls,javafx.fxml \
  --output $RUNTIME \
  --strip-debug \
  --compress=2 \
  --no-header-files \
  --no-man-pages

# ========================
# 6. Crear ejecutable con jpackage
# ========================
echo "📦 Empaquetando aplicación..."
run rm -rf "dist/$APP_NAME"
run jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --input target \
  --main-jar "$MAIN_JAR" \
  --main-class "$MAIN_CLASS" \
  --dest dist \
  --runtime-image javafx-runtime \
  --java-options "
  --enable-native-access=ALL-UNNAMED
  --enable-native-access=javafx.graphics
  -Djna.library.path=\$APPDIR/lib/app/vlc"


echo "✅ Proceso completado. Proyecto empaquetado en en la carpeta dist/$APP_NAME"
echo "✅ Comando para ejecutar el proyecto: ./dist/$APP_NAME/bin/$APP_NAME"

