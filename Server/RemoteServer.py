import socket
import SizeOfSize
import win32api
from pynput.mouse import Button
from pynput.mouse import Controller as mouse_controller
import pyautogui

def controller(sock):
    sock.setblocking(1)
    mouse = mouse_controller()
    last_x, last_y = pyautogui.position()

    while True:
        data = SizeOfSize.recv_by_size(sock)
        if data == "":
            break
        elif data.split("_")[0] == "MOUSE":
            last_x += int(float(data.split("_")[1]))
            last_y += int(float(data.split("_")[2]))
            win32api.SetCursorPos((last_x, last_y))
        elif data.split("_")[0] == "LEFT":
            if data.split("_")[1] == "DOWN":
                mouse.press(Button.left)
            elif data.split("_")[1] == "UP":
                mouse.release(Button.left)
        elif data.split("_")[0] == "RIGHT":
            if data.split("_")[1] == "DOWN":
                mouse.press(Button.right)
            elif data.split("_")[1] == "UP":
                mouse.release(Button.right)
        elif data.split("_")[0] == "TYPE":
            pyautogui.write(data.split("_")[1])
        elif data == "DEL":
            pyautogui.press('backspace')


def main():
    srv_sock = socket.socket()
    ip = "0.0.0.0"
    port = 12345
    print ("bind start")
    srv_sock.bind((ip, port))
    print ("listen start")
    srv_sock.listen(10)
    srv_sock.settimeout(600)
    print ("con start")

    while True:
        try:
            (conn, (ip, port)) = srv_sock.accept()

            print ("connected")
            controller(conn)
        except socket.timeout:
            break



    srv_sock.close()
if __name__ == '__main__':
    main()