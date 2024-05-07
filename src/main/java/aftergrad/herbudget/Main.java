package aftergrad.herbudget;

import java.io.IOException;
/**
 *
 * @author David Beltran
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Statement statement = new Statement("NovDec.pdf");
        statement.getExpenses();
    }
}
