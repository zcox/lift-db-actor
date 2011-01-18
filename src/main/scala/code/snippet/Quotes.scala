package code.snippet

import scala.xml._
import code.model._
import net.liftweb.http.SHtml._
import net.liftweb.util._
import net.liftweb.util.Helpers._
import net.liftweb.http.js.JsCmds._

class Quotes {
  def create = "#text" #> textarea("", Quote.create.text(_).user(User.currentUser).save)

  def recent = list(Quote.recent())
  def popular = list(Quote.popular())
  def list(qs: List[Quote]) = {

    //all this, just for a Like/Unlike link...
    def likeLinkText(q: Quote) = Text(User.currentUser map { u => if (u likes_? q) "Unlike" else "Like" } openOr "Like")
    def likeLinkId(q: Quote) = "quoteLike" + q.id.is
    def likeLink(q: Quote): NodeSeq = a(likeLinkText(q), "id" -> likeLinkId(q)) { 
      for (u <- User.currentUser)
	u toggle q
      Replace(likeLinkId(q), likeLink(q))
    }

    //render the quotes
    ".quote *" #> qs.map { q => 
      ".name *" #> (q.user map { _.firstName.is } openOr "<no user>" ) & 
      ".text *" #> q.text.is & 
      ".likeCount *" #> q.likeCount &
      ".likeLink" #> likeLink(q)
    }
  }
}
