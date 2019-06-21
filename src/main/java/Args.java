import com.beust.jcommander.Parameter;
import lombok.ToString;

@ToString
public class Args {

    @Parameter(names ="-pr", description = "Private Key output file name path")
    public String prPath = "PRIVATE.key";

    @Parameter(names = "-pub", description = "Public Key output file name path")
    public String pubPath = "PUBLIC.key";

    @Parameter(names = {"-i", "--input"} , description = "Input file name path")
    public String inputFilePath = "input.txt";

    @Parameter(names = {"-o", "--output"} , description = "Output file name path")
    public String outputFilePath = "input.txt.enc";

    @Parameter(names = "-debug", description = "Debug mode")
    public boolean debug = false;
}
