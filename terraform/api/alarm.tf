resource "aws_cloudwatch_metric_alarm" "cpu_usage_is_very_high" {
  alarm_name = "tf-${var.service_instance_name}-CPU-is-too-high"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods = 3 # The number of periods over which data is compared to the specified threshold.
  metric_name = "CPUUtilization"
  namespace = "AWS/EC2"
  period = 120 # The period in seconds over which the specified statistic is applied
  statistic = "Average"
  threshold = 85 # The value against which the specified statistic is compared
  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.service_asg.name
  }
  alarm_description = "Add an instance if CPU Utilization is too high"
  alarm_actions = [
    aws_autoscaling_policy.scale_out_policy.arn,
    aws_sns_topic.asg_notifications.arn
  ]
  ok_actions = [
    aws_sns_topic.asg_notifications.arn
  ]
}

resource "aws_cloudwatch_metric_alarm" "cpu_usage_is_very_low" {
  alarm_name = "tf-${var.service_instance_name}-CPU-is-too-low"
  comparison_operator = "LessThanOrEqualToThreshold"
  evaluation_periods = 1
  metric_name = "CPUUtilization"
  namespace = "AWS/EC2"
  period = 60
  statistic = "Average"
  threshold = 40
  dimensions = {
    AutoScalingGroupName = aws_autoscaling_group.service_asg.name
  }
  alarm_description = "Remove an instance if CPU Utilization is too low"
  alarm_actions = [
    aws_autoscaling_policy.scale_in_policy.arn
  ]
}
