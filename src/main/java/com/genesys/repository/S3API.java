package com.genesys.repository;

import java.io.*;
import java.util.*;
import java.net.URL;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import com.genesys.SystemServlet;

public class S3API
{	
	public static void uploadFile(String fileName, InputStream content){
		AmazonS3 s3 = S3API.getS3Connection();
		ObjectMetadata metaData = new ObjectMetadata();
	    metaData.setContentType("application/octet-stream");
		s3.putObject(new PutObjectRequest( SystemServlet.getGenesysS3Bucket(), fileName, content, metaData));
	}
	
	public static void deleteFile(String fileName){
		AmazonS3 s3 = S3API.getS3Connection();
		s3.deleteObject(SystemServlet.getGenesysS3Bucket(), fileName);
	}
	
	public static String[] getFiles(String prefix){
		try{
			List<String> fileList = new ArrayList<String>();
			AmazonS3 s3 = S3API.getS3Connection();
			ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
											.withBucketName(SystemServlet.getGenesysS3Bucket())
											.withPrefix(prefix));
			for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
				fileList.add(objectSummary.getKey());
			}
			return fileList.toArray(new String[fileList.size()]);
		}
		catch(Exception e){
			System.out.println("getFiles: error=" + e.getMessage() );
		}
		return null;
	}
	
	public static ImageLibraryItem getFile(String key){
		AmazonS3 s3 = S3API.getS3Connection();
		S3Object object = s3.getObject(new GetObjectRequest(SystemServlet.getGenesysS3Bucket(), key));
		return new ImageLibraryItem(key, object.getObjectMetadata().getContentLength(), (InputStream)object.getObjectContent());
	}
	
	public static String getSignedFileUrl(String key){
		AmazonS3 s3 = S3API.getS3Connection();

		try {
			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);
			GeneratePresignedUrlRequest generatePresignedUrlRequest = 
				    new GeneratePresignedUrlRequest(SystemServlet.getGenesysS3Bucket(), key);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET); 
			generatePresignedUrlRequest.setExpiration(expiration);
			URL url = s3.generatePresignedUrl(generatePresignedUrlRequest); 
			return url.toString();
		} catch (AmazonServiceException exception) {
//			System.out.println("Caught an AmazonServiceException, " +
//					"which means your request made it " +
//					"to Amazon S3, but was rejected with an error response " +
//			"for some reason.");
		} catch (AmazonClientException ace) {
//			System.out.println("Caught an AmazonClientException, " +
//					"which means the client encountered " +
//					"an internal error while trying to communicate" +
//					" with S3, " +
//					"such as not being able to access the network.");
		}
		return null;
	}
	
	private static AmazonS3 getS3Connection(){
		AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        AmazonS3 s3 = new AmazonS3Client(credentials);
        Region usWest2 = Region.getRegion(Regions.US_EAST_1);
        s3.setRegion(usWest2);
        return s3;
	}
}