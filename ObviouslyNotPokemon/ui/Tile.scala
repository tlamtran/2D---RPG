package ui

import java.awt.image.BufferedImage
import java.io.{BufferedReader, InputStreamReader}
import javax.imageio.ImageIO
import scala.swing.Graphics2D

class Tile(val gamePanel: GamePanel) {

  var mapTileCoordinate: Vector[Vector[Int]] = Vector()
  var mapInput = "/Pokemon/maps/map00.txt"

  val image: Map[Int, BufferedImage] = Map(
    0 -> ImageIO.read(getClass.getResourceAsStream("/pictures/grass.png")),
    1 -> ImageIO.read(getClass.getResourceAsStream("/pictures/ground.png")),
    2 -> ImageIO.read(getClass.getResourceAsStream("/pictures/water.png")),
    3 -> ImageIO.read(getClass.getResourceAsStream("/pictures/tree.png")),
    4 -> ImageIO.read(getClass.getResourceAsStream("/pictures/wood.png")),
    5 -> ImageIO.read(getClass.getResourceAsStream("/pictures/house.png")),
    6 -> ImageIO.read(getClass.getResourceAsStream("/pictures/mushroom.png")),
  )

  def drawHouse(g: Graphics2D) = {
    if (gamePanel.player.currentMapX == -2 && gamePanel.player.currentMapY == 0) {
      g.drawImage(image(5), 8*48, 48*3, 110, 120, null)
    }
  }


  def loadMap() = {
    var newMapTileCoordinate: Vector[Vector[Int]] = Vector()
    val reader = new BufferedReader(new InputStreamReader(getClass.getResourceAsStream(mapInput)))
    for (i <- 1 to gamePanel.row) {
      var line = reader.readLine().filter( "0123456".contains(_)).toVector.map( _.asDigit)
      newMapTileCoordinate = newMapTileCoordinate :+ line
      mapTileCoordinate = newMapTileCoordinate
    }
  }

  def drawMap(g: Graphics2D) = {
    for (i <- 1 to gamePanel.row) {
      for (j <- 1 to gamePanel.column) {
        draw(g, i, j, this.mapTileCoordinate(i-1)(j-1))
      }
    }
  }

  def draw(g: Graphics2D, i: Int, j: Int, tileType: Int) = { // draws an individual tile
    g.drawImage(image(tileType), 48*(j-1), 48*(i-1), gamePanel.tileSize, gamePanel.tileSize, null)
  }

  var maps = Map(
    "00"  -> "/Pokemon/maps/map00.txt",
    "01"  -> "/Pokemon/maps/map01.txt",
    "0-1" -> "/Pokemon/maps/map0-1.txt",
    "10"  -> "/Pokemon/maps/map10.txt",
    "-10" -> "/Pokemon/maps/map-10.txt",
    "-20" -> "/Pokemon/maps/map-20.txt",
  )
  private def newMap() = this.gamePanel.tile.mapInput = maps(s"${gamePanel.player.currentMapX}${gamePanel.player.currentMapY}")

  def changeMap() = {
    if (gamePanel.player.x < 0) {
      gamePanel.player.moved = 48
      gamePanel.player.x = 15*48
      gamePanel.player.currentMapX -= 1
      newMap()
    }
    if (gamePanel.player.x > 15*48) {
      gamePanel.player.moved = 48
      gamePanel.player.x = 0
      gamePanel.player.currentMapX += 1
      newMap()
    }
    if (gamePanel.player.y < 0) {
      gamePanel.player.moved = 48
      gamePanel.player.y = 11*48
      gamePanel.player.currentMapY += 1
      newMap()
    }
    if (gamePanel.player.y > 11*48) {
      gamePanel.player.moved = 48
      gamePanel.player.y = 0
      gamePanel.player.currentMapY -= 1
      newMap()
    }
  }


}

