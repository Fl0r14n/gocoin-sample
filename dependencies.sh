#!/bin/bash

#node package manager
sudo apt-get install -y npm
#ui tools
sudo npm install -g grunt-cli less bower
#fix for node symlink
sudo ln -s /usr/bin/nodejs /usr/bin/node
