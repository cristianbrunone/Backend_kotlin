FROM gitpod/workspace-full

# Instala JDK 11 (si no está ya disponible en la imagen base)
RUN sudo apt-get update && sudo apt-get install -y openjdk-11-jdk

# Otras herramientas que necesites
