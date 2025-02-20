terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}
variable "aws_region" {
  description = "AWS Region"
  type        = string
  default     = "eu-north-1"
}
variable "aws_account_id" {
  description = "AWS Account ID"
  type        = string
}

provider "aws" {
  region = var.aws_region
}

resource "aws_security_group" "ec2_sg" {
  name        = "terraform-sg"
  description = "Allow SSH and HTTP"

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Allows SSH access
  }

  ingress {
    from_port = 80
    to_port   = 80
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Allows HTTP access
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]  # Allows all outbound traffic
  }
}


resource "aws_instance" "web" {
  ami = "ami-07a64b147d3500b6a"  # Amazon Linux 2 AMI (update if needed)
  instance_type = "t3.micro"
  security_groups = [aws_security_group.ec2_sg.name]
  #associate_public_ip_address = true
  user_data     = <<-EOF
              #!/bin/bash
              sudo yum update -y
              sudo yum install docker -y
              sudo systemctl start docker
              sudo systemctl enable docker
              aws ecr get-login-password --region ${var.aws_region} | docker login --username AWS --password-stdin ${var.aws_account_id}.dkr.ecr.${var.aws_region}.amazonaws.com
              docker pull ${var.aws_account_id}.dkr.ecr.${var.aws_region}.amazonaws.com/movify:latest
              docker run -d -p 80:8080 ${var.aws_account_id}.dkr.ecr.${var.aws_region}.amazonaws.com/movify:latest
              EOF

  tags = {
    Name = "Movify-Terraform-EC2-Docker"
  }
}

#ECR for docker images
resource "aws_ecr_repository" "movify_repo" {
  name = "movify"
}
#IP
#resource "aws_eip" "eip" {
#  instance = aws_instance.web.id
#}
resource "aws_eip" "web_eip" {
  domain = "vpc"
}
resource "aws_eip_association" "web_eip_assoc" {
  instance_id   = aws_instance.web.id
  allocation_id = aws_eip.web_eip.id
}
#END IP

#public ip
output "public_ip" {
  value = aws_instance.web.public_ip
}
output "theId" {
  value = aws_instance.web.id
}
output "public_ip_elastic" {
  value       = aws_eip.web_eip.public_ip
  description = "Elastic IP assigned to EC2"
}
