#SUPPORTED=en_US.UTF-8:en_US:en
SUPPORTED=en_US.ISO-8859-1
#JAVA_HOME="$ORACLE_HOME"/jdk
JAVA_HOME=/opt/jdk1.7.0_13
export JAVA_HOME
CLASSPATH=.
for i in lib/*.jar; do
	CLASSPATH="$CLASSPATH":$i
done;

default() {
   "$JAVA_HOME"/bin/java -Dlog4j.configuration=conf/log4j.properties -cp $CLASSPATH -jar app.jar $1 nohup
   sleep 4
   echo
}

default "$1"
