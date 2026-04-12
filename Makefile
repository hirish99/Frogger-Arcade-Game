SHELL := /bin/bash

.PHONY: setup-javafx test compile verify run

setup-javafx:
	./scripts/setup-javafx.sh

test:
	./scripts/test.sh

compile:
	./scripts/compile.sh

verify:
	./scripts/verify.sh

run:
	@if [[ ! -d ".javafx/lib" ]]; then ./scripts/setup-javafx.sh; fi
	./scripts/run-game.sh
