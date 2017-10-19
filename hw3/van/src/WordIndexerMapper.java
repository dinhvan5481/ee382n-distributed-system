import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;


import java.io.IOException;
import java.util.StringTokenizer;

public class WordIndexerMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1); // type of output value
    private Text word = new Text();   // type of output key

    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        StringTokenizer itr = new StringTokenizer(value.toString().toLowerCase().replaceAll("[^a-z0-9]", " ")); // line to string token
        FileSplit fileInputSplit = (FileSplit) context.getInputSplit();
        String fileName = fileInputSplit.getPath().getName();



        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken() + "_" + fileName);    // set word as each input keyword
            context.write(word, one);     // create a pair <keyword, 1>
        }
    }
}
