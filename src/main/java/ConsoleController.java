import java.util.ArrayList;
import java.util.List;

public class ConsoleController {
    public List<Argument> cutsArguments(String[] args) throws CertGenException {
        List<Argument> arguments = new ArrayList<>();
        for (int i = 0; i < args.length; ) {
            if ("-".equals(args[i].substring(0, 1))) {
                Argument arg = new Argument();
                try {
                    arg.setName(NameArgument.valueOf(args[i].substring(1)));
                } catch (IllegalArgumentException e) {
                    throw new CertGenException("Niepoprawne polecenie " + args[i], e);
                }
                i++;
                if (arg.getName().isValue()) {
                    arg.setValue(args[i]);
                    i++;
                }
                arguments.add(arg);
            } else {
                Argument arg = new Argument();
                arg.setName(NameArgument.url);
                arg.setValue(args[i]);
                arguments.add(arg);
                i++;
            }
        }
        return arguments;
    }

    public void execute(List<Argument> listTask) throws CertGenException {
        CertGenerator certGenerator = new CertGenerator();
        String URL = null;
        for (Argument arg : listTask) {
            switch (arg.getName()) {
                case tsFile:
                    certGenerator.setTrueStoreFile(arg.getValue());
                    break;
                case tsPass:
                    certGenerator.setTruestorePassword(arg.getValue());
                    break;
                case ksFile:
                    certGenerator.setKeyStoreFile(arg.getValue());
                    break;
                case ksPass:
                    certGenerator.setKeyStorePassword(arg.getValue());
                    break;
                case kskPass:
                    certGenerator.setKeyStoreKeyPassword(arg.getValue());
                    break;
                case kskAlias:
                    certGenerator.setKeyStoreKeyAlias(arg.getValue());
                    break;
                case key:
                    certGenerator.setKeyFile(arg.getValue());
                    break;
                case keyType:
                    certGenerator.setKeyType(arg.getValue());
                    break;
                case keyPass:
                    certGenerator.setKeyPassword(arg.getValue());
                    break;
                case keyCPass:
                    certGenerator.setKeyClientPassword(arg.getValue());
                    break;
                case gks:
                    certGenerator.setGenerateKS(true);
                    break;
                case url:
                    URL = arg.getValue();
                    break;
                case help:
                    printCommand();
                    return;
            }
        }
        certGenerator.generateCertificates(URL);
        System.out.printf("Zapis certyfikatow do pliku przbiegl pomyslnie.");
    }

    private void printCommand() {
        System.out.printf("All arguments:%n");
        for (NameArgument arg : NameArgument.values()) {
            System.out.printf("-%-9s %-8s %s - %s%n", arg.name(), (arg.isValue() ? " [...]" : " flag"), arg.getFullName(), arg.getDescription());
        }
    }
}
