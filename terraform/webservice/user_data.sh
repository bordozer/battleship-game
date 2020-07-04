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

# awslogs (the service log: /var/log/awslogs.log) -->
sudo rm /etc/awslogs/awslogs.conf
cat <<EOT > /etc/awslogs/awslogs.conf
[general]
state_file = /var/lib/awslogs/agent-state

[/var/log/messages]
datetime_format = %b %d %H:%M:%S
file = /var/log/bordozer/${t_service_name}/${t_service_name}.log
buffer_duration = 5000
log_stream_name = {instance_id}
initial_position = start_of_file
log_group_name = /${t_service_instance_name}/logs
EOT

sudo rm /etc/awslogs/awscli.conf
cat <<EOT > /etc/awslogs/awscli.conf
[plugins]
cwlogs = cwlogs
[default]
region = ${t_region}
EOT

sudo service awslogsd start
# awslogs <--

# sudo netstat -tulpn | grep 8966
# sudo kill -9 <pid>
