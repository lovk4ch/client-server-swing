/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Arthur Novikov
 */
public class ResponseManager {
    
    
    public static final String _objectNotFound = "Объект не найден";
    public static final String _objectExists = "Данный объект уже существует";
    
    public static void message_DatabaseRequestException() {
        System.err.println("Ошибка при выполнении запроса к базе:");
    }
    
    public static void message_FileNotFound() {
        System.err.println("Файл со списком рейсов повреждён или отсутствует. Будет создан новый список.");
    }
}