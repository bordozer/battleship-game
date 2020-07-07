resource "aws_lb" "front_end" {
  name = "tf-${var.service_instance_name}-lb"
  load_balancer_type = "application"
  subnets = var.subnets
  security_groups = [ aws_security_group.lb_sg.id ]

  internal = false
  enable_cross_zone_load_balancing = true
  idle_timeout = 60

  tags = local.common_tags
}

resource "aws_lb_listener" "front_end" {
  load_balancer_arn = aws_lb.front_end.arn
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = var.certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.lb_tg.arn
  }
}
