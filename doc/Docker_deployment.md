# Deploying a module in a local Docker development environment

## Build and implement your module

See [Module_builder.md](Module_builder.md).

## Obtain prerequisites

1. Docker Toolbox: https://www.docker.com/toolbox

## Building base developer environment

Note: this has only been tested in OS X.

2. Run Kitematic (which comes with Docker Toolbox) in default configuration (this will start up a VirtualBox machine called "default")
3. Open the "Docker CLI" (button in lower-left window)
4. Run these commands in the terminal (these are to work around an issue with using private docker registries inside a VirtualBox VM):

        VBoxManage modifyvm "default" --natdnshostresolver1 on
        VBoxManage modifyvm "default --natdnsproxy1 on
6. Exit terminal
7. Quit and restart Kitematic
8. Open Docker CLI
9. docker pull kbase/deplbase

## Deploying a module

1. docker build -t mymodulename .
2. docker run --name mymodulename -d mymodulename
