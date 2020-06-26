resource "aws_lb" "front_end" {
  name = "tf-${var.service_instance_name}-lb"
  load_balancer_type = "application"
  subnets = var.subnets
  security_groups = [ aws_security_group.lb_sg.id ]

  internal = var.internal_lb_scheme
  enable_cross_zone_load_balancing = true
  idle_timeout = 60

//  enable_deletion_protection = true

  /*access_logs {
    bucket = aws_s3_bucket.app_log_bucket.bucket
    prefix = ""
    enabled = true
  }*/

  tags = local.common_tags
}

resource "aws_lb_listener" "front_end" {
  load_balancer_arn = aws_lb.front_end.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.lb_tg.arn
  }
}
