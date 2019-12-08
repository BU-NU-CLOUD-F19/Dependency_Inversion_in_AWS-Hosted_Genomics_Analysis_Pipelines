package seqrdependencylambdaservice

import java.io.FileOutputStream
import scala.language.postfixOps

object ExtractBinary {

  /**
    * Copies binary placed in packageBinaryPath, a package accessible by the current object
    * to a location fsPath, a path in the local filesystem of the host machine where the JVM is run.
    *
    * @param packageBinaryPath Path of the binary in package relative to the current object.
    * @param fsPath            Path where the binary needs to be created in the local filesystem.
    */
  @throws(classOf[Exception])
  def copyToLocalFS(packageBinaryPath: String, fsPath: String): Unit = {
    val output = new FileOutputStream(fsPath)
    val input = this.getClass.getClassLoader.getResourceAsStream(packageBinaryPath)
    val bytes = new Array[Byte](2048) //1024 bytes - Buffer size
    Iterator
    .continually (input.read(bytes))
    .takeWhile (-1 !=)
    .foreach (read=>output.write(bytes,0,read))
    output.close()
    input.close()
  }
}