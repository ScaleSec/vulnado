provider "aws"  {
  region = "${var.region}"
}

resource "aws_vpc" "main" {
  cidr_block = "${var.vpc_cidr}"
  tags = {
    Name = "tmp_vulnado_rev_shell_vpc"
  }
}

resource "aws_internet_gateway" "gw" {
  vpc_id = "${aws_vpc.main.id}"

  tags = {
    Name = "tmp_vulnado_rev_shell_igw"
  }
}

resource "aws_route_table" "r" {
  vpc_id = "${aws_vpc.main.id}"

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = "${aws_internet_gateway.gw.id}"
  }

  tags = {
    Name = "tmp_vulnado_rev_shell_rt"
  }
}

resource "aws_subnet" "subnet" {
  vpc_id     = "${aws_vpc.main.id}"
  cidr_block = "${var.subnet_cidr}"
  availability_zone = "${var.region}b"
  map_public_ip_on_launch = true
  tags = {
    Name = "tmp_vulnado_rev_shell_subnet"
  }
}

resource "aws_route_table_association" "assoc" {
  subnet_id      = "${aws_subnet.subnet.id}"
  route_table_id = "${aws_route_table.r.id}"
}

resource "aws_security_group" "sg" {
  name        = "tmp_vulnado_rev_shell_sg"
  vpc_id      = "${aws_vpc.main.id}"

  ingress {
    protocol    = "tcp"
    from_port   = 22
    to_port     = 22
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    protocol    = "tcp"
    from_port   = 443
    to_port     = 443
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

data "aws_ami" "amznlinux" {
  most_recent = true
  owners = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-2.0.20181114-x86_64-gp2"]

  }
}


resource "aws_key_pair" "attacker" {
  key_name   = "tmp-vulnado-deploy-key"
  public_key = "${var.public_key}"
}

resource "aws_instance" "receiver" {
  ami           = "${data.aws_ami.amznlinux.id}"
  instance_type = "t2.micro"
  subnet_id = "${aws_subnet.subnet.id}"
  key_name = "${aws_key_pair.attacker.key_name}"
  vpc_security_group_ids = ["${aws_security_group.sg.id}"]
  user_data = <<EOF
#!/bin/bash
yum update
yum install -y nmap
EOF
  tags = {
    Name = "VulnadoReverseShellReceiver"
  }
}
