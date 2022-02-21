package Pokemon

import ui.GamePanel

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import scala.collection.mutable._
import scala.swing.Graphics2D




trait Character {
  val movement: Map[String, (Int, Int)] = Map(
    "down" -> (0, 4),
    "up" -> (0, -4),
    "right" -> (4, 0),
    "left" -> (-4, 0) )

}



class Player(val gamePanel: GamePanel) extends Character {

  // initial values and gatherers
  var facing = "down"
  var x = 7*48
  var y = 5*48
  var moved = 0
  var pokemon: Option[Pokemon] = None
  var currentMapX = 0
  var currentMapY = 0
  var moving = false
  var bag: Vector[Items] = Vector()
  // FOR WALKING ANIMATION
  private var step = 1
  private var timing = 0


  def potion() = {
    if (bag.exists(_.name == "HP potion")) {
      bag = bag.filter( _.name != "HP potion")
      pokemon.head.hp = 10 + pokemon.head.level * 2
      "Your pokemon's HP is fully recovered"
    }
    else {
      "You don't have a potion"
    }
  }

  def pick() = {
    val item = gamePanel.items.filter( n => n.map._1 == this.currentMapX && n.map._2 == currentMapY && n.xCoord == this.x && n.yCoord == this.y )
    if ( item.nonEmpty ) {
      item(0).statusTaken = true
      bag = item(0) +: bag
      s"You obtained " + item(0).name + "!"
    }
    else {
      "There is nothing to take"
    }
  }

  def build(): String = {
    if (currentMapX == -1 && currentMapY == 0 && x == 3*48 && y == 5*48) {
      if (bag.count( _.name == "Wood planks") == 3) {
        bag = bag.filter( _.name != "Wood planks")
        var modifiedMaps = gamePanel.tile.maps.removed("-10")
        modifiedMaps = modifiedMaps + ("-10" -> "/Pokemon/maps/map-10v2.txt")
        gamePanel.tile.maps = modifiedMaps
        gamePanel.tile.mapInput = "/Pokemon/maps/map-10v2.txt"
        "You've succesfully built a bridge!"
      }
      else "I need wood to build the bridge"
    }
    else {
      "I can't build here"
    }
  }


  def attack(): String = {
    if ((currentMapX, currentMapY) == gamePanel.mushroom.mapXY && (x, y) == (8*48, 7*48) && !gamePanel.mushroom.statusDefeated) {
        pokemon.head.hp -= 4
        gamePanel.mushroom.hp -= 4
        if (gamePanel.mushroom.hp <= 0) {
          gamePanel.mushroom.statusDefeated = true
          var modifiedMaps = gamePanel.tile.maps.removed("0-1")
          modifiedMaps = modifiedMaps + ("0-1" -> "/Pokemon/maps/map0-1v2.txt")
          gamePanel.tile.maps = modifiedMaps
          gamePanel.tile.mapInput = "/Pokemon/maps/map0-1v2.txt"
          gamePanel.mushroom.statusDefeated = true
          "You defeated a mushroom"
        }
        else "You both attack each other and lose 4 hp\n" + pokemon.head.name + ": " + pokemon.head.hp + " HP\n"  + s"Mushroom: ${gamePanel.mushroom.hp} HP"


    }
    else {
      "There's no one to fight."
    }
  }

  def move(direction: String): String = { //moves player 4 pixels in the given direction
    this.moving = true
    this.x += movement(direction)._1
    this.y += movement(direction)._2
    moved += 4
    facing = direction
    direction
  }

  def movable(direction: String): Boolean = {
    def xCoord = (this.x + 12*movement(direction)._1)/48
    def yCoord = (this.y + 12*movement(direction)._2)/48
    if (xCoord < 0 || xCoord > 15 || yCoord < 0 || yCoord > 11) {
      true
    }
    else {
      gamePanel.tile.mapTileCoordinate(yCoord)(xCoord) != 2 && gamePanel.tile.mapTileCoordinate(yCoord)(xCoord) != 3 && gamePanel.tile.mapTileCoordinate(yCoord)(xCoord) != 6
    }
  }


  private val image: Map[String, BufferedImage] = Map(
  "up1"     -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyup1.png")),
  "up2"     -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyup2.png")),
  "up3"     -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyup3.png")),
  "down1"   -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffydown1.png")),
  "down2"   -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffydown2.png")),
  "down3"   -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffydown3.png")),
  "right1"  -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyright1.png")),
  "right2"  -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyright2.png")),
  "right3"  -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyright3.png")),
  "left1"   -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyleft1.png")),
  "left2"   -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyleft2.png")),
  "left3"   -> ImageIO.read(getClass.getResourceAsStream("/pictures/luffy/luffyleft3.png"))
  )


  def draw(g: Graphics2D) = { // UPDATES PLAYER MODEL
    timing += 1
    if (timing > 12) { // TIMING HERE TO SLOW DOWN THE ANIMATION
      if (!this.moving) {
        step = 1
      }
      else if (this.moving && step == 1 || step >= 3) {
        step = 2
      }
      else if (this.moving && step == 2) {
        step = 3
      }
      timing = 0
    }
    g.drawImage(image(this.facing + this.step), this.x, this.y, gamePanel.tileSize, gamePanel.tileSize, null)
  }


}

