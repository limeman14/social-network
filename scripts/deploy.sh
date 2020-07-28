#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa \
    social-network/target/social-network-1.1-SNAPSHOT.jar \
    javapro@176.118.165.204:/home/javapro/

scp -i ~/.ssh/id_rsa \
    social-network-admin/target/social-network-admin-1.1-SNAPSHOT.jar \
    javapro@176.118.165.204:/home/javapro/

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa javapro@176.118.165.204 << EOF
pgrep java | xargs kill -9
nohup java -jar social-network-1.1-SNAPSHOT.jar > log.txt &
nohup java -jar social-network-admin-1.1-SNAPSHOT.jar > log-admin.txt &
EOF

echo 'Bye'