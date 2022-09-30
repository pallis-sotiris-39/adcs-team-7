FROM maven:3.8.5-openjdk-17 as build
COPY . /build
WORKDIR /build/broker/
RUN mvn -Dmaven.test.skip clean package -Pdev
CMD [ "bash", "-c", "java -jar target/broker*.jar"]