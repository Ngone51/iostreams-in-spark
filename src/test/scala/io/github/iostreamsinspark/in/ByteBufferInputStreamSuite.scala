package io.github.iostreamsinspark.in

import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

import org.scalatest.FunSuite

class ByteBufferInputStreamSuite extends FunSuite {

  test("read data by ByteBufferInputStream") {
    val userHome: String = System.getProperty("user.dir")
    val projectAbsolutePath: String = userHome.split("iostreams")(0)
    // the testfile include content below (without double quotes):
    // "Hello, World. This is from wuyi."
    // and size of the whole file is 32 bytes.
    val testFile: String = "iostreams-in-spark/src/test/resources/testfile"

    // we first read data from testfile into buf.
    val fis: FileInputStream = new FileInputStream(projectAbsolutePath + testFile)
    val channel: FileChannel = fis.getChannel
    val len = channel.size()
    var buf: ByteBuffer = ByteBuffer.allocate(len.toInt)
    assert(0 === channel.position())
    channel.read(buf)
    buf.flip()
    assert(len === buf.remaining())

    // and then we use ByteBufferInputStream to wrap buf
    val is: ByteBufferInputStream = new ByteBufferInputStream(buf)

    // at the end, we read data from is into bytes.
    val bytes: Array[Byte] = Array.fill[Byte](len.toInt)(0)
    is.read(bytes)
    assert(bytes.length === len)
    assert(buf.remaining() === 0)
    assert(buf.position() === len)
    assert(new String(bytes) === "Hello, World. This is from wuyi.")

    fis.close()
    // set null to let gc reclaim it
    buf = null
    // it seems ByteBufferInputStream do not override this method
    is.close()
  }
}
