import requests

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

the server is handled by springboot itslef so just start your spring boot app and run this script
"""

ISO8583_MESSAGE = (
   "0200" + "7238000000000000" + "16" + "1234567890123456" + "000000" + "000000010000" + "0709163030" + "123456" + "163030" + "0709"
)


def main():
    url = "http://localhost:8080/api/iso8583"  # change if your controller path is different!
    headers = {"Content-Type": "text/plain"}
    print("Sent ISO 8583 is of length : ", len(ISO8583_MESSAGE))
    print(f"Posting to {url}")
    resp = requests.post(url, data=ISO8583_MESSAGE, headers=headers)
    print("Server responded:", resp.text)
    print("HTTP status:", resp.status_code)

if __name__ == "__main__":
    main()
