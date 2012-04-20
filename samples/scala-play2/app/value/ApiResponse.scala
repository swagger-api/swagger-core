package value

import javax.xml.bind.annotation._

object ApiResponse {
  val ERROR = 1
  val WARNING = 2
  val INFO = 3
  val OK = 4
  val TOO_BUSY = 5
}

@XmlRootElement
class ApiResponse(@XmlElement var code: Int, @XmlElement var message: String) {
  def this() = this(0, null)

  @XmlTransient
  def getCode(): Int = code
  def setCode(code: Int) = this.code = code

  def getType(): String = code match {
    case ApiResponse.ERROR => "error"
    case ApiResponse.WARNING => "warning"
    case ApiResponse.INFO => "info"
    case ApiResponse.OK => "ok"
    case ApiResponse.TOO_BUSY => "too busy"
    case _ => "unknown"
  }
  def setType(`type`: String) = {}

  def getMessage(): String = message
  def setMessage(message: String) = this.message = message
}