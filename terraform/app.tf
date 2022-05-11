variable "heroku_app_name" {
  description = "unique application name for invoice-service , e.g. invoice-service-<user>"
}

resource "heroku_app" "invoice-service-demo" {
  name   = var.heroku_app_name
  region = "us"
  stack  = "container"
  config_vars = {
    "SPRING_PROFILES_ACTIVE" = "prod"
  }
}

# add postgres hobby dev add-on
resource "heroku_addon" "invoice-service-db" {
  app_id = heroku_app.invoice-service-demo.id
  plan   = "heroku-postgresql:hobby-dev" //hobby-dev is free service on heroku platform
}

# Setup postgres config variable
# TODO : format database-url to set app related variables
resource "heroku_app_config_association" "invoice-service-demo" {
  app_id = heroku_app.invoice-service-demo.id
  sensitive_vars = {
    "DATABASE_URL" = heroku_addon.invoice-service-db.config_var_values["DATABASE_URL"]

  }
}
