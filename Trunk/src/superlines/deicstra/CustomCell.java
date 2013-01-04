package superlines.deicstra;

/*
* CustomCell.java
*
* Created on 18 Август 2007 г., 0:21
*
*/

/**
*
* @author _shef_
*/
import java.awt.Point;

public class CustomCell {

/** Creates a new instance of CustomCell */
public CustomCell() {
}

/** Конструктор создающий слетку по переданным
* координатам и стоимости проходжения
* @param cost - стоимость прохождения
* если стоимость меньше нуля - клетка не проходима
* x, y - координаты
*/
public CustomCell(int cost, int x, int y) {
this.Cost = Math.abs(cost);

if (cost < 0) {
this.Passableness = false;

} else {
this.Passableness = true;
}
this.setPosition(x, y);


}

public double Cost = 1.0;

//Проходимость клетки
private boolean Passableness = true;
/** Метод информирует о том проходима клетка или нет
* @return true - клетка проходима
* false - клетка не проходима
*/
public boolean isPassable() {return this.Passableness;}

/** Метод устанавливает проходимость клетки
* @param true - клетка проходима
* false - клетка не проходима
*/ 
public void setPassableness(boolean Passableness){
this.Passableness = Passableness;
}

//Положение клетки
private Point Position = new Point();
/**Метод устанавливает координаты клетки
* через переданный обьект Point
*/
public void setPosition (Point Position){
this.Position = Position;
}
/**Метод возвращает координаты клетки
*/
public Point getPosition(){
return this.Position;
}
/**Метод устанавливает координаты клетки
*/
public void setPosition (int X, int Y){
this.Position = new Point(X,Y);
}
/**Метод возвращает абсциссу клетки
*/ 
public int getX(){
return this.Position.x;
}
/**Метод возвращает ординату клетки
*/ 
public int getY(){
return this.Position.y;
}



public String toString(){
return ("("+ getX() + ";" + getY() +"):\n"
+ "Passableness: " + isPassable() + "\n"
);
}



}