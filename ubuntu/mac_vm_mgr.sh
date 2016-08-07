#!/usr/bin/env bash


Make sure Parallels Desktop is not running. If it is running, the commands below will still create a console window.

From Terminal.app, use the Parallels command line: prlctl

# List available VMs
prlctl list --all


prlctl set vm01 --startup-view headless
prlctl set vm02 --startup-view headless
prlctl set vm03 --startup-view headless
prlctl start vm01 && prlctl start vm02 && prlctl start vm03
prlctl stop vm01 && prlctl stop vm02 && prlctl stop vm03

# Pause and Resume
prlctl pause UbuntuServer
prlctl resume UbuntuServer

# Reset and Restart
prlctl reset UbuntuServer
prlctl restart UbuntuServer



utgerw's answer does work, but doesn't work well if you need to run alongside virtual machines for which you need to use the GUI. Try this command in Terminal...

prlctl set UbuntuServer --on-window-close keep-running
Now if you close the UbuntuServer window it will continue to run in the background. Parallels doesn't support true "headless" mode, but this gets you pretty close.

Further Reading: Parallels prlctl Reference

If you are a Parallels Desktop Enterprise user, you can run a VM as a background service. This is an enterprise-only feature. See the documentation.