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
  echo "Uso: $0 <ruta_javafx_mods> <ruta_javafx_sdk> [--verbose|-v]"
  exit 1
fi

JAVAFX_SDK=${PARAMS[0]}
JAVAFX_JMODS=${PARAMS[1]}

if [ -d "$JAVAFX_SDK/lib" ]; then
  JAVAFX_SDK="$JAVAFX_SDK/lib"
else
  JAVAFX_SDK="$JAVAFX_SDK"  # fallback
fi

# Configuraciones
APP_NAME="DemoApp"
MAIN_CLASS="libraries.demo.application.App"
MAIN_JAR="Demo-1.0-SNAPSHOT.jar"
JAVA_RUNTIME="javafx-runtime"
DEST_DIR="dist"

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
  PATH_SEPARATOR=":"
else
  PATH_SEPARATOR=";"
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

echo "!!!!! INICIANDO EMPAQUETACIÓN !!!!!! Usa el parámetro --verbose o -v para ver los detalles"
echo "..."

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
# 3. Crear runtime con jlink
# ========================
echo "⚙️  Generando runtime personalizado..."
run rm -rf "./$JAVA_RUNTIME"

run jlink \
  --module-path "$JAVAFX_JMODS$PATH_SEPARATOR$JAVAFX_SDK" \
  --add-modules javafx.controls,javafx.fxml,javafx.graphics,jdk.unsupported,java.desktop,java.logging,java.naming,java.base \
  --output $JAVA_RUNTIME \
  --strip-debug \
  --compress=2 \
  --no-header-files \
  --no-man-pages

# ========================
# 4. Crear ejecutable con jpackage
# ========================
echo "📦 Empaquetando aplicación..."

run rm -rf "$DEST_DIR/$APP_NAME"

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

echo "✅ Proceso completado. Ejecutable creado en: dist/$APP_NAME"
echo "Ejecuta desde esa carpeta para probar tu aplicación."
