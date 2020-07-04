resource "aws_cloudwatch_log_group" "yada" {
  name = "/${var.service_instance_name}/logs"

  retention_in_days = var.keep_logs_days

  tags = local.common_tags
}
