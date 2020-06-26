// TODO: the issue: nesseccity to confirm email each time after topic creation
resource "aws_sns_topic" "asg_notifications" {
  name = "tf-${var.service_instance_name}-asg-notifications-sns-topic"

  provisioner "local-exec" {
    command = "sh ./files/sns_subscription.sh"
    environment = {
      sns_arn = self.arn
      sns_emails = var.alarm_notification_emails
    }
  }

  tags = local.common_tags
}
