package com.ritolaaudio.simplewavio.files.riff.wave;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import com.ritolaaudio.simplewavio.files.RiffChunk;

public class RiffChunk_data extends RiffChunk
	{
	byte [] rawData;
	@Override
	public void fromData(ByteBuffer fileBuffer)
		{
		long audioDataSizeInBytes=readUnsignedInt(fileBuffer);
		System.out.println("audio data size in bytes: "+audioDataSizeInBytes);
		rawData = new byte[(int)audioDataSizeInBytes];
		fileBuffer.get(rawData);
		}//end initialize(...)
	
	@Override
	public void _toData(ByteBuffer buffer)
		{
		buffer.putInt(rawData.length);
		try{buffer.put(rawData);}
		catch(BufferOverflowException e)
			{
			e.printStackTrace();
			System.out.println("tried to put "+rawData.length+" into buffer of remaining "+buffer.remaining());
			}
		}//end _toData
	@Override
	public int _sizeEstimateInBytes()
		{
		return 4+rawData.length;//data size indicator plus data
		}
	
	/**
	 * @return the rawData
	 */
	public byte[] getRawData()
		{
		return rawData;
		}
	/**
	 * @param rawData the rawData to set
	 */
	public void setRawData(byte[] rawData)
		{
		this.rawData = rawData;
		System.out.println("Set raw data to an array of length "+rawData.length);
		}
	}//end RiffChunk_data
