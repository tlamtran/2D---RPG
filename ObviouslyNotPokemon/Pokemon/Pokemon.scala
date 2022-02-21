package Pokemon

import ui.GamePanel


abstract class Pokemon(gamePanel: GamePanel) {
  val name: String
  var hp: Int
  var level: Int
  var exp: Int


  def levelUp(): Unit = {
    if (this.exp >= 10) {
      this.level += this.exp / 10
      this.exp = this.exp % 10
      s"${this.name} leveled up to $level!"
    }
  }


  def attack(target: Pokemon): Unit = {
    target.hp -= 4
    if (target.hp == 0) {
      "You defeated " + target.name
    }
    else if (this.hp == 0) {
      this.gamePanel.lost()
    }
  }

  override def toString: String = s"$name\nHP: $hp\nlevel: $level"
}



class Charmander(gP: GamePanel) extends Pokemon(gP) {
  val name = "charmander"
  var level = 1
  var hp = 10 + this.level * 2
  var exp = 0

}

class Bulbasaur(gP: GamePanel) extends Pokemon(gP) {
  val name = "bulbasaur"
  var level = 1
  var hp = 10 + this.level * 2
  var exp = 0

}

class Squirtle(gP: GamePanel) extends Pokemon(gP) {
  val name = "squirtle"
  var level = 1
  var hp = 10 + this.level * 2
  var exp = 0

}


class Mushroom(gP: GamePanel) extends Pokemon(gP) {
  val mapXY = (0, -1)
  val xy = (8*48, 8*48)

  var statusDefeated = false
  val name = "mushroom"
  var level = 3
  var hp = 10 + this.level * 2
  var exp = 0


}


