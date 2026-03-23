FROM eclipse-temurin:8-jdk

WORKDIR /app

COPY . .

EXPOSE 8080

CMD ["java", "-cp", "signer-ws.jar:lib/*", "com.example.signerws.SignerWsServer"]
