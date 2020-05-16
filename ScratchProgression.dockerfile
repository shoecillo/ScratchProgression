FROM java:8
EXPOSE 8099
ADD ScratchProgression.jar ScratchProgression.jar
RUN git config --global user.email "shoecillo@gmail.com"
RUN git config --global user.name "shoecillo"
ENTRYPOINT ["sh","-c","java -jar ScratchProgression.jar --spring.config.location=/code/config/ScratchProgression.properties --server.port=8099"]