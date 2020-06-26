resource "aws_key_pair" "ssh_key" {
  key_name = var.ssh_public_key
  public_key = file(".ssh/${var.ssh_public_key}.pub")
}
