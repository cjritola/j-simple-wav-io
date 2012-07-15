package com.ritolaaudio.simplewavio.files;

import java.nio.ByteBuffer;

public class Unrecognized extends RiffChunk
	{
	byte [] unrecognizedRawData;
	@Override
	public void fromData(ByteBuffer fileBuffer)
		{
		//Get the block size
		int blockSize=(int)RiffChunk.readUnsignedInt(fileBuffer);
		System.out.println("Unrecognized chunk is of size "+blockSize);
		unrecognizedRawData = new byte[(int)blockSize];
		fileBuffer.get(unrecognizedRawData);
		}

	@Override
	public void _toData(ByteBuffer buffer)
		{
		//Do nothing
		}

	@Override
	public int _sizeEstimateInBytes()
		{
		// Not relevant
		return 0;
		}

	/**
	 * @return the unrecognizedRawData
	 */
	public byte[] getUnrecognizedRawData()
		{
		return unrecognizedRawData;
		}

	/**
	 * @param unrecognizedRawData the unrecognizedRawData to set
	 */
	public void setUnrecognizedRawData(byte[] unrecognizedRawData)
		{
		this.unrecognizedRawData = unrecognizedRawData;
		}

	}
