package com.ritolaaudio.simplewavio.files;

import java.nio.ByteBuffer;

public class RiffChunk_RIFF extends RiffChunk
	{
	@Override
	public void fromData(ByteBuffer fileBuffer)
		{
		//System.out.println("Found RIFF identifier chunk of size "+dataLength);
		long payloadSize = readUnsignedInt(fileBuffer);
		parseRiff(fileBuffer);
		}

	@Override
	public void _toData(ByteBuffer buffer)
		{
		//Payload size (not including this)
		buffer.putInt(this.childrenSizeEstimateInBytes());
		}

	@Override
	public int _sizeEstimateInBytes()
		{
		return 4;
		}
	}//end RiffChunk_RIFF
