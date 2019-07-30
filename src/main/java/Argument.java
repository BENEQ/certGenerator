import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Argument {
    NameArgument name;
    private String value;

    public Argument() {
    }

    public Argument(NameArgument name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return name == argument.name &&
                Objects.equals(value, argument.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }
}
