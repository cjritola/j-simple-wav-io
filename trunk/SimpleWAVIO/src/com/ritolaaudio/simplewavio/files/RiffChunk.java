package com.ritolaaudio.simplewavio.files;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.sun.org.apache.bcel.internal.classfile.Unknown;

public abstract class RiffChunk implements Comparable<RiffChunk>
	{
	protected static byte [] workChunk4 = new byte[4];
	protected static byte [] anotherWorkChunk4 = new byte[4];
	protected static byte [] workChunk2 = new byte[2];
	
	protected HashMap<Class,RiffChunk> childMap = new HashMap<Class,RiffChunk>();
	public RiffChunk getChildChunk(Class c){System.out.println(this.getClass().getName()+".getChildChunk("+c.getName()+") returned "+childMap.get(c).getClass().getName());return childMap.get(c);}
	
	public static List<RiffChunk> ParseRiff(ByteBuffer fileBuffer, Class<? extends RiffChunk> parentClass)
		{
		String packageName=parentClass.getPackage().getName();
		String parentClassSimpleName=parentClass.getSimpleName();
		String parentTag=parentClassSimpleName.substring(parentClassSimpleName.length()-4);
		String parentTagSeparator=".";
		
		//The exception: If this is the root of the file.
		if(parentClass==RiffRoot.class)
			{
			parentTag="";
			parentTagSeparator="";
			}
		
		System.out.println("Parent tag: "+parentTag);
		System.out.println("package: "+packageName);
		LinkedList<RiffChunk>subChunks = new LinkedList<RiffChunk>();
		System.out.println("position: "+fileBuffer.position());
		while(fileBuffer.position()<fileBuffer.capacity())
			{
			//Read first 4 bytes
			fileBuffer.get(workChunk4);
			String tag = new String(workChunk4);
			tag=tag.replace(' ', '_');//Replace spaces in tags with underscore
			//Convert tag sequence to String
			System.out.println("tag:"+tag);
			final String className=packageName+parentTagSeparator+parentTag.toLowerCase()+".RiffChunk_"+tag;
			try
				{
				//Find class to decode that tag
				Class<RiffChunk> chunkClass = (Class<RiffChunk>)Class.forName(className);
				RiffChunk decoder = (RiffChunk)chunkClass.newInstance();
				decoder.fromData(fileBuffer);
				subChunks.add(decoder);
				}
			catch(ClassNotFoundException e)
				{System.out.println("Failed to find class to decode riff tag: "+tag);System.out.println("was looking for "+className+".\nInserting 'Unrecognized' placeholder.");
				RiffChunk unknown = new Unrecognized();
				unknown.fromData(fileBuffer);
				subChunks.add(unknown);
				}
			catch(IllegalAccessException e){e.printStackTrace();}//TODO Better
			catch(InstantiationException e){e.printStackTrace();}//TODO Better
			}
		System.out.println("RiffChunk.ParseRiff returning...");
		return subChunks;
		}//end RiffChunk
	
	protected final void parseRiff(ByteBuffer fileBuffer)
		{
		List<RiffChunk> children = ParseRiff(fileBuffer,this.getClass());
		for(RiffChunk c:children)
			{childMap.put(c.getClass(), c);}
		}
	
	public static long readUnsignedInt(ByteBuffer fileBuffer)
		{
		fileBuffer.get(workChunk4);
		//Utils.flipEndian(workChunk, anotherWorkChunk);
		long result=0;
		for(int i=0; i<4; i++)
			{result|=((long)(workChunk4[i]&0xFF))<<i*8;}
		return result;
		}
	
	public static int readUnsignedShort(ByteBuffer fileBuffer)
		{
		fileBuffer.get(workChunk2);
		//Utils.flipEndian(workChunk, anotherWorkChunk);
		int result=0;
		for(int i=0; i<2; i++)
			{result|=((int)(workChunk2[i]&0xFF))<<i*8;}
		return result;
		}
	
	@Override
	public int compareTo(RiffChunk other)
		{
		if(this.getOrderID()>other.getOrderID())return 1;
		else if(this.getOrderID()==other.getOrderID())return 0;
		else return -1;
		}
	
	public int getOrderID(){return 5;}
	
	public abstract void fromData(ByteBuffer fileBuffer);
	public abstract void _toData(ByteBuffer buffer);
	public final void toData(ByteBuffer buffer)
		{
		//Write the RIFF name
		String className=this.getClass().getSimpleName();
		String RIFFName = className.substring(className.length()-4).replace('_', ' ');
		System.out.println("Writing RIFF tag to buffer: "+RIFFName);
		try{if(!RIFFName.contentEquals("Root") && !className.contentEquals("Unrecognized"))buffer.put(RIFFName.getBytes());}
		catch(BufferOverflowException e){throw new RuntimeException("Buffer Overflow Exception while pushing RIFF name into buffer of size "+buffer.capacity());}
		_toData(buffer);
		childrenToData(buffer);
		}
	public void childrenToData(ByteBuffer buffer)
		{
		System.out.println(this.getClass().getName()+".childrenToData()");
		List<RiffChunk>children = new ArrayList<RiffChunk>(childMap.values());
		Collections.sort(children);
		for(RiffChunk child:children)
			{System.out.println("child: "+child.getClass().getName());child.toData(buffer);}
		}//end childrenToData()
	
	public abstract int _sizeEstimateInBytes();
	public final int sizeEstimateInBytes()
		{return childrenSizeEstimateInBytes()+_sizeEstimateInBytes()+4;}//4 is for the tag id
	int childrenSizeEstimateInBytes()
		{
		int accumulator=0;
		for(RiffChunk c:childMap.values()){accumulator+=c.sizeEstimateInBytes();}
		return accumulator;
		}
	
	public void addChildChunk(RiffChunk chunkToAdd)
		{childMap.put(chunkToAdd.getClass(), chunkToAdd);System.out.println(this.getClass().getName()+".addChildChunk("+chunkToAdd.getClass().getName()+")");}
	
	public void printChildChunks()
		{
		System.out.println(this.getClass()+".printChildChunks():");
		if(childMap.isEmpty())System.out.println("	(No child chunks in this object)");
		for(RiffChunk c:childMap.values())
			{System.out.println("		"+c.getClass().getName());}
		}//end printChildChunks()
	}//end RiffChunk
