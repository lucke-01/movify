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
#ROLES
resource "aws_iam_role" "ec2_role" {
  name = "ec2_ecr_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_policy" "ecr_access_policy" {
  name        = "EC2ECRAccessPolicy"
  description = "Allow EC2 to access ECR"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchGetImage"
        ]
        Resource = "*"
      }
    ]
  })
}
resource "aws_iam_role_policy_attachment" "ecr_policy_attachment" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = aws_iam_policy.ecr_access_policy.arn
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "ec2_instance_profile"
  role = aws_iam_role.ec2_role.name
}
#END ROLES

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
  ingress {
    from_port = 8080
    to_port   = 8080
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
  instance_type        = "t3.micro"
  security_groups = [aws_security_group.ec2_sg.name]
  #associate_public_ip_address = true
  iam_instance_profile = aws_iam_instance_profile.ec2_profile.name
  user_data            = <<-EOF
              #!/bin/bash
              sudo yum update -y
              sudo yum install docker -y
              sudo systemctl start docker
              sudo systemctl enable docker
              aws ecr get-login-password --region ${var.aws_region} | docker login --username AWS --password-stdin ${var.aws_account_id}.dkr.ecr.${var.aws_region}.amazonaws.com
              docker pull ${var.aws_account_id}.dkr.ecr.${var.aws_region}.amazonaws.com/movify:latest
              docker run -d -p 80:8080 --name movify-instance ${var.aws_account_id}.dkr.ecr.${var.aws_region}.amazonaws.com/movify:latest
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
  instance = aws_instance.web.id
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
