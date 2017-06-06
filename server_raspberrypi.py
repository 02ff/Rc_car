import RPi. GPIO as GPIO
GPIO.setmode(GPIO.BCM)

from socket import *
from time import ctime

HOST = ''
PORT = 21567
BUFSIZ = 1024
ADDR = (HOST, PORT)

tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.bind(ADDR)
tcpSerSock.listen(5)

pwm1_pin=18
pwm2_pin=22

in1_pin=18
in2_pin=17

in3_pin=22
in4_pin=27

GPIO.setup(pwm1_pin, GPIO.OUT)
GPIO.setup(pwm2_pin, GPIO.OUT)

GPIO.setup(in1_pin, GPIO.OUT)
GPIO.setup(in2_pin, GPIO.OUT)

GPIO.setup(in3_pin, GPIO.OUT)
GPIO.setup(in4_pin, GPIO.OUT)

pwm_motor1=GPIO.PWM(pwm1_pin, 100)
pwm_motor2=GPIO.PWM(pwm2_pin, 100)
pwm_motor1.start(0)
pwm_motor2.start(0)

while True:

    print ("waiting for connection...")
    tcpCliSock, addr = tcpSerSock.accept()
    print ("...connected from:", addr)

    while True:
        gear = tcpCliSock.recv(BUFSIZ)
        gear = gear.decode('utf-8')[0:len(gear)-1]

        if not gear :
            break

        if gear =="s":
            print (gear, "motor stop")
            pwm_motor1.ChangeDutyCycle(0)
            pwm_motor2.ChangeDutyCycle(0)
            GPIO.output(in1_pin, False)
            GPIO.output(in2_pin, False)
            GPIO.output(in3_pin, False)
            GPIO.output(in4_pin, False)

        elif gear =="g":
            print (gear, "motor moving forward")
            pwm_motor1.ChangeDutyCycle(75)
            GPIO.output(in1_pin, True)
            GPIO.output(in2_pin, False)
            pwm_motor2.ChangeDutyCycle(75)
            GPIO.output(in3_pin, True)
            GPIO.output(in4_pin, False)

        else:
            GPIO.cleanup()
            break;

    tcpCliSock.close()
tcpSerSock.close()