package controllers

import play.api._
import libs.iteratee.Enumerator
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import org.scilab.forge.jlatexmath.{TeXConstants, TeXFormula}
import java.awt.{Color, Insets}
import java.awt.image.BufferedImage
import javax.swing.JLabel
import javax.imageio.ImageIO
import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import play.api.data.format.Formats._
import concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val form = Form.apply(
    single(
      "latex" -> Forms.of[String]
    )
  )

  def jlatexmath = Action { implicit req =>
    val latex = form.bindFromRequest().get
    val formula = new TeXFormula(latex)

    val icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20)

    // insert a border
    icon.setInsets(new Insets(5, 5, 5, 5));

    // now create an actual image of the rendered equation
    val image = new BufferedImage(icon.getIconWidth,
      icon.getIconHeight, BufferedImage.TYPE_INT_ARGB)
    val g2 = image.createGraphics()
    g2.setColor(Color.white)
    g2.fillRect(0, 0, icon.getIconWidth, icon.getIconHeight)

    val jl = new JLabel()
    jl.setForeground(new Color(0, 0, 0))
    icon.paintIcon(jl, g2, 0, 0)

    val stream = new ByteArrayOutputStream()

    ImageIO.write(image, "png", stream)

    SimpleResult(
      header = ResponseHeader(200, Map(CONTENT_TYPE -> "image/png")),
      body = Enumerator.fromStream(new ByteArrayInputStream(stream.toByteArray))
    )
  }

}
