import lombok.Getter;

public enum NameArgument {
    tsFile(true, "TrueStoreFile", " path to file with truestore, default truestore.jks"),
    tsPass(true, "TrueStorePassword", " password to file with truestore, default changeit"),
    key(true, "Key", " path to file with key"),
    keyType(true, "KeyType", " type key, default PKCS12"),
    keyPass(true, "KeyPassword", " password to file with key"),
    keyCPass(true,"KeyClientPassword", "passwort to client key in file with key, default this same how key password"),
    ksFile(true, "KeyStoreFile", " path to file with keystore, default keyestore.jks"),
    ksPass(true, "KeyStorePassword", " password to file with keystore, default changeit123!"),
    kskAlias(true,"KeyStoreKeyAlias","alias to key in generate keystore"),
    kskPass(true,"KeyStoreKeyPassword","password to key in generate keystore, default this same how key store password"),
    gks(false, "GenerateKeyStore", "when use this flag, application generate keystore"),
    help(false, "Help", "print all command with description"),
    url(true, "URL", "adres URL serwera with certificates");

    private boolean isValue;
    @Getter
    private String fullName;
    @Getter
    private String description;

    NameArgument(boolean isValue, String fullName, String description) {
        this.isValue = isValue;
        this.fullName = fullName;
        this.description = description;
    }

    public boolean isValue() {
        return isValue;
    }

}
