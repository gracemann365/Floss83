import socket

"""
This is exactly 82 chars. It covers:
- MTI (4 chars) "0200"
- Primary bitmap (16 hex chars) "7238000000000000"
- Field 2 (LLVAR = "16" + "1234567890123456") = 18 chars
- Field 3 (6 chars) "000000"
- Field 4 (12 chars) "000000010000"
- Field 7 (10 chars) "0709163030"
- Field 11 (6 chars) "123456"
- Field 12 (6 chars) "163030"
- Field 13 (4 chars) "0709"
"""

ISO8583_MESSAGE = (
   "0200" + "7238000000000000" + "16" + "1234567890123456" + "000000" + "000000010000" + "0709163030" + "123456" + "163030" + "0709"
)

def main():
    host = "localhost"
    port = 5000

    print(f"Connecting to {host}:{port}...")
    print("Sent ISO 8583 is of length : ", len(ISO8583_MESSAGE))
    with socket.create_connection((host, port)) as s:
        s.sendall(ISO8583_MESSAGE.encode("ascii"))
        s.shutdown(socket.SHUT_WR)  # Tell server we're done sending
        print("Sent ISO 8583 message, waiting for server response...")
        response = s.recv(1024)
        print("Server responded:", response.decode())

if __name__ == "__main__":
    main()
