import java.util.ArrayList;
import java.util.List;

public class ConsoleController {
    public List<Argument> cutsArguments(String[] args){
        List<Argument> arguments = new ArrayList<>();
        for(int i=0;i<args.length;){
            if("-".equals(args[i].substring(0,1))){
                Argument arg = new Argument();
                arg.setName(NameArgument.valueOf(args[i].substring(1)));
                i++;
                if(arg.getName().isValue()){
                    arg.setValue(args[i]);
                    i++;
                }
                arguments.add(arg);
            }else {
                Argument arg = new Argument();
                arg.setName(NameArgument.url);
                arg.setValue(args[i]);
                arguments.add(arg);
                i++;
            }
        }
        return arguments;
    }

    public void execute(List<Argument> listTask){
        CertGenerator certGenerator=new CertGenerator();
        String URL=null;
        for (Argument arg:listTask){
            switch(arg.getName()){
                case tsname:
                    certGenerator.setOutTrueStoreName(arg.getValue());
                    break;
                case tspass:
                    certGenerator.setTruestorePassword(arg.getValue());
                    break;
                case key:
                case keyType:
                case url:
                    URL = arg.getValue();
                    break;
            }
        }
        certGenerator.generateCertificates(URL);

    }
}
