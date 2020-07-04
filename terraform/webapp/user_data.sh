#!/bin/bash

# install software
sudo yum update -y
sudo yum install mc -y
sudo amazon-linux-extras install java-openjdk11 -y
sudo yum install awscli -y
sudo yum install -y util-linux-user
sudo yum install -y awslogs

# create app dirs
mkdir "${t_app_dir}"
chmod 777 "${t_app_dir}" -R

# create log dirs
mkdir -p "/var/log/bordozer/${t_service_name}/"
chmod 777 -R "/var/log/bordozer/${t_service_name}/"

# Get app artifact
echo "RUN_ARGS='--spring.profiles.active=aws-${t_env}'" >"${t_app_dir}/${t_app_artifact_name}.conf"
aws s3 cp "s3://${t_app_artifact_s3_bucket}/${t_app_artifact_name}.jar" "${t_app_dir}/"

useradd springboot
chsh -s /sbin/nologin springboot
chown springboot:springboot "${t_app_dir}/${t_app_artifact_name}.jar"
chmod 500 "${t_app_dir}/${t_app_artifact_name}.jar"

ln -s "${t_app_dir}/${t_app_artifact_name}.jar" "/etc/init.d/${t_service_instance_name}"

chkconfig "${t_service_instance_name}" on
service "${t_service_instance_name}" start

# /etc/awslogs/awslogs.conf
# TODO: set region in /etc/awslogs/awscli.conf
sudo systemctl start awslogsd

# sudo netstat -tulpn | grep 8966
# sudo kill -9 <pid>

# /var/log/messages
