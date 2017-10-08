import java.io.IOException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.io.*;

public class ACMCitationMapper extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String line = value.toString();
		
		Pattern pattern1 = Pattern.compile("#index[a-zA-Z0-9]+"); //#index5390877920f70186a0d2ca87
		Pattern pattern2 = Pattern.compile("#%[a-zA-Z0-9]+");  //#%5390877920f70186a0d2ca87

		//check every split with above patterns
		Matcher matchIndex = pattern1.matcher(line);
		Matcher matchCites = pattern2.matcher(line);
		
		//check for a match to get an index
		if(matchIndex.find()){
			//on successful match return the index
			String index = matchIndex.group(0).substring(6,matchIndex.group(0).length());
			//check for citation match
			while(matchCites.find()){
				//if citation exist emit index and citation
				String citation = matchCites.group(0).substring(2,matchCites.group(0).length());
				context.write(new Text(index), new Text(citation));
			}		
		}
	}
}

