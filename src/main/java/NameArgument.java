public enum NameArgument {
    tsname(true),
    tspass(true),
    key(true),
    keyType(true),
    url(true),
    u(true);
    private boolean isValue;

    NameArgument(boolean isValue) {
        this.isValue = isValue;
    }

    public boolean isValue() {
        return isValue;
    }

}
