import org.apache.hadoop.conf.Configuration;

public class WordIndexer {
    private String inputPath, outputPath;
    private WordIndexer(){}
    public WordIndexer(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public static void main(String[] args) throws Exception {

        WordIndexer wordIndexer = new WordIndexer();
        Configuration conf = wordIndexer.createConfiguration();
    }

    public Configuration createConfiguration() {
        Configuration conf = new Configuration();

        Job job = new
        return conf;
    }
}
