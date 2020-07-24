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
    data.aws_sns_topic.notification.arn
  ]
  ok_actions = [
    data.aws_sns_topic.notification.arn
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

resource "aws_cloudwatch_metric_alarm" "no_healthy_hosts" {
  alarm_name          = "tf-${var.service_instance_name}-healthy-hosts-count"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = "2"
  metric_name         = "HealthyHostCount"
  namespace           = "AWS/ApplicationELB"
  period              = "60"
  statistic           = "Average"
  threshold           = 1
  alarm_description   = "Number of instance healthy in Target Group"
  actions_enabled     = "true"
  alarm_actions       = [data.aws_sns_topic.notification.arn]
  ok_actions          = [data.aws_sns_topic.notification.arn]
  dimensions = {
    TargetGroup  = aws_lb_target_group.lb_tg.arn_suffix
    LoadBalancer = aws_lb.front_end.arn_suffix
  }
}

resource "aws_cloudwatch_metric_alarm" "error_rate_exceeded" {
  alarm_name                = "tf-${var.service_instance_name}-error-rate-exceeded"
  comparison_operator       = "GreaterThanOrEqualToThreshold"
  evaluation_periods        = "2"
  threshold                 = "10"
  alarm_description         = "Request error rate has exceeded 10%"
  insufficient_data_actions = []

  metric_query {
    id          = "e1"
    expression  = "m2/m1*100"
    label       = "Error Rate"
    return_data = "true"
  }

  metric_query {
    id = "m1"

    metric {
      metric_name = "RequestCount"
      namespace   = "AWS/ApplicationELB"
      period      = "120"
      stat        = "Sum"
      unit        = "Count"

      dimensions = {
        LoadBalancer = "app/web"
      }
    }
  }

  metric_query {
    id = "m2"

    metric {
      metric_name = "HTTPCode_ELB_5XX_Count"
      namespace   = "AWS/ApplicationELB"
      period      = "120"
      stat        = "Sum"
      unit        = "Count"

      dimensions = {
        LoadBalancer = "app/web"
      }
    }
  }
}
