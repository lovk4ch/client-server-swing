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
public class getObject implements Serializable, Cloneable {
    
    public socketTypes type;
    private Object list;
    public int id;
    private boolean replicate;
    
    public getObject(socketTypes type, boolean replicate) {
        this.type = type;
        this.replicate = true;
    }
    
    public boolean isReplicated() {
        return replicate;
    }
    
    public Object getList() {
        return list;
    }
    
    public void setList(Object list) {
        this.list = list;
    }
    
    public getObject(socketTypes type) {
        this.type = type;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}