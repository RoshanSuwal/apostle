#!/bin/bash

dir=/home/roshan/workspace/ekbana/bigdata/apostle/apostle-core/target/dependency/
plugin_dir=/home/roshan/workspace/ekbana/bigdata/apostle/plugins/

JAVA=/home/roshan/.jdks/openjdk-17.0.1/bin/java

#classpath_jars=/home/roshan/workspace/ekbana/bigdata/apostle/apostle-core/target/classes

classpath_jars=/home/roshan/workspace/ekbana/bigdata/apostle/apostle-core/target/apostle-core-1.0-SNAPSHOT.jar
plugin_jars=""

for entry in "$dir"*.jar
do
  classpath_jars="$classpath_jars:$entry"
done

for plugin in "$plugin_dir"*.jar
do
  plugin_jars="$plugin_jars:$plugin"
done

echo "$classpath_jars"
echo "$plugin_jars"

exec \
  "$JAVA" \
  -Dspring.output.ansi.enabled=always \
  -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true \
  -Dspring.liveBeansView.mbeanDomain \
  -Dspring.application.admin.enabled=true \
  -javaagent:/snap/intellij-idea-ultimate/348/lib/idea_rt.jar=35527:/snap/intellij-idea-ultimate/348/bin \
  -Dfile.encoding=UTF-8 \
  -classpath "$classpath_jars$plugin_jars" \
  com.ekbana.bigdata.ApostleApp \
  "$@"
#  --spring.config.location=/home/roshan/workspace/ekbana/bigdata/apostle/application.properties \

