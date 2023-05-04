FROM gradle:jdk18

WORKDIR /app

COPY . .

RUN sed -i -e 's/\r$//' ./gradlew

RUN chmod +x ./gradlew

RUN ./gradlew build

WORKDIR /app/build/libs

CMD ["java", "-jar", "./smallcloud-0.0.1-SNAPSHOT.jar"]

