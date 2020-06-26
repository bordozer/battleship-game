resource "aws_lb_target_group" "lb_tg" {
  name     = "tf-${var.service_instance_name}-tg"
  target_type = "instance"
  protocol = "HTTP"
  port     = var.app_port
  vpc_id   = var.vpc

  health_check {
    protocol = var.app_protocol
    port = var.app_port
    path = var.app_health_check_uri
    healthy_threshold = 2
    unhealthy_threshold = 3
    timeout = 2
    interval = 30
  }

  tags = local.common_tags
}
