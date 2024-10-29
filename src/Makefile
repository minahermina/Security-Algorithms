# Variables
JAVA_FILES = $(wildcard *.java)
CLASS_FILES = $(JAVA_FILES:.java=.class)
MAIN_CLASS = Main # Change this to your main class name

# Targets
all: compile

compile: $(CLASS_FILES)

%.class: %.java
	@echo "[INFO] Compiling $< to $(CLASS_FILES)"
	@echo -n "---> "
	javac $<

run: compile
	@echo "[INFO] Running $(MAIN_CLASS)..."
	@echo -n "---> "
	java $(MAIN_CLASS)

clean:
	@echo "[INFO] Cleaning up $(CLASS_FILES)"
	@echo -n "---> "
	rm -f *.class

.PHONY: all compile run clean
