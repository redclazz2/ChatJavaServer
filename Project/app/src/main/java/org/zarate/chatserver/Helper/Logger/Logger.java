/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.zarate.chatserver.Helper.Logger;

/**
 *
 * @author sebastian
 */
public class Logger {
    public static void Log(LoggerLevel level, String caller, String message)
        {
            String format = String.format("[%s]::[%s]::[%s]", level,caller,message);
            System.out.println(format);
        }
}
