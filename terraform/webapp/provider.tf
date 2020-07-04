provider "aws" {
  version = "~> 2.48"
  profile = "default"
  region = var.aws_region
}

provider "template" {
  version = "~> 2.1"
}
