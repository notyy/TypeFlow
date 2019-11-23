package $PackageName$;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class $DefinitionName$ {
    public $ReturnType$ execute($Params$) {
        //TODO give me path
        File file = new File("./give-me-path");
        BufferedWriter writer = null;
        try {
            //using append mode, if you need overwritten mode, set false.
            writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(param1.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
