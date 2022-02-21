package ui

import Pokemon._

import java.awt.image.BufferedImage
import java.awt.{Color, Dimension, Font}
import javax.imageio.ImageIO
import javax.sound.sampled.{AudioSystem, Clip, FloatControl}
import scala.collection.mutable.Map
import scala.swing._
import scala.swing.event.{Key, KeyPressed}

class GamePanel extends Panel with Runnable {

  private val keyHandler = new KeyHandler(this)
  private var win = false
  private var lose = false
  var started = false
  val player = new Player(this)
  val tile = new Tile(this)
  val potion = new Potion(this, (0, 0), 10, 10)
  val mushroom = new Mushroom(this)
  val items: Vector[Items] = Vector(new Potion(this, (1, 0), 2, 3), new Wood(this, (0, 0), 10, 7), new Wood(this, (0, 1), 7, 7), new Wood(this, (0, -1), 7, 9))

  private val pokemonImage: Map[String, BufferedImage] = Map(
    "charmander" -> ImageIO.read(getClass.getResourceAsStream("/pictures/pokemon/charmander.png")),
    "bulbasaur" -> ImageIO.read(getClass.getResourceAsStream("/pictures/pokemon/bulbasaur.png")),
    "squirtle" -> ImageIO.read(getClass.getResourceAsStream("/pictures/pokemon/squirtle.png"))
  )


    // AUDIO
    val audioStream = AudioSystem.getAudioInputStream(getClass.getResourceAsStream("/audio/bgm.wav"))
    val audioStream2 = AudioSystem.getAudioInputStream(getClass.getResourceAsStream("/audio/win.wav"))
    val clip = AudioSystem.getClip()
    val clip2 = AudioSystem.getClip()
    clip.open(audioStream)
    clip2.open(audioStream2)
    val volume = clip.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
    volume.setValue(-20.0f)
    clip.start()
    clip.loop(Clip.LOOP_CONTINUOUSLY)



  // INPUT/OUTPUT TEXT BOXES
  val inputArea = new TextField(40)
    hasFocus
  val textOutput = new TextArea(7, 40) {
    editable = false
    wordWrap = true
    lineWrap = true
    text = "Pick and name one of the three pokemons to proceed."
    font = new Font("Bold", 4, 20)
  }
  listenTo(inputArea.keys)
  listenTo(keys)



  def update() = {
    keyHandler.update()
    lost()
    won()
  }


  def lost() = {
    if (started && player.pokemon.forall( _.hp == 0)) {
      textOutput.text = "You lost"
      textOutput.editable = false
      lose = true
    }
  }


  def won() = {
    if ((player.currentMapX, player.currentMapY) == (-2, 0) && (player.x, player.y) == (12*48, 5*48) && !win) {
      win = true
      textOutput.text = "win"
      textOutput.editable = false
      clip.close()
      val volume = clip2.getControl(FloatControl.Type.MASTER_GAIN).asInstanceOf[FloatControl]
      volume.setValue(-10.0f)
      clip2.start()
    }
  }


  // STARTS THE MAIN GAME
  def startGame(pokemon: Pokemon) = {
    if (!started) {
      this.reactions -= {case KeyPressed(_,Key.Enter,_,_) =>}
      player.pokemon = Some(pokemon)
      textOutput.text = "Nice choice.\nNow find your way home.\nTry 'help'"
      started = true
    }
  }



  //GAME SCREEN
  val tileSize = 48 // 48x48 tile
  val row = 12
  val column = 16
  val panelWidth = tileSize * column
  val panelHeight = tileSize * row
  preferredSize = new Dimension(panelWidth, panelHeight)
  background = Color.black



  // RUNNING AND UPDATING THE GAME SCREEN 60fps
  var gameThread: Option[Thread] = None

  def startGameThread() = {
    gameThread = Some(new Thread(this))
    gameThread.foreach( _.start() )
  }

  override def paintComponent(g: Graphics2D) = {
    super.paintComponent(g)
    if (started) {
      tile.loadMap()
      tile.drawMap(g)
      items.foreach( _.draw(g) )
      player.draw(g)
      tile.drawHouse(g)
    }
    else {
      g.drawImage(pokemonImage("charmander"), 50, 205, 190, 163, null)
      g.drawImage(pokemonImage("bulbasaur"), 290, 205, 190, 163, null)
      g.drawImage(pokemonImage("squirtle"), 530, 205, 190, 163,  null)
    }
    if (win) g.drawImage(ImageIO.read(getClass.getResourceAsStream("/pictures/win.png")), 0, 0, 768, 576, null)
    if (lose) g.drawImage(ImageIO.read(getClass.getResourceAsStream("/pictures/lose.png")), 0, 0, 768, 576, null)
    g.dispose()

  }

  override def run(): Unit = {
    // updates game every 0.01667..s => 60 fps
    val repaintInterval = 1000000000 / 60.0
    var nextRepaint = System.nanoTime() + repaintInterval
    var waitTime = (nextRepaint - System.nanoTime())/1000000

    while (gameThread.isDefined) {
      update()
      repaint()
      if (waitTime < 0) waitTime = 0
      Thread.sleep(waitTime.toLong)
      nextRepaint += repaintInterval
    }

  }


}
