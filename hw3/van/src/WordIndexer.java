import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;


public class WordIndexer {
    private String inputPath, outputPath;
    private WordIndexer(){}
    public WordIndexer(String inputPath, String outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        WordIndexer wordIndexer = new WordIndexer();
        Job job = wordIndexer.createJob(conf, args[0], args[1]);

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    private Job createJob(Configuration conf, String inputPath, String outputPath) throws IOException {


        Job job = Job.getInstance(conf, "word index");
        job.setJarByClass(WordIndexer.class);
        job.setMapperClass(WordIndexerMapper.class);
        job.setReducerClass(WordIndexerReducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));

        job.setNumReduceTasks(5);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        return job;
    }
}
