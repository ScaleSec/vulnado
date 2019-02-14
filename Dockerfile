FROM tomcat

RUN apt-get update && \
    apt-get install maven default-jdk -y && \
    update-alternatives --config javac
COPY . .

CMD ["mvn", "spring-boot:run"]
