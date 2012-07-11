// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.api

import java.util.{ ArrayList, LinkedHashMap, List => JList, Map => JMap }
import collection.JavaConverters._

final class Program(val interfaceGlobals: JList[String], val is3D: Boolean) {

  def this(is3D: Boolean) = this(new ArrayList[String], is3D)

  val globals: collection.mutable.Buffer[String] =
    (AgentVariables.getImplicitObserverVariables ++
     interfaceGlobals.asScala.map(_.toUpperCase)).toBuffer

  val turtlesOwn: collection.mutable.Buffer[String] =
    AgentVariables.getImplicitTurtleVariables(is3D).toBuffer

  val patchesOwn: collection.mutable.Buffer[String] =
    AgentVariables.getImplicitPatchVariables(is3D).toBuffer

  val linksOwn: collection.mutable.Buffer[String] =
    AgentVariables.getImplicitLinkVariables.toBuffer

  // use a LinkedHashMap to store the breeds so that the Renderer can retrieve them in order of
  // definition, for proper z-ordering - ST 6/9/04
  // Using LinkedHashMap on the other maps isn't really necessary for proper functioning, but makes
  // writing unit tests easier - ST 1/19/09
  // Yuck on this AnyRef stuff -- should be cleaned up - ST 3/7/08, 6/17/11
  val breeds: JMap[String, AnyRef] = new LinkedHashMap[String, AnyRef]
  val breedsSingular: JMap[String, String] = new LinkedHashMap[String, String]
  val linkBreeds: JMap[String, AnyRef] = new LinkedHashMap[String, AnyRef]
  val linkBreedsSingular: JMap[String, String] = new LinkedHashMap[String, String]
  val breedsOwn: JMap[String, JList[String]] = new LinkedHashMap[String, JList[String]]
  val linkBreedsOwn: JMap[String, JList[String]] = new LinkedHashMap[String, JList[String]]

  def dump = {
    def seq(xs: Seq[_]) =
      xs.mkString("[", " ", "]")
    def list(xs: JList[_]) =
      xs.asScala.mkString("[", " ", "]")
    def map(xs: JMap[_, _]) =
      xs.asScala
        .map{case (k, v) => k.toString + " = " + v.toString}
        .mkString("", "\n", "\n").trim
    "globals " + seq(globals) + "\n" +
      "interfaceGlobals " + list(interfaceGlobals) + "\n" +
      "turtles-own " + seq(turtlesOwn) + "\n" +
      "patches-own " + seq(patchesOwn) + "\n" +
      "links-own " + seq(linksOwn) + "\n" +
      "breeds " + map(breeds) + "\n" +
      "breeds-own " + map(breedsOwn) + "\n" +
      "link-breeds " + map(linkBreeds) + "\n" +
      "link-breeds-own " + map(linkBreedsOwn) + "\n"
  }

}
