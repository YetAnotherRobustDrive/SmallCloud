FROM openjdk:11

WORKDIR /app

COPY ./gradlew .
COPY ./gradle ./gradle
COPY ./build.gradle .
COPY ./settings.gradle .
COPY ./gradlew.bat .

RUN chmod +x ./gradlew
RUN sed -i -e 's/\r$//' ./gradlew

RUN ./gradlew dependencies

COPY . .

RUN ./gradlew build

WORKDIR /app/build/libs

CMD ["java", "-jar", "./smallcloud-0.0.1-SNAPSHOT.jar"]

