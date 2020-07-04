terraform {
  backend "s3" {
    bucket = "remote-state-bucket"
    region = "us-west-2"
  }
}
