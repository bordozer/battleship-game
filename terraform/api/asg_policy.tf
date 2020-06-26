resource "aws_autoscaling_policy" "scale_out_policy" {
  name = "tf-${var.service_instance_name}-asg-scale-out-policy"
  policy_type = "SimpleScaling"
  scaling_adjustment = 1
  adjustment_type = "ChangeInCapacity"
  cooldown = 180 # The amount of time, in seconds, after a scaling activity completes and before the next scaling activity can start
  autoscaling_group_name = aws_autoscaling_group.service_asg.name
}

resource "aws_autoscaling_policy" "scale_in_policy" {
  name = "tf-${var.service_instance_name}-asg-scale-in-policy"
  policy_type = "SimpleScaling"
  scaling_adjustment = -1
  adjustment_type = "ChangeInCapacity"
  cooldown = 180
  autoscaling_group_name = aws_autoscaling_group.service_asg.name
}

/*
// TargetTrackingScaling
resource "aws_autoscaling_policy" "cpu_target_tracking_policy" {
  name = "tf-${var.service_instance_name}-asg-increase_instances_count-policy"
  policy_type = "TargetTrackingScaling"
  autoscaling_group_name    = aws_autoscaling_group.service_asg.name
  estimated_instance_warmup = 240

  target_tracking_configuration {
    predefined_metric_specification {
      predefined_metric_type = "ASGAverageCPUUtilization"
    }

    target_value = "80"
  }
}
*/

# https://docs.aws.amazon.com/AmazonCloudWatch/latest/monitoring/aws-services-cloudwatch-metrics.html
