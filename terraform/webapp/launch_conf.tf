resource "aws_launch_configuration" "launch_conf" {
  name_prefix           = "tf-${var.service_instance_name}-lc-"

  /* EC2 parameters */
  instance_type         = var.instance_type
  image_id              = var.ami_id
  enable_monitoring     = true
  associate_public_ip_address = true
  root_block_device {
    volume_type = var.ec2_instance_root_volume_type
    volume_size = var.ec2_instance_root_volume_size
  }
  user_data = data.template_file.ec2_userdata.rendered

  security_groups       = [aws_security_group.ec2_sg.id]
  iam_instance_profile  = aws_iam_instance_profile.instance_profile.id
  key_name              = aws_key_pair.ssh_key.id

  lifecycle {
    create_before_destroy = false
  }
}
