resource "aws_cloudwatch_log_group" "yada" {
  name = "/${var.service_instance_name}/logs"

  retention_in_days = 3

  tags = local.common_tags
}
