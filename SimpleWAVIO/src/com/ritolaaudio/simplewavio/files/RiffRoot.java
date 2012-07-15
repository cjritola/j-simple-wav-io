package com.ritolaaudio.simplewavio.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class RiffRoot extends RiffChunk
	{

	@Override
	public void fromData(ByteBuffer fileBuffer)
		{
		// TODO Auto-generated method stub

		}

	public static List<RiffChunk> readRIFF(File input) throws FileNotFoundException,IOException
		{
		FileChannel fc = new FileInputStream(input).getChannel();
		ByteBuffer fileBuffer = ByteBuffer.allocateDirect((int)fc.size());//Limited to 3.5GB files
		fc.read(fileBuffer);
		fileBuffer.rewind();
		//fc.close();
		return RiffChunk.ParseRiff(fileBuffer,RiffRoot.class);
		}

	@Override
	public void _toData(ByteBuffer buffer)
		{
		//Nothing to do
		}//end toData

	@Override
	public int _sizeEstimateInBytes()
		{
		// TODO Auto-generated method stub
		return 0;
		}
	}//end RiffRoot
