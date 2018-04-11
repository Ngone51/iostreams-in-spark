package io.github.iostreamsinspark.out

import java.nio.ByteBuffer

import org.scalatest.FunSuite

class ByteBufferOutputStreamSuite extends FunSuite {
  test("convert a byte array to a ByteBuffer by ByteBufferOutputStream") {
    val output = new ByteBufferOutputStream()
    // prepare a byte array
    val bytes: Array[Byte] = "Hello, World.".getBytes()
    // write this byte array into output
    output.write(bytes)
    // NOTE: we should close ByteBufferOutputStream before call toByteBuffer() on it
    output.close()
    // convert byte array to ByteBuffer internally
    var buf: ByteBuffer = output.toByteBuffer
    val originalPosition = buf.position()
    assert(originalPosition === 0)
    assert(buf.limit() === bytes.length)
    assert(buf.get() === 'H')
    assert(buf.position() === originalPosition + 1)

    // set null to let gc reclaim it
    buf = null
  }
}
