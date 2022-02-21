package Pokemon

import ui.GamePanel

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import scala.swing.Graphics2D



abstract class Items(gamePanel: GamePanel, val map: (Int, Int), val i: Int, val j: Int) {
  val xCoord = i * 48
  val yCoord = j * 48
  val name: String
  var statusTaken: Boolean
  val image: BufferedImage

  def draw(g: Graphics2D) = {
    if (!this.statusTaken && gamePanel.player.currentMapX == this.map._1 && gamePanel.player.currentMapY == this.map._2) {
      g.drawImage(this.image, xCoord, yCoord, gamePanel.tileSize, gamePanel.tileSize, null)
    }
  }
}


class Potion(gP: GamePanel, map: (Int, Int), i: Int, j: Int) extends Items(gP, map, i, j) {
  var statusTaken = false
  val name = "HP potion"
  val image = ImageIO.read(getClass.getResourceAsStream("/pictures/potion.png"))
}


class Wood(gP: GamePanel, map: (Int, Int), i: Int, j: Int) extends Items(gP, map, i, j) {
  var statusTaken = false
  val name = "Wood planks"
  val image = ImageIO.read(getClass.getResourceAsStream("/pictures/plank.png"))
}

