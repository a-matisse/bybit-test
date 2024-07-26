FROM maven:3.9.8-eclipse-temurin-22
RUN apt-get update && apt-get install -y git
COPY . /app
WORKDIR /app
RUN mvn clean package
CMD ["mvn", "install"]
