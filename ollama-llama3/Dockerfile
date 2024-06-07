FROM --platform=$BUILDPLATFORM ollama/ollama:latest
RUN /bin/sh -c "/bin/ollama serve & sleep 1 && ollama pull llama3:70b && ollama run llama3:70b"
ENTRYPOINT ["/bin/ollama"]
CMD ["serve"]