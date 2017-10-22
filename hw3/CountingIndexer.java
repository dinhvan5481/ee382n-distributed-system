import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.google.common.base.Charsets;

public class CountingIndexer extends Configured implements Tool {

	private double mean = 0;

	private final static Text COUNT = new Text("count");
	private final static Text WORD = new Text("word");
	private final static LongWritable ONE = new LongWritable(1);

    public static class CountingIndexerMapper extends
      Mapper<Object, Text, Text, LongWritable> {

        private LongWritable wordLen = new LongWritable();
        private Text word = new Text();

        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
	        String fileName = getCurrentInputFileName(context);
	        StringTokenizer itr = new StringTokenizer(value.toString());
	        while (itr.hasMoreTokens()) {
	
		        String[] strings = removeSpecialCharacters(itr.nextToken().toLowerCase()).split(" ");
		        for (String str : strings) {			
		            word.set(String.format("%s:%s", str, fileName));
		            context.write(word, ONE);
		        }
	        }
        }

        private String getCurrentInputFileName(Context context) { 
	        FileSplit fileSplit = (FileSplit)context.getInputSplit();
	        String fileName = fileSplit.getPath().getName();
	        return fileName;
        }

        private String removeSpecialCharacters(String str) {
	        return str.replaceAll("[^\\w]", " ").trim();
        }
    }
    
    
    public static class CountingIndexerReducer extends
        Reducer<Text, LongWritable, Text, LongWritable> {

        private LongWritable sum = new LongWritable();

        public void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException {

            int theSum = 0;
            for (LongWritable val : values) {
                theSum += val.get();
            }
            
            sum.set(theSum);
            context.write(key, sum);
        }
    }


    private double readAndReport(Path path, Configuration conf)
        throws IOException {
        
        FileSystem fs = FileSystem.get(conf);
        Path file = new Path(path, "part-r-00000");

        if (!fs.exists(file))
          throw new IOException("Output not found!");

        BufferedReader br = null;
        HashMap<String, HashMap<String, Integer>> countIndex = new HashMap<>();

        try {
	        br = new BufferedReader(new InputStreamReader(fs.open(file), Charsets.UTF_8));
	        String[] word_chapter = null;
	        long count = -1;

	        String line;
	        while ((line = br.readLine()) != null) {
	            System.out.println(line);
		        StringTokenizer st = new StringTokenizer(line);

                word_chapter = st.nextToken().split(":");			
		        count = Long.parseLong(st.nextToken());

                HashMap<String, Integer> chapters = countIndex.get(word_chapter[0]);
                if (chapters == null) {
                    chapters = new HashMap<String, Integer>();
                    countIndex.put(word_chapter[0], chapters);
                }
                
                Integer chapterCount = chapters.get(word_chapter[1]);
                if (chapterCount == null) {
                    chapterCount = new Integer(0);
                }
                
                chapters.put(word_chapter[1], new Integer(chapterCount.intValue() + (int)count));
                word_chapter = null;
                count = -1;
	        }
	
	        printCountIndex(countIndex);
	

	        return 0;
        } finally {
	        if (br != null) {
		        br.close();
	        }
        }
    }
  
    public static class ChapterComparator implements Comparator<Map.Entry<String, Integer>> {
        
        public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {
            int comparison = me2.getValue().compareTo(me1.getValue());
            if (comparison == 0) {
                return me1.getKey().compareTo(me2.getKey());
            }
            return comparison;
        }
    }
  
    private void printCountIndex(HashMap<String, HashMap<String, Integer>> countIndex) {
        Object[] keys = countIndex.keySet().toArray();
        System.out.println(countIndex.toString());
        String[] words = Arrays.copyOf(keys, keys.length, String[].class);
        Arrays.sort(words);
        for (String word : words) {
            HashMap<String, Integer> chapters = countIndex.get(word);
            System.out.println(word);
            
            Object[] entries = chapters.entrySet().toArray();
            Map.Entry<String, Integer>[] chapter_entries = new Map.Entry[entries.length];
            chapter_entries = chapters.entrySet().toArray(chapter_entries);
            
            Arrays.sort(chapter_entries, new ChapterComparator());
            
            for (Map.Entry<String, Integer> entry : chapter_entries) {
                System.out.printf("<%s, %d>\n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(), new CountingIndexer(), args);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: CountingIndexer <in> <out>");
            return 0;
        }

        Configuration conf = getConf();

        Job job = Job.getInstance(conf, "count index");
        job.setJarByClass(CountingIndexer.class);
        job.setMapperClass(CountingIndexerMapper.class);
        job.setCombinerClass(CountingIndexerReducer.class);
        job.setReducerClass(CountingIndexerReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        Path outputpath = new Path(args[1]);
        FileOutputFormat.setOutputPath(job, outputpath);
        boolean result = job.waitForCompletion(true);
        mean = readAndReport(outputpath, conf);

        return (result ? 0 : 1);
    }

    public double getMean() {
        return mean;
    }
}
