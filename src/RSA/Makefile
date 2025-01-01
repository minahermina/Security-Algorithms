JC = javac
.SUFFIXES: .java .class

SRCS = $(wildcard *.java)
CLASSES = $(SRCS:.java=.class)

all: $(CLASSES)

.java.class:
	$(JC) $<

clean:
	rm -f *.class
