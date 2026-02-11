package com.genesys.repository;

import java.io.*;
import java.util.*;

public class ImageLibraryItem{
	public ImageLibraryItem(String key, long size, InputStream content){
		this.key = key;
		this.size = size;
		this.content = content;
	}
	public String key;
	public long size;
	public InputStream content;
};