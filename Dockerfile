# Utilisez une image de base contenant une installation de Java
FROM bitnami/java:20

# Définissez le répertoire de travail
WORKDIR /app

# Copiez le code source Java
COPY src/*.java .

# Installez les outils Java
RUN apt-get update && apt-get install -y openjdk-11-jdk

# Compilez le code source Java
RUN javac src/*.java

# Créez le fichier JAR
RUN jar cfm my-app.jar manifest.txt src/*.class

# Exposez le port sur lequel votre application Java écoute
EXPOSE 8080

# Créez un utilisateur non root (optionnel)
RUN useradd -m appuser
USER appuser

# Commande pour exécuter votre application Java
CMD ["java", "-jar", "app.jar"]