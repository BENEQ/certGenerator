public class Main {
    public static void main(String[] args) {
        ConsoleController cc = new ConsoleController();
        try {
            cc.execute(cc.cutsArguments(args));
        } catch (CertGenException e) {
            System.out.printf("ERROR: %s", e.getMessage());
            return;
        }
    }
}
