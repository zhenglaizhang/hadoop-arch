package net.zhenglai.scalding

import cascading.tuple.TupleEntry
import com.twitter.scalding.{Args, Job, TextLine, Tsv}

import scala.util.matching.Regex

/**
  * Created by Zhenglai on 8/4/16.
  */
class Main(args: Args) extends Job(args) {

  val input = TextLine(args("input"))
  val output = Tsv(args("output"))

  val inputFields = 'line
  val regexFields = ('ip, 'time, 'request, 'response, 'size)

  val filterFields = input.read.mapTo(inputFields → regexFields) {
    te: TupleEntry ⇒
      val regex = new Regex("^([^ ]*) \\S+ \\S+ \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) ([^ ]*).*$")
      val split = regex.findFirstMatchIn(te.getString("line")).get.subgroups
      (split(0), split(1), split(2), split(3), split(4))
  }.filterNot('size) {
    { size: String ⇒ size == "-" }
  }.write(output)
}

