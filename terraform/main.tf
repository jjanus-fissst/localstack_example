terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 3.49"
    }
  }

  required_version = ">= 0.14.9"
}

provider "aws" {
  profile = "local"
  region = var.aws_region
  access_key = "mock_access_key"
  s3_force_path_style = true
  secret_key = "mock_secret_key"
  skip_credentials_validation = true
  skip_metadata_api_check = true
  skip_requesting_account_id = true

  endpoints {
    sqs = "http://localhost:4566"
    s3 = "http://localhost:4566"
    lambda = "http://localhost:4566"
  }
}

variable "aws_region" {
  type = string
}

variable "prefix" {
  type = string
  default = "codeandcoffee"
}

resource "aws_sqs_queue" "queueExample" {
  name = "${var.prefix}-queue-test"
  visibility_timeout_seconds = 30
  message_retention_seconds = 86400
}

resource "aws_s3_bucket" "s3BucketExample" {
  bucket = "${var.prefix}-s3bucket-test"
}

resource "aws_s3_bucket_object" "s3BucketExampleDirectory" {
  bucket = aws_s3_bucket.s3BucketExample.id
  key = "directory1/"
}

resource "aws_lambda_function" "lambdaExample" {
  function_name = "${var.prefix}-lambda-test"
  filename = "../lambda-example/target/lambda-0.0.1-SNAPSHOT.jar"
  source_code_hash = filebase64sha256("../lambda-example/target/lambda-0.0.1-SNAPSHOT.jar")
  runtime = "java11"
  handler = "pl.fissst.codeandcoffee.lambda.ExampleRequestHandler"
  role = "arn:aws:iam::12345:role/ignoreme"
  memory_size = 512
  timeout = 5

  environment {
    variables = {
      RUN_PROFILE = "local"
    }
  }
}

resource "aws_s3_bucket_notification" "lambdaS3NotificationExample" {
  bucket = aws_s3_bucket.s3BucketExample.id

  lambda_function {
    lambda_function_arn = aws_lambda_function.lambdaExample.arn
    events = ["s3:ObjectCreated:*"]
  }
}