#dir="/home/roshan/workspace/ekbana/bigdata/apostle/apostle"
dir="$1"
echo $dir

if [ -d "${dir}/libs" ]; then rm -Rf "${dir}/libs"; fi
if [ -d "${dir}/plugins" ]; then rm -Rf "${dir}/plugins"; fi
if [ -d "${dir}/bin" ]; then rm -Rf "${dir}/bin"; fi
#rm -f ${dir}/libs
#rm -f ${dir}/plugins
#rm -f ${dir}/bin

#mkdir ${dir}/libs

echo "copying dependencies to libs"
mvn -DoutputDirectory=${dir}/libs dependency:copy-dependencies -pl apostle-core

echo ""
echo "installing plugin-core"
mvn install -pl apostle-core
echo "copying apostle-core to libs"
cp ./apostle-core/target/apostle-core-1.0-SNAPSHOT.jar ${dir}/libs/

echo "making directories"
mkdir ${dir}/bin
mkdir ${dir}/plugins

cp ./apostle.sh ${dir}/bin/
cp ./application.properties ${dir}/bin/
cp ./logback-spring.xml ${dir}/bin/

echo "installing mattermost-webhook"
mvn install -pl mattermost-webhook
echo "copying to plugins"
cp ./mattermost-webhook/target/mattermost-webhook-1.0-SNAPSHOT.jar ${dir}/plugins/

