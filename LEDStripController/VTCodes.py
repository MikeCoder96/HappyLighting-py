
def fg_fromRGB(r,g,b,string):
    result = chr(0x1B) + "[38;2;{};{};{}m"
    return (result.format(r,g,b)+string)
def bg_fromRGB(r,g,b,string):
    result = chr(0x1B) + "[48;2;{};{};{}m"
    return (result.format(r,g,b)+string)

RESET=chr(0x1B) + "[0m"

class Fore:

    BRIGHTRED=chr(0x1B) + "[91m"
    BRIGHTYELLOW=chr(0x1B) + "[93m"
    BRIGHTGREEN=chr(0x1B) + "[92m"
    BRIGHTCYAN=chr(0x1B) + "[96m"
    BRIGHTBLUE=chr(0x1B) + "[94m"
    BRIGHTMAGENTA=chr(0x1B) + "[95m"

    DARKRED=chr(0x1B) + "[31m"
    DARKYELLOW=chr(0x1B) + "[33m"
    DARKGREEN=chr(0x1B) + "[32m"
    DARKCYAN=chr(0x1B) + "[36m"
    DARKBLUE=chr(0x1B) + "[34m"
    DARKMAGENTA=chr(0x1B) + "[35m"

class Back:

    BRIGHTRED=chr(0x1B) + "[101m"
    BRIGHTYELLOW=chr(0x1B) + "[103m"
    BRIGHTGREEN=chr(0x1B) + "[102m"
    BRIGHTCYAN=chr(0x1B) + "[106m"
    BRIGHTBLUE=chr(0x1B) + "[104m"
    BRIGHTMAGENTA=chr(0x1B) + "[105m"

    DARKRED=chr(0x1B) + "[41m"
    DARKYELLOW=chr(0x1B) + "[43m"
    DARKGREEN=chr(0x1B) + "[42m"
    DARKCYAN=chr(0x1B) + "[46m"
    DARKBLUE=chr(0x1B) + "[44m"
    DARKMAGENTA=chr(0x1B) + "[45m"

BRIGHT=chr(0x1B) + "[0m"
REMOVEBRIGHT=chr(0x1B) + "[0m"
UNDERLINE=chr(0x1B) + "[0m"
REMOVEUNDERLINE=chr(0x1B) + "[0m"
NEGATIVE=chr(0x1B) + "[0m"
POSITIVE=chr(0x1B) + "[0m"