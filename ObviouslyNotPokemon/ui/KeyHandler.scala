package ui

import Pokemon.{Bulbasaur, Charmander, Squirtle}

import scala.swing.event.{Key, KeyPressed}

class KeyHandler(gP: GamePanel) {

  private var response = Vector(
    "hahah just a typo no worries",
    "you can always try again dont mind",
    "third times the charm",
    "come on now...",
    "did you spend your childhood under a rock??"
  )

  private def respond(text: String) = {
    if (!gP.started && text.nonEmpty) {
      gP.textOutput.text = response.head
      if (response.length > 1) {
        response = response.tail
      }
    }
  }



  def update() = {  // UPDATES ALL THE INFO RELATED TO INPUTS (PRE-GAME REACTIONS + PLAYER MOVEMENT)

    if (!gP.player.moving) { // MOVEMENT / RESPONSOE ACCORDING TO KEYBOARD / TEXT INPUT

      gP.reactions += {
        case KeyPressed(_,Key.Up,_,_)    => gP.textOutput.text = if (!gP.player.moving && gP.player.movable("up")) gP.player.move("up") else "up"
        case KeyPressed(_,Key.Down,_,_)  => gP.textOutput.text = if (!gP.player.moving && gP.player.movable("down")) gP.player.move("down") else "down"
        case KeyPressed(_,Key.Right,_,_) => gP.textOutput.text = if (!gP.player.moving && gP.player.movable("right")) gP.player.move("right") else "right"
        case KeyPressed(_,Key.Left,_,_)  => gP.textOutput.text = if (!gP.player.moving && gP.player.movable("left")) gP.player.move("left") else "left"

        case KeyPressed(gP.inputArea,Key.Enter,_,_) => {
          gP.inputArea.text.toLowerCase match {
            case "charmander" => if (!gP.started) gP.startGame(new Charmander(gP))
            case "bulbasaur"  => if (!gP.started) gP.startGame(new Bulbasaur(gP))
            case "squirtle"   => if (!gP.started) gP.startGame(new Squirtle(gP))
            case "up"       => gP.textOutput.text = if (gP.started && !gP.player.moving && gP.player.movable("up")) gP.player.move("up") else "up"
            case "down"     => gP.textOutput.text = if (gP.started && !gP.player.moving && gP.player.movable("down")) gP.player.move("down") else "down"
            case "right"    => gP.textOutput.text = if (gP.started && !gP.player.moving && gP.player.movable("right")) gP.player.move("right") else "right"
            case "left"     => gP.textOutput.text = if (gP.started && !gP.player.moving && gP.player.movable("left")) gP.player.move("left") else "left"
            case "pick"     => gP.textOutput.text = if (gP.started) gP.player.pick() else response.head
            case "build"    => gP.textOutput.text = if (gP.started) gP.player.build() else response.head
            case "attack"   => gP.textOutput.text = if (gP.started) gP.player.attack() else response.head
            case "potion"   => gP.textOutput.text = if (gP.started) gP.player.potion() else response.head
            case "pokemon"  => gP.textOutput.text = if (gP.started) gP.player.pokemon.head.toString else response.head
            case "bag"      => gP.textOutput.text = if (gP.started && gP.player.bag.isEmpty) "Bag is empty" else if (!gP.started) response.head else gP.player.bag.map( _.name ).mkString(", ")
            case "help"     => gP.textOutput.text = "Text commands: up, down, right, left, pick, bag, build, attack, pokemon, potion \nKeyboard commands: movement with arrow keys"
            case other      => respond(other)
          }
          if (gP.inputArea.text.nonEmpty) {
            gP.inputArea.text = ""
          }
        }
      }
    }

    else if (gP.player.moving) { // 48x48 Tile based moving
      gP.player.move(gP.player.facing)
      gP.tile.changeMap()
      if (gP.player.moved >= 48) {
        gP.player.moved = 0
        gP.player.moving = false
      }

    }
  }

}

