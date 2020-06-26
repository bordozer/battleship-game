resource "aws_autoscaling_group" "service_asg" {
  name                      = "tf-${var.service_instance_name}-asg"
  vpc_zone_identifier       = var.subnets
  placement_group           = aws_placement_group.pgroup.name
  min_size                  = 1
  desired_capacity          = 1
  max_size                  = 1
  health_check_grace_period = 360   # Time (in seconds) after instance comes into service before checking health
  health_check_type         = "ELB" # ELB or EC2
  min_elb_capacity          = 0
  wait_for_elb_capacity     = 0
  wait_for_capacity_timeout = 0
  default_cooldown          = 300

  launch_configuration      = aws_launch_configuration.launch_conf.name
  target_group_arns         = [aws_lb_target_group.lb_tg.arn]

  lifecycle {
    create_before_destroy = false
  }

  timeouts {
    delete = "5m"
  }

  /* EC2 instances tags */
  tag {
    key                 = "Name"
    value               = var.service_instance_name
    propagate_at_launch = true
  }
  tag {
    key                 = "ServiceName"
    value               = var.service_name
    propagate_at_launch = true
  }
  tag {
    key                 = "Environment"
    value               = var.environment_name
    propagate_at_launch = true
  }
}

resource "aws_autoscaling_notification" "asg_notifications" {
  group_names = [
    aws_autoscaling_group.service_asg.name
  ]

  notifications = [
    "autoscaling:EC2_INSTANCE_LAUNCH",
    "autoscaling:EC2_INSTANCE_TERMINATE",
    "autoscaling:EC2_INSTANCE_LAUNCH_ERROR",
    "autoscaling:EC2_INSTANCE_TERMINATE_ERROR",
  ]

  topic_arn = aws_sns_topic.asg_notifications.arn
}

resource "aws_placement_group" "pgroup" {
  name     = "tf-${var.service_instance_name}-pg"
  strategy = "spread"
}
