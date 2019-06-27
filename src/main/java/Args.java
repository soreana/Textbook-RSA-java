import com.beust.jcommander.Parameter;
import lombok.ToString;

@ToString
public class Args {

    @Parameter(names ="--pr", description = "Private Key output file name path")
    public String prPath = "PRIVATE.key";

    @Parameter(names = "--pub", description = "Public Key output file name path")
    public String pubPath = "PUBLIC.key";

    @Parameter(names = {"-i", "--input"} , description = "Input file name path")
    public String inputFilePath = "input.txt";

    @Parameter(names = {"-o", "--output"} , description = "Output file name path")
    public String outputFilePath = "output.txt";

    @Parameter(names = "--gen", description = "Generate keys")
    public boolean gen = false;

    @Parameter(names = "--enc", description = "Encryption mode")
    public boolean encrypt = false;

    @Parameter(names = "--dec", description = "Encryption mode")
    public boolean decrypt = false;
}
