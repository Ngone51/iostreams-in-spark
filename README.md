# iostreams-in-spark

This project extracts input/output streams from Spark. 
Also, there're some JDK streams included, which were used by Spark.
Learning these I/O streams' design and usage is helpful for us to know better with I/O streams.

We added unit tests for each stream to show it's feature and usage.

## streams collected yet

* [LimitedInputStream](src/main/java/io/github/iostreamsinspark/in/LimitedInputStream.java)
    
    A LimitedInputStream only allow to read a size limited data from a InputStream.

* [TimeTrackingOutputStream](src/main/java/io/github/iostreamsinspark/out/TimeTrackingOutputStream.java)

    A TimeTrackingOutputStream can track the time we spending on writing data to a OutputStream.

* [ByteBufferInputStream](src/main/scala/io/github/iostreamsinspark/in/ByteBufferInputStream.scala)

    ByteBufferInputStream supports read data from a ByteBuffer and save it into a byte array.
    
* [ByteBufferOutputStream](src/main/scala/io/github/iostreamsinspark/out/ByteBufferOutputStream.scala)

    ByteBufferOutputStream supports read data from a byte array, and then convert into
    a ByteBuffer after close() called.
    
    
