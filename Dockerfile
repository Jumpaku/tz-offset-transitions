FROM azul/zulu-openjdk:21.0.1-21.30.15

RUN DEBIAN_FRONTEND=noninteractive apt update -y \
    && DEBIAN_FRONTEND=noninteractive apt install -y curl

RUN curl https://cdn.azul.com/tools/ziupdater1.1.1.1-jse8+7-any_jvm.tar.gz | tar -zxv \
    && mkdir -p /workspace \
    && mv ziupdater-1.1.1.1.jar /workspace/ziupdater.jar

COPY gradle /workspace/gradle
COPY gradle.properties /workspace/gradle.properties
COPY gradlew /workspace/gradlew

# Add gradle wrapper
RUN /workspace/gradlew --version

COPY build.gradle /workspace/build.gradle
COPY settings.gradle /workspace/settings.gradle
COPY src /workspace/src

WORKDIR /workspace
