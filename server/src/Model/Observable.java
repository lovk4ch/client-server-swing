/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Arthur Novikov
 */
public interface Observable {
    void notifyObservers(String message);
    void registerObserver(Observer o);
    void removeObserver(Observer o);
}