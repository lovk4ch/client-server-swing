/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Socket;

import java.io.Serializable;

/**
 *
 * @author Arthur Novikov
 */
public class updateObject implements Serializable {
    
    public socketTypes type;
    public Object obj;
    public int id;
    
    public updateObject(socketTypes type) {
        this.type = type;
    }
}