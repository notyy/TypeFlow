package $PackageName$;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class $DefinitionName$ {
    public $ReturnType$ execute($Params$) {
        //TODO give me path
        Path file = Paths.get("./give-me-path");

        //using append mode, if you need overwritten mode, set StandardOpenOption.WRITE.
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardOpenOption.APPEND)) {
            writer.write(param1.toString());
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
