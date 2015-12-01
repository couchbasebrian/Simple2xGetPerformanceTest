package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.Document;
import com.couchbase.client.java.document.StringDocument;

// Brian Williams
// August 6, 2015
// Updated November 30, 2015
// Very simple get() test
// For Couchbase Java Client 2.1.4


public class Simple2xGetPerformanceTest {

	public static void main(String[] args) {

		// Put your values here
		String sourceClusterAddress = "10.111.82.101",  
			   sourceBucketName = "default";

		// Connect to both clusters
		CouchbaseCluster sourceCluster = CouchbaseCluster.create(sourceClusterAddress);
		Bucket sourceBucket = sourceCluster.openBucket(sourceBucketName);

		String documentKey = "document1234";
		String documentBody = "{ \"message\" : \"Hello there\" }";
		
		Document<String> foobar = StringDocument.create(documentKey, documentBody);
		sourceBucket.upsert(foobar);
		
		int successCount = 0;
		int exceptionCount = 0;
		int iteration = 0;
		int nullCount = 0;
		
		long startTime = System.currentTimeMillis();
		long timeNow = 0;
		long elapsedTimeSinceStart = 0;
		
		boolean keepGoing = true;
		while (keepGoing) {
			
			try {
				StringDocument foo = sourceBucket.get(documentKey, StringDocument.class);
				successCount++;
				if (foo == null) {
					nullCount++;
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
				exceptionCount++;
			}
			
			iteration++;
		
			if ((iteration % 1000) == 0) { 
				
				timeNow = System.currentTimeMillis();
				elapsedTimeSinceStart = timeNow - startTime;
				float averageIterationsPerSecond = ((float) iteration / (float) elapsedTimeSinceStart) * 1000;

				System.out.printf("Iteration: %6d Success: %6d Nulls: %6d Failures: %6d IPS: %7.1f Elapsed: %6d ms\n", 
						iteration, successCount, nullCount, exceptionCount, averageIterationsPerSecond, elapsedTimeSinceStart);
			}
		
			
			
		} // main while loop
		
	} // main

} // class
