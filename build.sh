set -e
trap 'echo "❌ Error en la ejecución del script."; exit 1;' ERR

# Variables por defecto
VERBOSE=false
PARAMS=()

# Procesar argumentos
for arg in "$@"; do
  if [[ "$arg" == "--verbose" || "$arg" == "-v" ]]; then
    VERBOSE=true
  else
    PARAMS+=("$arg")
  fi
done

# Validar que queden exactamente 2 parámetros (excluyendo verbose)
if [ "${#PARAMS[@]}" -ne 2 ]; then
  echo "Uso: $0 [--verbose|-v] <ruta_javafx_mods> <ruta_lib>"
  exit 1
fi


JAVAFX_LIB=$1
JAVAFX_JMODS=$2

# Configuraciones
APP_NAME="DemoApp"
MAIN_CLASS="libraries.demo.application.App"
MAIN_JAR="Demo-1.0-SNAPSHOT.jar"
JAVA_RUNTIME="javafx-runtime"
DEST_DIR="dist"

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

echo "Editar este script y personalize los directorios LIB y JMODS de javafx por los suyos propios"
echo "...."
echo "....."
echo "!!!!!INICIANDO EMPAQUETACION!!!!!! usa el parametro --verbose o -v para ver los detalles"
echo "..."

# ========================
# 1. Limpiar y compilar
# ========================
echo "🧹 Limpiando y compilando el proyecto..."
run mvn clean package -Pprod

# ========================
# Copiar dependencias necesarias
# ========================
echo "📦 Copiando dependencias..."
run mvn dependency:copy-dependencies -DincludeScope=runtime

# ========================
# Crear runtime con jlink
# ========================
echo "⚙️  Generando runtime personalizado..."
run rm -rf "./$JAVA_RUNTIME"
run jlink \
  --module-path "$JAVAFX_JMODS:$JAVAFX_LIB" \
  --add-modules java.base,java.desktop,java.naming,java.logging,javafx.controls,javafx.fxml \
  --output $JAVA_RUNTIME \
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
    --dest "$DEST_DIR" \
    --runtime-image "$JAVA_RUNTIME" \
    --app-version 1.0 \
    --java-options "--enable-native-access=ALL-UNNAMED --enable-native-access=javafx.graphics"



# shellcheck disable=SC2028
echo "✅ Proceso completado. Empaquetado en la carpeta  dist/$DIR"
echo "Porfavor ejecutarlo desde dicha carpeta para que se genere la configuracion correctamente"
