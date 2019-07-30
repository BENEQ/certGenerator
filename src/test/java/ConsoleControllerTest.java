import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ConsoleControllerTest {

    @Test
    void cutsArgumentsTest1() {
        String[] givenArgs = {"-key", "path/to/key"};
        List<Argument> expectedList = new ArrayList<>();
        Argument argExpected = new Argument(NameArgument.key,"path/to/key");
        expectedList.add(argExpected);

        ConsoleController cc = new ConsoleController();
        List<Argument> cutsArg = cc.cutsArguments(givenArgs);

        assertIterableEquals(expectedList, cutsArg);
    }

    @Test
    void cutsArgumentsTest2() {
        String[] givenArgs = {"-key", "path/to/key", "http://google.pl"};
        List<Argument> expectedList = new ArrayList<>();
        expectedList.add(new Argument(NameArgument.key, "path/to/key"));
        expectedList.add(new Argument(NameArgument.url, "http://google.pl"));

        ConsoleController cc = new ConsoleController();
        List<Argument> cutsArg = cc.cutsArguments(givenArgs);

        assertIterableEquals(expectedList, cutsArg);
    }

    @Test
    void cutsArgumentsTest3() {
        String[] givenArgs = {"http://google.pl", "-key", "path/to/key"};
        List<Argument> expectedList = new ArrayList<>();
        expectedList.add(new Argument(NameArgument.url, "http://google.pl"));
        expectedList.add(new Argument(NameArgument.key, "path/to/key"));

        ConsoleController cc = new ConsoleController();
        List<Argument> cutsArg = cc.cutsArguments(givenArgs);

        assertIterableEquals(expectedList, cutsArg);
    }

    @Test
    void cutsArgumentsTest4() {
        String[] givenArgs = {"-key", "path/to/key","-keyType","JKS", "http://google.pl"};
        List<Argument> expectedList = new ArrayList<>();
        expectedList.add(new Argument(NameArgument.key, "path/to/key"));
        expectedList.add(new Argument(NameArgument.keyType, "JKS"));
        expectedList.add(new Argument(NameArgument.url, "http://google.pl"));

        ConsoleController cc = new ConsoleController();
        List<Argument> cutsArg = cc.cutsArguments(givenArgs);

        assertIterableEquals(expectedList, cutsArg);
    }

    @Test
    void cutsArgumentsTest5() {
        String[] givenArgs = {"-key", "path/to/key","http://google.pl","-keyType","JKS"};
        List<Argument> expectedList = new ArrayList<>();
        expectedList.add(new Argument(NameArgument.key, "path/to/key"));
        expectedList.add(new Argument(NameArgument.url, "http://google.pl"));
        expectedList.add(new Argument(NameArgument.keyType, "JKS"));

        ConsoleController cc = new ConsoleController();
        List<Argument> cutsArg = cc.cutsArguments(givenArgs);

        assertIterableEquals(expectedList, cutsArg);
    }
}