package main.scala.example

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
    Iterator
      .continually (input.read)
      .takeWhile (-1 !=)
      .foreach (output.write)
    output.close()
    input.close()
  }
}
