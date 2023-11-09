import psycopg2
import hashlib

conn = psycopg2.connect(
    dbname="project",
    user="postgres",
    password="Sairen1360!",
    host="192.168.87.93",
    port="5432"
)

def encrypt_string(salt, hash_string):
    md = hashlib.new('sha256')
    md.update(salt.encode())
    md.update(hash_string.encode())
    sha_signature = md.digest()
    return "".join(["{:x}".format(b) for b in sha_signature])

def check_pass(string):
    admin = conn.cursor()
    user = conn.cursor()

    admin.execute("SELECT password, salt from users WHERE username = 'admin'")
    user.execute("SELECT password, salt from users WHERE username = 'user'")

    recordAdmin = admin.fetchone()
    recordUser = user.fetchone()
    
    if recordAdmin[0] == encrypt_string(recordAdmin[1], string):
        return 0
    elif recordUser[0] == encrypt_string(recordUser[1], string):
        return 1
    
    conn.close()
    
def check_hardware(component):
    status = conn.cursor()
    status.execute("SELECT " + component + " from settings")
    result = status.fetchone()
    return result[0]