## Summary ##
A small java library for straightforward reading and writing of .WAV (RIFF) mono and multichannel audio files as arrays of numbers in the range of [-1,1]

## Example - Applying Gain ##
```
float [][] inputAudio = Utils.WAVToFloats(new File("input.wav"));

final double gain=2;

for(float [] frame:inputAudio)
	{
	for(int channelIndex=0; channelIndex<frame.length; channelIndex++)
		{
		frame[channelIndex]*=gain;
		}//end for(channels)
	}//end for(frames)
Utils.floatsToWAV(inputAudio,new File("output.wav"),44100);
```

Quick download JAR of source and docs:
https://drive.google.com/file/d/0BxvD4PHJkNOnREltWWNzR1hSN1U/view?usp=sharing

## What It Does ##
  * Read non-arbitrary length .WAV files to `float [][]` arrays.
  * Write these arrays to .WAV files
  * Takes care of boilerplate work when doing experiments in audio processing within Java so you can concentrate on your experimental code.
  * Uses ~~Java's AudioSystem class~~ it's own RIFF I/O library for the parsing of RIFF/WAV files.
  * Writes as 16-bit signed little-endian by default.
  * Automatically determines the channel count when writing to .WAV from the length of the first frame in the array.

## What It's Not ##
  * ~~Fast~~ (it is fairly fast now)
  * Flexible
  * Failsafe
  * A streaming utility

## Motivation ##
This library was made to bridge the abstraction gap in Java's audio API between raw bytes and abstract `DataLine`s, `Mixer`s, `Source`s and `Sink`s. The pragmatic purpose is to obtain mathematically-usable numerical representations of PCM WAV/RIFF files in the [-1,1] range which is customary in digital signal processing.

## Other Features ##
SimpleWAVIO utilizes a small extensible RIFF parsing/writing library in the package `com.ritolaaudio.simplewavio.files.*`. RIFF chunks are identified by their position in the package hierarchy:
![https://j-simple-wav-io.googlecode.com/svn/trunk/SimpleWAVIO/doc/com/ritolaaudio/simplewavio/files/RiffChunkParsing.png](https://j-simple-wav-io.googlecode.com/svn/trunk/SimpleWAVIO/doc/com/ritolaaudio/simplewavio/files/RiffChunkParsing.png)

## References ##
http://www.daubnet.com/en/file-format-riff

https://ccrma.stanford.edu/courses/422/projects/WaveFormat/

(c) 2012 Chuck Ritola, Licensed GPLv3

![http://convolve-j.googlecode.com/svn/trunk/ConvolveJ/doc/gplv3-127x51.png](http://convolve-j.googlecode.com/svn/trunk/ConvolveJ/doc/gplv3-127x51.png)