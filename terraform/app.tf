variable "heroku_app_name" {
  description = "unique application name for invoice-service , e.g. invoice-service-<user>"
}

variable "postgres_plan" {
  type        = string
  nullable    = false
  description = "name of add-on plan for postgres-service"
}

variable "logging_plan" {
  type        = string
  nullable    = false
  description = "name of add-on plan for coralogix logging"
}

resource "heroku_app" "invoice-service-demo" {
  name   = var.heroku_app_name
  region = "us"
  stack  = "container"
  config_vars = {
    "SPRING_PROFILES_ACTIVE" = "demo"
  }
}

# add postgres hobby dev add-on
resource "heroku_addon" "invoice-service-db" {
  app_id = heroku_app.invoice-service-demo.id
  plan   = var.postgres_plan
}

# add logging plan
resource "heroku_addon" "invoice-service-logging" {
   app_id = heroku_app.invoice-service-demo.id
   plan = var.logging_plan
}

#save db url in local variable for resuse
#db url format is postgres://user:password@host:port/db
locals {
  db_url          = heroku_addon.invoice-service-db.config_var_values["DATABASE_URL"]
  userpasswd_host = split("@", heroku_addon.invoice-service-db.config_var_values["DATABASE_URL"])
}

locals {
  user_passwd = split(":", local.userpasswd_host[0])
  host        = local.userpasswd_host[1]
}

# Setup postgres config variable
resource "heroku_app_config_association" "invoice-service-demo" {
  app_id = heroku_app.invoice-service-demo.id
  sensitive_vars = {
    "DATABASE_URL"      = local.db_url
    "DB_USER"           = trimprefix(local.user_passwd[1], "//")
    "DB_PASSWORD"       = local.user_passwd[2]
    "JDBC_DATABASE_URL" = "jdbc:postgresql://${local.host}"
  }
}
