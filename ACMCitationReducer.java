import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.mapreduce.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.io.*;

public class ACMCitationReducer extends Reducer<Text, Text, Text, Text> {
  public void reduce(Text key, Iterable<Text> values, Context context) 
		  throws IOException, InterruptedException {
  
	  for(Text val:values){
		  context.write(key, new Text(val.toString()));
	  }
  }
}