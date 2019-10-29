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
public class deleteObject implements Serializable {
    
    public socketTypes type;
    public int delete_id;
    public int id;
    
    public deleteObject(socketTypes type) {
        this.type = type;
    }
}