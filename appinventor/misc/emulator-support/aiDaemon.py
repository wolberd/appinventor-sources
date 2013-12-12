#!/usr/bin/python
from bottle import run,route,app,request,response,template,default_app,Bottle,debug,abort
import sys
import os
import subprocess
import re
#from flup.server.fcgi import WSGIServer
#from cStringIO import StringIO
#import memcache

app = Bottle()
default_app.push(app)

platform = os.uname()[0]
if platform == 'Linux':
    PLATDIR = '/usr/google/appinventor/'
elif platform == 'Darwin':               # MacOS
    PLATDIR = '/Applications/AppInventor/'
else:                                   # Need to add Windows
    sys.exit(1)

@route('/start/')
def start():
    subprocess.call(PLATDIR + "commands-for-Appinventor/run-emulator ", shell=True, close_fds=True)
    response.headers['Access-Control-Allow-Origin'] = '*'
    response.headers['Access-Control-Allow-Headers'] = 'origin, content-type'
    return ''

@route('/echeck/')
def echeck():
    response.headers['Access-Control-Allow-Origin'] = '*'
    response.headers['Access-Control-Allow-Headers'] = 'origin, content-type'
    response.headers['Content-Type'] = 'application/json'
    device = checkrunning(True)
    if device:
        return '{ "status" : "OK", "device" : "%s"}' % device
    else:
        return '{ "status" : "NO" }'

@route('/ucheck/')
def ucheck():
    response.headers['Access-Control-Allow-Origin'] = '*'
    response.headers['Access-Control-Allow-Headers'] = 'origin, content-type'
    response.headers['Content-Type'] = 'application/json'
    device = checkrunning(False)
    if device:
        return '{ "status" : "OK", "device" : "%s"}' % device
    else:
        return '{ "status" : "NO" }'

@route('/replstart/:device')
def replstart(device=None):
    print "Device = %s" % device
    subprocess.call((PLATDIR + "commands-for-Appinventor/adb -s %s forward tcp:8001 tcp:8001") % device, shell=True, close_fds=True)
    if re.match('.*emulat.*', device): #  Only fake the menu key for the emulator
        subprocess.call((PLATDIR + "commands-for-Appinventor/adb -s %s shell input keyevent 82") % device, shell=True, close_fds=True)
    subprocess.call((PLATDIR + "commands-for-Appinventor/adb -s %s shell am start -a android.intent.action.VIEW -n edu.mit.appinventor.aicompanion3/.Screen1 --ez rundirect true") % device, shell=True, close_fds=True)
    response.headers['Access-Control-Allow-Origin'] = '*'
    response.headers['Access-Control-Allow-Headers'] = 'origin, content-type'
    return ''

def checkrunning(emulator):
    result = subprocess.check_output(PLATDIR + 'commands-for-Appinventor/adb devices', shell=True, close_fds=True)
    lines = result.split('\n')
    for line in lines[1:]:
        if emulator:
            m = re.search('^(.*emulator-[1-9]+)\t+device.*', line)
        else:
            m = re.search('^([0-9.:]+.*?)\t+device.*', line)
        if m:
            break
    if m:
        return m.group(1)
    return False

if __name__ == '__main__':
    run(host='127.0.0.1', port=8004)
    ##WSGIServer(app).run()

