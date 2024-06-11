/**
 *
 * @author David Beltran
 */
package aftergrad.herbudget;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Statement statement = new Statement("NovDec23.pdf");
        //statement.practice();
        statement.sendToDatabase();
    }
}
