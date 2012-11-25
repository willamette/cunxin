package org.cunxin.support.util

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.`type`.TypeReference
import java.lang.reflect.{Type, ParameterizedType}
import java.io.{OutputStream, File, InputStream, StringWriter}

class JsonSerDe {
  protected[this] val jacksonMapper = new ObjectMapper()
  jacksonMapper.registerModule(DefaultScalaModule)

  def prettySerialize[T](t: T): String = {
    jacksonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(t)
  }

  def serialize[T](t: T): String = {
    val writer = new StringWriter()
    jacksonMapper.writeValue(writer, t)
    writer.toString
  }

  def serialize[T](t: T, stream: OutputStream) {
    jacksonMapper.writeValue(stream, t)
  }

  def deserialize[T: Manifest](t: String): T = {
    jacksonMapper.readValue(t, newTypeReference[T])
  }

  def deserialize[T: Manifest](t: Array[Byte]): T = {
    jacksonMapper.readValue(t, newTypeReference[T])
  }

  def deserialize[T: Manifest](stream: InputStream): T = {
    jacksonMapper.readValue(stream, newTypeReference[T])
  }

  def deserialize[T: Manifest](file: File): T = {
    jacksonMapper.readValue(file, newTypeReference[T])
  }

  def getIterator[T: Manifest](stream: InputStream): Iterator[T] = {
    jacksonMapper.readValues(jacksonMapper.getJsonFactory.createJsonParser(stream), manifest[T].erasure.asInstanceOf[Class[T]])
  }

  def getIterator[T: Manifest](file: File): Iterator[T] = {
    jacksonMapper.readValues(jacksonMapper.getJsonFactory.createJsonParser(file), manifest[T].erasure.asInstanceOf[Class[T]])
  }

  private[this] def newTypeReference[T: Manifest]: TypeReference[T] = new TypeReference[T]() {
    override def getType = toType[T]
  }

  private[this] def toType[T: Manifest]: Type = {
    if (manifest[T].typeArguments.isEmpty)
      return manifest[T].erasure

    new ParameterizedType {
      val getActualTypeArguments = manifest[T].typeArguments.map(toType(_)).toArray
      val getRawType = manifest[T].erasure
      val getOwnerType = null
    }
  }
}