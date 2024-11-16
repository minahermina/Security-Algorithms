JAVA_FILE = lfsr
CLASS_FILE = $(JAVA_FILE).class

all: $(CLASS_FILE)

$(CLASS_FILE): $(JAVA_FILE).java
	@echo "[INFO] Compiling $< to $@"
	@echo -n "---> "
	javac $<

run: all
	@echo "[INFO] Running $(JAVA_FILE)..."
	@echo -n "---> "
	java $(JAVA_FILE)

clean:
	@echo "[INFO] Cleaning up $(CLASS_FILE)"
	@echo -n "---> "
	rm -f $(CLASS_FILE)

.PHONY: all run clean

