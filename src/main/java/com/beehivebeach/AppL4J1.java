package com.beehivebeach;

/**
 * Log4j1
 * 
 * run like: 
 * 
 * mvn compile exec:java -Dexec.mainClass="com.beehivebeach.AppL4J1" -Dexec.args="'\${jndi:ldap://127.0.0.1:1234/test}'"
 */

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;  
import org.apache.log4j.Logger;

public class AppL4J1 
{
    private static final Logger L = LogManager.getLogger(AppL4J1.class);
    public static void main( String[] args )
    {
        BasicConfigurator.configure();
        System.out.println( "Arguments AppL4J1 (" + args.length + ") :" );
        for (int i=0; i < args.length; i+=1) {
            L.info(i + ": " + args[i]);
        } 
    }
}
