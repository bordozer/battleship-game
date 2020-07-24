data "aws_sns_topic" "notification" {
  name = var.sns_topic_name
}
