PROJECTNAME=$(shell basename "$(PWD)")

# Make is verbose in Linux. Make it silent.
MAKEFLAGS += --silent

GREEN  := $(shell tput -Txterm setaf 2)
YELLOW := $(shell tput -Txterm setaf 3)
WHITE  := $(shell tput -Txterm setaf 7)
CYAN   := $(shell tput -Txterm setaf 6)
RESET  := $(shell tput -Txterm sgr0)

.PHONY: all clean compile test run package

all: help

clean: ## Clean-up previous build artifacts
	@echo "  >  Clean-up previou build artifacts ..."
	./mvnw clean

compile: ## Build the project
	@echo "  >  Build the project ..."
	./mvnw compile

test: ## Test the project
	@echo "  >  Test the project ..."
	./mvnw test

run: ## Run the project
	@echo "  >  Run the project"
	@echo "  >  Open a browser at address http://localhost:8080"
	./mvnw spring-boot:run

package: ## Deploy the project and create packages
	@echo "  >  Deploy the project and create packages"
	./mvnw clean package

## Help:
help: ## Show this help.
	@echo ''
	@echo 'Usage:'
	@echo '  ${YELLOW}make${RESET} ${GREEN}<target>${RESET}'
	@echo ''
	@echo 'Targets:'
	@awk 'BEGIN {FS = ":.*?## "} { \
		if (/^[a-zA-Z_-]+:.*?##.*$$/) {printf "    ${YELLOW}%-20s${GREEN}%s${RESET}\n", $$1, $$2} \
		else if (/^## .*$$/) {printf "  ${CYAN}%s${RESET}\n", substr($$1,4)} \
		}' $(MAKEFILE_LIST)
