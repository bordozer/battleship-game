resource "aws_iam_role" "service_iam_role" {
  name = "tf-${var.service_instance_name}-iam-role"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF

  tags = local.common_tags
}

resource "aws_iam_role_policy" "service_s3_access_policy" {
  name = "tf-${var.service_instance_name}-role-policy"
  role = aws_iam_role.service_iam_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:Get*",
                "s3:List*"
            ],
            "Resource": "*"
        }
    ]
}
EOF

  tags = local.common_tags
}

resource "aws_iam_role_policy" "cloud_watch_logs_policy" {
  name = "tf-${var.service_instance_name}-cloud-watch-logs-policy"
  role = aws_iam_role.service_iam_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Action": [
                "logs:*"
            ],
            "Effect": "Allow",
            "Resource": "*"
        }
    ]
}
EOF

  tags = local.common_tags
}

resource "aws_iam_role_policy" "send_email_policy" {
  name = "tf-${var.service_instance_name}-send-email-policy"
  role = aws_iam_role.service_iam_role.id
  policy = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ses:SendEmail",
                "ses:SendRawEmail"
            ],
            "Resource": "*"
        }
    ]
}
EOF

  tags = local.common_tags
}
