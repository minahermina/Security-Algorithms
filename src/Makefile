LFSR_FILE = lfsr
DES_FILE = DES
AES_FILE = AES
RSA_FILE = RSA
CLASS_LFSR = $(LFSR_FILE).class
CLASS_DES = $(DES_FILE).class
CLASS_AES = $(AES_FILE).class
CLASS_RSA = $(RSA_FILE).class

all: $(CLASS_LFSR) $(CLASS_DES) $(CLASS_AES) $(CLASS_RSA)

$(CLASS_LFSR): $(LFSR_FILE).java
	@echo "[INFO] Compiling $< to $@"
	@echo -n "---> "
	javac $<

$(CLASS_DES): $(DES_FILE).java
	@echo "[INFO] Compiling $< to $@"
	@echo -n "---> "
	javac $<

$(CLASS_AES): AES.java MCTables.java
	@echo "[INFO] Compiling AES files"
	@echo -n "---> "
	javac AES.java MCTables.java

$(CLASS_RSA): $(RSA_FILE).java
	@echo "[INFO] Compiling $< to $@"
	@echo -n "---> "
	javac $<

lfsr: $(CLASS_LFSR)
	@echo "[INFO] Running $(LFSR_FILE)..."
	@echo -n "---> "
	java $(LFSR_FILE)

des: $(CLASS_DES)
	@echo "[INFO] Running $(DES_FILE)..."
	@echo -n " ---> "
	java $(DES_FILE)

aes: $(CLASS_AES)
	@echo "[INFO] Running $(AES_FILE)..."
	@echo -n "---> "
	java $(AES_FILE)

rsa: $(CLASS_RSA)
	@echo "[INFO] Running $(RSA_FILE)..."
	@echo -n "---> "
	java $(RSA_FILE)

clean:
	@echo "[INFO] Cleaning up generated files"
	@echo -n "---> "
	rm -f *.class encrypted.txt decrypted.txt private_key.txt

.PHONY: all lfsr des aes rsa clean
