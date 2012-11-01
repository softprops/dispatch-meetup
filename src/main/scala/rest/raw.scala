package meetup.rest

import dispatch._

trait Raw { self: Client =>
  type Params = Traversable[(String, String)]

  def get[T](path: String, params: Params)(handler: Client.Handler[T]) =
    apply(mkurl(path) <<? params)(handler)

  def post[T](path: String, params: Params)(handler: Client.Handler[T]) =
    apply(mkurl(path) << params)(handler)

  def delete[T](path: String)(handler: Client.Handler[T]) =
    apply(mkurl(path).DELETE)(handler)

  private def mkurl(path: String) =
    (apiHost /: path.split("/"))((a, e) => a / e)
}
