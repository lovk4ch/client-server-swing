package Controller;

import javax.swing.*;

/**
 * Created by Arthur Novikov on 01.10.2016.
 */
public class MessageManager {
    
    public static final String selectString = "Выберите рейс из списка";

    public static void checkAllFields() {
        MessageManager.Error("Пожалуйста, заполните все поля");
    }

    public static void Info(String e) {
        JOptionPane.showMessageDialog(null, e, "Информация:", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void Error(String e) {
        JOptionPane.showMessageDialog(null, e, "Внимание!", JOptionPane.ERROR_MESSAGE);
    }

    public static void objectExists() {
        JOptionPane.showMessageDialog(null, "Данный объект уже существует!", "Внимание!", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean windowClosing() {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Закрыть форму?", "Внимание!", JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }

    public static boolean saveConfirm() {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Сохранить изменения?", "Внимание!", JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }
    
    public static boolean updateConfirm() {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Обновить существующую запись?", "Внимание!", JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }
    
    public static boolean deleteConfirm() {
        int dialogResult = JOptionPane.showConfirmDialog (null, "Удалить выбранную запись?", "Внимание!", JOptionPane.YES_NO_OPTION);
        return dialogResult == JOptionPane.YES_OPTION;
    }
}