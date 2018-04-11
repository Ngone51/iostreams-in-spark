package io.github.iostreamsinspark.out

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
  * Provide a zero-copy way to convert data in ByteArrayOutputStream to ByteBuffer
  */
class ByteBufferOutputStream(capacity: Int) extends ByteArrayOutputStream(capacity) {

  def this() = this(32)

  def getCount(): Int = count

  private[this] var closed: Boolean = false

  // NOTE: this method is not to write a int number, but a byte(since a byte is
  // belong to [0, 127]).
  override def write(b: Int): Unit = {
    require(!closed, "cannot write to a closed ByteBufferOutputStream")
    super.write(b)
  }

  override def write(b: Array[Byte], off: Int, len: Int): Unit = {
    require(!closed, "cannot write to a closed ByteBufferOutputStream")
    super.write(b, off, len)
  }

  override def reset(): Unit = {
    require(!closed, "cannot reset a closed ByteBufferOutputStream")
    super.reset()
  }

  override def close(): Unit = {
    if (!closed) {
      super.close()
      closed = true
    }
  }

  // convert a byte array to a ByteBuffer
  def toByteBuffer: ByteBuffer = {
    require(closed, "can only call toByteBuffer() after ByteBufferOutputStream has been closed")
    ByteBuffer.wrap(buf, 0, count)
  }
}

