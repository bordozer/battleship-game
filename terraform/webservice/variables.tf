
variable "service_name" { default = "battleship-game" }
variable "service_instance_name" {}
variable "environment_name" {}
variable "route53_record" {}
variable "ssh_public_key" { default = "battleship-game-key"}

/* Amazon account network parameters */
variable "vpc" { default = "vpc-74c2c81d" }
variable "aws_region" { default = "eu-west-3" }
variable "availability_zones" {
        default = [
          "eu-west-3a",
          "eu-west-3b",
          "eu-west-3c"
        ]
}
variable "subnets" {
  default = [
    "subnet-08d6e761",
    "subnet-f2d79f89",
    "subnet-096bf644"
  ]
}
variable "route53_zone_id" { default = "ZYQ37WWIE7SAZ" }
variable "lb_access_logs_s3_bucket" { default = "lb_access_logs" }

/* EC2 parameters */
variable "instance_type" { default = "t2.micro" }
variable "ami_id" {
  default = "ami-007fae589fdf6e955"
  description = "Amazon Linux 2 AMI (HVM), SSD Volume Type (64-bit x86)"
}
variable "ec2_instance_root_volume_type" { default = "gp2" }
variable "ec2_instance_root_volume_size" { default = "8" }

/* Application parameters */
variable "app_port" { default = 8036 }
variable "app_health_port" { default = 9112 }
variable "app_health_check_uri" { default = "/actuator/health" }
variable "app_protocol" { default = "HTTP" }
variable "app_artifacts_s3_bucket" { default = "bordozer-artifacts" }
variable "keep_logs_days" {}

locals {
  common_tags = {
    Name = var.service_instance_name
    ServiceName = var.service_name
    Environment = var.environment_name
  }
  s3_app_artifact_name = "tf-${var.service_instance_name}"
}

# List of email addresses as a string (space separated)
variable "alarm_notification_emails" { default = "bordozer@gmail.com" }
