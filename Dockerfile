FROM eclipse-temurin:8-jdk AS build
WORKDIR /build

COPY lib/ ./lib/
COPY src/ ./src/
COPY Manifest.txt .

RUN mkdir bin && \
    javac -cp "lib/*" -d bin src/com/example/signerws/*.java && \
    jar cfm signer-ws.jar Manifest.txt -C bin .

FROM eclipse-temurin:8-jre
WORKDIR /app

COPY --from=build /build/signer-ws.jar .
COPY --from=build /build/lib/ ./lib/

COPY lisans/ ./lisans/
COPY ["sertifika deposu/", "./sertifika deposu/"]
COPY config/ ./config/
COPY config.properties .

EXPOSE 8080

CMD ["java", "-cp", "signer-ws.jar:lib/*", "com.example.signerws.SignerWsServer"]