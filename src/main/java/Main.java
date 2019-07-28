public class Main {
    public static void main(String[] args) {
        CertGenerator certGenerator = new CertGenerator();
        certGenerator.generateCertificates(args[0]);
    }
}
