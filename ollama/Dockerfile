FROM --platform=$BUILDPLATFORM ollama/ollama:0.1.48
RUN /bin/sh -c "/bin/ollama serve & sleep 1 && ollama pull llama3 && ollama run llama3"
ENTRYPOINT ["/bin/ollama"]
CMD ["serve"]