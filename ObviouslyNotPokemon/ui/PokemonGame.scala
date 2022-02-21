package ui



import scala.swing.GridBagPanel.Anchor._
import scala.swing.GridBagPanel.Fill
import scala.swing._




object PokemonGame extends SimpleSwingApplication {

  def top = new MainFrame {

    title = "ObviouslyNotPokemon"
    resizable = false
    centerOnScreen()
    visible = true

    val gamePanel = new GamePanel

    // GAME LAYOUT
    contents = new GridBagPanel {
      layout += gamePanel            -> new Constraints(1, 0, 1, 1, 1, 1, North.id, Fill.None.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += gamePanel.inputArea  -> new Constraints(1, 2, 1, 1, 1, 1, SouthWest.id, Fill.Horizontal.id, new Insets(5, 5, 5, 5), 0, 0)
      layout += gamePanel.textOutput -> new Constraints(1, 2, 1, 1, 1, 1, SouthWest.id, Fill.Horizontal.id, new Insets(5, 5, 5, 5), 0, 0)
    }
    gamePanel.startGameThread()

  }
}
